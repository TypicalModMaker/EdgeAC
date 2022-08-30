package xyz.edge.ac.checks.checks.invalid;

import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.license.LicenseType;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Invalid (E)", type = "E", experimental = true, licenseType = LicenseType.ENTERPRISE)
public class InvalidE extends EdgeCheck
{
    public InvalidE(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_FLYING()) {
            final double deltaY = this.user.getMovementHandler().getDeltaY();
            final double lastDeltaY = this.user.getMovementHandler().getLastDeltaY();
            final boolean exempt = this.isExempt(Exempts.CHUNK, Exempts.PLACING, Exempts.TPS, Exempts.VELOCITY, Exempts.SLIME, Exempts.PISTON, Exempts.VELOCITY_TICK, Exempts.TELEPORT_TIME, Exempts.WEB, Exempts.FLYING, Exempts.CREATIVE) || this.user.getMovementHandler().getSinceBlockNearHeadTicks() < 3;
            if (lastDeltaY - deltaY > 0.2 && !exempt && deltaY != 0.0) {
                if (Math.abs(lastDeltaY - 0.2) < 1.0E-5) {
                    this.user.dragDown();
                }
                else if (this.increaseBuffer() > 2.0) {
                    this.fail("Impossible jump ratio", "MALDY=" + Math.abs(lastDeltaY - 0.2));
                }
            }
            else {
                this.decreaseBufferBy(0.15);
            }
        }
    }
}
