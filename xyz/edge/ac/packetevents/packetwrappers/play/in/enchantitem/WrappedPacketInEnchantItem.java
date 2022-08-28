package xyz.edge.ac.packetevents.packetwrappers.play.in.enchantitem;

import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInEnchantItem extends WrappedPacket
{
    public WrappedPacketInEnchantItem(final NMSPacket packet) {
        super(packet);
    }
    
    public int getWindowId() {
        return this.readInt(0);
    }
    
    public void setWindowId(final int windowID) {
        this.writeInt(0, windowID);
    }
    
    public int getButton() {
        return this.readInt(1);
    }
    
    public void setButton(final int button) {
        this.writeInt(1, button);
    }
}
