package xyz.edge.ac;

import xyz.edge.ac.util.color.ColorUtil;
import org.bukkit.Bukkit;

public final class Messages
{
    public static void sendValidAuth(final String email, final String user) {
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&7&m-------------------------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&b  ______    _                     _____ "));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&b |  ____|  | |              /\\   / ____|"));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&b | |__   __| | __ _  ___   /  \\ | |     "));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&b |  __| / _` |/ _` |/ _ \\ / /\\ \\| |     "));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&b | |___| (_| | (_| |  __// ____ \\ |____ "));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&b |______\\__,_|\\__, |\\___/_/    \\_\\_____|"));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&b               __/ |                    "));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&b              |___/                     "));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate(""));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&7| &cLicensed to " + email + " [" + user + "]"));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate(""));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&7| &cYou are currently running latest version of &bEdge"));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&7| &bIf you need any help don't hesitate to contact us at &7discord.edgeac.xyz"));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&7&m-------------------------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate(""));
    }
    
    public static void start() {
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate(""));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&b&lLoading Edge &7| &c&lv" + Edge.getInstance().getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&cLoading..."));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&cAuthenticating..."));
    }
    
    public static void sendInvalid() {
        Error.sendAuthError();
    }
    
    private Messages() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
