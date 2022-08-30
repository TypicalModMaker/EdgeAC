package xyz.edge.ac.checks.checks.badpacket.payload;

import io.github.retrooper.packetevents.packetwrappers.play.in.custompayload.WrappedPacketInCustomPayload;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "BadPacket (B1)", type = "B1")
public class BadPacketB1 extends EdgeCheck
{
    public BadPacketB1(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_CUSTOM_PAYLOAD()) {
            final WrappedPacketInCustomPayload wrappedPacketInCustomPayload = new WrappedPacketInCustomPayload(packet.getRawPacket());
            if (wrappedPacketInCustomPayload.getChannelName().equals("Vanilla")) {
                this.fail("joined with capital V in vanilla", "CB=" + wrappedPacketInCustomPayload.getChannelName());
                this.kick();
            }
        }
    }
}
