package edgeproxy.edge.ac.listener;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import edgeac.shared.ac.StaffManager;
import net.md_5.bungee.api.plugin.Listener;

public class ConnectionListener implements Listener
{
    private final StaffManager staffManager;
    
    public ConnectionListener(final StaffManager staffManager) {
        this.staffManager = staffManager;
    }
    
    @EventHandler(priority = 32)
    public void onPostLogin(final PostLoginEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        if (player.hasPermission("edge.alerts")) {
            this.staffManager.addStaff(player.getName());
        }
    }
    
    @EventHandler(priority = 32)
    public void onDisconnect(final PlayerDisconnectEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        final String name = player.getName();
        this.staffManager.removeStaff(name);
    }
}
