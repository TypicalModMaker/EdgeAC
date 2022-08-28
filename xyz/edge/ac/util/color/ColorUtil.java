package xyz.edge.ac.util.color;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;

public final class ColorUtil
{
    public static String translate(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
    
    public static ArrayList<String> translate(final List<String> message) {
        final ArrayList<String> list = new ArrayList<String>();
        for (final String string : message) {
            list.add(translate(string));
        }
        return list;
    }
    
    private ColorUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
