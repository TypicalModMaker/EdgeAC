package xyz.edge.ac.packetevents.packetwrappers.play.out.keepalive;

import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutKeepAlive extends WrappedPacket implements SendableWrapper
{
    private static Constructor<?> keepAliveConstructor;
    private static boolean integerMode;
    private long id;
    
    public WrappedPacketOutKeepAlive(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutKeepAlive(final long id) {
        this.id = id;
    }
    
    @Override
    protected void load() {
        final Class<?> packetClass = PacketTypeClasses.Play.Server.KEEP_ALIVE;
        WrappedPacketOutKeepAlive.integerMode = (Reflection.getField(packetClass, Integer.TYPE, 0) != null);
        if (WrappedPacketOutKeepAlive.integerMode) {
            try {
                WrappedPacketOutKeepAlive.keepAliveConstructor = packetClass.getConstructor(Integer.TYPE);
            }
            catch (final NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                WrappedPacketOutKeepAlive.keepAliveConstructor = packetClass.getConstructor(Long.TYPE);
            }
            catch (final NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
    
    public long getId() {
        if (this.packet == null) {
            return this.id;
        }
        if (WrappedPacketOutKeepAlive.integerMode) {
            return this.readInt(0);
        }
        return this.readLong(0);
    }
    
    public void setId(final long id) throws UnsupportedOperationException {
        if (this.packet != null && WrappedPacketOutKeepAlive.integerMode && (id < -2147483648L || id > 2147483647L)) {
            throw new UnsupportedOperationException("PacketEvents failed to set the Keep Alive ID in WrappedPacketOutKeepAlive. Your server version does not support IDs outside the range of an int primitive type. Your Keep Alive ID seems to be in the range of a long primitive type.");
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        if (WrappedPacketOutKeepAlive.integerMode) {
            return WrappedPacketOutKeepAlive.keepAliveConstructor.newInstance((int)this.getId());
        }
        return WrappedPacketOutKeepAlive.keepAliveConstructor.newInstance(this.getId());
    }
}
