package xyz.edge.ac.util;

import org.bukkit.plugin.Plugin;
import org.bson.conversions.Bson;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Filters;
import org.bukkit.Bukkit;
import xyz.edge.ac.Edge;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.google.gson.annotations.SerializedName;

public abstract class Storable
{
    @SerializedName("_id")
    private final String id;
    
    public Storable(final String id) {
        this.id = id;
    }
    
    public abstract MongoCollection<Document> getCollection();
    
    public final Storable save() {
        final Document document = Document.parse(Edge.GSON.toJson(this));
        final Document update = new Document("$set", document);
        Bukkit.getServer().getScheduler().runTaskAsynchronously(Edge.getInstance(), () -> {
            this.getCollection().updateOne(Filters.eq("_id", this.id), update, new UpdateOptions().upsert(true));
            this.onSave();
            return;
        });
        return this;
    }
    
    public final Storable destroy() {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(Edge.getInstance(), () -> {
            final Document document = this.getCollection().find(Filters.eq("_id", this.id)).first();
            if (document == null) {
                throw new NullPointerException("Document cannot be null.");
            }
            else {
                this.getCollection().deleteOne(document);
                this.onDestroy();
                return;
            }
        });
        return this;
    }
    
    public void onSave() {
    }
    
    public void onDestroy() {
    }
    
    public String getId() {
        return this.id;
    }
}
