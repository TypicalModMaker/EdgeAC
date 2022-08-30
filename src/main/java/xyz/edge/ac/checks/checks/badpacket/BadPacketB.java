package xyz.edge.ac.checks.checks.badpacket;

import io.github.retrooper.packetevents.packetwrappers.play.in.helditemslot.WrappedPacketInHeldItemSlot;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "BadPacket (B)", type = "B")
public final class BadPacketB extends EdgeCheck
{
    private int lastSlot;
    
    public BadPacketB(final User user) {
        super(user);
        this.lastSlot = -1;
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_HELD_ITEM_SLOT()) {
            final WrappedPacketInHeldItemSlot wrapper = new WrappedPacketInHeldItemSlot(packet.getRawPacket());
            final int slot = wrapper.getCurrentSelectedSlot();
            this.debug("Switched to same slot &8[&cS=" + slot + "/" + this.lastSlot + "&8]");
            if (slot == this.lastSlot) {
                this.fail("Switched to same slot", "S=" + slot + " : LS=" + this.lastSlot);
            }
            this.lastSlot = slot;
        }
    }
}
