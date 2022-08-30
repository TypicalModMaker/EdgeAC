package edgeproxy.edge.ac.listener;

import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import java.util.Iterator;
import com.google.common.io.ByteArrayDataInput;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ProxyServer;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.event.PluginMessageEvent;
import edgeac.shared.ac.StaffManager;
import net.md_5.bungee.api.plugin.Listener;

public class PluginMessageListener implements Listener
{
    private final StaffManager staffManager;
    
    public PluginMessageListener(final StaffManager staffManager) {
        this.staffManager = staffManager;
    }
    
    @EventHandler
    public void onMessage(final PluginMessageEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (!event.getTag().equalsIgnoreCase("edgeac:channel")) {
            return;
        }
        final ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        final String subChannel = in.readUTF();
        if (!subChannel.equals("notification")) {
            return;
        }
        final String notification = in.readUTF();
        for (final String staffName : this.staffManager.getAllStaff()) {
            final ProxiedPlayer staff = ProxyServer.getInstance().getPlayer(staffName);
            if (staff == null) {
                continue;
            }
            staff.sendMessage(TextComponent.fromLegacyText(notification));
        }
    }
}
