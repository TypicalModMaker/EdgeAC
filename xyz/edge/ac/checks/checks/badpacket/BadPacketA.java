package xyz.edge.ac.checks.checks.badpacket;

import xyz.edge.ac.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "BadPacket (A)", type = "A")
public final class BadPacketA extends EdgeCheck
{
    public BadPacketA(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_USE_ENTITY()) {
            final WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(packet.getRawPacket());
            this.debug("Attacked themselves");
            if (wrapper.getEntityId() == this.user.getPlayer().getEntityId()) {
                this.fail("Attacked themselves", "EID=" + wrapper.getEntityId());
            }
        }
    }
}
