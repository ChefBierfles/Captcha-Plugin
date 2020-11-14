package nl.chefbierfles.capatcha.module;

import com.mongodb.*;
import nl.chefbierfles.capatcha.Plugin;
import nl.chefbierfles.capatcha.models.enums.DatabaseFields;
import org.bukkit.Bukkit;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDate;
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

    public static void addCapatchaData(UUID uuid, Instant instant) {
        DBObject dbObject = new BasicDBObject("uuid", uuid);
        DBObject result = getResults(dbObject);

        if (result != null) {
            //Result already exists
            updateCapatchaData(uuid, instant);
            return;
        }

        dbObject.put(DatabaseFields.CAPATCHA_LASTDONE_DATE.toString(), instant);
        players.insert(dbObject);
    }

    public static void updateCapatchaData(UUID uuid, Instant instant) {
        DBObject dbObject = new BasicDBObject("uuid", uuid);

        DBObject found = players.findOne(dbObject);
        if (found == null ){
            addCapatchaData(uuid, instant);
            return;
        }

        DBObject obj = new BasicDBObject("uuid", uuid);
        obj.put(DatabaseFields.CAPATCHA_LASTDONE_DATE.toString(), instant);

        players.update(found, obj);
    }

    @Nullable
    public static Instant getCapatchaData(UUID uuid) {

        DBObject dbObject = new BasicDBObject("uuid", uuid);

        DBObject result = getResults(dbObject);

        if (result == null) return null;

        Date date = (Date) result.get(DatabaseFields.CAPATCHA_LASTDONE_DATE.toString());

        return date.toInstant();
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
