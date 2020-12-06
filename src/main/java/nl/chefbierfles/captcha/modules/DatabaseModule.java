package nl.chefbierfles.captcha.modules;

import com.mongodb.*;
import nl.chefbierfles.captcha.helpers.constants.DatabaseFields;
import nl.chefbierfles.captcha.interfaces.IDatabaseModule;
import nl.chefbierfles.captcha.modules.base.BaseModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public final class DatabaseModule extends BaseModule implements IDatabaseModule {

    private DBCollection players;
    private DB playersDb;
    private MongoClient client;

    public DatabaseModule() {
        super();

        name = "IDatabaseModule";

        String username = getConfigManager().getUsername();
        String password = getConfigManager().getPassword();
        String hostName = getConfigManager().getHostname();
        String port = getConfigManager().getPort();
        String dbName = getConfigManager().getDbName();

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
            playersDb = client.getDB("Captcha");
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
