package nl.chefbierfles.captcha.modules;

import com.mongodb.*;
import nl.chefbierfles.captcha.helpers.constants.DatabaseFields;
import nl.chefbierfles.captcha.modules.base.BaseModule;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class DatabaseModule extends BaseModule implements nl.chefbierfles.captcha.interfaces.DatabaseModule {

    private DBCollection players;
    private DB playersDb;
    private MongoClient client;

    public DatabaseModule() {
        super();

        name = "DatabaseModule";
        connect(getConfigManager().getDatabaseConnectionString());
    }

    public void connect(String connectionString) {
        // Mongodb connection string.

        String client_url = connectionString;
        MongoClientURI uri = new MongoClientURI(client_url);

        client = new MongoClient(uri);

        playersDb = client.getDB("Captcha");
        players = playersDb.getCollection("players");
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
