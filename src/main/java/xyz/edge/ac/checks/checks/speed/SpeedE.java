package xyz.edge.ac.checks.checks.speed;

import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.ac.config.ConfigValue;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Speed (E)", type = "E", exemptBedrock = true)
public final class SpeedE extends EdgeCheck
{
    private static final ConfigValue threshold;
    private static final ConfigValue decay;
    
    public SpeedE(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_ROTATION()) {
            final float deltaYaw = this.user.getRotationHandler().getDeltaYaw();
            final double deltaXZ = this.user.getMovementHandler().getDeltaXZ();
            final double squaredAccel = this.user.getMovementHandler().getAccelXZ() * 100.0;
            final boolean invalid = deltaYaw > 1.5f && squaredAccel < 1.0E-5 && deltaXZ > 0.15 + (this.user.getVelocityHandler().isTakingVelocity() ? Math.abs(this.user.getVelocityHandler().getVelocityXZ()) : 0.0);
            final boolean isExempt = this.isExempt(Exempts.CLIMBABLE, Exempts.TELEPORT, Exempts.INSIDE_VEHICLE);
            this.debug("Tried to move incorrect on rotate packet");
            if (invalid && !isExempt) {
                final double buffer = this.buffer + ((this.buffer < 175.0) ? 5.0 : 0.0);
                this.buffer = buffer;
                if (buffer > SpeedE.threshold.getInt()) {
                    this.fail("Tried to move incorrect on rotate packet", "DY=" + deltaYaw);
                }
            }
            else {
                this.buffer = Math.max(this.buffer - SpeedE.decay.getDouble(), 0.0);
            }
        }
    }
    
    static {
        threshold = new ConfigValue(ConfigValue.ValueType.INTEGER, "settings.threshold");
        decay = new ConfigValue(ConfigValue.ValueType.DOUBLE, "settings.decay");
    }
}
