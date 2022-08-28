package xyz.edge.ac.bothandler;

import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.Listener;

public class Bothandler implements Listener
{
    @EventHandler
    public void ping(final ServerListPingEvent event) {
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onConnection(final PlayerJoinEvent event) {
    }
}
