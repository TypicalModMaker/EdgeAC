package xyz.edge.ac.listener;

import java.util.Iterator;
import xyz.edge.ac.util.anticheat.AlertUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import xyz.edge.ac.util.color.ColorUtil;
import io.github.retrooper.packetevents.PacketEvents;
import xyz.edge.ac.util.ProviderUtil;
import xyz.edge.ac.config.Config;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.event.EventHandler;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.edge.ac.user.User;
import java.io.UnsupportedEncodingException;
import xyz.edge.ac.util.SystemLogsUtil;
import java.util.Arrays;
import xyz.edge.ac.util.ClientHandler;
import xyz.edge.ac.Edge;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.PluginMessageListener;

public final class ClientBrandListener implements PluginMessageListener, Listener
{
    public void onPluginMessageReceived(final String channel, final Player player, final byte[] msg) {
        try {
            final User user = Edge.getInstance().getUserManager().getPlayeruser(player);
            if (user == null) {
                return;
            }
            user.setClientBrand(new String(msg, "UTF-8").substring(1));
            user.setJoinThing(user.getJoinThing() + 1);
            if (user.getJoinThing() == 1) {
                if (ClientHandler.process(new String(msg, "UTF-8").substring(1))) {
                    this.doJoinMessage(player, new String(msg, "UTF-8").substring(1));
                }
                else {
                    user.fuckOff();
                }
            }
        }
        catch (final UnsupportedEncodingException e) {
            SystemLogsUtil.createNewLog(Arrays.toString(e.getStackTrace()), "ClientBrandListener (PluginMessageReceived)", e.getMessage());
        }
    }
    
    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (ServerVersion.getVersion().isNewerThanOrEquals(ServerVersion.v_1_13)) {
            this.addChannel(player, "minecraft:brand");
        }
        else {
            this.addChannel(player, "MC|BRAND");
        }
    }
    
    private void addChannel(final Player player, final String channel) {
        try {
            player.getClass().getMethod("addChannel", String.class).invoke(player, channel);
        }
        catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            SystemLogsUtil.createNewLog(Arrays.toString(e.getStackTrace()), "ClientBrandListener (AddChannel)", e.getMessage());
        }
    }
    
    public void doJoinMessage(final Player player, final String payload) {
        if (Config.CLIENTJOINENABLED) {
            final User user = Edge.getInstance().getUserManager().getPlayeruser(player);
            final StringBuilder stringBuilder = new StringBuilder();
            final int listSize = Config.CLIENTJOINHOVER.size();
            int i = 1;
            for (final String hoverMessages : Config.CLIENTJOINHOVER) {
                if (i == listSize) {
                    stringBuilder.append(hoverMessages);
                }
                else {
                    stringBuilder.append(hoverMessages).append("\n");
                }
                ++i;
            }
            final TextComponent alertMessage = new TextComponent(ColorUtil.translate(Config.CLIENTJOINMESSAGE.replace("%player%", user.getPlayer().getName()).replace("%brand%", payload.replace(" (Velocity)", "")).replace("%ip%", player.getAddress().getAddress().getHostAddress()).replace("%host%", player.getAddress().getAddress().getHostName()).replace("%provider%", ProviderUtil.getProvider(player)).replace("%version%", String.valueOf(PacketEvents.get().getPlayerUtils().getClientVersion(player)))));
            final String hoverMessage = ColorUtil.translate(stringBuilder.toString().replace("%player%", user.getPlayer().getName()).replace("%brand%", payload.replace(" (Velocity)", "")).replace("%ip%", player.getAddress().getAddress().getHostAddress()).replace("%provider%", ProviderUtil.getProvider(player)).replace("%host%", player.getAddress().getAddress().getHostName()).replace("%version%", String.valueOf(PacketEvents.get().getPlayerUtils().getClientVersion(player))));
            alertMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverMessage).create()));
            alertMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + Config.CLIENTJOINCMD.replace("%player%", user.getPlayer().getName()).replace("%command%", Edge.getInstance().getCommand())));
            AlertUtil.sendSpigotMessage(alertMessage);
        }
    }
}
