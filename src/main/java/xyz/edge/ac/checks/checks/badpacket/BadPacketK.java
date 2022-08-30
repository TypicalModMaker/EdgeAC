package xyz.edge.ac.checks.checks.badpacket;

import io.github.retrooper.packetevents.packetwrappers.play.in.custompayload.WrappedPacketInCustomPayload;
import io.github.retrooper.packetevents.packetwrappers.play.in.itemname.WrappedPacketInItemName;
import io.github.retrooper.packetevents.packetwrappers.play.in.chat.WrappedPacketInChat;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "BadPacket (K)", type = "K")
public class BadPacketK extends EdgeCheck
{
    public BadPacketK(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_CHAT()) {
            final WrappedPacketInChat wrapper = new WrappedPacketInChat(packet.getRawPacket());
            if (wrapper.getMessage().contains("${")) {
                wrapper.setMessage("");
                this.fail("Attemped Log4J", "T=chat");
                this.kick();
            }
        }
        else if (packet.PACKET_ITEM_NAME()) {
            final WrappedPacketInItemName wrapper2 = new WrappedPacketInItemName(packet.getRawPacket());
            if (wrapper2.getItemName().contains("${")) {
                wrapper2.setItemName("Word Blocked");
                this.fail("Attemped Log4J", "T=item");
                this.kick();
            }
        }
        else if (packet.PACKET_CUSTOM_PAYLOAD()) {
            final WrappedPacketInCustomPayload wrapper3 = new WrappedPacketInCustomPayload(packet.getRawPacket());
            if (wrapper3.getChannelName().contains("${")) {
                wrapper3.setChannelName("WordBlocked");
                this.fail("Attemped Log4J", "T=payload");
                this.kick();
            }
        }
    }
}
