package xyz.edge.ac.util.proxy;

import java.util.Collection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import java.io.IOException;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import xyz.edge.ac.Edge;

public class ProxyUtil
{
    private final Edge plugin;
    
    public ProxyUtil(final Edge plugin) {
        this.plugin = plugin;
    }
    
    public void sendPluginMessage(final String notification) {
        final Player randomPlayer = this.getRandomPlayer();
        if (randomPlayer != null) {
            final ByteArrayOutputStream b = new ByteArrayOutputStream();
            final DataOutputStream out = new DataOutputStream(b);
            try {
                out.writeUTF("notification");
                out.writeUTF(notification);
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
            randomPlayer.sendPluginMessage(this.plugin, "edgeac:channel", b.toByteArray());
        }
    }
    
    private Player getRandomPlayer() {
        final Collection<? extends Player> players = this.plugin.getServer().getOnlinePlayers();
        if (!players.isEmpty()) {
            final int i = (int)(players.size() * Math.random());
            return players.toArray(new Player[0])[i];
        }
        return null;
    }
}
