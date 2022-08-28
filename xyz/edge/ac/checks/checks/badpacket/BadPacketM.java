package xyz.edge.ac.checks.checks.badpacket;

import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "BadPacket (M)", type = "M")
public class BadPacketM extends EdgeCheck
{
    public BadPacketM(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_FLYING()) {
            final double deltaXZ = this.user.getMovementHandler().getDeltaXZ();
            final double deltaY = this.user.getMovementHandler().getDeltaY();
            if (deltaXZ > 1.0E10 && Math.abs(deltaY) > 1.0E10) {
                this.fail("Chunk crasher", "DXZ=" + deltaXZ + " : DYABS=" + Math.abs(deltaY));
                this.kick();
            }
        }
    }
}
