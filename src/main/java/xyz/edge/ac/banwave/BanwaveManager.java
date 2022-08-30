package xyz.edge.ac.banwave;

import xyz.edge.ac.checks.EdgeCheck;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map;

public class BanwaveManager
{
    private final Map<UUID, BanwaveEntry> banwave;
    
    public BanwaveManager() {
        this.banwave = new HashMap<UUID, BanwaveEntry>();
    }
    
    public void addBanwave(final Player player, final EdgeCheck check) {
        this.banwave.put(player.getUniqueId(), new BanwaveEntry(System.currentTimeMillis(), player.getUniqueId(), check));
    }
    
    public void removeBanwave(final Player player) {
        this.banwave.remove(player.getUniqueId());
    }
    
    public boolean isInBanwave(final Player player) {
        return this.banwave.containsKey(player.getUniqueId());
    }
    
    public BanwaveEntry getBanwaveInfo(final Player player) {
        return this.banwave.get(player.getUniqueId());
    }
}
