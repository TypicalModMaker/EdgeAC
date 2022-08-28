package xyz.edge.ac.packetevents.packetwrappers.play.in.closewindow;

import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInCloseWindow extends WrappedPacket
{
    public WrappedPacketInCloseWindow(final NMSPacket packet) {
        super(packet);
    }
    
    public int getWindowId() {
        return this.readInt(0);
    }
    
    public void setWindowId(final int windowID) {
        this.writeInt(0, windowID);
    }
}
