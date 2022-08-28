package xyz.edge.ac.user;

import java.util.Iterator;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

public class MongoUserManager
{
    private List<MongoUser> users;
    
    public MongoUserManager() {
        this.users = new ArrayList<MongoUser>();
    }
    
    public MongoUser create(final UUID uuid, final String username) {
        return (MongoUser)new MongoUser(uuid, username).save();
    }
    
    public boolean exists(final UUID uuid) {
        for (final MongoUser user : this.users) {
            if (user.getId().equalsIgnoreCase(uuid.toString())) {
                return true;
            }
        }
        return false;
    }
    
    public MongoUser get(final UUID uuid) {
        for (final MongoUser user : this.users) {
            if (user.getId().equalsIgnoreCase(uuid.toString())) {
                return user;
            }
        }
        return null;
    }
    
    public MongoUser createIfRequired(final UUID uuid, final String username) {
        if (this.exists(uuid)) {
            return this.get(uuid);
        }
        return this.create(uuid, username);
    }
    
    public List<MongoUser> getUsers() {
        return this.users;
    }
}
