package xyz.edge.ac.packetevents.packetwrappers.play.out.spawnentity;

import xyz.edge.ac.packetevents.utils.math.MathUtils;
import java.util.Optional;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.utils.vector.Vector3d;
import java.util.UUID;
import xyz.edge.ac.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

public class WrappedPacketOutSpawnEntity extends WrappedPacketEntityAbstraction
{
    private static boolean v_1_9;
    private static boolean v_1_17;
    private static final float rotationFactor = 0.7111111f;
    private UUID uuid;
    private Vector3d position;
    private Vector3d velocity;
    private float pitch;
    private float yaw;
    
    public WrappedPacketOutSpawnEntity(final NMSPacket packet) {
        super(packet);
    }
    
    @Override
    protected void load() {
        WrappedPacketOutSpawnEntity.v_1_9 = WrappedPacketOutSpawnEntity.version.isNewerThanOrEquals(ServerVersion.v_1_9);
        WrappedPacketOutSpawnEntity.v_1_17 = WrappedPacketOutSpawnEntity.version.isNewerThanOrEquals(ServerVersion.v_1_17);
    }
    
    public Optional<UUID> getUUID() {
        if (!WrappedPacketOutSpawnEntity.v_1_9) {
            return Optional.empty();
        }
        if (this.packet != null) {
            return Optional.of(this.readObject(0, (Class<? extends UUID>)UUID.class));
        }
        return Optional.of(this.uuid);
    }
    
    public void setUUID(final UUID uuid) {
        if (WrappedPacketOutSpawnEntity.v_1_9) {
            if (this.packet != null) {
                this.writeObject(0, uuid);
            }
            else {
                this.uuid = uuid;
            }
        }
    }
    
    public Vector3d getPosition() {
        if (this.packet != null) {
            double x;
            double y;
            double z;
            if (WrappedPacketOutSpawnEntity.v_1_9) {
                x = this.readDouble(WrappedPacketOutSpawnEntity.v_1_17 ? 1 : 0);
                y = this.readDouble(WrappedPacketOutSpawnEntity.v_1_17 ? 2 : 1);
                z = this.readDouble(WrappedPacketOutSpawnEntity.v_1_17 ? 3 : 2);
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
            if (WrappedPacketOutSpawnEntity.v_1_9) {
                this.writeDouble(WrappedPacketOutSpawnEntity.v_1_17 ? 1 : 0, position.x);
                this.writeDouble(WrappedPacketOutSpawnEntity.v_1_17 ? 2 : 1, position.y);
                this.writeDouble(WrappedPacketOutSpawnEntity.v_1_17 ? 3 : 2, position.z);
            }
            else {
                this.writeInt(1, MathUtils.floor(position.x * 32.0));
                this.writeInt(2, MathUtils.floor(position.y * 32.0));
                this.writeInt(3, MathUtils.floor(position.z * 32.0));
            }
        }
        else {
            this.position = position;
        }
    }
    
    public Vector3d getVelocity() {
        if (this.packet != null) {
            double velX;
            double velY;
            double velZ;
            if (WrappedPacketOutSpawnEntity.v_1_9) {
                velX = this.readInt(1) / 8000.0;
                velY = this.readInt(2) / 8000.0;
                velZ = this.readInt(3) / 8000.0;
            }
            else {
                velX = this.readInt(4) / 8000.0;
                velY = this.readInt(5) / 8000.0;
                velZ = this.readInt(6) / 8000.0;
            }
            return new Vector3d(velX, velY, velZ);
        }
        return this.velocity;
    }
    
    public void setVelocity(final Vector3d velocity) {
        if (this.packet != null) {
            final int velX = (int)(velocity.x * 8000.0);
            final int velY = (int)(velocity.y * 8000.0);
            final int velZ = (int)(velocity.z * 8000.0);
            if (WrappedPacketOutSpawnEntity.v_1_9) {
                this.writeInt(1, velX);
                this.writeInt(2, velY);
                this.writeInt(3, velZ);
            }
            else {
                this.writeInt(4, velX);
                this.writeInt(5, velY);
                this.writeInt(6, velZ);
            }
        }
        else {
            this.velocity = velocity;
        }
    }
    
    public float getPitch() {
        if (this.packet != null) {
            int factoredPitch;
            if (!WrappedPacketOutSpawnEntity.v_1_9) {
                factoredPitch = this.readInt(7);
            }
            else {
                factoredPitch = this.readInt(4);
            }
            return factoredPitch / 0.7111111f;
        }
        return this.pitch;
    }
    
    public void setPitch(final float pitch) {
        if (this.packet != null) {
            if (!WrappedPacketOutSpawnEntity.v_1_9) {
                this.writeInt(7, MathUtils.floor(pitch * 0.7111111f));
            }
            else {
                this.writeInt(4, MathUtils.floor(pitch * 0.7111111f));
            }
        }
        else {
            this.pitch = pitch;
        }
    }
    
    public float getYaw() {
        if (this.packet != null) {
            int factoredYaw;
            if (!WrappedPacketOutSpawnEntity.v_1_9) {
                factoredYaw = this.readInt(8);
            }
            else {
                factoredYaw = this.readInt(5);
            }
            return factoredYaw / 0.7111111f;
        }
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        if (this.packet != null) {
            if (!WrappedPacketOutSpawnEntity.v_1_9) {
                this.writeInt(8, MathUtils.floor(yaw * 0.7111111f));
            }
            else {
                this.writeInt(5, MathUtils.floor(yaw * 0.7111111f));
            }
        }
        else {
            this.yaw = yaw;
        }
    }
}
