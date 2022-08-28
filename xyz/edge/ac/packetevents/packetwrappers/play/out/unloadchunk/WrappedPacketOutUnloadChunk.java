package xyz.edge.ac.packetevents.packetwrappers.play.out.unloadchunk;

import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public final class WrappedPacketOutUnloadChunk extends WrappedPacket implements SendableWrapper
{
    private static Constructor<?> packetConstructor;
    private int chunkX;
    private int chunkZ;
    
    public WrappedPacketOutUnloadChunk(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutUnloadChunk(final int chunkX, final int chunkZ) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }
    
    @Override
    protected void load() {
        try {
            WrappedPacketOutUnloadChunk.packetConstructor = PacketTypeClasses.Play.Server.UNLOAD_CHUNK.getConstructor(Integer.TYPE, Integer.TYPE);
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public int getChunkX() {
        if (this.packet != null) {
            return this.readInt(0);
        }
        return this.chunkX;
    }
    
    public void setChunkX(final int chunkX) {
        if (this.packet != null) {
            this.writeInt(0, chunkX);
        }
        else {
            this.chunkX = chunkX;
        }
    }
    
    public int getChunkZ() {
        if (this.packet != null) {
            return this.readInt(1);
        }
        return this.chunkZ;
    }
    
    public void setChunkZ(final int chunkZ) {
        if (this.packet != null) {
            this.writeInt(1, chunkZ);
        }
        else {
            this.chunkZ = chunkZ;
        }
    }
    
    @Override
    public boolean isSupported() {
        return WrappedPacketOutUnloadChunk.version.isNewerThan(ServerVersion.v_1_8_8);
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        return WrappedPacketOutUnloadChunk.packetConstructor.newInstance(this.getChunkX(), this.getChunkZ());
    }
}
