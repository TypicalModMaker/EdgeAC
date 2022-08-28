package xyz.edge.ac.packetevents.packetwrappers.play.in.helditemslot;

import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public final class WrappedPacketInHeldItemSlot extends WrappedPacket
{
    public WrappedPacketInHeldItemSlot(final NMSPacket packet) {
        super(packet);
    }
    
    public int getCurrentSelectedSlot() {
        return this.readInt(0);
    }
    
    public void setCurrentSelectedSlot(final int slot) {
        this.writeInt(0, slot);
    }
}
