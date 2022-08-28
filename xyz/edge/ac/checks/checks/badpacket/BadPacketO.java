package xyz.edge.ac.checks.checks.badpacket;

import xyz.edge.ac.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "BadPacket (O)", type = "O")
public class BadPacketO extends EdgeCheck
{
    public BadPacketO(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_FLYING()) {
            final WrappedPacketInFlying wrapper = new WrappedPacketInFlying(packet.getRawPacket());
            if (Math.abs(wrapper.getPosition().getY()) > 1.0E9) {
                this.fail("Large Y position", "Y=" + wrapper.getPosition().getY());
                this.kick();
            }
        }
    }
}
