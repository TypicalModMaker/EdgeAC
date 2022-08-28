package xyz.edge.ac.packetevents.packetwrappers.play.out.ping;

import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutPing extends WrappedPacket implements SendableWrapper
{
    private static Constructor<?> packetConstructor;
    private int id;
    
    public WrappedPacketOutPing(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutPing(final int id) {
        this.id = id;
    }
    
    @Override
    protected void load() {
        try {
            WrappedPacketOutPing.packetConstructor = PacketTypeClasses.Play.Server.PING.getConstructor(Integer.TYPE);
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public int getId() {
        if (this.packet != null) {
            return this.readInt(0);
        }
        return this.id;
    }
    
    public void setId(final int id) {
        if (this.packet != null) {
            this.writeInt(0, id);
        }
        else {
            this.id = id;
        }
    }
    
    @Override
    public boolean isSupported() {
        return WrappedPacketOutPing.version.isNewerThanOrEquals(ServerVersion.v_1_17);
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        return WrappedPacketOutPing.packetConstructor.newInstance(this.getId());
    }
}
