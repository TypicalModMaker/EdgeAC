package xyz.edge.ac.packetevents.packetwrappers.play.out.spawnentityliving;

import xyz.edge.ac.packetevents.PacketEvents;
import xyz.edge.ac.packetevents.utils.math.MathUtils;
import java.util.Optional;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.util.UUID;
import org.bukkit.entity.EntityType;
import xyz.edge.ac.packetevents.utils.vector.Vector3d;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

public class WrappedPacketOutSpawnEntityLiving extends WrappedPacketEntityAbstraction implements SendableWrapper
{
    private static final byte[] byteBufAllocation;
    private static final float ROTATION_FACTOR = 0.7111111f;
    private static final double VELOCITY_FACTOR = 8000.0;
    private static boolean v_1_9;
    private static boolean v_1_17;
    private static Constructor<?> packetConstructor;
    private Vector3d position;
    private Vector3d velocity;
    private EntityType entityType;
    private UUID uuid;
    private float yaw;
    private float pitch;
    private float headPitch;
    
    public WrappedPacketOutSpawnEntityLiving(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutSpawnEntityLiving(final Entity entity, final UUID uuid, final EntityType entityType, final Vector3d position, final Vector3d velocity, final float yaw, final float pitch, final float headPitch) {
        this.setEntity(entity);
        this.uuid = uuid;
        this.entityType = entityType;
        this.position = position;
        this.velocity = velocity;
        this.yaw = yaw;
        this.pitch = pitch;
        this.headPitch = headPitch;
    }
    
    public WrappedPacketOutSpawnEntityLiving(final int entityID, final UUID uuid, final EntityType entityType, final Vector3d position, final Vector3d velocity, final float yaw, final float pitch, final float headPitch) {
        this.setEntityId(entityID);
        this.uuid = uuid;
        this.entityType = entityType;
        this.position = position;
        this.velocity = velocity;
        this.yaw = yaw;
        this.pitch = pitch;
        this.headPitch = headPitch;
    }
    
    @Override
    protected void load() {
        try {
            WrappedPacketOutSpawnEntityLiving.v_1_9 = WrappedPacketOutSpawnEntityLiving.version.isNewerThanOrEquals(ServerVersion.v_1_9);
            WrappedPacketOutSpawnEntityLiving.v_1_17 = WrappedPacketOutSpawnEntityLiving.version.isNewerThanOrEquals(ServerVersion.v_1_17);
            if (WrappedPacketOutSpawnEntityLiving.v_1_17) {
                WrappedPacketOutSpawnEntityLiving.packetConstructor = PacketTypeClasses.Play.Server.SPAWN_ENTITY_LIVING.getConstructor(NMSUtils.packetDataSerializerClass);
            }
            else {
                WrappedPacketOutSpawnEntityLiving.packetConstructor = PacketTypeClasses.Play.Server.SPAWN_ENTITY_LIVING.getConstructor((Class<?>[])new Class[0]);
            }
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public Optional<UUID> getUUID() {
        if (!WrappedPacketOutSpawnEntityLiving.v_1_9) {
            return Optional.empty();
        }
        if (this.packet != null) {
            return Optional.of(this.readObject(0, (Class<? extends UUID>)UUID.class));
        }
        return Optional.of(this.uuid);
    }
    
    public void setUUID(final UUID uuid) {
        if (WrappedPacketOutSpawnEntityLiving.v_1_9) {
            if (this.packet != null) {
                this.writeObject(0, uuid);
            }
            else {
                this.uuid = uuid;
            }
        }
    }
    
    public EntityType getEntityType() {
        if (this.packet != null) {
            final int entityTypeID = this.readInt(1);
            return EntityType.fromId(entityTypeID);
        }
        return this.entityType;
    }
    
    public void setEntityType(final EntityType entityType) {
        if (this.packet != null) {
            this.writeInt(1, entityType.getTypeId());
        }
        else {
            this.entityType = entityType;
        }
    }
    
    public Vector3d getPosition() {
        if (this.packet != null) {
            double x;
            double y;
            double z;
            if (!WrappedPacketOutSpawnEntityLiving.v_1_9) {
                x = this.readInt(2) / 32.0;
                y = this.readInt(3) / 32.0;
                z = this.readInt(4) / 32.0;
            }
            else {
                x = this.readDouble(0);
                y = this.readDouble(1);
                z = this.readDouble(2);
            }
            return new Vector3d(x, y, z);
        }
        return this.position;
    }
    
    public void setPosition(final Vector3d position) {
        if (this.packet != null) {
            if (!WrappedPacketOutSpawnEntityLiving.v_1_9) {
                this.writeInt(2, MathUtils.floor(position.x * 32.0));
                this.writeInt(3, MathUtils.floor(position.y * 32.0));
                this.writeInt(4, MathUtils.floor(position.z * 32.0));
            }
            else {
                this.writeDouble(0, position.x);
                this.writeDouble(1, position.y);
                this.writeDouble(2, position.z);
            }
        }
        else {
            this.position = position;
        }
    }
    
    public Vector3d getVelocity() {
        if (this.packet != null) {
            int factoredVelX;
            int factoredVelY;
            int factoredVelZ;
            if (!WrappedPacketOutSpawnEntityLiving.v_1_9) {
                factoredVelX = this.readInt(5);
                factoredVelY = this.readInt(6);
                factoredVelZ = this.readInt(7);
            }
            else {
                factoredVelX = this.readInt(2);
                factoredVelY = this.readInt(3);
                factoredVelZ = this.readInt(4);
            }
            return new Vector3d(factoredVelX / 8000.0, factoredVelY / 8000.0, factoredVelZ / 8000.0);
        }
        return this.velocity;
    }
    
    public void setVelocity(final Vector3d velocity) {
        if (this.packet != null) {
            final int factoredVelX = (int)(velocity.x * 8000.0);
            final int factoredVelY = (int)(velocity.y * 8000.0);
            final int factoredVelZ = (int)(velocity.z * 8000.0);
            if (!WrappedPacketOutSpawnEntityLiving.v_1_9) {
                this.writeInt(5, factoredVelX);
                this.writeInt(6, factoredVelY);
                this.writeInt(7, factoredVelZ);
            }
            else {
                this.writeInt(2, factoredVelX);
                this.writeInt(3, factoredVelY);
                this.writeInt(4, factoredVelZ);
            }
        }
        else {
            this.velocity = velocity;
        }
    }
    
    public float getYaw() {
        if (this.packet != null) {
            final byte factoredYaw = this.readByte(0);
            return factoredYaw / 0.7111111f;
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
            final byte factoredPitch = this.readByte(1);
            return factoredPitch / 0.7111111f;
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
    
    public float getHeadPitch() {
        if (this.packet != null) {
            final byte factoredHeadPitch = this.readByte(2);
            return factoredHeadPitch / 0.7111111f;
        }
        return this.headPitch;
    }
    
    public void setHeadPitch(final float headPitch) {
        if (this.packet != null) {
            this.writeByte(2, (byte)(headPitch * 0.7111111f));
        }
        else {
            this.headPitch = headPitch;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance;
        if (WrappedPacketOutSpawnEntityLiving.v_1_17) {
            final Object byteBuf = PacketEvents.get().getByteBufUtil().newByteBuf(WrappedPacketOutSpawnEntityLiving.byteBufAllocation);
            final Object packetDataSerializer = NMSUtils.generatePacketDataSerializer(byteBuf);
            packetInstance = WrappedPacketOutSpawnEntityLiving.packetConstructor.newInstance(packetDataSerializer);
        }
        else {
            packetInstance = WrappedPacketOutSpawnEntityLiving.packetConstructor.newInstance(new Object[0]);
        }
        final WrappedPacketOutSpawnEntityLiving wrappedPacketOutSpawnEntityLiving = new WrappedPacketOutSpawnEntityLiving(new NMSPacket(packetInstance));
        wrappedPacketOutSpawnEntityLiving.setEntityId(this.getEntityId());
        if (WrappedPacketOutSpawnEntityLiving.v_1_9) {
            wrappedPacketOutSpawnEntityLiving.setUUID(this.getUUID().get());
        }
        wrappedPacketOutSpawnEntityLiving.setEntityType(this.getEntityType());
        wrappedPacketOutSpawnEntityLiving.setPosition(this.getPosition());
        wrappedPacketOutSpawnEntityLiving.setVelocity(this.getVelocity());
        wrappedPacketOutSpawnEntityLiving.setYaw(this.getYaw());
        wrappedPacketOutSpawnEntityLiving.setPitch(this.getPitch());
        wrappedPacketOutSpawnEntityLiving.setHeadPitch(this.getHeadPitch());
        return packetInstance;
    }
    
    static {
        byteBufAllocation = new byte[48];
    }
}
