package xyz.edge.ac.checks.checks.aimbot;

import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "AimBot (A)", type = "A", exemptBedrock = true)
public final class AimBotA extends EdgeCheck
{
    public AimBotA(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_ROTATION()) {
            final float pitch = this.user.getRotationHandler().getPitch();
            if (pitch > 89.0f || pitch < 1.0f) {
                return;
            }
            final float deltaPitch = this.user.getRotationHandler().getDeltaPitch();
            final float deltaYaw = this.user.getRotationHandler().getDeltaYaw();
            final boolean invalid = deltaPitch == 0.0f && deltaYaw >= 15.6f;
            this.debug("Rotated unlikely &8[&cP=" + deltaPitch + " : Y=" + deltaYaw + "&8]");
            if (invalid) {
                if (this.increaseBuffer() > 6.0) {
                    this.fail("Rotated unlikely", "P=" + deltaPitch + " : Y=" + deltaYaw);
                }
            }
            else {
                this.decreaseBufferBy(0.5);
            }
        }
    }
}
