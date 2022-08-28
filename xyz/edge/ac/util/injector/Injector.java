package xyz.edge.ac.util.injector;

import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import xyz.edge.ac.user.User;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import xyz.edge.ac.util.CustomConfig;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import java.util.Arrays;
import xyz.edge.ac.user.impl.Collision;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.util.utils.ServerUtil;
import xyz.edge.ac.util.color.ColorUtil;
import xyz.edge.ac.config.Config;
import xyz.edge.ac.Edge;
import org.bukkit.event.player.PlayerJoinEvent;
import java.util.UUID;
import org.bukkit.event.Listener;

public final class Injector implements Listener
{
    public static UUID[] EDGE_DEVELOPERS;
    
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Edge.getInstance().getUserManager().add(event.getPlayer());
        final User user = Edge.getInstance().getUserManager().getPlayeruser(event.getPlayer());
        final Player player = event.getPlayer();
        if (Config.BEDROCKSUPPORTENABLED && Config.BEDROCKKICK && player.getName().contains(Config.BEDROCKKEY)) {
            player.kickPlayer(ColorUtil.translate(Config.BEDROCKKICKREASON));
        }
        if (ServerUtil.getServerVersion().isNewerThanOrEquals(ServerVersion.v_1_9)) {
            Collision.updateCollision(player);
        }
        if (event.getPlayer().hasPermission("edge.alerts")) {
            user.setAlerts(true);
        }
        if (Arrays.stream(Injector.EDGE_DEVELOPERS).anyMatch(uuid -> uuid.toString().equalsIgnoreCase(player.getUniqueId().toString()))) {
            final String customPluginName = Edge.getInstance().getName();
            player.sendMessage("");
            final TextComponent message = new TextComponent(ColorUtil.translate("&7Welcome back &b" + player.getName() + "&7, This server is running &b&lEdge" + (customPluginName.equals("Edge") ? "&7" : (" &7(" + customPluginName + ")")) + " &Cv" + Edge.getInstance().getDescription().getVersion()));
            player.sendMessage("");
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + Edge.getInstance().getCommand()));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtil.translate("&bLicenceKey&7: " + CustomConfig.getAuthKey() + "\n&bLicenceType: &7" + Edge.getInstance().getLicenseType().name() + "\n&bLicenceEmail&7: " + Edge.getInstance().getLicenceEmail() + "\n&bLicenceOwner&7: " + Edge.getInstance().getLicenceUsername() + "\n&bChecks&7: " + user.getChecks().size() + "\n\n&cClick to run /" + Edge.getInstance().getCommand())).create()));
            player.spigot().sendMessage(message);
        }
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        Edge.getInstance().getUserManager().remove(event.getPlayer());
    }
    
    static {
        Injector.EDGE_DEVELOPERS = new UUID[] { UUID.fromString("2c0f01ff-8e4a-49c4-939a-c9694568fe57"), UUID.fromString("5099c405-9bfb-40f0-b1e5-b3ca00f9a36a") };
    }
}
