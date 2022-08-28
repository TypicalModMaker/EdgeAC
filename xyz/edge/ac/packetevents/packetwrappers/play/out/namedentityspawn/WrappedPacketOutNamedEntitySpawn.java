package xyz.edge.ac.packetevents.packetwrappers.play.out.namedentityspawn;

import xyz.edge.ac.packetevents.PacketEvents;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;
import org.bukkit.Location;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.utils.vector.Vector3d;
import java.util.UUID;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

public class WrappedPacketOutNamedEntitySpawn extends WrappedPacketEntityAbstraction implements SendableWrapper
{
    private static boolean v_1_17;
    private static final float rotationDividend = 0.7111111f;
    private static boolean doublesPresent;
    private static boolean dataWatcherPresent;
    private static Constructor<?> packetConstructor;
    private UUID uuid;
    private Vector3d position;
    private float yaw;
    private float pitch;
    
    public WrappedPacketOutNamedEntitySpawn(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutNamedEntitySpawn(final int entityID, final UUID uuid, final Location location) {
        this.entityID = entityID;
        this.uuid = uuid;
        this.position = new Vector3d(location.getX(), location.getY(), location.getZ());
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }
    
    public WrappedPacketOutNamedEntitySpawn(final Entity entity, final UUID uuid, final Location location) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.uuid = uuid;
        this.position = new Vector3d(location.getX(), location.getY(), location.getZ());
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }
    
    public WrappedPacketOutNamedEntitySpawn(final Entity entity, final Location location) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.uuid = entity.getUniqueId();
        this.position = new Vector3d(location.getX(), location.getY(), location.getZ());
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }
    
    public WrappedPacketOutNamedEntitySpawn(final Entity entity) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.uuid = entity.getUniqueId();
        final Location location = entity.getLocation();
        this.position = new Vector3d(location.getX(), location.getY(), location.getZ());
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }
    
    public WrappedPacketOutNamedEntitySpawn(final int entityID, final UUID uuid, final Vector3d position, final float yaw, final float pitch) {
        this.entityID = entityID;
        this.uuid = uuid;
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
    }
    
    public WrappedPacketOutNamedEntitySpawn(final Entity entity, final UUID uuid, final Vector3d position, final float yaw, final float pitch) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.uuid = uuid;
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
    }
    
    @Override
    protected void load() {
        WrappedPacketOutNamedEntitySpawn.v_1_17 = WrappedPacketOutNamedEntitySpawn.version.isNewerThanOrEquals(ServerVersion.v_1_17);
        WrappedPacketOutNamedEntitySpawn.doublesPresent = (Reflection.getField(PacketTypeClasses.Play.Server.NAMED_ENTITY_SPAWN, Double.TYPE, 1) != null);
        WrappedPacketOutNamedEntitySpawn.dataWatcherPresent = (Reflection.getField(PacketTypeClasses.Play.Server.NAMED_ENTITY_SPAWN, NMSUtils.dataWatcherClass, 0) != null);
        try {
            if (WrappedPacketOutNamedEntitySpawn.v_1_17) {
                WrappedPacketOutNamedEntitySpawn.packetConstructor = PacketTypeClasses.Play.Server.NAMED_ENTITY_SPAWN.getConstructor(NMSUtils.packetDataSerializerClass);
            }
            else {
                WrappedPacketOutNamedEntitySpawn.packetConstructor = PacketTypeClasses.Play.Server.NAMED_ENTITY_SPAWN.getConstructor((Class<?>[])new Class[0]);
            }
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public UUID getUUID() {
        if (this.packet != null) {
            return this.readObject(0, (Class<? extends UUID>)UUID.class);
        }
        return this.uuid;
    }
    
    public void setUUID(final UUID uuid) {
        if (this.packet != null) {
            this.write(UUID.class, 0, uuid);
        }
        else {
            this.uuid = uuid;
        }
    }
    
    public Vector3d getPosition() {
        if (this.packet != null) {
            double x;
            double y;
            double z;
            if (WrappedPacketOutNamedEntitySpawn.doublesPresent) {
                x = this.readDouble(0);
                y = this.readDouble(1);
                z = this.readDouble(2);
            }
            else {
                x = this.readInt(1) / 32.0;
                y = this.readInt(2) / 32.0;
                z = this.readInt(3) / 32.0;
            }
            return new Vector3d(x, y, z);
        }
        return this.position;
    }
    
    public void setPosition(final Vector3d position) {
        if (this.packet != null) {
            if (WrappedPacketOutNamedEntitySpawn.doublesPresent) {
                this.writeDouble(0, position.x);
                this.writeDouble(1, position.y);
                this.writeDouble(2, position.z);
            }
            else {
                this.writeInt(1, (int)(position.x * 32.0));
                this.writeInt(2, (int)(position.y * 32.0));
                this.writeInt(3, (int)(position.z * 32.0));
            }
            this.writeByte(0, (byte)(this.yaw * 0.7111111f));
            this.writeByte(1, (byte)(this.pitch * 0.7111111f));
        }
        else {
            this.position = position;
        }
    }
    
    public float getYaw() {
        if (this.packet != null) {
            return this.readByte(0) / 0.7111111f;
        }
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        if (this.packet != null) {
            this.writeByte(0, (byte)(yaw * 0.7111111f));
        }
        else {
            this.yaw = yaw;
        }
    }
    
    public float getPitch() {
        if (this.packet != null) {
            return this.readByte(1) / 0.7111111f;
        }
        return this.pitch;
    }
    
    public void setPitch(final float pitch) {
        if (this.packet != null) {
            this.writeByte(1, (byte)(pitch * 0.7111111f));
        }
        else {
            this.pitch = pitch;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance;
        if (WrappedPacketOutNamedEntitySpawn.v_1_17) {
            final byte[] bytes = new byte[60];
            final Object byteBuf = PacketEvents.get().getByteBufUtil().newByteBuf(bytes);
            final Object packetDataSerializer = NMSUtils.generatePacketDataSerializer(byteBuf);
            packetInstance = WrappedPacketOutNamedEntitySpawn.packetConstructor.newInstance(packetDataSerializer);
        }
        else {
            packetInstance = WrappedPacketOutNamedEntitySpawn.packetConstructor.newInstance(new Object[0]);
        }
        final WrappedPacketOutNamedEntitySpawn wrappedPacketOutNamedEntitySpawn = new WrappedPacketOutNamedEntitySpawn(new NMSPacket(packetInstance));
        wrappedPacketOutNamedEntitySpawn.setEntityId(this.getEntityId());
        wrappedPacketOutNamedEntitySpawn.setUUID(this.getUUID());
        wrappedPacketOutNamedEntitySpawn.setPosition(this.getPosition());
        wrappedPacketOutNamedEntitySpawn.setYaw(this.getYaw());
        wrappedPacketOutNamedEntitySpawn.setPitch(this.getPitch());
        if (WrappedPacketOutNamedEntitySpawn.dataWatcherPresent) {
            wrappedPacketOutNamedEntitySpawn.write(NMSUtils.dataWatcherClass, 0, NMSUtils.generateDataWatcher(null));
        }
        return packetInstance;
    }
}
