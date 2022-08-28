package xyz.edge.ac.checks.checks.speed;

import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.util.utils.PlayerUtil;
import org.bukkit.potion.PotionEffectType;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Speed (B)", type = "B", experimental = true)
public final class SpeedB extends EdgeCheck
{
    public SpeedB(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_FLYING()) {
            final double deltaXZ = this.user.getMovementHandler().getDeltaXZ();
            final double deltaY = this.user.getMovementHandler().getDeltaY();
            final int groundTicks = this.user.getMovementHandler().getGroundTicks();
            final int airTicks = this.user.getMovementHandler().getClientAirTicks();
            final float modifierJump = PlayerUtil.getPotionLevel(this.user.getPlayer(), PotionEffectType.JUMP) * 0.1f;
            final float jumpMotion = 0.42f + modifierJump;
            double groundLimit = PlayerUtil.getBaseGroundSpeed(this.user.getPlayer());
            double airLimit = PlayerUtil.getBaseSpeed(this.user.getPlayer());
            if (Math.abs(deltaY - jumpMotion) < 1.0E-4 && airTicks == 1) {
                groundLimit = this.getAfterJumpSpeed();
                airLimit = this.getAfterJumpSpeed();
            }
            if (this.user.getMovementHandler().getSinceIceTicks() < 20 || this.user.getMovementHandler().getSinceSlimeTicks() < 20) {
                airLimit += 0.3400000035762787;
                groundLimit += 0.3400000035762787;
            }
            if (this.user.getMovementHandler().getSinceBlockNearHeadTicks() < 6) {
                airLimit += 0.91f / Math.max(1, this.user.getMovementHandler().getSinceBlockNearHeadTicks());
                groundLimit += 0.91f / Math.max(1, this.user.getMovementHandler().getSinceBlockNearHeadTicks());
            }
            if (groundTicks < 7) {
                groundLimit += 0.25f / groundTicks;
            }
            if (this.user.getVelocityHandler().isTakingVelocity() || this.user.getVelocityHandler().isWaitingForTransaction()) {
                groundLimit += this.user.getVelocityHandler().getVelocityXZ() + 0.15;
                airLimit += this.user.getVelocityHandler().getVelocityXZ() + 0.15;
            }
            final boolean exempt = this.isExempt(Exempts.FLYING, Exempts.SLAB, Exempts.STAIRS, Exempts.TELEPORT, Exempts.GLIDING, Exempts.CREATIVE, Exempts.ELYTA_TICK, Exempts.INSIDE_VEHICLE);
            if (this.user.getMovementHandler().isLevitation()) {
                return;
            }
            if (this.user.getMovementHandler().isRiptide()) {
                return;
            }
            if (airTicks > 0 && !exempt) {
                this.debug("Illegally moved faster in air &8[&cXZ=" + deltaXZ + "&8]");
                if (deltaXZ > airLimit) {
                    if (this.increaseBuffer() > 3.0) {
                        this.fail("Illegally moved faster in air", "SIA=" + deltaXZ);
                    }
                }
                else {
                    this.decreaseBufferBy(0.25);
                }
            }
            else {
                this.debug("Illegally moved faster in ground &8[&cXZ=" + deltaXZ + "&8]");
                if (deltaXZ > groundLimit & !exempt) {
                    if (this.increaseBuffer() > 3.0) {
                        this.fail("Illegally moved faster on ground", "SOG=" + deltaXZ);
                    }
                }
                else {
                    this.decreaseBufferBy(0.25);
                }
            }
        }
    }
    
    private double getAfterJumpSpeed() {
        return 0.62 + 0.033 * PlayerUtil.getPotionLevel(this.user.getPlayer(), PotionEffectType.SPEED);
    }
}
