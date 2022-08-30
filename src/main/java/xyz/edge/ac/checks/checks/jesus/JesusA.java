package xyz.edge.ac.checks.checks.jesus;

import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.license.LicenseType;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Jesus (A)", type = "A", licenseType = LicenseType.DEVELOPMENT)
public class JesusA extends EdgeCheck
{
    public JesusA(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_FLYING()) {
            if (this.user.getMovementHandler().isInWater()) {
                final double deltaY = this.user.getMovementHandler().getDeltaY();
                this.debug("deltaY=" + deltaY);
                if (deltaY > -0.02 && deltaY < 0.002) {
                    this.debug("Checked.");
                    if (!this.user.getMovementHandler().isOnSolidGround()) {
                        final double buffer = this.buffer;
                        this.buffer = buffer + 1.0;
                        if (buffer > 2.0) {
                            this.debug("Flagging.");
                        }
                        this.fail();
                    }
                }
                else {
                    this.decreaseBufferBy(1.75);
                }
            }
            else {
                this.decreaseBufferBy(1.0);
            }
        }
    }
}
