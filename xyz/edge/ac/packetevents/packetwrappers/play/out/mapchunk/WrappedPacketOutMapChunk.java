package xyz.edge.ac.packetevents.packetwrappers.play.out.mapchunk;

import java.lang.reflect.InvocationTargetException;
import java.util.BitSet;
import java.util.Optional;
import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import xyz.edge.ac.packetevents.utils.reflection.SubclassUtil;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutMapChunk extends WrappedPacket
{
    private static boolean v_1_8_x;
    private static boolean v_1_17;
    private static boolean v_1_18;
    private static Class<?> chunkMapClass;
    private static Class<?> chunkPacketDataClass;
    private Constructor<?> chunkMapConstructor;
    private Object nmsChunkMap;
    
    public WrappedPacketOutMapChunk(final NMSPacket packet) {
        super(packet);
    }
    
    @Override
    protected void load() {
        WrappedPacketOutMapChunk.v_1_8_x = (WrappedPacketOutMapChunk.version.isNewerThan(ServerVersion.v_1_7_10) && WrappedPacketOutMapChunk.version.isOlderThan(ServerVersion.v_1_9));
        WrappedPacketOutMapChunk.v_1_17 = WrappedPacketOutMapChunk.version.isNewerThanOrEquals(ServerVersion.v_1_17);
        WrappedPacketOutMapChunk.v_1_18 = WrappedPacketOutMapChunk.version.isNewerThanOrEquals(ServerVersion.v_1_18);
        if (WrappedPacketOutMapChunk.v_1_8_x) {
            WrappedPacketOutMapChunk.chunkMapClass = SubclassUtil.getSubClass(PacketTypeClasses.Play.Server.MAP_CHUNK, 0);
            try {
                this.chunkMapConstructor = WrappedPacketOutMapChunk.chunkMapClass.getConstructor((Class<?>[])new Class[0]);
            }
            catch (final NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        WrappedPacketOutMapChunk.chunkPacketDataClass = Reflection.getClassByNameWithoutException("net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData");
    }
    
    public int getChunkX() {
        return this.readInt((WrappedPacketOutMapChunk.v_1_17 && !WrappedPacketOutMapChunk.v_1_18) ? 1 : 0);
    }
    
    public void setChunkX(final int chunkX) {
        this.writeInt((WrappedPacketOutMapChunk.v_1_17 && !WrappedPacketOutMapChunk.v_1_18) ? 1 : 0, chunkX);
    }
    
    public int getChunkZ() {
        return this.readInt((WrappedPacketOutMapChunk.v_1_17 && !WrappedPacketOutMapChunk.v_1_18) ? 2 : 1);
    }
    
    public void setChunkZ(final int chunkZ) {
        this.writeInt((WrappedPacketOutMapChunk.v_1_17 && !WrappedPacketOutMapChunk.v_1_18) ? 2 : 1, chunkZ);
    }
    
    public Optional<BitSet> getBitSet() {
        if (WrappedPacketOutMapChunk.v_1_18) {
            return Optional.empty();
        }
        if (WrappedPacketOutMapChunk.v_1_17) {
            return Optional.of(this.readObject(0, (Class<? extends BitSet>)BitSet.class));
        }
        if (WrappedPacketOutMapChunk.v_1_8_x) {
            if (this.nmsChunkMap == null) {
                this.nmsChunkMap = this.readObject(0, WrappedPacketOutMapChunk.chunkMapClass);
            }
            final WrappedPacket nmsChunkMapWrapper = new WrappedPacket(new NMSPacket(this.nmsChunkMap));
            return Optional.of(BitSet.valueOf(new long[] { nmsChunkMapWrapper.readInt(0) }));
        }
        return Optional.of(BitSet.valueOf(new long[] { this.readInt(2) }));
    }
    
    public void setPrimaryBitMask(final BitSet bitSet) {
        if (WrappedPacketOutMapChunk.v_1_18) {
            return;
        }
        if (WrappedPacketOutMapChunk.v_1_17) {
            this.writeObject(0, bitSet);
            return;
        }
        this.setPrimaryBitMask((int)bitSet.toLongArray()[0]);
    }
    
    @Deprecated
    public void setPrimaryBitMask(final int primaryBitMask) {
        if (WrappedPacketOutMapChunk.v_1_18) {
            return;
        }
        if (WrappedPacketOutMapChunk.v_1_17) {
            this.writeObject(0, BitSet.valueOf(new long[] { primaryBitMask }));
            return;
        }
        if (WrappedPacketOutMapChunk.v_1_8_x) {
            if (this.nmsChunkMap == null) {
                try {
                    this.nmsChunkMap = this.chunkMapConstructor.newInstance(new Object[0]);
                }
                catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            final WrappedPacket nmsChunkMapWrapper = new WrappedPacket(new NMSPacket(this.nmsChunkMap));
            nmsChunkMapWrapper.writeInt(0, primaryBitMask);
            this.write(WrappedPacketOutMapChunk.chunkMapClass, 0, this.nmsChunkMap);
        }
        else {
            this.writeInt(2, primaryBitMask);
        }
    }
    
    public Optional<Boolean> isGroundUpContinuous() {
        if (WrappedPacketOutMapChunk.v_1_17) {
            return Optional.empty();
        }
        return Optional.of(this.readBoolean(0));
    }
    
    public void setGroundUpContinuous(final boolean groundUpContinuous) {
        if (WrappedPacketOutMapChunk.v_1_17) {
            return;
        }
        this.writeBoolean(0, groundUpContinuous);
    }
    
    public byte[] getCompressedData() {
        if (WrappedPacketOutMapChunk.v_1_8_x) {
            if (this.nmsChunkMap == null) {
                this.nmsChunkMap = this.readObject(0, WrappedPacketOutMapChunk.chunkMapClass);
            }
            final WrappedPacket nmsChunkMapWrapper = new WrappedPacket(new NMSPacket(this.nmsChunkMap));
            return nmsChunkMapWrapper.readByteArray(0);
        }
        if (WrappedPacketOutMapChunk.v_1_18) {
            return new WrappedPacket(new NMSPacket(this.readObject(0, WrappedPacketOutMapChunk.chunkPacketDataClass))).readByteArray(0);
        }
        return this.readByteArray(0);
    }
    
    public void setCompressedData(final byte[] data) {
        if (WrappedPacketOutMapChunk.v_1_8_x) {
            if (this.nmsChunkMap == null) {
                try {
                    this.nmsChunkMap = this.chunkMapConstructor.newInstance(new Object[0]);
                }
                catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            final WrappedPacket nmsChunkMapWrapper = new WrappedPacket(new NMSPacket(this.nmsChunkMap));
            nmsChunkMapWrapper.writeByteArray(0, data);
            this.write(WrappedPacketOutMapChunk.chunkMapClass, 0, this.nmsChunkMap);
        }
        else if (WrappedPacketOutMapChunk.v_1_18) {
            new WrappedPacket(new NMSPacket(this.readObject(0, WrappedPacketOutMapChunk.chunkPacketDataClass))).writeByteArray(0, data);
        }
        else {
            this.writeByteArray(0, data);
        }
    }
}
