package xyz.edge.ac.storage;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClient;
import xyz.edge.ac.util.SystemLogsUtil;
import java.util.Arrays;
import com.mongodb.client.MongoClients;
import org.bson.Document;
import com.mongodb.client.MongoCollection;

public class MongoManager
{
    private boolean mongoEnabled;
    private MongoCollection<Document> data;
    private MongoCollection<Document> logs;
    
    public MongoManager(final String URI, final String databaseName) {
        this.mongoEnabled = false;
        final MongoClient mongoClient = MongoClients.create(URI);
        try {
            final MongoDatabase database = mongoClient.getDatabase(databaseName);
            this.data = database.getCollection("data");
            this.logs = database.getCollection("logs");
        }
        catch (final Exception e) {
            SystemLogsUtil.createNewLog(Arrays.toString(e.getStackTrace()), "Failed to connect to Mongo", e.getMessage());
        }
    }
    
    public boolean isMongoEnabled() {
        return this.mongoEnabled;
    }
    
    public void setMongoEnabled(final boolean mongoEnabled) {
        this.mongoEnabled = mongoEnabled;
    }
    
    public MongoCollection<Document> getData() {
        return this.data;
    }
    
    public MongoCollection<Document> getLogs() {
        return this.logs;
    }
}
