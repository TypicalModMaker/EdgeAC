package xyz.edge.ac;

import xyz.edge.ac.util.color.ColorUtil;
import org.bukkit.Bukkit;

public final class Messages
{
    public static void sendValidAuth(final String email, final String user) {
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&7&m-------------------------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&b   _____ _    _     _          _____ "));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&b / ____| |  (_)   | |   /\\   / ____| "));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&b | (___ | | ___  __| |  /  \\ | | "));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&b \\___ \\| |/ / |/ _` | / /\\ \\| | "));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&b  ____) |   <| | (_| |/ ____ \\ | "));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&b |_____/|_|\\_\\_|\\__,_/_/    \\_\\_____| "));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate(""));
        //Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&7| &cLicensed to " + email + " [" + user + "]"));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&cCracked by &45170 &c[&45170#5170&c]"));
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
