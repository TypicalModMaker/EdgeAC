package xyz.edge.ac.listener;

import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import xyz.edge.ac.user.MongoUser;
import xyz.edge.ac.user.User;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import xyz.edge.ac.storage.StorageMethod;
import xyz.edge.ac.Edge;
import org.bukkit.event.player.PlayerJoinEvent;
import edgeac.shared.ac.StaffManager;
import org.bukkit.event.Listener;

public class ConnectionListener implements Listener
{
    private final StaffManager staffManager;
    
    public ConnectionListener(final StaffManager staffManager) {
        this.staffManager = staffManager;
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (player.hasPermission("edge.alerts")) {
            this.staffManager.addStaff(player.getName());
        }
        if (Edge.getInstance().getStorageMethod() == StorageMethod.MONGO) {
            Bukkit.getServer().getScheduler().runTaskLater(Edge.getInstance(), () -> {
                final User localUser = Edge.getInstance().getUserManager().getPlayeruser(player);
                final MongoUser mongoUser = Edge.getInstance().getMongoUserManager().createIfRequired(player.getUniqueId(), player.getName());
                mongoUser.setUsername(player.getName());
                mongoUser.addClientBrand(localUser.getClientBrand());
                mongoUser.save();
                System.out.println("mongoUser=" + Edge.GSON.toJson(mongoUser));
            }, 10L);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final String name = player.getName();
        this.staffManager.removeStaff(name);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onKick(final PlayerKickEvent event) {
        final Player player = event.getPlayer();
        final String name = player.getName();
        this.staffManager.removeStaff(name);
    }
}
