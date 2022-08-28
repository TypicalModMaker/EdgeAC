package xyz.edge.ac.checks.checks.badpacket;

import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "BadPacket (D)", type = "D")
public final class BadPacketD extends EdgeCheck
{
    public BadPacketD(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_ROTATION()) {
            final float pitch = this.user.getRotationHandler().getPitch();
            final boolean teleport = this.isExempt(Exempts.TELEPORT);
            this.debug("Impossible Pitch &8[&cP=" + pitch + "/90&8]");
            if (Math.abs(pitch) > 90.0f && !teleport) {
                this.fail("Impossible Pitch", "P=" + pitch + " : OC=false");
            }
        }
    }
}
