package xyz.edge.ac.checks.checks.speed;

import org.bukkit.entity.Player;
import xyz.edge.ac.util.utils.PlayerUtil;
import org.bukkit.potion.PotionEffectType;
import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.ac.config.ConfigValue;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Speed (A)", type = "A", experimental = true, exemptBedrock = true)
public final class SpeedA extends EdgeCheck
{
    private static final ConfigValue threshold;
    private static final ConfigValue decay;
    private boolean prevPrevOnGround;
    private boolean prevOnGround;
    private boolean onGround;
    
    public SpeedA(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_FLYING()) {
            final boolean exempt = this.isExempt(Exempts.FLYING, Exempts.CREATIVE, Exempts.SLIME, Exempts.STAIRS, Exempts.SLAB, Exempts.ICE, Exempts.TELEPORT_TIME, Exempts.GLIDING, Exempts.INSIDE_VEHICLE, Exempts.ELYTA_TICK);
            if (this.user.getMovementHandler().isRiptide() || this.user.getMovementHandler().isLeashed()) {
                return;
            }
            final Player player = this.user.getPlayer();
            this.prevPrevOnGround = this.prevOnGround;
            this.prevOnGround = this.onGround;
            this.onGround = this.user.getMovementHandler().isOnGround();
            float attributeSpeed = 1.0f;
            attributeSpeed += PlayerUtil.getPotionLevel(player, PotionEffectType.SPEED) * 0.2f * attributeSpeed;
            attributeSpeed += PlayerUtil.getPotionLevel(player, PotionEffectType.SLOW) * -0.15f * attributeSpeed;
            final double friction = this.user.getMovementHandler().getFriction() / 0.91;
            final double prevFriction = this.user.getMovementHandler().getPrevFriction() / 0.91;
            final double prevDeltaXZ = this.user.getMovementHandler().getLastDeltaXZ();
            final double momentum = prevDeltaXZ * (prevFriction * 0.91);
            double acceleration = 0.0;
            int calculation = 0;
            final double movementType = 1.3;
            if (this.onGround && this.prevOnGround) {
                calculation = 1;
                acceleration = 0.1 * movementType * attributeSpeed * Math.pow(0.6 / friction, 3.0);
            }
            else if (this.onGround || this.prevOnGround) {
                calculation = 2;
                acceleration = 0.1 * movementType * attributeSpeed * Math.pow(0.6593406593406593, 3.0) + 0.2 + 0.26;
            }
            else {
                calculation = 3;
                acceleration = 0.026;
            }
            if (this.onGround && this.prevOnGround && !this.prevPrevOnGround) {
                acceleration += 0.13 * attributeSpeed;
            }
            if (this.user.getVelocityHandler().getTicksSinceVelocity() <= 2 || this.user.getVelocityHandler().isWaitingForTransaction()) {
                acceleration += this.user.getVelocityHandler().getVelocityXZ() + 0.15;
            }
            final double limit = Math.max(momentum + acceleration, 0.26);
            this.debug("Tried to illegally move faster than possible &8[&cXZ=" + this.user.getMovementHandler().getDeltaXZ() + " : OG=" + this.onGround + "&8]");
            if (this.user.getMovementHandler().getDeltaXZ() - limit > 1.0E-4 && !exempt) {
                final double buffer = this.buffer + 5.0;
                this.buffer = buffer;
                if (buffer > SpeedA.threshold.getInt()) {
                    this.buffer = Math.max(30.0, this.buffer);
                    this.fail("Tried to illegally move faster than possible", "MS=" + this.user.getMovementHandler().getDeltaXZ() + " : G=" + this.onGround + " + POG=" + this.prevOnGround);
                }
                else {
                    this.buffer = Math.max(0.0, this.buffer - SpeedA.decay.getDouble());
                }
            }
        }
    }
    
    static {
        threshold = new ConfigValue(ConfigValue.ValueType.INTEGER, "settings.threshold");
        decay = new ConfigValue(ConfigValue.ValueType.DOUBLE, "settings.decay");
    }
}
