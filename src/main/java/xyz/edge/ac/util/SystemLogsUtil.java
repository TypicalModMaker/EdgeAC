package xyz.edge.ac.util;

import java.io.IOException;
import java.util.Date;
import java.io.PrintWriter;
import xyz.edge.ac.util.color.ColorUtil;
import xyz.edge.ac.config.Config;
import org.bukkit.Bukkit;
import java.io.File;
import java.text.SimpleDateFormat;

public final class SystemLogsUtil
{
    public static SimpleDateFormat simpleDateFormat;
    
    public static void createNewLog(final String string, final String place, final String message) {
        final File file = new File("plugins/Edge/errorlogs/errorlogs.yml");
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate(Config.PREFIX + "An error occurred! &7A stacktrace occurred At: " + place + " &7| &Cplugins/Edge/edgelogs.yml &7(&cReason: &c" + message + "&7)"));
        try {
            final PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.println("");
            writer.println("New StackTrace on: " + SystemLogsUtil.simpleDateFormat.format(new Date(System.currentTimeMillis())));
            writer.println(string);
            writer.println("");
            writer.close();
        }
        catch (final IOException ex) {}
    }
    
    private SystemLogsUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    static {
        SystemLogsUtil.simpleDateFormat = new SimpleDateFormat("E, MMMMM d, yyyy hh:mm aaa");
    }
}
