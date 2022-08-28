package xyz.edge.ac.packetevents.packetwrappers.play.out.viewdistance;

import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutViewDistance extends WrappedPacket implements SendableWrapper
{
    private static Constructor<?> packetConstructor;
    private int viewDistance;
    
    public WrappedPacketOutViewDistance(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutViewDistance(final int viewDistance) {
        this.viewDistance = viewDistance;
    }
    
    @Override
    protected void load() {
        try {
            WrappedPacketOutViewDistance.packetConstructor = PacketTypeClasses.Play.Server.VIEW_DISTANCE.getConstructor(Integer.TYPE);
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public int getViewDistance() {
        if (this.packet != null) {
            return this.readInt(0);
        }
        return this.viewDistance;
    }
    
    public void setViewDistance(final int viewDistance) {
        if (this.packet != null) {
            this.writeInt(0, viewDistance);
        }
        else {
            this.viewDistance = viewDistance;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        return WrappedPacketOutViewDistance.packetConstructor.newInstance(this.getViewDistance());
    }
    
    @Override
    public boolean isSupported() {
        return WrappedPacketOutViewDistance.version.isNewerThan(ServerVersion.v_1_13_2);
    }
}
