package xyz.edge.ac.packetevents.packetwrappers.play.in.setcreativeslot;

import org.bukkit.inventory.ItemStack;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInSetCreativeSlot extends WrappedPacket
{
    public WrappedPacketInSetCreativeSlot(final NMSPacket packet) {
        super(packet);
    }
    
    public int getSlot() {
        return this.readInt(0);
    }
    
    public void setSlot(final int value) {
        this.writeInt(0, value);
    }
    
    public ItemStack getClickedItem() {
        return this.readItemStack(0);
    }
    
    public void setClickedItem(final ItemStack stack) {
        this.writeItemStack(0, stack);
    }
}
