package xyz.edge.ac.user;

import java.util.Collection;
import org.bukkit.entity.Player;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.List;

public final class UserManager
{
    private static final UserManager instance;
    public List<String> suspectedPlayers;
    public static SimpleDateFormat simpleDateFormat;
    private final Map<UUID, User> playeruserMap;
    
    public UserManager() {
        this.suspectedPlayers = new ArrayList<String>();
        this.playeruserMap = new ConcurrentHashMap<UUID, User>();
    }
    
    public User getPlayeruser(final Player player) {
        return this.playeruserMap.get(player.getUniqueId());
    }
    
    public void add(final Player player) {
        this.playeruserMap.put(player.getUniqueId(), new User(player));
    }
    
    public boolean has(final Player player) {
        return this.playeruserMap.containsKey(player.getUniqueId());
    }
    
    public void remove(final Player player) {
        this.playeruserMap.remove(player.getUniqueId());
    }
    
    public Collection<User> getAlluser() {
        return this.playeruserMap.values();
    }
    
    public static UserManager getInstance() {
        return UserManager.instance;
    }
    
    public List<String> getSuspectedPlayers() {
        return this.suspectedPlayers;
    }
    
    public Map<UUID, User> getPlayeruserMap() {
        return this.playeruserMap;
    }
    
    static {
        instance = new UserManager();
        UserManager.simpleDateFormat = new SimpleDateFormat("E, MMMMM d, yyyy hh:mm aaa");
    }
}
