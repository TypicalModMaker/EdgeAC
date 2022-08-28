package xyz.edge.ac.user;

import java.util.HashMap;
import xyz.edge.ac.checks.checkloader.CheckLoader;
import org.bukkit.plugin.Plugin;
import xyz.edge.ac.Edge;
import org.bukkit.Bukkit;
import xyz.edge.ac.util.block.BlockUtil;
import java.util.Arrays;
import xyz.edge.ac.packetevents.packetwrappers.play.out.helditemslot.WrappedPacketOutHeldItemSlot;
import xyz.edge.ac.util.anticheat.AlertUtil;
import xyz.edge.ac.util.color.ColorUtil;
import xyz.edge.ac.config.Config;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.PacketEvents;
import xyz.edge.ac.packetevents.packetwrappers.play.out.kickdisconnect.WrappedPacketOutKickDisconnect;
import xyz.edge.ac.util.utils.PlayerUtil;
import java.util.Map;
import xyz.edge.ac.user.impl.Lol;
import xyz.edge.ac.user.impl.Speed;
import xyz.edge.ac.user.impl.Velocity;
import xyz.edge.ac.user.impl.Gliding;
import xyz.edge.ac.user.impl.Rotations;
import xyz.edge.ac.user.impl.Movement;
import xyz.edge.ac.user.impl.Click;
import xyz.edge.ac.user.impl.Transactions;
import xyz.edge.ac.user.impl.Action;
import xyz.edge.ac.user.impl.Combat;
import xyz.edge.ac.util.exempts.Exempt;
import xyz.edge.ac.util.logger.Logs;
import org.bukkit.Location;
import xyz.edge.ac.util.type.Pair;
import xyz.edge.ac.util.type.EvictingList;
import xyz.edge.ac.checks.EdgeCheck;
import java.util.List;
import xyz.edge.ac.packetevents.utils.player.ClientVersion;
import org.bukkit.entity.Player;
import java.text.SimpleDateFormat;

public final class User
{
    public static SimpleDateFormat simpleDateFormat;
    private final Player player;
    private ClientVersion version;
    private String clientBrand;
    private boolean isRouterFried;
    private boolean viewingStats;
    private boolean isAlerts;
    private int joinThing;
    private long setBackTicks;
    private boolean exempt;
    private boolean banning;
    private boolean setBack;
    private int totalViolations;
    private int pageIndex;
    private int combatViolations;
    private int botViolations;
    private int ticks;
    private int currentTick;
    private final long joinTime;
    private final List<EdgeCheck> checks;
    private final EvictingList<Pair<Location, Integer>> targetLocations;
    private Logs.TextFile logFile;
    private Location lastGroundLocation;
    private final Exempt exemptionHandler;
    private final Combat fightHandler;
    private final Action packetActionHandler;
    private final Transactions transactionsHandler;
    private final Click clickHandler;
    private final Movement movementHandler;
    private final Rotations rotationHandler;
    private final Gliding gliding;
    private final Velocity velocityHandler;
    private final Speed speed;
    private final Exempt exempts;
    private final Lol lol;
    private final Map<EdgeCheck, Boolean> debugging;
    
    public boolean isDebugging(final EdgeCheck check) {
        return this.debugging.containsKey(check) && this.debugging.get(check);
    }
    
    public ClientVersion getVersion() {
        if (this.version == null || !this.version.isResolved()) {
            this.version = PlayerUtil.getClientVersion(this.player);
        }
        return this.version;
    }
    
    public void routerFry() {
        this.setRouterFried(true);
        final WrappedPacketOutKickDisconnect wrapper = new WrappedPacketOutKickDisconnect("Fatty Fatty");
        PacketEvents.get().getPlayerUtils().sendPacket(this.player, wrapper);
    }
    
    int getPotionEffectLevel(final User user, final PotionEffectType potionEffectType) {
        final PotionEffect potionEffect = user.getPlayer().getActivePotionEffects().stream().filter(effect -> effect.getType().equals(potionEffectType)).findAny().orElse(null);
        return (potionEffect != null) ? (potionEffect.getAmplifier() + 1) : 0;
    }
    
    public void fuckOff() {
        final WrappedPacketOutKickDisconnect kick = new WrappedPacketOutKickDisconnect("io.connection.ReadTimeOutException");
        PacketEvents.get().getPlayerUtils().sendPacket(this.player, kick);
    }
    
    public void exploit(final String crasher, final String crasherType) {
        AlertUtil.sendMessage(ColorUtil.translate(Config.EXPLOIT_ALERT.replaceAll("%player%", this.player.getName()).replaceAll("%crasher%", crasher).replaceAll("%crashertype%", crasherType).replaceAll("%ping%", String.valueOf(PlayerUtil.getPing(this.player)))));
        final WrappedPacketOutKickDisconnect kick = new WrappedPacketOutKickDisconnect(ColorUtil.translate(Config.EXPLOIT_KICK));
        PacketEvents.get().getPlayerUtils().sendPacket(this.player, kick);
    }
    
    public void tooManyPackets() {
        final WrappedPacketOutKickDisconnect wrapper = new WrappedPacketOutKickDisconnect(ColorUtil.translate("&fYou are sending too many packets!"));
        PacketEvents.get().getPlayerUtils().sendPacket(this.player, wrapper);
    }
    
    public void slotSwitch() {
        final int slot = (this.getPacketActionHandler().getHeldItemSlot() == 8) ? 1 : 8;
        final WrappedPacketOutHeldItemSlot wrapper = new WrappedPacketOutHeldItemSlot(slot);
        PacketEvents.get().getPlayerUtils().sendPacket(this.player, wrapper);
    }
    
    public List<Double> getPossibleEyeHeights() {
        if (this.version.isNewerThanOrEquals(ClientVersion.v_1_14)) {
            return Arrays.asList(0.4, 1.27, 1.62);
        }
        if (this.version.isNewerThanOrEquals(ClientVersion.v_1_9)) {
            return Arrays.asList(0.4, 1.54, 1.62);
        }
        return Arrays.asList(1.54, 1.62);
    }
    
    public void dragDown() {
        if (this.player.isOnline()) {
            final Location loc = this.player.getLocation().clone();
            loc.setPitch(this.getRotationHandler().getLastPitch());
            loc.setYaw(this.getRotationHandler().getLastYaw());
            double i;
            for (double y = i = loc.getBlockY(); i >= 0.0; --i) {
                loc.setY(i);
                if (i <= 0.0) {
                    break;
                }
                try {
                    if (BlockUtil.getBlockAsync(loc).getType().toString().contains("WATER") || BlockUtil.getBlockAsync(loc).getType().toString().contains("LAVA")) {
                        break;
                    }
                }
                catch (final NullPointerException e) {
                    break;
                }
            }
            this.teleport(this.player, loc.add(0.0, 1.0, 0.0));
        }
    }
    
    public void teleport(final Player player, final Location location) {
        Bukkit.getScheduler().runTask(Edge.getInstance(), () -> player.teleport(location));
    }
    
    public boolean isExempt() {
        return this.exempt;
    }
    
    public User(final Player player) {
        this.joinTime = System.currentTimeMillis();
        this.checks = CheckLoader.loadChecks(this);
        this.targetLocations = new EvictingList<Pair<Location, Integer>>(40);
        this.exemptionHandler = new Exempt(this);
        this.fightHandler = new Combat(this);
        this.packetActionHandler = new Action(this);
        this.transactionsHandler = new Transactions(this);
        this.clickHandler = new Click(this);
        this.movementHandler = new Movement(this);
        this.rotationHandler = new Rotations(this);
        this.gliding = new Gliding(this);
        this.velocityHandler = new Velocity(this);
        this.speed = new Speed();
        this.exempts = new Exempt(this);
        this.lol = new Lol(this);
        this.debugging = new HashMap<EdgeCheck, Boolean>();
        this.player = player;
        if (Config.LOG) {
            this.logFile = new Logs.TextFile("" + player.getUniqueId());
        }
    }
    
    public void flush() {
        PacketEvents.get().getPlayerUtils().flushPackets(this.player);
    }
    
    public void isGeyser() {
        PacketEvents.get().getPlayerUtils().isGeyserPlayer(this.player);
    }
    
    public void eject() {
        PacketEvents.get().getInjector().ejectPlayer(this.player);
    }
    
    public void inject() {
        PacketEvents.get().getInjector().injectPlayer(this.player);
    }
    
    public void debug(final String message) {
        this.player.sendMessage(ColorUtil.translate(message));
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public String getClientBrand() {
        return this.clientBrand;
    }
    
    public boolean isRouterFried() {
        return this.isRouterFried;
    }
    
    public boolean isViewingStats() {
        return this.viewingStats;
    }
    
    public boolean isAlerts() {
        return this.isAlerts;
    }
    
    public int getJoinThing() {
        return this.joinThing;
    }
    
    public long getSetBackTicks() {
        return this.setBackTicks;
    }
    
    public boolean isBanning() {
        return this.banning;
    }
    
    public boolean isSetBack() {
        return this.setBack;
    }
    
    public int getTotalViolations() {
        return this.totalViolations;
    }
    
    public int getPageIndex() {
        return this.pageIndex;
    }
    
    public int getCombatViolations() {
        return this.combatViolations;
    }
    
    public int getBotViolations() {
        return this.botViolations;
    }
    
    public int getTicks() {
        return this.ticks;
    }
    
    public int getCurrentTick() {
        return this.currentTick;
    }
    
    public long getJoinTime() {
        return this.joinTime;
    }
    
    public List<EdgeCheck> getChecks() {
        return this.checks;
    }
    
    public EvictingList<Pair<Location, Integer>> getTargetLocations() {
        return this.targetLocations;
    }
    
    public Logs.TextFile getLogFile() {
        return this.logFile;
    }
    
    public Location getLastGroundLocation() {
        return this.lastGroundLocation;
    }
    
    public Exempt getExemptionHandler() {
        return this.exemptionHandler;
    }
    
    public Combat getFightHandler() {
        return this.fightHandler;
    }
    
    public Action getPacketActionHandler() {
        return this.packetActionHandler;
    }
    
    public Transactions getTransactionsHandler() {
        return this.transactionsHandler;
    }
    
    public Click getClickHandler() {
        return this.clickHandler;
    }
    
    public Movement getMovementHandler() {
        return this.movementHandler;
    }
    
    public Rotations getRotationHandler() {
        return this.rotationHandler;
    }
    
    public Gliding getGliding() {
        return this.gliding;
    }
    
    public Velocity getVelocityHandler() {
        return this.velocityHandler;
    }
    
    public Speed getSpeed() {
        return this.speed;
    }
    
    public Exempt getExempts() {
        return this.exempts;
    }
    
    public Lol getLol() {
        return this.lol;
    }
    
    public Map<EdgeCheck, Boolean> getDebugging() {
        return this.debugging;
    }
    
    public void setVersion(final ClientVersion version) {
        this.version = version;
    }
    
    public void setClientBrand(final String clientBrand) {
        this.clientBrand = clientBrand;
    }
    
    public void setRouterFried(final boolean isRouterFried) {
        this.isRouterFried = isRouterFried;
    }
    
    public void setViewingStats(final boolean viewingStats) {
        this.viewingStats = viewingStats;
    }
    
    public void setAlerts(final boolean isAlerts) {
        this.isAlerts = isAlerts;
    }
    
    public void setJoinThing(final int joinThing) {
        this.joinThing = joinThing;
    }
    
    public void setExempt(final boolean exempt) {
        this.exempt = exempt;
    }
    
    public void setBanning(final boolean banning) {
        this.banning = banning;
    }
    
    public void setSetBack(final boolean setBack) {
        this.setBack = setBack;
    }
    
    public void setTotalViolations(final int totalViolations) {
        this.totalViolations = totalViolations;
    }
    
    public void setPageIndex(final int pageIndex) {
        this.pageIndex = pageIndex;
    }
    
    public void setCombatViolations(final int combatViolations) {
        this.combatViolations = combatViolations;
    }
    
    public void setBotViolations(final int botViolations) {
        this.botViolations = botViolations;
    }
    
    public void setTicks(final int ticks) {
        this.ticks = ticks;
    }
    
    public void setCurrentTick(final int currentTick) {
        this.currentTick = currentTick;
    }
    
    public void setLogFile(final Logs.TextFile logFile) {
        this.logFile = logFile;
    }
    
    public void setLastGroundLocation(final Location lastGroundLocation) {
        this.lastGroundLocation = lastGroundLocation;
    }
    
    public void setSetBackTicks(final long setBackTicks) {
        this.setBackTicks = setBackTicks;
    }
    
    static {
        User.simpleDateFormat = new SimpleDateFormat("E, MMMMM d, yyyy hh:mm aaa");
    }
}
