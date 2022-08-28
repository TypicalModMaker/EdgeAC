package xyz.edge.ac.checks.checks.badpacket;

import java.net.InetAddress;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "BadPacket (L)", type = "L")
public class BadPacketL extends EdgeCheck
{
    public BadPacketL(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_CUSTOM_PAYLOAD()) {
            final InetAddress address = this.user.getPlayer().getAddress().getAddress();
            if (address == null) {
                this.fail("Join with null address", "A=null");
                this.kick();
            }
        }
    }
}
