package nl.chefbierfles.captcha.modules;

import com.mongodb.*;
import nl.chefbierfles.captcha.helpers.constants.DatabaseFields;
import nl.chefbierfles.captcha.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public final class DatabaseModule {

    private DBCollection players;
    private DB playersDb;
    private MongoClient client;
    private final ConfigManager configManager;

    public DatabaseModule(ConfigManager configManager) {

        this.configManager = configManager;

        String username = this.configManager.getUsername();
        String password = this.configManager.getPassword();
        String hostName = this.configManager.getHostname();
        String port = this.configManager.getPort();
        String dbName = this.configManager.getDbName();

        if (hostName.isEmpty()) {
            return;
        }

        String connectionString = "mongodb+srv://" + username + (password.isEmpty() ? "" : ":" + password) + "@" + hostName + (port.isEmpty() ? "" : ":" + port) + "/" + dbName + "?retryWrites=true&w=majority";

        connect(connectionString);
    }

    public void connect(String connectionString) {

        String client_url = connectionString;
        try {
            MongoClientURI uri = new MongoClientURI(client_url);
            client = new MongoClient(uri);
            playersDb = client.getDB("CaptchaPlugin");
            players = playersDb.getCollection("players");
        } catch (MongoConfigurationException exc) {
            Bukkit.getLogger().log(Level.SEVERE, "Kon geen connectie maken met de database.");
        }
    }

    public void addCaptchaData(UUID uuid, Date date) {
        CompletableFuture.runAsync(() -> {
            DBObject dbObject = new BasicDBObject(DatabaseFields.CAPTCHA_UUID, uuid);
            DBObject result = getResults(dbObject);

            if (result != null) {
                //Result already exists
                updateCaptchaData(uuid, date);
                return;
            }

            dbObject.put(DatabaseFields.CAPTCHA_LASTDONE_DATE, date);
            players.insert(dbObject);
        });
    }

    public void updateCaptchaData(UUID uuid, Date date) {
        CompletableFuture.runAsync(() -> {
            DBObject dbObject = new BasicDBObject(DatabaseFields.CAPTCHA_UUID, uuid);

            DBObject found = players.findOne(dbObject);
            if (found == null) {
                addCaptchaData(uuid, date);
                return;
            }

            DBObject obj = new BasicDBObject(DatabaseFields.CAPTCHA_UUID, uuid);
            obj.put(DatabaseFields.CAPTCHA_LASTDONE_DATE, date);

            players.update(found, obj);
        });
    }

    public Date getCaptchaData(Player player) {
        CompletableFuture<Date> completableFuture = CompletableFuture.supplyAsync(() -> {
            DBObject dbObject = new BasicDBObject(DatabaseFields.CAPTCHA_UUID, player.getUniqueId());

            DBObject result = getResults(dbObject);

            return (result == null) ? null : (Date) result.get(DatabaseFields.CAPTCHA_LASTDONE_DATE);
        });

        Date date = null;

        try {
            date = completableFuture.get();
        } catch (Exception exc) {
            getCaptchaData(player);
        }
        return date;
    }

    private DBObject getResults(DBObject dbObject) {
        DBObject resultFound = null;

        try (DBCursor cursor = players.find(dbObject)) {
            while (cursor.hasNext()) {
                resultFound = cursor.next();
            }
        }

        return resultFound;
    }
}
