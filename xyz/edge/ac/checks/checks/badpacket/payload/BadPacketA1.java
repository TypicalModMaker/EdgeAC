package xyz.edge.ac.checks.checks.badpacket.payload;

import xyz.edge.ac.packetevents.packetwrappers.play.in.custompayload.WrappedPacketInCustomPayload;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "BadPacket (A1)", type = "A1")
public class BadPacketA1 extends EdgeCheck
{
    public BadPacketA1(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_CUSTOM_PAYLOAD()) {
            final WrappedPacketInCustomPayload wrappedPacketInCustomPayload = new WrappedPacketInCustomPayload(packet.getRawPacket());
            if (wrappedPacketInCustomPayload.getChannelName().length() > 1500) {
                this.fail("Large payload size", "PLS=" + wrappedPacketInCustomPayload.getChannelName().length());
                this.kick();
            }
        }
    }
}
