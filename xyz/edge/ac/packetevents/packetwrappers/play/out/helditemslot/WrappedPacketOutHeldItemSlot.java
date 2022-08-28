package xyz.edge.ac.packetevents.packetwrappers.play.out.helditemslot;

import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutHeldItemSlot extends WrappedPacket implements SendableWrapper
{
    private static Constructor<?> packetConstructor;
    private int slot;
    
    public WrappedPacketOutHeldItemSlot(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutHeldItemSlot(final int slot) {
        this.slot = slot;
    }
    
    @Override
    protected void load() {
        try {
            WrappedPacketOutHeldItemSlot.packetConstructor = PacketTypeClasses.Play.Server.HELD_ITEM_SLOT.getConstructor(Integer.TYPE);
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public int getCurrentSelectedSlot() {
        if (this.packet == null) {
            return this.slot;
        }
        return this.readInt(0);
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        return WrappedPacketOutHeldItemSlot.packetConstructor.newInstance(this.slot);
    }
}
