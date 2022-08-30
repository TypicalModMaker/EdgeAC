package xyz.edge.ac.util;

import xyz.edge.api.license.LicenseType;
import xyz.edge.ac.Edge;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.IOException;
import java.io.File;

public final class CustomConfig
{
    private static String authKey;
    private static String zerotrustmessage;
    private static String zerotrustbypass;
    private static boolean zerotrustEnabled;
    private static boolean bstatsEnabled;
    private static boolean antibotEnabled;
    
    public static void createAuthConfig() {
        final File f = new File("plugins/Edge/authentication.yml");
        if (!f.exists()) {
            try {
                f.createNewFile();
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
        yml.addDefault("auth.key", "xxxx-xxxx-xxxx-xxxx");
        try {
            yml.options().copyDefaults(true);
            yml.save(f);
        }
        catch (final IOException e2) {
            e2.printStackTrace();
        }
        CustomConfig.authKey = yml.getString("auth.key");
    }
    
    public static void createZeroTrust() {
        if (Edge.getInstance().getLicenseType() == LicenseType.ENTERPRISE) {
            final File f = new File("plugins/Edge/enterprise/zerotrust.yml");
            if (!f.exists()) {
                try {
                    f.createNewFile();
                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
            yml.addDefault("zerotrust.enabled", false);
            yml.addDefault("zerotrust.message", "[ZeroTrust] %player% / %command%");
            yml.addDefault("zerotrust.bypass", "edge.zerotrust.bypass");
            try {
                yml.options().copyDefaults(true);
                yml.save(f);
            }
            catch (final IOException e2) {
                e2.printStackTrace();
            }
            CustomConfig.zerotrustbypass = yml.getString("zerotrust.bypass");
            CustomConfig.zerotrustmessage = yml.getString("zerotrust.message");
            CustomConfig.zerotrustEnabled = yml.getBoolean("zerotrust.enabled");
        }
    }
    
    public static void createAntiBot() {
        if (Edge.getInstance().getLicenseType() == LicenseType.ENTERPRISE) {
            final File f = new File("plugins/Edge/enterprise/antibot.yml");
            if (!f.exists()) {
                try {
                    f.createNewFile();
                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
            yml.addDefault("antibot.enabled", true);
            try {
                yml.options().copyDefaults(true);
                yml.save(f);
            }
            catch (final IOException e2) {
                e2.printStackTrace();
            }
            CustomConfig.antibotEnabled = yml.getBoolean("zerotrust.enabled");
        }
    }
    
    public static void createBstats() {
        final File f = new File("plugins/Edge/bstats.yml");
        if (!f.exists()) {
            try {
                f.createNewFile();
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
        yml.addDefault("bstats.enabled", true);
        try {
            yml.options().copyDefaults(true);
            yml.save(f);
        }
        catch (final IOException e2) {
            e2.printStackTrace();
        }
        CustomConfig.bstatsEnabled = yml.getBoolean("bstats.enabled");
    }
    
    public static void createLogsFile() {
        final File f = new File("plugins/Edge/errorlogs/errorlogs.yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
        if (!f.exists()) {
            try {
                f.createNewFile();
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }
        try {
            yml.options().copyDefaults(true);
            yml.save(f);
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    private CustomConfig() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    public static String getAuthKey() {
        return CustomConfig.authKey;
    }
    
    public static String getZerotrustmessage() {
        return CustomConfig.zerotrustmessage;
    }
    
    public static String getZerotrustbypass() {
        return CustomConfig.zerotrustbypass;
    }
    
    public static boolean isZerotrustEnabled() {
        return CustomConfig.zerotrustEnabled;
    }
    
    public static boolean isBstatsEnabled() {
        return CustomConfig.bstatsEnabled;
    }
    
    public static boolean isAntibotEnabled() {
        return CustomConfig.antibotEnabled;
    }
}
