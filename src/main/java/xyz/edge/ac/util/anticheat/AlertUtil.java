package xyz.edge.ac.util.anticheat;

import org.bukkit.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.play.out.kickdisconnect.WrappedPacketOutKickDisconnect;
import org.bukkit.entity.Player;
import xyz.edge.ac.Edge;
import java.util.Iterator;
import java.io.IOException;
import xyz.edge.ac.util.discord.DiscordWebhook;
import xyz.edge.ac.util.logger.Logs;
import java.util.Date;
import org.bukkit.event.Event;
import org.bukkit.Bukkit;
import xyz.edge.api.check.APICheck;
import xyz.edge.api.listener.EdgeSendAlert;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import xyz.edge.ac.util.utils.ServerUtil;
import java.text.DecimalFormat;
import xyz.edge.ac.util.utils.PlayerUtil;
import xyz.edge.ac.util.color.ColorUtil;
import xyz.edge.ac.config.Config;
import xyz.edge.ac.user.User;
import xyz.edge.ac.checks.EdgeCheck;
import java.text.SimpleDateFormat;
import edgeac.shared.ac.StaffManager;

public final class AlertUtil
{
    private static StaffManager staffManager;
    public static SimpleDateFormat simpleDateFormat;
    private static long lastAlert;
    private static long lastAlertFlag;
    
    public static void handleAlert(final EdgeCheck check, final User user, final String info, final String debug) {
        if (System.currentTimeMillis() - AlertUtil.lastAlertFlag > Config.ALERTDELAY) {
            final TextComponent alertMessage = new TextComponent(ColorUtil.translate(Config.ALERT).replaceAll("%player%", user.getPlayer().getName()).replaceAll("%uuid%", user.getPlayer().getUniqueId().toString()).replaceAll("%check%", check.getCustomCheckName().isEmpty() ? ColorUtil.translate(check.getJustTheName()) : check.getCustomCheckName()).replaceAll("%ping%", Integer.toString(PlayerUtil.getPing(user.getPlayer()))).replaceAll("%type%", check.getCustomCheckType().isEmpty() ? ColorUtil.translate(check.getCheckInfo().type()) : check.getCustomCheckType()).replaceAll("%setback%", String.valueOf(check.isSetback())).replaceAll("%dev%", check.getCheckInfo().experimental() ? ColorUtil.translate(Config.DEVTHING) : "").replaceAll("%displayname%", user.getPlayer().getDisplayName()).replaceAll("%vl%", Integer.toString(check.getVl())).replaceAll("%info%", info).replaceAll("%debug%", debug).replaceAll("%ping%", Integer.toString(PlayerUtil.getPing(user.getPlayer()))).replaceAll("%tps%", new DecimalFormat("##.##").format(ServerUtil.getTPS())));
            final String proxyAlert = ColorUtil.translate(Config.PROXYALERT).replaceAll("%player%", user.getPlayer().getName()).replaceAll("%uuid%", user.getPlayer().getUniqueId().toString()).replaceAll("%check%", check.getCustomCheckName().isEmpty() ? ColorUtil.translate(check.getJustTheName()) : check.getCustomCheckName()).replaceAll("%ping%", Integer.toString(PlayerUtil.getPing(user.getPlayer()))).replaceAll("%type%", check.getCustomCheckType().isEmpty() ? ColorUtil.translate(check.getCheckInfo().type()) : check.getCustomCheckType()).replaceAll("%setback%", String.valueOf(check.isSetback())).replaceAll("%displayname%", user.getPlayer().getDisplayName()).replaceAll("%info%", info).replaceAll("%debug%", debug).replaceAll("%dev%", check.getCheckInfo().experimental() ? ColorUtil.translate(Config.DEVTHING) : "").replaceAll("%vl%", Integer.toString(check.getVl())).replaceAll("%ping%", Integer.toString(PlayerUtil.getPing(user.getPlayer()))).replaceAll("%tps%", new DecimalFormat("##.##").format(ServerUtil.getTPS()));
            final StringBuilder stringBuilder = new StringBuilder();
            final int listSize = Config.HOVER.size();
            int i = 1;
            for (final String hoverMessages : Config.HOVER) {
                if (i == listSize) {
                    stringBuilder.append(hoverMessages);
                }
                else {
                    stringBuilder.append(hoverMessages).append("\n");
                }
                ++i;
            }
            final String hoverMessage = ColorUtil.translate(stringBuilder.toString().replaceAll("%info%", info).replaceAll("%debug%", debug).replaceAll("%ping%", Integer.toString(PlayerUtil.getPing(user.getPlayer()))).replaceAll("%tps%", new DecimalFormat("##.##").format(ServerUtil.getTPS()))).replaceAll("%vl%", String.valueOf(check.getVl())).replaceAll("%player%", user.getPlayer().getName()).replaceAll("%displayname%", user.getPlayer().getDisplayName()).replaceAll("%check%", check.getCustomCheckName().isEmpty() ? ColorUtil.translate(check.getJustTheName()) : check.getCustomCheckName()).replaceAll("%setback%", String.valueOf(check.isSetback()));
            alertMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + user.getPlayer().getName()));
            alertMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverMessage).create()));
            final EdgeSendAlert event = new EdgeSendAlert(alertMessage, user.getPlayer(), check, info);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
            sendSpigotMessage(alertMessage);
            if (Config.PROXYENABLED) {
                sendProxyAlets(proxyAlert);
            }
            AlertUtil.lastAlertFlag = System.currentTimeMillis();
            if (Config.LOG) {
                final String log = Config.LOGMSG.replaceAll("%player%", user.getPlayer().getName()).replaceAll("%player%", user.getPlayer().getName()).replaceAll("%uuid%", user.getPlayer().getUniqueId().toString()).replaceAll("%check%", check.getCustomCheckName().isEmpty() ? ColorUtil.translate(check.getJustTheName()) : check.getCustomCheckName()).replaceAll("%ping%", Integer.toString(PlayerUtil.getPing(user.getPlayer()))).replaceAll("%type%", check.getCustomCheckType().isEmpty() ? ColorUtil.translate(check.getCheckInfo().type()) : check.getCustomCheckType()).replaceAll("%dev%", check.getCheckInfo().experimental() ? ColorUtil.translate(Config.DEVTHING) : "").replaceAll("%vl%", Integer.toString(check.getVl())).replaceAll("%displayname%", user.getPlayer().getDisplayName()).replaceAll("%setback%", String.valueOf(user.isSetBack())).replaceAll("%ping%", Integer.toString(PlayerUtil.getPing(user.getPlayer()))).replaceAll("%time%", AlertUtil.simpleDateFormat.format(new Date(System.currentTimeMillis()))).replaceAll("%info%", info).replaceAll("%debug%", debug).replaceAll("%tps%", new DecimalFormat("##.##").format(ServerUtil.getTPS()));
                Logs.logToFile(user.getLogFile(), log);
            }
        }
        if (Config.DISCORDALERTSENABLED && System.currentTimeMillis() - AlertUtil.lastAlert > 1000L) {
            final StringBuilder discordMessage = new StringBuilder();
            final int listSize2 = Config.DISCORDALERTSMESSAGE.size();
            int j = 1;
            for (final String hoverMessages2 : Config.DISCORDALERTSMESSAGE) {
                if (j == listSize2) {
                    discordMessage.append(hoverMessages2);
                }
                else {
                    discordMessage.append(hoverMessages2).append("\\n");
                }
                ++j;
            }
            final DiscordWebhook webhook = new DiscordWebhook(Config.DISCORDALERTSURL);
            webhook.setUsername(Config.DISCORDUSER);
            webhook.addEmbed(new DiscordWebhook.EmbedObject().setAuthor(Config.DISCORDALERTSTITLE, null, null).setDescription("```md\\n" + discordMessage.toString().replaceAll("%player%", user.getPlayer().getName()).replaceAll("%uuid%", user.getPlayer().getUniqueId().toString()).replaceAll("%check%", check.getCustomCheckName().isEmpty() ? ColorUtil.translate(check.getJustTheName()) : check.getCustomCheckName()).replaceAll("%ping%", Integer.toString(PlayerUtil.getPing(user.getPlayer()))).replaceAll("%type%", check.getCustomCheckType().isEmpty() ? ColorUtil.translate(check.getCheckInfo().type()) : check.getCustomCheckType()).replaceAll("%info%", info).replaceAll("%debug%", debug).replaceAll("%setback%", String.valueOf(check.isSetback())).replaceAll("%displayname%", user.getPlayer().getDisplayName()).replaceAll("%dev%", check.getCheckInfo().experimental() ? ColorUtil.translate("&c\u21af") : "").replaceAll("%vl%", Integer.toString(check.getVl())).replaceAll("%ping%", Integer.toString(PlayerUtil.getPing(user.getPlayer()))).replaceAll("%tps%", new DecimalFormat("##.##").format(ServerUtil.getTPS())) + "```").setThumbnail(Config.DISCORDALERTSIMAGESERVICE + user.getPlayer().getName() + "/" + Config.DISCORDALERTSIMAGESIZE).setFooter(AlertUtil.simpleDateFormat.format(new Date(System.currentTimeMillis())), ""));
            try {
                webhook.execute();
            }
            catch (final IOException exception) {
                exception.printStackTrace();
            }
        }
        AlertUtil.lastAlert = System.currentTimeMillis();
    }
    
    public static void sendMessage(final String message) {
        for (final User user : Edge.getInstance().getUserManager().getPlayeruserMap().values()) {
            if (user.isAlerts()) {
                user.getPlayer().sendMessage(message);
            }
        }
    }
    
    public static void sendAntiBot(final Player player, final String method, final int vl) {
        for (final User user : Edge.getInstance().getUserManager().getPlayeruserMap().values()) {
            final WrappedPacketOutKickDisconnect wrapper = new WrappedPacketOutKickDisconnect("[Edge] Blacklisted from server!");
            PacketEvents.get().getPlayerUtils().sendPacket(player, wrapper);
            if (user.isAlerts()) {
                user.getPlayer().sendMessage(ColorUtil.translate(Config.PREFIX + player.getName() + " &cwas blacklisted! &7[&b" + method + "&7]"));
            }
        }
    }
    
    public static void sendSpigotMessage(final TextComponent message) {
        for (final User user : Edge.getInstance().getUserManager().getPlayeruserMap().values()) {
            if (user.isAlerts()) {
                user.getPlayer().spigot().sendMessage(message);
            }
        }
    }
    
    public static void sendProxyAlets(final String string) {
        final StaffManager staffManager = new StaffManager();
        for (final String staffName : staffManager.getAllStaff()) {
            final Player staff = Edge.getInstance().getServer().getPlayer(staffName);
            if (staff == null) {
                Bukkit.broadcastMessage("null");
            }
            if (staff == null) {
                continue;
            }
            staff.sendMessage(ChatColor.translateAlternateColorCodes('&', string.replace("%server%", Config.PROXYSERVERNAME)));
        }
    }
    
    private AlertUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    static {
        AlertUtil.simpleDateFormat = new SimpleDateFormat("E, MMMMM d, yyyy hh:mm aaa");
    }
    
    public enum ToggleAlertType
    {
        ADD, 
        REMOVE;
    }
}
