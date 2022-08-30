package xyz.edge.ac;

import xyz.edge.ac.util.color.ColorUtil;
import org.bukkit.Bukkit;

public final class Error
{
    public static void sendAuthError() {
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&7[&6&lError&7] &7(&cError: 100&7)"));
    }
    
    public static void failedHooKTick() {
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&7[&6&lError&7] &7(&cError: 103&7)"));
    }
    
    public static void unsupportedServer() {
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&7[&6&lError&7] &7(&cError: 106&7)"));
    }
    
    public static void unsupportedSpigot() {
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&7[&6&lError&7] &7(&cError: 107&7)"));
    }
    
    public static void sendWindows() {
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&7[&6&lError&7] &7(&cError: 109&7)"));
    }
    
    private Error() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
