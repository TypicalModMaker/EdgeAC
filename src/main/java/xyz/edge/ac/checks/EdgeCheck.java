package xyz.edge.ac.checks;

import org.bukkit.Material;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.play.out.kickdisconnect.WrappedPacketOutKickDisconnect;
import java.lang.annotation.Annotation;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.util.utils.ServerUtil;
import java.text.DecimalFormat;
import xyz.edge.ac.util.utils.PlayerUtil;
import org.bukkit.command.CommandSender;
import xyz.edge.ac.util.color.ColorUtil;
import xyz.edge.ac.util.anticheat.AlertUtil;
import java.util.Objects;
import org.bukkit.event.Event;
import xyz.edge.api.listener.EdgeFlag;
import java.util.Iterator;
import org.bukkit.plugin.Plugin;
import org.bukkit.block.Block;
import org.bukkit.Bukkit;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.Edge;
import xyz.edge.ac.config.Config;
import xyz.edge.ac.util.type.Pair;
import java.util.List;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.APICheck;

public abstract class EdgeCheck implements APICheck
{
    public final User user;
    private boolean enabled;
    private boolean ban;
    private boolean setback;
    private boolean bedrock;
    private boolean annotationBan;
    private String customCheckName;
    private String customCheckType;
    private int vl;
    private long lastFlagTime;
    private CheckType checkType;
    private boolean debugging;
    public double buffer;
    public String justTheName;
    public char type;
    private List<Pair<Integer, String>> commandVlMap;
    
    public void setEnabled(final boolean state) {
        if (this.enabled) {
            if (Config.ENABLEDCHECKS.contains(this.getClass().getSimpleName())) {
                Config.ENABLEDCHECKS.add(this.getClass().getSimpleName());
            }
            else {
                Config.ENABLEDCHECKS.remove(this.getClass().getSimpleName());
            }
        }
    }
    
    public void hardSetEnabled(final boolean state) {
        this.enabled = state;
    }
    
    public EdgeCheck(final User user) {
        this.user = user;
        this.enabled = Config.ENABLEDCHECKS.contains(this.getClass().getSimpleName());
        this.setback = Config.SETBACKCHECKS.contains(this.getClass().getSimpleName());
        this.commandVlMap = Config.PUNISH_COMMANDS.get(this.getClass().getSimpleName());
        this.bedrock = this.getCheckInfo().exemptBedrock();
        this.annotationBan = this.getCheckInfo().punish();
        this.customCheckName = Config.CUSTOMCHECKNAME.get(this.getClass().getSimpleName());
        this.customCheckType = Config.CUSTOMCHECKTYPE.get(this.getClass().getSimpleName());
        this.ban = Config.BANNINGCHECKS.contains(this.getClass().getSimpleName());
        this.justTheName = this.getCheckInfo().name().split("\\(")[0].replace(" ", "");
        this.type = this.getCheckInfo().name().split("\\(")[1].split("\\)")[0].replaceAll(" ", "").toCharArray()[0];
    }
    
    public void tickEndEvent() {
    }
    
    public final int ticks() {
        return Edge.getInstance().getTickManager().getTicks();
    }
    
    public abstract void handle(final PacketUtil p0);
    
    public void fixGhostBlocks() {
        Bukkit.getScheduler().runTask(Edge.getInstance(), () -> {
            synchronized (this.user.getMovementHandler().getBlocks()) {
                for (Block block : this.user.getMovementHandler().getBlocks()) {
                    this.user.getPlayer().sendBlockChange(block.getLocation(), block.getType(), block.getData());
                }
            }
        });
    }
    
    public void fail(final Object info, final Object debug) {
        if (this.user.getPlayer() != null) {
            if (this.bedrock && this.user.getPlayer().getName().contains(Config.BEDROCKKEY)) {
                return;
            }
            final EdgeFlag event = new EdgeFlag(this.user.getPlayer(), this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
            ++this.vl;
            this.user.setTotalViolations(this.user.getTotalViolations() + 1);
            if (this.setback) {
                this.user.dragDown();
            }
            AlertUtil.handleAlert(this, this.user, Objects.toString(info), Objects.toString(debug));
            if (this.ban && this.annotationBan) {
                for (final Pair<Integer, String> punish : this.commandVlMap) {
                    if (punish.getX() == this.vl) {
                        Bukkit.getScheduler().runTask(Edge.getInstance(), () -> {
                            final String s = punish.getY().replaceAll("%player%", this.user.getPlayer().getName()).replace("%check%", this.customCheckName.isEmpty() ? ColorUtil.translate(this.justTheName) : this.customCheckName);
                            String replacement = "";
                            if (this.customCheckType.isEmpty()) {
                                replacement = ColorUtil.translate(this.type + "");
                            }
                            else {
                                replacement = this.customCheckType + "";
                            }
                            final CharSequence target;
                            //TODO: FIX
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("", replacement)); // i have no fucking idea what the "target" is
                        });
                    }
                }
            }
        }
    }
    
    public void debug(final String... info) {
        if (this.user.isDebugging(this)) {
            final String message = ColorUtil.translate(ColorUtil.translate(Config.PREFIX.replace("Edge", "Debug") + " &b%check% %type%%dev% &7\u239c &c%description%").replaceAll("%description%", String.join(", ", (CharSequence[])info)).replaceAll("%info%", String.join(", ", (CharSequence[])info)).replaceAll("%ping%", Integer.toString(PlayerUtil.getPing(this.user.getPlayer()))).replaceAll("%tps%", new DecimalFormat("##.##").format(ServerUtil.getTPS()))).replaceAll("%vl%", String.valueOf(this.vl)).replaceAll("%player%", this.user.getPlayer().getName()).replaceAll("%displayname%", this.user.getPlayer().getDisplayName()).replaceAll("%check%", this.customCheckName.isEmpty() ? ColorUtil.translate(this.justTheName) : this.customCheckName).replaceAll("%type%", this.customCheckType.isEmpty() ? ColorUtil.translate(this.type + "") : this.customCheckType).replaceAll("%dev%", this.getCheckInfo().experimental() ? ColorUtil.translate(Config.DEVTHING) : "").replaceAll("%setback%", String.valueOf(this.setback));
            this.user.getPlayer().sendMessage(message);
        }
    }
    
    public void fail() {
        this.fail("Information not found!", "Debug not found!");
    }
    
    protected boolean isExempt(final Exempts exemptType) {
        return this.user.getExemptionHandler().isExempt(exemptType);
    }
    
    protected boolean isExempt(final Exempts... exemptTypes) {
        return this.user.getExemptionHandler().isExempt(exemptTypes);
    }
    
    protected long now() {
        return System.currentTimeMillis();
    }
    
    @Override
    public DetectionData getCheckInfo() {
        if (this.getClass().isAnnotationPresent(DetectionData.class)) {
            return this.getClass().getAnnotation(DetectionData.class);
        }
        System.err.println("CheckInfo annotation hasn't been added to the class " + this.getClass().getSimpleName() + ".");
        return null;
    }
    
    public double getBuffer() {
        return this.buffer;
    }
    
    public final int hitTicks() {
        return this.user.getFightHandler().getHitTicks();
    }
    
    public final double increaseBuffer() {
        return this.buffer = Math.min(10000.0, this.buffer + 1.0);
    }
    
    public final double increaseBufferBy(final double amount) {
        return this.buffer = Math.min(10000.0, this.buffer + amount);
    }
    
    public final double decreaseBuffer() {
        return this.buffer = Math.max(0.0, this.buffer - 1.0);
    }
    
    public final double decreaseBufferBy(final double amount) {
        return this.buffer = Math.max(0.0, this.buffer - amount);
    }
    
    public final void resetBuffer() {
        this.buffer = 0.0;
    }
    
    public final void setBuffer(final double amount) {
        this.buffer = amount;
    }
    
    public final void timerkick() {
        this.user.getPlayer().kickPlayer(ColorUtil.translate("&cAbnormal game speed."));
        AlertUtil.sendMessage(ColorUtil.translate("&7" + this.user.getPlayer().getName() + " &cwas kicked for sending an excessive amount of packets."));
    }
    
    public void kick() {
        final WrappedPacketOutKickDisconnect wrappedPacketOutKickDisconnect = new WrappedPacketOutKickDisconnect(Config.EXPLOIT_KICK);
        PacketEvents.get().getPlayerUtils().sendPacket(this.user.getPlayer(), wrappedPacketOutKickDisconnect);
    }
    
    public boolean isBridging() {
        return this.user.getPlayer().getLocation().clone().subtract(0.0, 2.0, 0.0).getBlock().getType() == Material.AIR;
    }
    
    public void onEndOfTickEvent() {
    }
    
    public User getUser() {
        return this.user;
    }
    
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public boolean isBan() {
        return this.ban;
    }
    
    public boolean isSetback() {
        return this.setback;
    }
    
    public boolean isBedrock() {
        return this.bedrock;
    }
    
    public boolean isAnnotationBan() {
        return this.annotationBan;
    }
    
    public String getCustomCheckName() {
        return this.customCheckName;
    }
    
    public String getCustomCheckType() {
        return this.customCheckType;
    }
    
    @Override
    public int getVl() {
        return this.vl;
    }
    
    public long getLastFlagTime() {
        return this.lastFlagTime;
    }
    
    public CheckType getCheckType() {
        return this.checkType;
    }
    
    public boolean isDebugging() {
        return this.debugging;
    }
    
    public String getJustTheName() {
        return this.justTheName;
    }
    
    public char getType() {
        return this.type;
    }
    
    public List<Pair<Integer, String>> getCommandVlMap() {
        return this.commandVlMap;
    }
    
    public void setBan(final boolean ban) {
        this.ban = ban;
    }
    
    public void setSetback(final boolean setback) {
        this.setback = setback;
    }
    
    public void setBedrock(final boolean bedrock) {
        this.bedrock = bedrock;
    }
    
    public void setAnnotationBan(final boolean annotationBan) {
        this.annotationBan = annotationBan;
    }
    
    public void setCustomCheckName(final String customCheckName) {
        this.customCheckName = customCheckName;
    }
    
    public void setCustomCheckType(final String customCheckType) {
        this.customCheckType = customCheckType;
    }
    
    public void setVl(final int vl) {
        this.vl = vl;
    }
    
    public void setLastFlagTime(final long lastFlagTime) {
        this.lastFlagTime = lastFlagTime;
    }
    
    public void setCheckType(final CheckType checkType) {
        this.checkType = checkType;
    }
    
    public void setDebugging(final boolean debugging) {
        this.debugging = debugging;
    }
    
    public void setJustTheName(final String justTheName) {
        this.justTheName = justTheName;
    }
    
    public void setType(final char type) {
        this.type = type;
    }
    
    public void setCommandVlMap(final List<Pair<Integer, String>> commandVlMap) {
        this.commandVlMap = commandVlMap;
    }
    
    public enum CheckType
    {
        COMBAT("Combat");
        
        private final String name;
        
        private CheckType(final String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static CheckType fromPackageName(final String packageName) {
            for (final CheckType checkType : values()) {
                if (packageName.contains(checkType.getName().toLowerCase())) {
                    return checkType;
                }
            }
            return null;
        }
    }
}
