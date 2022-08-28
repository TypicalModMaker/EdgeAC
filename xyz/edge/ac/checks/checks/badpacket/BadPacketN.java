package xyz.edge.ac.checks.checks.badpacket;

import xyz.edge.ac.packetevents.packetwrappers.play.in.chat.WrappedPacketInChat;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "BadPacket (N)", type = "N")
public class BadPacketN extends EdgeCheck
{
    public BadPacketN(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_CHAT()) {
            final WrappedPacketInChat chat = new WrappedPacketInChat(packet.getRawPacket());
            final String message = chat.getMessage();
            if (message == null || message.isEmpty()) {
                this.fail("Empty chat message", "M=null");
                this.kick();
            }
        }
    }
}
