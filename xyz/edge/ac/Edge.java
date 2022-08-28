package xyz.edge.ac;

import com.google.gson.LongSerializationPolicy;
import com.google.gson.GsonBuilder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import xyz.edge.ac.listener.ConnectionListener;
import edgeac.shared.ac.StaffManager;
import org.bukkit.plugin.messaging.Messenger;
import java.lang.reflect.Field;
import xyz.edge.ac.checks.checkloader.CheckLoader;
import xyz.edge.ac.checks.GhostBlock;
import xyz.edge.ac.packetevents.event.PacketListenerDynamic;
import xyz.edge.ac.listener.NetworkListener;
import xyz.edge.ac.bothandler.Bothandler;
import xyz.edge.ac.util.injector.Injector;
import org.bukkit.event.Listener;
import xyz.edge.ac.util.events.BukkitEvents;
import org.bukkit.plugin.messaging.PluginMessageListener;
import xyz.edge.ac.listener.ClientBrandListener;
import org.bukkit.command.Command;
import xyz.edge.ac.cmd.commands.AlertsCommand;
import org.bukkit.command.CommandMap;
import xyz.edge.ac.util.connections.BStats;
import xyz.edge.ac.util.PlaceholderAPI;
import org.bukkit.Bukkit;
import xyz.edge.ac.config.Config;
import java.io.File;
import xyz.edge.ac.auth.Auth;
import xyz.edge.ac.util.CustomConfig;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import org.bukkit.plugin.Plugin;
import xyz.edge.ac.packetevents.PacketEvents;
import java.util.concurrent.Executors;
import java.util.Arrays;
import xyz.edge.ac.util.ticking.TickEnd;
import java.util.concurrent.ExecutorService;
import xyz.edge.ac.cmd.CommandManager;
import xyz.edge.ac.user.UserManager;
import xyz.edge.ac.packetutils.inout.Sending;
import xyz.edge.ac.packetutils.inout.Receiving;
import xyz.edge.ac.util.ticking.TickManager;
import xyz.edge.ac.user.MongoUserManager;
import xyz.edge.ac.storage.MongoManager;
import xyz.edge.ac.storage.StorageMethod;
import xyz.edge.ac.util.proxy.ProxyUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.edge.api.license.LicenseType;
import java.util.List;
import com.google.gson.Gson;
import org.bukkit.plugin.java.JavaPlugin;

public class Edge extends JavaPlugin
{
    public static Gson GSON;
    private String command;
    private List<String> commandAliases;
    private static Edge instance;
    private LicenseType licenseType;
    private YamlConfiguration yaml;
    private int purchaseActive;
    private long expireDate;
    private String licenceUsername;
    private String licenceEmail;
    private String product;
    private long startTime;
    private ProxyUtil proxyUtil;
    private StorageMethod storageMethod;
    private MongoManager mongoManager;
    private MongoUserManager mongoUserManager;
    private final TickManager tickManager;
    private final Receiving receivingPacketProcessor;
    private final Sending sendingPacketProcessor;
    private final UserManager userManager;
    private CommandManager commandManager;
    private final ExecutorService packetExecutor;
    private final ExecutorService tickEndEvent;
    private TickEnd pledge;
    
    public Edge() {
        this.command = "edge";
        this.commandAliases = Arrays.asList("edgeac");
        this.tickManager = new TickManager();
        this.receivingPacketProcessor = new Receiving();
        this.sendingPacketProcessor = new Sending();
        this.userManager = new UserManager();
        this.packetExecutor = Executors.newSingleThreadExecutor();
        this.tickEndEvent = Executors.newSingleThreadExecutor();
    }
    
    public static Edge getInstance() {
        return Edge.instance;
    }
    
    public void onEnable() {
        Edge.instance = this;
        PacketEvents.create(this).getSettings().compatInjector(false).checkForUpdates(false).backupServerVersion(ServerVersion.v_1_7_10);
        PacketEvents.get().load();
        this.setupCustomPluginConfiguration();
        PacketEvents.get().init();
        this.storageMethod = StorageMethod.valueOf(this.getConfig().getString("storage.method"));
        switch (this.storageMethod) {
            case MONGO: {
                this.mongoManager = new MongoManager(this.getConfig().getString("storage.mongo.URI"), this.getConfig().getString("storage.mongo.database"));
                this.mongoUserManager = new MongoUserManager();
                break;
            }
        }
        final int pluginId = 15257;
        CustomConfig.createAuthConfig();
        this.setupProxy();
        System.setProperty("com.viaversion.handlePingsAsInvAcknowledgements", "true");
        (this.pledge = new TickEnd()).start();
        CustomConfig.createZeroTrust();
        CustomConfig.createBstats();
        CustomConfig.createLogsFile();
        CustomConfig.createAntiBot();
        final File checksFile = new File("plugins/Edge/checks.yml");
        final File messagesFile = new File("plugins/Edge/messages.yml");
        this.saveResource("checks.yml", false);
        this.saveResource("messages.yml", false);
        this.saveResource("settings.yml", false);
        this.saveDefaultConfig();
        Config.updateConfig();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI(this).register();
        }
        if (CustomConfig.isBstatsEnabled()) {
            new BStats(this, 15257);
        }
        Bukkit.getOnlinePlayers().forEach(player -> this.getUserManager().add(player));
        this.commandManager = new CommandManager(this);
        try {
            final Field field = this.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            final CommandMap commandMap = (CommandMap)field.get(this.getServer());
            commandMap.register(this.getDescription().getName(), new AlertsCommand());
            field.setAccessible(false);
        }
        catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        this.tickManager.start();
        final Messenger messenger = Bukkit.getMessenger();
        if (ServerVersion.getVersion().isNewerThanOrEquals(ServerVersion.v_1_13)) {
            messenger.registerIncomingPluginChannel(this, "minecraft:brand", new ClientBrandListener());
        }
        else {
            messenger.registerIncomingPluginChannel(this, "MC|Brand", new ClientBrandListener());
        }
        Bukkit.getServer().getPluginManager().registerEvents(new BukkitEvents(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ClientBrandListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Injector(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Bothandler(), this);
        PacketEvents.get().registerListener(new NetworkListener());
        PacketEvents.get().registerListener(new GhostBlock());
        CheckLoader.setup();
    }
    
    public void onDisable() {
        this.tickManager.stop();
    }
    
    public void setupProxy() {
        final PluginManager pluginManager = this.getServer().getPluginManager();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "edgeac:channel");
        this.proxyUtil = new ProxyUtil(this);
        final StaffManager staffManager = new StaffManager();
        pluginManager.registerEvents(new ConnectionListener(staffManager), this);
    }
    
    private void setupCustomPluginConfiguration() {
        this.command = this.getConfig().getString("plugin.command.name");
        this.commandAliases = this.getConfig().getStringList("plugin.command.aliases");
        final PluginDescriptionFile pluginDescriptionFile = this.getDescription();
        try {
            final Field name = pluginDescriptionFile.getClass().getDeclaredField("name");
            final Field rawName = pluginDescriptionFile.getClass().getDeclaredField("rawName");
            final Field description = pluginDescriptionFile.getClass().getDeclaredField("description");
            final Field version = pluginDescriptionFile.getClass().getDeclaredField("version");
            final Field authors = pluginDescriptionFile.getClass().getDeclaredField("authors");
            final Field website = pluginDescriptionFile.getClass().getDeclaredField("website");
            name.setAccessible(true);
            rawName.setAccessible(true);
            description.setAccessible(true);
            version.setAccessible(true);
            authors.setAccessible(true);
            website.setAccessible(true);
            name.set(pluginDescriptionFile, this.getConfig().getString("plugin.description.name"));
            rawName.set(pluginDescriptionFile, this.getConfig().getString("plugin.description.name"));
            description.set(pluginDescriptionFile, this.getConfig().getString("plugin.description.description"));
            version.set(pluginDescriptionFile, this.getConfig().getString("plugin.description.version"));
            authors.set(pluginDescriptionFile, this.getConfig().getStringList("plugin.description.authors"));
            website.set(pluginDescriptionFile, this.getConfig().getString("plugin.description.website"));
            name.setAccessible(false);
            rawName.setAccessible(false);
            description.setAccessible(false);
            version.setAccessible(false);
            authors.setAccessible(false);
            website.setAccessible(false);
            final Field pluginDescriptionField = this.getClass().getSuperclass().getDeclaredField("description");
            pluginDescriptionField.setAccessible(true);
            pluginDescriptionField.set(this, pluginDescriptionFile);
            pluginDescriptionField.setAccessible(false);
        }
        catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    public ProxyUtil getProxyUtil() {
        return this.proxyUtil;
    }
    
    public LicenseType getLicenseType() {
        return this.licenseType;
    }
    
    public YamlConfiguration getYaml() {
        return this.yaml;
    }
    
    public int getPurchaseActive() {
        return this.purchaseActive;
    }
    
    public long getExpireDate() {
        return this.expireDate;
    }
    
    public String getLicenceUsername() {
        return this.licenceUsername;
    }
    
    public String getLicenceEmail() {
        return this.licenceEmail;
    }
    
    public String getProduct() {
        return this.product;
    }
    
    public long getStartTime() {
        return this.startTime;
    }
    
    public StorageMethod getStorageMethod() {
        return this.storageMethod;
    }
    
    public MongoManager getMongoManager() {
        return this.mongoManager;
    }
    
    public MongoUserManager getMongoUserManager() {
        return this.mongoUserManager;
    }
    
    public TickManager getTickManager() {
        return this.tickManager;
    }
    
    public Receiving getReceivingPacketProcessor() {
        return this.receivingPacketProcessor;
    }
    
    public Sending getSendingPacketProcessor() {
        return this.sendingPacketProcessor;
    }
    
    public UserManager getUserManager() {
        return this.userManager;
    }
    
    public CommandManager getCommandManager() {
        return this.commandManager;
    }
    
    public ExecutorService getPacketExecutor() {
        return this.packetExecutor;
    }
    
    public ExecutorService getTickEndEvent() {
        return this.tickEndEvent;
    }
    
    public TickEnd getPledge() {
        return this.pledge;
    }
    
    public String getCommand() {
        return this.command;
    }
    
    public List<String> getCommandAliases() {
        return this.commandAliases;
    }
    
    public void setYaml(final YamlConfiguration yaml) {
        this.yaml = yaml;
    }
    
    public void setPurchaseActive(final int purchaseActive) {
        this.purchaseActive = purchaseActive;
    }
    
    public void setExpireDate(final long expireDate) {
        this.expireDate = expireDate;
    }
    
    public void setLicenceUsername(final String licenceUsername) {
        this.licenceUsername = licenceUsername;
    }
    
    public void setLicenceEmail(final String licenceEmail) {
        this.licenceEmail = licenceEmail;
    }
    
    public void setProduct(final String product) {
        this.product = product;
    }
    
    static {
        Edge.GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().setLongSerializationPolicy(LongSerializationPolicy.STRING).create();
    }
}
