package xyz.edge.ac.packetevents.packetwrappers.login.out.setcompression;

import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketLoginOutSetCompression extends WrappedPacket implements SendableWrapper
{
    private static Constructor<?> constructor;
    private int threshold;
    
    public WrappedPacketLoginOutSetCompression(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketLoginOutSetCompression(final int threshold) {
        this.threshold = threshold;
    }
    
    @Override
    protected void load() {
        try {
            if (PacketTypeClasses.Login.Server.SET_COMPRESSION != null) {
                WrappedPacketLoginOutSetCompression.constructor = PacketTypeClasses.Login.Server.SET_COMPRESSION.getConstructor(Integer.TYPE);
            }
        }
        catch (final NoSuchMethodException ex) {}
    }
    
    public int getThreshold() {
        if (this.packet != null) {
            return this.readInt(0);
        }
        return this.threshold;
    }
    
    public void setThreshold(final int threshold) {
        if (this.packet != null) {
            this.writeInt(0, threshold);
        }
        else {
            this.threshold = threshold;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        return WrappedPacketLoginOutSetCompression.constructor.newInstance(this.getThreshold());
    }
    
    @Override
    public boolean isSupported() {
        return WrappedPacketLoginOutSetCompression.version.isNewerThan(ServerVersion.v_1_7_10);
    }
}
