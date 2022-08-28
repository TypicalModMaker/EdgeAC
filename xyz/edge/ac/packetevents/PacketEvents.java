package xyz.edge.ac.packetevents;

import org.bukkit.entity.Entity;
import java.util.Map;
import java.util.Iterator;
import xyz.edge.ac.packetevents.event.PacketEvent;
import xyz.edge.ac.packetevents.event.impl.PostPlayerInjectEvent;
import org.bukkit.entity.Player;
import xyz.edge.ac.packetevents.bstats.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.concurrent.ExecutorService;
import xyz.edge.ac.packetevents.utils.netty.bytebuf.ByteBufUtil_8;
import xyz.edge.ac.packetevents.utils.netty.bytebuf.ByteBufUtil_7;
import xyz.edge.ac.packetevents.exceptions.PacketEventsLoadFailureException;
import xyz.edge.ac.packetevents.packetwrappers.play.out.entityequipment.WrappedPacketOutEntityEquipment;
import xyz.edge.ac.packetevents.utils.guava.GuavaUtils;
import xyz.edge.ac.packetevents.packettype.PacketType;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.entityfinder.EntityFinderUtils;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.Bukkit;
import xyz.edge.ac.packetevents.event.manager.PEEventManager;
import xyz.edge.ac.packetevents.updatechecker.UpdateChecker;
import xyz.edge.ac.packetevents.utils.netty.bytebuf.ByteBufUtil;
import xyz.edge.ac.packetevents.settings.PacketEventsSettings;
import java.util.concurrent.atomic.AtomicBoolean;
import xyz.edge.ac.packetevents.injector.GlobalChannelInjector;
import xyz.edge.ac.packetevents.processor.BukkitEventProcessorInternal;
import xyz.edge.ac.packetevents.processor.PacketProcessorInternal;
import xyz.edge.ac.packetevents.utils.server.ServerUtils;
import xyz.edge.ac.packetevents.utils.player.PlayerUtils;
import xyz.edge.ac.packetevents.utils.version.PEVersion;
import org.bukkit.plugin.Plugin;
import xyz.edge.ac.packetevents.event.manager.EventManager;
import org.bukkit.event.Listener;

public final class PacketEvents implements Listener, EventManager
{
    private static PacketEvents instance;
    private static Plugin plugin;
    private final PEVersion version;
    private final EventManager eventManager;
    private final PlayerUtils playerUtils;
    private final ServerUtils serverUtils;
    private final PacketProcessorInternal packetProcessorInternal;
    private final BukkitEventProcessorInternal bukkitEventProcessorInternal;
    private final GlobalChannelInjector injector;
    private final AtomicBoolean injectorReady;
    private String handlerName;
    private PacketEventsSettings settings;
    private ByteBufUtil byteBufUtil;
    private UpdateChecker updateChecker;
    private volatile boolean loading;
    private volatile boolean loaded;
    private boolean initialized;
    private boolean initializing;
    private boolean terminating;
    private boolean lateBind;
    
    public PacketEvents() {
        this.version = new PEVersion(new int[] { 1, 8, 4 });
        this.eventManager = new PEEventManager();
        this.playerUtils = new PlayerUtils();
        this.serverUtils = new ServerUtils();
        this.packetProcessorInternal = new PacketProcessorInternal();
        this.bukkitEventProcessorInternal = new BukkitEventProcessorInternal();
        this.injector = new GlobalChannelInjector();
        this.injectorReady = new AtomicBoolean();
        this.settings = new PacketEventsSettings();
        this.lateBind = false;
    }
    
    public static PacketEvents create(final Plugin plugin) {
        if (!Bukkit.isPrimaryThread()) {
            if (PacketEvents.instance == null) {
                PacketEvents.plugin = plugin;
                PacketEvents.instance = new PacketEvents();
            }
            return PacketEvents.instance;
        }
        if (!Bukkit.getServicesManager().isProvidedFor(PacketEvents.class)) {
            PacketEvents.instance = new PacketEvents();
            Bukkit.getServicesManager().register(PacketEvents.class, PacketEvents.instance, plugin, ServicePriority.Normal);
            PacketEvents.plugin = plugin;
            return PacketEvents.instance;
        }
        return PacketEvents.instance = (PacketEvents)Bukkit.getServicesManager().load(PacketEvents.class);
    }
    
    public static PacketEvents get() {
        return PacketEvents.instance;
    }
    
    @Deprecated
    public static PacketEvents getAPI() {
        return PacketEvents.instance;
    }
    
    public void load() {
        if (!this.loaded && !this.loading) {
            this.loading = true;
            final ServerVersion version = EntityFinderUtils.version = (NMSUtils.version = (WrappedPacket.version = ServerVersion.getVersion()));
            this.handlerName = "pe-" + PacketEvents.plugin.getName();
            try {
                NMSUtils.load();
                PacketTypeClasses.load();
                PacketType.load();
                EntityFinderUtils.load();
                this.getServerUtils().entityCache = (Map<Integer, Entity>)GuavaUtils.makeMap();
                if (version.isNewerThanOrEquals(ServerVersion.v_1_9)) {
                    for (final WrappedPacketOutEntityEquipment.EquipmentSlot slot : WrappedPacketOutEntityEquipment.EquipmentSlot.values()) {
                        slot.id = (byte)slot.ordinal();
                    }
                }
                else {
                    WrappedPacketOutEntityEquipment.EquipmentSlot.MAINHAND.id = 0;
                    WrappedPacketOutEntityEquipment.EquipmentSlot.OFFHAND.id = -1;
                    WrappedPacketOutEntityEquipment.EquipmentSlot.BOOTS.id = 1;
                    WrappedPacketOutEntityEquipment.EquipmentSlot.LEGGINGS.id = 2;
                    WrappedPacketOutEntityEquipment.EquipmentSlot.CHESTPLATE.id = 3;
                    WrappedPacketOutEntityEquipment.EquipmentSlot.HELMET.id = 4;
                }
            }
            catch (final Exception ex) {
                this.loading = false;
                throw new PacketEventsLoadFailureException(ex);
            }
            this.byteBufUtil = (NMSUtils.legacyNettyImportMode ? new ByteBufUtil_7() : new ByteBufUtil_8());
            this.updateChecker = new UpdateChecker();
            if (!this.injectorReady.get()) {
                this.injector.load();
                if (!(this.lateBind = !this.injector.isBound())) {
                    this.injector.inject();
                }
                this.injectorReady.set(true);
            }
            this.loaded = true;
            this.loading = false;
        }
    }
    
    @Deprecated
    public void loadAsyncNewThread() {
        new Thread(this::load).start();
    }
    
    @Deprecated
    public void loadAsync(final ExecutorService executorService) {
        executorService.execute(this::load);
    }
    
    public void loadSettings(final PacketEventsSettings settings) {
        this.settings = settings;
    }
    
    public void init() {
        this.init(this.getSettings());
    }
    
    public void init(final PacketEventsSettings packetEventsSettings) {
        this.load();
        if (!this.initialized && !this.initializing) {
            this.initializing = true;
            (this.settings = packetEventsSettings).lock();
            if (this.settings.shouldCheckForUpdates()) {
                this.handleUpdateCheck();
            }
            if (this.settings.isbStatsEnabled()) {
                final Metrics metrics = new Metrics((JavaPlugin)this.getPlugin(), 11327);
                metrics.addCustomChart(new Metrics.SimplePie("packetevents_version", () -> this.getVersion().toString()));
            }
            while (!this.injectorReady.get()) {}
            final Runnable postInjectTask = () -> {
                Bukkit.getPluginManager().registerEvents(this.bukkitEventProcessorInternal, PacketEvents.plugin);
                Bukkit.getOnlinePlayers().iterator();
                final Iterator iterator;
                while (iterator.hasNext()) {
                    final Player p = iterator.next();
                    try {
                        this.injector.injectPlayer(p);
                        this.getEventManager().callEvent(new PostPlayerInjectEvent(p, false));
                    }
                    catch (final Exception ex) {
                        p.kickPlayer("Failed to inject... Please rejoin!");
                    }
                }
                return;
            };
            if (this.lateBind) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(PacketEvents.plugin, this.injector::inject);
                Bukkit.getScheduler().scheduleSyncDelayedTask(PacketEvents.plugin, postInjectTask);
            }
            else {
                postInjectTask.run();
            }
            this.initialized = true;
            this.initializing = false;
        }
    }
    
    @Deprecated
    public void init(final Plugin plugin) {
        this.init(plugin, this.settings);
    }
    
    @Deprecated
    public void init(final Plugin pl, final PacketEventsSettings packetEventsSettings) {
        this.init(packetEventsSettings);
    }
    
    public void terminate() {
        if (this.initialized && !this.terminating) {
            for (final Player p : Bukkit.getOnlinePlayers()) {
                this.injector.ejectPlayer(p);
            }
            this.injector.eject();
            this.getEventManager().unregisterAllListeners();
            this.initialized = false;
            this.terminating = false;
        }
    }
    
    @Deprecated
    public void stop() {
        this.terminate();
    }
    
    public boolean isLoading() {
        return this.loading;
    }
    
    public boolean hasLoaded() {
        return this.loaded;
    }
    
    public boolean isTerminating() {
        return this.terminating;
    }
    
    @Deprecated
    public boolean isStopping() {
        return this.isTerminating();
    }
    
    public boolean isInitializing() {
        return this.initializing;
    }
    
    public boolean isInitialized() {
        return this.initialized;
    }
    
    public Plugin getPlugin() {
        return PacketEvents.plugin;
    }
    
    public GlobalChannelInjector getInjector() {
        return this.injector;
    }
    
    public PacketProcessorInternal getInternalPacketProcessor() {
        return this.packetProcessorInternal;
    }
    
    public String getHandlerName() {
        return this.handlerName;
    }
    
    public PacketEventsSettings getSettings() {
        return this.settings;
    }
    
    public PEVersion getVersion() {
        return this.version;
    }
    
    public EventManager getEventManager() {
        return this.eventManager;
    }
    
    public PlayerUtils getPlayerUtils() {
        return this.playerUtils;
    }
    
    public ServerUtils getServerUtils() {
        return this.serverUtils;
    }
    
    public ByteBufUtil getByteBufUtil() {
        return this.byteBufUtil;
    }
    
    public UpdateChecker getUpdateChecker() {
        return this.updateChecker;
    }
    
    private void handleUpdateCheck() {
        if (this.updateChecker == null) {
            this.updateChecker = new UpdateChecker();
        }
        final Thread thread = new Thread(() -> {
            this.getPlugin().getLogger().info("[packetevents] Checking for an update, please wait...");
            UpdateChecker.UpdateCheckerStatus status = this.updateChecker.checkForUpdate();
            int seconds = 5;
            final int retryCount = 5;
            int i = 0;
            while (i < retryCount) {
                if (status != UpdateChecker.UpdateCheckerStatus.FAILED) {
                    break;
                }
                else {
                    this.getPlugin().getLogger().severe("[packetevents] Checking for an update again in " + seconds + " seconds...");
                    try {
                        Thread.sleep(seconds * 1000L);
                    }
                    catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                    seconds *= 2;
                    status = this.updateChecker.checkForUpdate();
                    if (i == retryCount - 1) {
                        this.getPlugin().getLogger().severe("[packetevents] PacketEvents failed to check for an update. No longer retrying.");
                        break;
                    }
                    else {
                        ++i;
                    }
                }
            }
            return;
        }, "packetevents-update-check-thread");
        thread.start();
    }
}
