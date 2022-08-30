package xyz.edge.ac.checks.checks.speed;

import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.ac.config.ConfigValue;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Speed (D)", type = "D")
public final class SpeedD extends EdgeCheck
{
    private static final ConfigValue threshold;
    private static final ConfigValue decay;
    
    public SpeedD(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_FLYING()) {
            final double prediction = this.user.getMovementHandler().getLastDeltaXZ() * 0.9100000262260437 + 0.025999998673796654;
            final double equalness = this.user.getMovementHandler().getDeltaXZ() - prediction;
            if (equalness > 1.0E-12 && !this.user.getMovementHandler().isOnGround() && !this.user.getMovementHandler().isLastOnGround() && this.user.getMovementHandler().getY() != this.user.getMovementHandler().getLastY() && this.user.getMovementHandler().getSinceLiquidTicks() > 20 && this.user.getMovementHandler().getSinceClimableTicks() > 20 && this.user.getMovementHandler().getSinceFlyingTicks() > 40 && this.user.getVelocityHandler().getTicksSinceVelocity() > 20 && this.user.getMovementHandler().getSinceWebTicks() > 5 && !this.isExempt(Exempts.INSIDE_VEHICLE, Exempts.TELEPORT, Exempts.GLIDING, Exempts.CREATIVE)) {
                this.debug("Client failed to follow friction &8[&cP=" + prediction + " : EQ=" + equalness + "&8]");
                final double buffer = this.buffer + 1.0;
                this.buffer = buffer;
                if (buffer > SpeedD.threshold.getInt()) {
                    this.fail("Client failed to follow friction", "EN=" + equalness + " : P=" + prediction);
                }
            }
            else {
                this.buffer *= SpeedD.decay.getDouble();
            }
        }
    }
    
    static {
        threshold = new ConfigValue(ConfigValue.ValueType.INTEGER, "settings.threshold");
        decay = new ConfigValue(ConfigValue.ValueType.DOUBLE, "settings.decay");
    }
}
