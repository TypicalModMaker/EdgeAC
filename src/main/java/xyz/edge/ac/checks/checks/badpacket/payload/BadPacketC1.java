package xyz.edge.ac.checks.checks.badpacket.payload;

import io.github.retrooper.packetevents.packetwrappers.play.in.custompayload.WrappedPacketInCustomPayload;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "BadPacket (C1)", type = "C1")
public class BadPacketC1 extends EdgeCheck
{
    public BadPacketC1(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_CUSTOM_PAYLOAD()) {
            final WrappedPacketInCustomPayload wrappedPacketInCustomPayload = new WrappedPacketInCustomPayload(packet.getRawPacket());
            final String payload = wrappedPacketInCustomPayload.getChannelName();
            if (payload.equals("MC|BOpen") || payload.equals("MC|BEdit")) {
                final double buffer = this.buffer + 2.0;
                this.buffer = buffer;
                if (buffer > 4.0) {
                    this.fail("Spamming MC|BOpen / MC|BEdit", "T=" + wrappedPacketInCustomPayload.getChannelName() + " : B=" + this.buffer);
                    this.kick();
                }
            }
        }
    }
}
