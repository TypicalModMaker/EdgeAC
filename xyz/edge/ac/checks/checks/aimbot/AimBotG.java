package xyz.edge.ac.checks.checks.aimbot;

import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "AimBot (G)", type = "G", exemptBedrock = true)
public class AimBotG extends EdgeCheck
{
    private float lastDeltaYaw;
    private float lastLastDeltaYaw;
    
    public AimBotG(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_ROTATION()) {
            final boolean exempt = this.isExempt(Exempts.TELEPORT_TIME, Exempts.JOINED, Exempts.INSIDE_VEHICLE);
            final float deltaYaw = this.user.getRotationHandler().getDeltaYaw();
            if (deltaYaw < 5.0f && this.lastDeltaYaw > 30.0f && this.lastLastDeltaYaw < 5.0f) {
                final double low = (deltaYaw + this.lastLastDeltaYaw) / 2.0f;
                final double high = this.lastDeltaYaw;
                if (this.increaseBuffer() > 5.0 && !exempt) {
                    this.fail("Fast rotation snap", "DY=" + deltaYaw + " : LDY=" + this.lastDeltaYaw);
                }
                else {
                    this.decreaseBufferBy(0.1);
                }
            }
            this.lastLastDeltaYaw = this.lastDeltaYaw;
            this.lastDeltaYaw = deltaYaw;
        }
    }
}
