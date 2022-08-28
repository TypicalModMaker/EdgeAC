package xyz.edge.ac.packetevents.packetwrappers.play.out.closewindow;

import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutCloseWindow extends WrappedPacket implements SendableWrapper
{
    private static Constructor<?> constructor;
    private int windowID;
    
    public WrappedPacketOutCloseWindow(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutCloseWindow(final int windowID) {
        this.windowID = windowID;
    }
    
    @Override
    protected void load() {
        try {
            WrappedPacketOutCloseWindow.constructor = PacketTypeClasses.Play.Server.CLOSE_WINDOW.getConstructor(Integer.TYPE);
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public int getWindowId() {
        if (this.packet != null) {
            return this.readInt(0);
        }
        return this.windowID;
    }
    
    public void setWindowId(final int windowID) {
        if (this.packet != null) {
            this.writeInt(0, windowID);
        }
        else {
            this.windowID = windowID;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        return WrappedPacketOutCloseWindow.constructor.newInstance(this.getWindowId());
    }
}
