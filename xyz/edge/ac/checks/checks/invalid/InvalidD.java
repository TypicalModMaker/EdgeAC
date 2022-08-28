package xyz.edge.ac.checks.checks.invalid;

import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Invalid (D)", type = "D")
public class InvalidD extends EdgeCheck
{
    public InvalidD(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_FLYING()) {
            final double deltaY = this.user.getMovementHandler().getDeltaY();
            final double lastDeltaY = this.user.getMovementHandler().getLastDeltaY();
            final double yAccel = Math.abs(deltaY - lastDeltaY);
            double limit = 2.0;
            if (this.user.getVelocityHandler().isTakingHorizontal() || this.user.getVelocityHandler().isWaitingForTransaction()) {
                limit += (this.user.getVelocityHandler().isWaitingForTransaction() ? this.user.getVelocityHandler().getVelocityY() : this.user.getVelocityHandler().getPendingVelocity().getY());
                limit *= 2.0;
            }
            final boolean exempt = this.isExempt(Exempts.FLYING, Exempts.TELEPORT_TIME, Exempts.GLIDING, Exempts.LIQUID, Exempts.CREATIVE);
            final boolean invalid = yAccel > limit && Math.abs(lastDeltaY) < 0.2 && limit > 1.0 && Math.abs(deltaY) > 2.0;
            this.debug("accel=" + yAccel);
            if (invalid && !exempt) {
                this.fail("Tried to suddenly move", "YA=" + yAccel + " : DY=" + deltaY);
            }
        }
    }
}
