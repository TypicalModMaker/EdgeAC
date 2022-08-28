package xyz.edge.ac.packetevents.packetwrappers.play.out.entityvelocity;

import xyz.edge.ac.packetevents.utils.vector.Vector3d;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import org.bukkit.entity.Entity;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

public final class WrappedPacketOutEntityVelocity extends WrappedPacketEntityAbstraction implements SendableWrapper
{
    private static Constructor<?> velocityConstructor;
    private static boolean isVec3dPresent;
    private double velocityX;
    private double velocityY;
    private double velocityZ;
    
    public WrappedPacketOutEntityVelocity(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutEntityVelocity(final Entity entity, final double velocityX, final double velocityY, final double velocityZ) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
    }
    
    public WrappedPacketOutEntityVelocity(final int entityID, final double velX, final double velY, final double velZ) {
        this.entityID = entityID;
        this.velocityX = velX;
        this.velocityY = velY;
        this.velocityZ = velZ;
    }
    
    @Override
    protected void load() {
        final Class<?> velocityClass = PacketTypeClasses.Play.Server.ENTITY_VELOCITY;
        try {
            WrappedPacketOutEntityVelocity.velocityConstructor = velocityClass.getConstructor(Integer.TYPE, Double.TYPE, Double.TYPE, Double.TYPE);
        }
        catch (final NoSuchMethodException e) {
            try {
                WrappedPacketOutEntityVelocity.velocityConstructor = velocityClass.getConstructor(Integer.TYPE, NMSUtils.vec3DClass);
                WrappedPacketOutEntityVelocity.isVec3dPresent = true;
            }
            catch (final NoSuchMethodException e2) {
                e2.printStackTrace();
            }
        }
    }
    
    public Vector3d getVelocity() {
        if (this.packet != null) {
            final double velX = this.readInt(1) / 8000.0;
            final double velY = this.readInt(2) / 8000.0;
            final double velZ = this.readInt(3) / 8000.0;
            return new Vector3d(velX, velY, velZ);
        }
        return new Vector3d(this.velocityX, this.velocityY, this.velocityZ);
    }
    
    public void setVelocity(final Vector3d velocity) {
        if (this.packet != null) {
            this.writeInt(1, (int)(velocity.x * 8000.0));
            this.writeInt(2, (int)(velocity.y * 8000.0));
            this.writeInt(3, (int)(velocity.z * 8000.0));
        }
        else {
            this.velocityX = velocity.x;
            this.velocityY = velocity.y;
            this.velocityZ = velocity.z;
        }
    }
    
    @Deprecated
    public double getVelocityX() {
        if (this.packet != null) {
            return this.readInt(1) / 8000.0;
        }
        return this.velocityX;
    }
    
    @Deprecated
    public void setVelocityX(final double x) {
        if (this.packet != null) {
            this.writeInt(1, (int)(x * 8000.0));
        }
        else {
            this.velocityX = x;
        }
    }
    
    @Deprecated
    public double getVelocityY() {
        if (this.packet != null) {
            return this.readInt(2) / 8000.0;
        }
        return this.velocityY;
    }
    
    @Deprecated
    public void setVelocityY(final double y) {
        if (this.packet != null) {
            this.writeInt(2, (int)(y * 8000.0));
        }
    }
    
    @Deprecated
    public double getVelocityZ() {
        if (this.packet != null) {
            return this.readInt(3) / 8000.0;
        }
        return this.velocityZ;
    }
    
    @Deprecated
    public void setVelocityZ(final double z) {
        if (this.packet != null) {
            this.writeInt(3, (int)(z * 8000.0));
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        if (!WrappedPacketOutEntityVelocity.isVec3dPresent) {
            return WrappedPacketOutEntityVelocity.velocityConstructor.newInstance(this.getEntityId(), this.getVelocityX(), this.getVelocityY(), this.getVelocityZ());
        }
        return WrappedPacketOutEntityVelocity.velocityConstructor.newInstance(this.getEntityId(), NMSUtils.generateVec3D(this.getVelocityX(), this.getVelocityY(), this.getVelocityZ()));
    }
}
