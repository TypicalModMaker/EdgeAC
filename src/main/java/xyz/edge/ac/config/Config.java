package xyz.edge.ac.config;

import java.util.HashMap;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.File;
import xyz.edge.api.check.DetectionData;
import xyz.edge.api.license.LicenseType;
import xyz.edge.ac.checks.checkloader.CheckLoader;
import xyz.edge.ac.Edge;
import xyz.edge.ac.util.type.Pair;
import org.bukkit.configuration.file.YamlConfiguration;
import java.util.Map;
import java.util.List;

public final class Config
{
    public static boolean LOG;
    public static boolean TOGGLEJOINALERTS;
    public static boolean GHOSTENABLED;
    public static boolean GHOSTUPDATE;
    public static boolean GHOSTSETBACK;
    public static boolean CLIENTJOINENABLED;
    public static boolean DISCORDALERTSENABLED;
    public static boolean PROXYENABLED;
    public static boolean BEDROCKKICK;
    public static boolean BEDROCKSUPPORTENABLED;
    public static boolean BLOCKBOOKS;
    public static String PREFIX;
    public static String BANCMD;
    public static String ALERT;
    public static String LOGMSG;
    public static String BREAKER;
    public static String NOLOGSFORPLAYER;
    public static String BASICLOGSTITLE;
    public static String BASICLOGLAYOUT;
    public static String EXPLOIT_KICK;
    public static String EXPLOIT_ALERT;
    public static String CRASHALERT;
    public static String CLIENTJOINMESSAGE;
    public static String DISCORDUSER;
    public static String DISCORDALERTSURL;
    public static String DISCORDALERTSTITLE;
    public static String DISCORDALERTSIMAGESIZE;
    public static String DISCORDALERTSIMAGESERVICE;
    public static String PROXYSERVERNAME;
    public static String PROXYALERT;
    public static String CLIENTJOINCMD;
    public static String DEVTHING;
    public static String BEDROCKKEY;
    public static String BEDROCKKICKREASON;
    public static String PERMISSION;
    public static String LOGNORESULT;
    public static String LOGNOENTRIES;
    public static String ALERTS_ENABLED;
    public static String ALERTS_DISABLED;
    public static String CONSOLE_ONLY;
    public static String PLAYER_ONLY;
    public static int ALERTDELAY;
    public static int GHOSTTICKS;
    public static int LOGSPERPAGE;
    public static List<String> GLOBAL_BAN_COMMANDS;
    public static List<String> ENABLEDCHECKS;
    public static List<String> HOVER;
    public static List<String> BANNINGCHECKS;
    public static List<String> SETBACKCHECKS;
    public static List<String> PAGE1;
    public static List<String> CLIENTJOINHOVER;
    public static List<String> DISCORDALERTSMESSAGE;
    public static List<String> GLOBALCMDSCHECKS;
    public static Map<String, String> CUSTOMCHECKNAME;
    public static Map<String, String> CUSTOMCHECKTYPE;
    public static YamlConfiguration checkConfig;
    public static Map<String, List<Pair<Integer, String>>> PUNISH_COMMANDS;
    
    public static void updateConfig() {
        try {
            Config.PREFIX = getStringFromMessages("global.prefix");
            Config.BLOCKBOOKS = getBooleanFromConfig("settings.block-books");
            Config.CLIENTJOINENABLED = getBooleanFromConfig("settings.client-join-message");
            Config.CLIENTJOINMESSAGE = getStringFromMessages("client-join.message");
            Config.CLIENTJOINHOVER = getStringListFromMessages("client-join.hover");
            Config.CLIENTJOINCMD = getStringFromConfig("settings.client-join-click-cmd");
            Config.PROXYENABLED = getBooleanFromConfig("settings.proxy");
            Config.PROXYSERVERNAME = getStringFromConfig("settings.servername");
            Config.PROXYALERT = getStringFromMessages("alerts.proxy-message");
            Config.PERMISSION = getStringFromMessages("global.permission");
            Config.BEDROCKSUPPORTENABLED = getBooleanChecks("bedrock.enabled");
            Config.BEDROCKKICK = getBooleanFromConfig("bedrock.kick");
            Config.BEDROCKKICKREASON = getStringFromMessages("bedrock.reason");
            Config.BEDROCKKEY = getStringFromConfig("bedrock.key");
            Config.DISCORDUSER = getStringFromConfig("discord.user");
            Config.DISCORDALERTSENABLED = getBooleanFromConfig("discord.alerts.enabled");
            Config.DISCORDALERTSURL = getStringFromConfig("discord.alerts.url");
            Config.DISCORDALERTSTITLE = getStringFromConfig("discord.alerts.message.title");
            Config.DISCORDALERTSMESSAGE = getStringListFromConfig("discord.alerts.message.message");
            Config.DISCORDALERTSIMAGESIZE = getStringFromConfig("discord.alerts.thumbnail.imagesize");
            Config.DISCORDALERTSIMAGESERVICE = getStringFromConfig("discord.alerts.thumbnail.imageservice");
            Config.GHOSTENABLED = getBooleanFromConfig("ghostblock.enabled");
            Config.GHOSTUPDATE = getBooleanFromConfig("ghostblock.update");
            Config.GHOSTSETBACK = getBooleanFromConfig("ghostblock.dragdown");
            Config.GHOSTTICKS = getIntegerFromConfig("ghostblock.ticks");
            Config.EXPLOIT_KICK = getStringFromMessages("crash.kick-reason");
            Config.LOG = getBooleanFromConfig("settings.logs");
            Config.LOGNOENTRIES = getStringFromMessages("logs.no-entries");
            Config.LOGNORESULT = getStringFromMessages("logs.no-results");
            Config.LOGSPERPAGE = getIntegerFromConfig("settings.logs-per-page");
            Config.NOLOGSFORPLAYER = getStringFromMessages("logs.nologs");
            Config.BASICLOGSTITLE = getStringFromMessages("logs.title");
            Config.TOGGLEJOINALERTS = getBooleanFromConfig("settings.alert-on-join");
            Config.ALERTDELAY = getIntegerFromConfig("settings.alert-delay");
            Config.CRASHALERT = getStringFromMessages("crash.alert");
            Config.GLOBAL_BAN_COMMANDS = getStringListChecks("settings.globalcmds");
            Config.DEVTHING = getStringFromMessages("alerts.dev");
            Config.ALERT = getStringFromMessages("alerts.message");
            Config.LOGMSG = getStringFromMessages("logs.layout");
            Config.BASICLOGLAYOUT = getStringFromMessages("logs.layout");
            Config.HOVER = getStringListFromMessages("alerts.hover");
            Config.BREAKER = getStringFromMessages("global.breaker");
            Config.CONSOLE_ONLY = getStringFromMessages("global.console-only");
            Config.PLAYER_ONLY = getStringFromMessages("global.player-only");
            Config.ALERTS_ENABLED = getStringFromMessages("alerts.enabled");
            Config.ALERTS_DISABLED = getStringFromMessages("alerts.disabled");
            Config.PAGE1 = getStringListFromMessages("command");
            for (int i = 0; i < Config.PAGE1.size(); ++i) {
                Config.PAGE1.set(i, Config.PAGE1.get(i).replace("%command%", Edge.getInstance().getCommand()).replace("%plugin%", Edge.getInstance().getName()));
            }
            Config.GLOBALCMDSCHECKS = getStringListChecks("settings.globalcmds");
            for (final Map.Entry<LicenseType, Class<?>[]> entry : CheckLoader.CHECKS.entrySet()) {
                for (final Class<?> check : entry.getValue()) {
                    final DetectionData checkInfo = check.getAnnotation(DetectionData.class);
                    final File file = new File(Edge.getInstance().getDataFolder(), "checks.yml");
                    Config.checkConfig = YamlConfiguration.loadConfiguration(file);
                    for (final Field field : check.getDeclaredFields()) {
                        if (field.getType().equals(ConfigValue.class)) {
                            final boolean accessible = field.isAccessible();
                            field.setAccessible(true);
                            final String name = ((ConfigValue)field.get(null)).getName();
                            final ConfigValue value = (ConfigValue)field.get(null);
                            final ConfigValue.ValueType type = value.getType();
                            switch (type) {
                                case BOOLEAN: {
                                    value.setValue(getBooleanChecks("check." + checkInfo.name().split("\\(")[0].replace(" ", "") + checkInfo.type() + "." + name));
                                    break;
                                }
                                case INTEGER: {
                                    value.setValue(getIntChecks("check." + checkInfo.name().split("\\(")[0].replace(" ", "") + checkInfo.type() + "." + name));
                                    break;
                                }
                                case DOUBLE: {
                                    value.setValue(getDoubleChecks("check." + checkInfo.name().split("\\(")[0].replace(" ", "") + checkInfo.type() + "." + name));
                                    break;
                                }
                                case STRING: {
                                    value.setValue(getStringChecks("check." + checkInfo.name().split("\\(")[0].replace(" ", "") + checkInfo.type() + "." + name));
                                    break;
                                }
                                case LONG: {
                                    value.setValue(getLongChecks("check." + checkInfo.name().split("\\(")[0].replace(" ", "") + checkInfo.type() + "." + name));
                                    break;
                                }
                            }
                            field.setAccessible(accessible);
                        }
                    }
                    final boolean enabled = getBooleanChecks("check." + checkInfo.name().split("\\(")[0].replace(" ", "") + checkInfo.type() + ".state");
                    final boolean setback = getBooleanChecks("check." + checkInfo.name().split("\\(")[0].replace(" ", "") + checkInfo.type() + ".dragdown");
                    final boolean ban = getBooleanChecks("check." + checkInfo.name().split("\\(")[0].replace(" ", "") + checkInfo.type() + ".violations.enabled");
                    final String customName = getStringChecks("check." + checkInfo.name().split("\\(")[0].replace(" ", "") + checkInfo.type() + ".custom.name");
                    final String customType = getStringChecks("check." + checkInfo.name().split("\\(")[0].replace(" ", "") + checkInfo.type() + ".custom.type");
                    final List<String> vls = getStringListChecks("check." + checkInfo.name().split("\\(")[0].replace(" ", "") + checkInfo.type() + ".violations.threshold");
                    final List<Pair<Integer, String>> punishMap = new ArrayList<Pair<Integer, String>>();
                    for (final String thresholdString : vls) {
                        final int vl = Integer.parseInt(thresholdString.split(":")[0]);
                        final String command = thresholdString.split(":")[1];
                        punishMap.add(new Pair<Integer, String>(vl, command));
                    }
                    Config.PUNISH_COMMANDS.put(check.getSimpleName(), punishMap);
                    Config.CUSTOMCHECKNAME.put(check.getSimpleName(), customName);
                    Config.CUSTOMCHECKTYPE.put(check.getSimpleName(), customType);
                    if (enabled) {
                        Config.ENABLEDCHECKS.add(check.getSimpleName());
                    }
                    if (ban) {
                        Config.BANNINGCHECKS.add(check.getSimpleName());
                    }
                    if (setback) {
                        Config.SETBACKCHECKS.add(check.getSimpleName());
                    }
                }
            }
        }
        catch (final Exception exception) {
            exception.printStackTrace();
        }
    }
    
    private static boolean getBooleanChecks(final String string) {
        final File f = new File("plugins/Edge/checks.yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
        return yml.getBoolean(string);
    }
    
    public static String getStringChecks(final String string) {
        final File f = new File("plugins/Edge/checks.yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
        return yml.getString(string).replace("%prefix%", Config.PREFIX);
    }
    
    public static double getDoubleChecks(final String string) {
        final File f = new File("plugins/Edge/checks.yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
        return yml.getDouble(string);
    }
    
    public static int getIntChecks(final String string) {
        final File f = new File("plugins/Edge/checks.yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
        return yml.getInt(string);
    }
    
    private static List<String> getStringListChecks(final String string) {
        final File f = new File("plugins/Edge/checks.yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
        return yml.getStringList(string);
    }
    
    private static long getLongChecks(final String string) {
        final File f = new File("plugins/Edge/checks.yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
        return yml.getLong(string);
    }
    
    private static boolean getBooleanFromConfig(final String string) {
        final File f = new File("plugins/Edge/settings.yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
        return yml.getBoolean(string);
    }
    
    public static String getStringFromConfig(final String string) {
        final File f = new File("plugins/Edge/settings.yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
        return yml.getString(string);
    }
    
    private static int getIntegerFromConfig(final String string) {
        final File f = new File("plugins/Edge/settings.yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
        return yml.getInt(string);
    }
    
    public static double getDoubleFromConfig(final String string) {
        final File f = new File("plugins/Edge/settings.yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
        return yml.getDouble(string);
    }
    
    private static List<String> getStringListFromConfig(final String string) {
        final File f = new File("plugins/Edge/settings.yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
        return yml.getStringList(string);
    }
    
    private static long getLongFromConfig(final String string) {
        final File f = new File("plugins/Edge/settings.yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
        return yml.getLong(string);
    }
    
    private static boolean getBooleanFromMessages(final String string) {
        final File f = new File("plugins/Edge/messages.yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
        return yml.getBoolean(string);
    }
    
    public static String getStringFromMessages(final String string) {
        final File f = new File("plugins/Edge/messages.yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
        if (string.contains("prefix")) {
            return yml.getString(string);
        }
        return yml.getString(string).replace("%prefix%", Config.PREFIX);
    }
    
    private static int getIntegerFromMessages(final String string) {
        final File f = new File("plugins/Edge/messages.yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
        return yml.getInt(string);
    }
    
    public static double getDoubleFromMessages(final String string) {
        final File f = new File("plugins/Edge/messages.yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
        return yml.getDouble(string);
    }
    
    private static List<String> getStringListFromMessages(final String string) {
        final File f = new File("plugins/Edge/messages.yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
        return yml.getStringList(string);
    }
    
    private static long getLongFromMessages(final String string) {
        final File f = new File("plugins/Edge/messages.yml");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
        return yml.getLong(string);
    }
    
    public static String getPathFromCheckName(final String name) {
        return name.toLowerCase().replaceFirst("[(]", ".").replaceAll("[ ()]", "");
    }
    
    static {
        Config.GLOBAL_BAN_COMMANDS = new ArrayList<String>();
        Config.ENABLEDCHECKS = new ArrayList<String>();
        Config.HOVER = new ArrayList<String>();
        Config.BANNINGCHECKS = new ArrayList<String>();
        Config.SETBACKCHECKS = new ArrayList<String>();
        Config.PAGE1 = new ArrayList<String>();
        Config.CLIENTJOINHOVER = new ArrayList<String>();
        Config.DISCORDALERTSMESSAGE = new ArrayList<String>();
        Config.GLOBALCMDSCHECKS = new ArrayList<String>();
        Config.CUSTOMCHECKNAME = new HashMap<String, String>();
        Config.CUSTOMCHECKTYPE = new HashMap<String, String>();
        Config.PUNISH_COMMANDS = new HashMap<String, List<Pair<Integer, String>>>();
    }
}
