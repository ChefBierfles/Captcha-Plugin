package nl.chefbierfles.captcha.module;

import com.mongodb.*;
import nl.chefbierfles.captcha.Plugin;
import nl.chefbierfles.captcha.models.constants.DatabaseFields;
import nl.chefbierfles.captcha.module.base.BaseModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class DatabaseModule extends BaseModule {

    private DBCollection players;
    private DB playersDb;
    private MongoClient client;

    public DatabaseModule() {

        name = this.getClass().getName();
        isEnabled = true;

        if (!connect(
                "admin",
                "O5oHINE77BvE",
                "cluster0.zfbz8.mongodb.net", "Capatcha")) {
            //Don't launch plugin if database connection doesnt succeeed
            JavaPlugin.getPlugin(Plugin.class).onDisable();
        }
    }

    public boolean connect(String authUser, String encodedPwd, String hostName, String dbName) {
        // Mongodb connection string.

        String client_url = "mongodb+srv://" + authUser + ":" + encodedPwd + "@" + hostName + "/" + dbName + "?retryWrites=true&w=majority";

        MongoClientURI uri = new MongoClientURI(client_url);
        client = new MongoClient(uri);

        playersDb = client.getDB("Capatcha");
        players = playersDb.getCollection("players");
        return true;
    }

    public void addCapatchaData(UUID uuid, Date date) {
        DBObject dbObject = new BasicDBObject("uuid", uuid);
        DBObject result = getResults(dbObject);

        if (result != null) {
            //Result already exists
            updateCapatchaData(uuid, date);
            return;
        }

        dbObject.put(DatabaseFields.CAPATCHA_LASTDONE_DATE, date);
        players.insert(dbObject);
    }

    public void updateCapatchaData(UUID uuid, Date date) {
        DBObject dbObject = new BasicDBObject("uuid", uuid);

        DBObject found = players.findOne(dbObject);
        if (found == null) {
            addCapatchaData(uuid, date);
            return;
        }

        DBObject obj = new BasicDBObject("uuid", uuid);
        obj.put(DatabaseFields.CAPATCHA_LASTDONE_DATE, date);

        players.update(found, obj);
    }

    public Date getCaptchaData(Player player) {

        CompletableFuture<Date> completableFuture = CompletableFuture.supplyAsync(() -> {
            DBObject dbObject = new BasicDBObject("uuid", player.getUniqueId());

            DBObject result = getResults(dbObject);

            return (result == null) ? null : (Date) result.get(DatabaseFields.CAPATCHA_LASTDONE_DATE);
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
