package xyz.edge.ac.packetevents.packetwrappers.play.out.blockchange;

import java.lang.reflect.InvocationTargetException;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import org.bukkit.Location;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import org.bukkit.Material;
import org.bukkit.World;
import xyz.edge.ac.packetevents.utils.vector.Vector3i;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutBlockChange extends WrappedPacket implements SendableWrapper
{
    private static boolean v_1_7_10;
    private static boolean v_1_17;
    private static Constructor<?> packetConstructor;
    private static Method getNMSBlockMethodCache;
    private static Method getNMSWorldTypeMethodCache;
    private Vector3i blockPos;
    private World world;
    private Material blockType;
    
    public WrappedPacketOutBlockChange(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutBlockChange(final World world, final Vector3i blockPos) {
        this.blockPos = blockPos;
        this.world = world;
    }
    
    public WrappedPacketOutBlockChange(final Location location) {
        this.blockPos = new Vector3i(location);
        this.world = location.getWorld();
    }
    
    public WrappedPacketOutBlockChange(final World world, final Vector3i blockPos, final Material blockType) {
        this(world, blockPos);
        this.blockType = blockType;
    }
    
    public WrappedPacketOutBlockChange(final Location location, final Material blockType) {
        this(location);
        this.blockType = blockType;
    }
    
    @Override
    protected void load() {
        WrappedPacketOutBlockChange.getNMSBlockMethodCache = Reflection.getMethod(NMSUtils.iBlockDataClass, "getBlock", 0);
        if (WrappedPacketOutBlockChange.getNMSBlockMethodCache == null) {
            final Class<?> blockDataClass = NMSUtils.iBlockDataClass.getSuperclass();
            WrappedPacketOutBlockChange.getNMSBlockMethodCache = Reflection.getMethod(blockDataClass, NMSUtils.blockClass, 0);
        }
        WrappedPacketOutBlockChange.getNMSWorldTypeMethodCache = Reflection.getMethod(NMSUtils.nmsWorldClass, "getType", 0);
        if (WrappedPacketOutBlockChange.getNMSWorldTypeMethodCache == null) {
            WrappedPacketOutBlockChange.getNMSWorldTypeMethodCache = Reflection.getMethod(NMSUtils.nmsWorldClass, "getBlockStateIfLoaded", 0);
        }
        WrappedPacketOutBlockChange.v_1_7_10 = WrappedPacketOutBlockChange.version.isOlderThan(ServerVersion.v_1_8);
        WrappedPacketOutBlockChange.v_1_17 = WrappedPacketOutBlockChange.version.isNewerThanOrEquals(ServerVersion.v_1_17);
        if (WrappedPacketOutBlockChange.v_1_17) {
            try {
                WrappedPacketOutBlockChange.packetConstructor = PacketTypeClasses.Play.Server.BLOCK_CHANGE.getConstructor(NMSUtils.blockPosClass, NMSUtils.iBlockDataClass);
            }
            catch (final NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        else if (WrappedPacketOutBlockChange.v_1_7_10) {
            try {
                WrappedPacketOutBlockChange.packetConstructor = PacketTypeClasses.Play.Server.BLOCK_CHANGE.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE, NMSUtils.nmsWorldClass);
            }
            catch (final NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                WrappedPacketOutBlockChange.packetConstructor = PacketTypeClasses.Play.Server.BLOCK_CHANGE.getConstructor((Class<?>[])new Class[0]);
            }
            catch (final NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
    
    public Vector3i getBlockPosition() {
        if (this.packet == null) {
            return this.blockPos;
        }
        if (WrappedPacketOutBlockChange.v_1_7_10) {
            final int x = this.readInt(0);
            final int y = this.readInt(1);
            final int z = this.readInt(2);
            return new Vector3i(x, y, z);
        }
        return this.readBlockPosition(0);
    }
    
    public void setBlockPosition(final Vector3i blockPos) {
        if (this.packet != null) {
            if (WrappedPacketOutBlockChange.v_1_7_10) {
                this.writeInt(0, blockPos.x);
                this.writeInt(1, blockPos.y);
                this.writeInt(2, blockPos.z);
            }
            else {
                this.writeBlockPosition(0, blockPos);
            }
        }
        else {
            this.blockPos = blockPos;
        }
    }
    
    @Deprecated
    public Material getMaterial() {
        return this.getBlockType();
    }
    
    @Deprecated
    public void setMaterial(final Material material) {
        this.setBlockType(material);
    }
    
    public Material getBlockType() {
        if (this.packet != null) {
            Object nmsBlock = null;
            if (WrappedPacketOutBlockChange.v_1_7_10) {
                nmsBlock = this.readObject(0, NMSUtils.blockClass);
            }
            else {
                final Object iBlockDataObj = this.readObject(0, NMSUtils.iBlockDataClass);
                try {
                    nmsBlock = WrappedPacketOutBlockChange.getNMSBlockMethodCache.invoke(iBlockDataObj, new Object[0]);
                }
                catch (final IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            return NMSUtils.getMaterialFromNMSBlock(nmsBlock);
        }
        return this.blockType;
    }
    
    public void setBlockType(final Material blockType) {
        if (this.packet != null) {
            final Object nmsBlock = NMSUtils.getNMSBlockFromMaterial(blockType);
            if (WrappedPacketOutBlockChange.v_1_7_10) {
                this.write(NMSUtils.blockClass, 0, nmsBlock);
            }
            else {
                final WrappedPacket nmsBlockWrapper = new WrappedPacket(new NMSPacket(nmsBlock), NMSUtils.blockClass);
                final Object iBlockData = nmsBlockWrapper.readObject(0, NMSUtils.iBlockDataClass);
                this.write(NMSUtils.iBlockDataClass, 0, iBlockData);
            }
        }
        else {
            this.blockType = blockType;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        final Vector3i blockPosition = this.getBlockPosition();
        Object packetPlayOutBlockChangeInstance;
        if (WrappedPacketOutBlockChange.v_1_17) {
            final Object nmsBlockPos = NMSUtils.generateNMSBlockPos(blockPosition);
            final Object nmsBlock = NMSUtils.getNMSBlockFromMaterial(this.getBlockType());
            final WrappedPacket nmsBlockWrapper = new WrappedPacket(new NMSPacket(nmsBlock), NMSUtils.blockClass);
            final Object nmsIBlockData = nmsBlockWrapper.readObject(0, NMSUtils.iBlockDataClass);
            packetPlayOutBlockChangeInstance = WrappedPacketOutBlockChange.packetConstructor.newInstance(nmsBlockPos, nmsIBlockData);
        }
        else if (WrappedPacketOutBlockChange.v_1_7_10) {
            final Object nmsWorld = NMSUtils.convertBukkitWorldToWorldServer(this.world);
            packetPlayOutBlockChangeInstance = WrappedPacketOutBlockChange.packetConstructor.newInstance(blockPosition.x, blockPosition.y, blockPosition.z, nmsWorld);
            final WrappedPacketOutBlockChange blockChange = new WrappedPacketOutBlockChange(new NMSPacket(packetPlayOutBlockChangeInstance));
            final Material bt = this.getBlockType();
            if (bt != null) {
                blockChange.setBlockType(bt);
            }
        }
        else {
            packetPlayOutBlockChangeInstance = WrappedPacketOutBlockChange.packetConstructor.newInstance(new Object[0]);
            final WrappedPacketOutBlockChange blockChange = new WrappedPacketOutBlockChange(new NMSPacket(packetPlayOutBlockChangeInstance));
            final Material bt2 = this.getBlockType();
            if (bt2 != null) {
                blockChange.setBlockType(bt2);
            }
            else {
                final Object nmsBlockPos2 = NMSUtils.generateNMSBlockPos(blockPosition);
                final Object worldServer = NMSUtils.convertBukkitWorldToWorldServer(this.world);
                final Object nmsBlockData = WrappedPacketOutBlockChange.getNMSWorldTypeMethodCache.invoke(worldServer, nmsBlockPos2);
                blockChange.write(NMSUtils.iBlockDataClass, 0, nmsBlockData);
            }
            blockChange.setBlockPosition(blockPosition);
        }
        return packetPlayOutBlockChangeInstance;
    }
    
    static {
        WrappedPacketOutBlockChange.getNMSBlockMethodCache = null;
        WrappedPacketOutBlockChange.getNMSWorldTypeMethodCache = null;
    }
}
