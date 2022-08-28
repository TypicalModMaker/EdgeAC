package xyz.edge.ac.packetevents.packetwrappers.status.out.pong;

import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketStatusPong extends WrappedPacket implements SendableWrapper
{
    private static Constructor<?> packetConstructor;
    private long payload;
    
    public WrappedPacketStatusPong(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketStatusPong(final long payload) {
        this.payload = payload;
    }
    
    @Override
    protected void load() {
        try {
            WrappedPacketStatusPong.packetConstructor = PacketTypeClasses.Status.Server.PONG.getConstructor(Long.TYPE);
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public long getPayload() {
        if (this.packet != null) {
            return this.readLong(0);
        }
        return this.payload;
    }
    
    public void setPayload(final long payload) {
        if (this.packet != null) {
            this.writeLong(0, payload);
        }
        else {
            this.payload = payload;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        return WrappedPacketStatusPong.packetConstructor.newInstance(this.getPayload());
    }
}
