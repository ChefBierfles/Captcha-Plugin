package nl.chefbierfles.capatcha.module;

import com.mongodb.*;
import nl.chefbierfles.capatcha.models.enums.DatabaseFields;

import javax.annotation.Nullable;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class DatabaseModule {

    private static DBCollection players;
    private static DB playersDb;
    private static MongoClient client;
    public static boolean connect(String authUser, String encodedPwd, String hostName, String dbName){
        try {
            // Mongodb connection string.
            String client_url = "mongodb://" + authUser + ":" + encodedPwd + "@" + hostName + "/" + dbName;
            MongoClientURI uri = new MongoClientURI(client_url);
            client = new MongoClient(uri);
        } catch (UnknownHostException e) {
            System.out.println("Could not connect to database!");
            e.printStackTrace();
            return false;
        }

        playersDb = client.getDB("Capatcha");
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

        dbObject.put(DatabaseFields.CAPATCHA_LASTDONE_DATE.toString(), date.toInstant());
        players.insert(dbObject);
    }

    public static void updateCapatchaData(UUID uuid, Date date) {
        DBObject dbObject = new BasicDBObject("uuid", uuid);

        DBObject result = getResults(dbObject);

        if (result == null) {
            addCapatchaData(uuid, date);
            return;
        }

        DBObject newDbObj = new BasicDBObject("uuid", uuid);
        result.put(DatabaseFields.CAPATCHA_LASTDONE_DATE.toString(), date);
        players.update(result, newDbObj);
    }

    @Nullable
    public static Date getCapatchaData(UUID uuid) {
        DBObject dbObject = new BasicDBObject("uuid", uuid);

        DBObject result = getResults(dbObject);

        return Date.from((Instant)result.get(DatabaseFields.CAPATCHA_LASTDONE_DATE.toString()));
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
