package xyz.edge.ac.packetevents.packetwrappers.play.in.tabcomplete;

import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInTabComplete extends WrappedPacket
{
    public WrappedPacketInTabComplete(final NMSPacket packet) {
        super(packet);
    }
    
    public String getText() {
        return this.readString(0);
    }
    
    public void setText(final String text) {
        this.writeString(0, text);
    }
}
