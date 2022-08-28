package xyz.edge.ac.checks.checks.aimbot;

import org.bukkit.GameMode;
import xyz.edge.ac.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig;
import xyz.edge.ac.util.utils.MathUtil;
import xyz.edge.ac.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.ac.config.ConfigValue;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "AimBot (E)", type = "E")
public class AimBotE extends EdgeCheck
{
    private static final ConfigValue threshold;
    private double lastDivisor;
    private long lastRotation;
    private boolean checking;
    private boolean waitForBreak;
    
    public AimBotE(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_FLYING()) {
            final WrappedPacketInFlying wrapper = new WrappedPacketInFlying(packet.getRawPacket());
            if (this.user.getMovementHandler().isTeleporting()) {
                return;
            }
            if (wrapper.isRotating()) {
                final float deltaPitch = Math.abs(this.user.getRotationHandler().getDeltaPitch());
                final float lastDeltaPitch = Math.abs(this.user.getRotationHandler().getLastDeltaPitch());
                if (deltaPitch > 0.1 && lastDeltaPitch > 0.1) {
                    final long expanded = (long)(deltaPitch * MathUtil.EXPANDER);
                    final long lastExpanded = (long)(lastDeltaPitch * MathUtil.EXPANDER);
                    final long gcd = MathUtil.getGcd(expanded, lastExpanded);
                    this.lastDivisor = gcd / MathUtil.EXPANDER;
                    this.checking = true;
                }
                this.lastRotation = System.currentTimeMillis();
            }
        }
        else if (packet.PACKET_BLOCK_DIG()) {
            final WrappedPacketInBlockDig wrapper2 = new WrappedPacketInBlockDig(packet.getRawPacket());
            if (this.user.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                return;
            }
            if (wrapper2.getDigType() == WrappedPacketInBlockDig.PlayerDigType.START_DESTROY_BLOCK) {
                this.waitForBreak = false;
                if (this.checking) {
                    if ((this.lastDivisor > 0.0 && this.lastDivisor < 0.009) || this.lastDivisor > 10.0) {
                        this.waitForBreak = true;
                    }
                    else {
                        this.buffer = Math.max(0.0, this.buffer - 2.0);
                    }
                }
                this.checking = false;
            }
            else if (wrapper2.getDigType() == WrappedPacketInBlockDig.PlayerDigType.STOP_DESTROY_BLOCK) {
                this.debug("Baritone like rotations [2] &8[&cLD=" + this.lastDivisor + " : LR=" + this.lastRotation + "&8]");
                if (this.waitForBreak) {
                    final double buffer = this.buffer;
                    this.buffer = buffer + 1.0;
                    if (buffer > AimBotE.threshold.getInt()) {
                        this.fail("Baritone like rotations [2]", "LD=" + this.lastDivisor + " : LR=" + this.lastRotation);
                    }
                }
                this.waitForBreak = false;
            }
        }
    }
    
    static {
        threshold = new ConfigValue(ConfigValue.ValueType.INTEGER, "settings.threshold");
    }
}
