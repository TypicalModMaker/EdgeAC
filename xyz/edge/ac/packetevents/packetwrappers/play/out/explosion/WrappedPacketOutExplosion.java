package xyz.edge.ac.packetevents.packetwrappers.play.out.explosion;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.ArrayList;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.utils.vector.Vector3f;
import xyz.edge.ac.packetevents.utils.vector.Vector3d;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.utils.vector.Vector3i;
import java.util.List;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutExplosion extends WrappedPacket implements SendableWrapper
{
    private static boolean v_1_8;
    private static Constructor<?> chunkPosConstructor;
    private static Constructor<?> packetConstructor;
    private double x;
    private double y;
    private double z;
    private float strength;
    private List<Vector3i> records;
    private float playerVelocityX;
    private float playerVelocityY;
    private float playerVelocityZ;
    
    public WrappedPacketOutExplosion(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutExplosion(final Vector3d position, final float strength, final List<Vector3i> records, final Vector3f playerVelocity) {
        this.x = position.x;
        this.y = position.y;
        this.z = position.z;
        this.strength = strength;
        this.records = records;
        this.playerVelocityX = playerVelocity.x;
        this.playerVelocityY = playerVelocity.y;
        this.playerVelocityZ = playerVelocity.z;
    }
    
    public WrappedPacketOutExplosion(final double x, final double y, final double z, final float strength, final List<Vector3i> records, final float playerVelocityX, final float playerVelocityY, final float playerVelocityZ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.strength = strength;
        this.records = records;
        this.playerVelocityX = playerVelocityX;
        this.playerVelocityY = playerVelocityY;
        this.playerVelocityZ = playerVelocityZ;
    }
    
    @Override
    protected void load() {
        WrappedPacketOutExplosion.v_1_8 = WrappedPacketOutExplosion.version.isNewerThanOrEquals(ServerVersion.v_1_8);
        try {
            final Class<?> chunkPosClass = NMSUtils.getNMSClassWithoutException("ChunkPosition");
            WrappedPacketOutExplosion.packetConstructor = PacketTypeClasses.Play.Server.EXPLOSION.getConstructor(Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, List.class, NMSUtils.vec3DClass);
            if (chunkPosClass != null) {
                WrappedPacketOutExplosion.chunkPosConstructor = chunkPosClass.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE);
            }
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public Vector3d getPosition() {
        if (this.packet != null) {
            return new Vector3d(this.readDouble(0), this.readDouble(1), this.readDouble(2));
        }
        return new Vector3d(this.x, this.y, this.z);
    }
    
    public void setPosition(final Vector3d position) {
        if (this.packet != null) {
            this.writeDouble(0, position.x);
            this.writeDouble(1, position.y);
            this.writeDouble(2, position.z);
        }
        else {
            this.x = position.x;
            this.y = position.y;
            this.z = position.z;
        }
    }
    
    @Deprecated
    public double getX() {
        if (this.packet != null) {
            return this.readDouble(0);
        }
        return this.x;
    }
    
    @Deprecated
    public void setX(final double x) {
        if (this.packet != null) {
            this.writeDouble(0, x);
        }
        else {
            this.x = x;
        }
    }
    
    @Deprecated
    public double getY() {
        if (this.packet != null) {
            return this.readDouble(1);
        }
        return this.y;
    }
    
    @Deprecated
    public void setY(final double y) {
        if (this.packet != null) {
            this.writeDouble(1, y);
        }
        else {
            this.y = y;
        }
    }
    
    @Deprecated
    public double getZ() {
        if (this.packet != null) {
            return this.readDouble(2);
        }
        return this.z;
    }
    
    @Deprecated
    public void setZ(final double z) {
        if (this.packet != null) {
            this.writeDouble(2, z);
        }
        else {
            this.z = z;
        }
    }
    
    public float getStrength() {
        if (this.packet != null) {
            return this.readFloat(0);
        }
        return this.strength;
    }
    
    public void setStrength(final float strength) {
        if (this.packet != null) {
            this.writeFloat(0, strength);
        }
        else {
            this.strength = strength;
        }
    }
    
    public List<Vector3i> getRecords() {
        if (this.packet == null) {
            return this.records;
        }
        final List<Vector3i> recordsList = new ArrayList<Vector3i>();
        final List<?> rawRecordsList = this.readObject(0, (Class<? extends List<?>>)List.class);
        if (rawRecordsList.isEmpty()) {
            return new ArrayList<Vector3i>();
        }
        for (final Object position : rawRecordsList) {
            final WrappedPacket posWrapper = new WrappedPacket(new NMSPacket(position));
            final int x = posWrapper.readInt(0);
            final int y = posWrapper.readInt(1);
            final int z = posWrapper.readInt(2);
            recordsList.add(new Vector3i(x, y, z));
        }
        return recordsList;
    }
    
    public void setRecords(final List<Vector3i> records) {
        if (this.packet != null) {
            final List<Object> nmsRecordsList = new ArrayList<Object>();
            for (final Vector3i record : records) {
                Object position = null;
                try {
                    position = (WrappedPacketOutExplosion.v_1_8 ? NMSUtils.generateNMSBlockPos(record) : WrappedPacketOutExplosion.chunkPosConstructor.newInstance(record.x, record.y, record.z));
                }
                catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                nmsRecordsList.add(position);
            }
            this.write(List.class, 0, nmsRecordsList);
        }
        else {
            this.records = records;
        }
    }
    
    public Vector3f getPlayerVelocity() {
        if (this.packet != null) {
            return new Vector3f(this.readFloat(1), this.readFloat(2), this.readFloat(3));
        }
        return new Vector3f(this.playerVelocityX, this.playerVelocityY, this.playerVelocityZ);
    }
    
    public void setPlayerVelocity(final Vector3f playerVelocity) {
        if (this.packet != null) {
            this.writeFloat(1, playerVelocity.x);
            this.writeFloat(2, playerVelocity.y);
            this.writeFloat(3, playerVelocity.z);
        }
        else {
            this.playerVelocityX = playerVelocity.x;
            this.playerVelocityY = playerVelocity.y;
            this.playerVelocityZ = playerVelocity.z;
        }
    }
    
    @Deprecated
    public float getPlayerMotionX() {
        if (this.packet != null) {
            return this.readFloat(1);
        }
        return this.playerVelocityX;
    }
    
    @Deprecated
    public void setPlayerMotionX(final float playerMotionX) {
        if (this.packet != null) {
            this.writeFloat(1, playerMotionX);
        }
        else {
            this.playerVelocityX = playerMotionX;
        }
    }
    
    @Deprecated
    public float getPlayerMotionY() {
        if (this.packet != null) {
            return this.readFloat(2);
        }
        return this.playerVelocityY;
    }
    
    @Deprecated
    public void setPlayerMotionY(final float playerMotionY) {
        if (this.packet != null) {
            this.writeFloat(2, playerMotionY);
        }
        else {
            this.playerVelocityY = playerMotionY;
        }
    }
    
    @Deprecated
    public float getPlayerMotionZ() {
        if (this.packet != null) {
            return this.readFloat(3);
        }
        return this.playerVelocityZ;
    }
    
    @Deprecated
    public void setPlayerMotionZ(final float playerMotionZ) {
        if (this.packet != null) {
            this.writeFloat(3, playerMotionZ);
        }
        else {
            this.playerVelocityZ = playerMotionZ;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        final List<Object> positions = new ArrayList<Object>();
        for (final Vector3i record : this.getRecords()) {
            final Object position = WrappedPacketOutExplosion.v_1_8 ? NMSUtils.generateNMSBlockPos(record) : WrappedPacketOutExplosion.chunkPosConstructor.newInstance(record.x, record.y, record.z);
            positions.add(position);
        }
        final Vector3f velocity = this.getPlayerVelocity();
        final Vector3f pos = this.getPlayerVelocity();
        final Object vec = NMSUtils.generateVec3D(velocity);
        return WrappedPacketOutExplosion.packetConstructor.newInstance(pos.x, pos.y, pos.z, this.getStrength(), positions, vec);
    }
}
