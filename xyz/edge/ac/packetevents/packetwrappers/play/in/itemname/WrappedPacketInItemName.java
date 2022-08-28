package xyz.edge.ac.packetevents.packetwrappers.play.in.itemname;

import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInItemName extends WrappedPacket
{
    public WrappedPacketInItemName(final NMSPacket packet) {
        super(packet);
    }
    
    public String getItemName() {
        return this.readString(0);
    }
    
    public void setItemName(final String itemName) {
        this.writeString(0, itemName);
    }
}
