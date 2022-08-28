package xyz.edge.ac.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;

private static final class Data
{
    private static final int VERSION;
    private static final boolean ISFLAT;
    
    static {
        final String version = Bukkit.getVersion();
        final Matcher matcher = Pattern.compile("MC: \\d\\.(\\d+)").matcher(version);
        if (matcher.find()) {
            VERSION = Integer.parseInt(matcher.group(1));
            ISFLAT = XMaterial.supports(13);
            return;
        }
        throw new IllegalArgumentException("Failed to parse server version from: " + version);
    }
}
