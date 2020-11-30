package nl.chefbierfles.captcha.module;

import com.mongodb.*;
import nl.chefbierfles.captcha.Plugin;
import nl.chefbierfles.captcha.models.constants.DatabaseFields;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;

public class DatabaseModule {

    private static DBCollection players;
    private static DB playersDb;
    private static MongoClient client;

    public static boolean connect(String authUser, String encodedPwd, String hostName, String dbName) {
        // Mongodb connection string.

        String client_url = "mongodb+srv://" + authUser + ":" + encodedPwd + "@" + hostName + "/" + dbName + "?retryWrites=true&w=majority";

        MongoClientURI uri = new MongoClientURI(client_url);
        client = new MongoClient(uri);

        playersDb = client.getDB("Capatcha");
        players = playersDb.getCollection("players");
        return true;
    }

    public static void addCapatchaData(UUID uuid, Date date) {
        DBObject dbObject = new BasicDBObject("uuid", uuid);
        DBObject result = getResults(dbObject);

        if (result != null) {
            //Result already exists
            updateCapatchaData(uuid, date);
            return;
        }

        dbObject.put(DatabaseFields.CAPATCHA_LASTDONE_DATE.toString(), date);
        players.insert(dbObject);
    }

    public static void updateCapatchaData(UUID uuid, Date date) {
        DBObject dbObject = new BasicDBObject("uuid", uuid);

        DBObject found = players.findOne(dbObject);
        if (found == null ){
            addCapatchaData(uuid, date);
            return;
        }

        DBObject obj = new BasicDBObject("uuid", uuid);
        obj.put(DatabaseFields.CAPATCHA_LASTDONE_DATE.toString(), date);

        players.update(found, obj);
    }

    public static void getCapatchaData(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(Plugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                DBObject dbObject = new BasicDBObject("uuid", player.getUniqueId());

                DBObject result = getResults(dbObject);

                Date date = (result == null) ? null : (Date) result.get(DatabaseFields.CAPATCHA_LASTDONE_DATE.toString());

                Bukkit.getScheduler().runTask(Plugin.getInstance(), () -> CapatchaModule.playerJoinCallback(date, player));
            }
        });
    }

    private static DBObject getResults(DBObject dbObject) {
        DBObject resultFound = null;

        try (DBCursor cursor = players.find(dbObject)) {
            while (cursor.hasNext()) {
                resultFound = cursor.next();
            }
        }

        return resultFound;
    }
}
