package xyz.edge.ac.user;

import xyz.edge.ac.Edge;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import java.util.ArrayList;
import java.util.UUID;
import java.util.List;
import xyz.edge.ac.util.Storable;

public class MongoUser extends Storable
{
    private String username;
    private final List<String> clientBrandHistory;
    
    public MongoUser(final UUID uuid, final String username) {
        super(uuid.toString());
        this.clientBrandHistory = new ArrayList<String>();
        this.username = username;
    }
    
    @Override
    public MongoCollection<Document> getCollection() {
        return Edge.getInstance().getMongoManager().getData();
    }
    
    public boolean addClientBrand(final String clientBrand) {
        if (this.clientBrandHistory.contains(clientBrand)) {
            return false;
        }
        this.clientBrandHistory.add(clientBrand);
        return true;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    public List<String> getClientBrandHistory() {
        return this.clientBrandHistory;
    }
}
