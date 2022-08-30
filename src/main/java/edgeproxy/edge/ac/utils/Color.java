package edgeproxy.edge.ac.utils;

import net.md_5.bungee.api.ChatColor;

public class Color
{
    public static String translate(final String source) {
        return ChatColor.translateAlternateColorCodes('&', source);
    }
}
