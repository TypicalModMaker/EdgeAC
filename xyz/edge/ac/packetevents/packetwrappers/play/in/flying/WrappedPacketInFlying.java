package xyz.edge.ac.packetevents.packetwrappers.play.in.flying;

import xyz.edge.ac.packetevents.utils.vector.Vector3d;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInFlying extends WrappedPacket
{
    public WrappedPacketInFlying(final NMSPacket packet) {
        super(packet);
    }
    
    @Deprecated
    public double getX() {
        return this.readDouble(0);
    }
    
    @Deprecated
    public void setX(final double x) {
        this.writeDouble(0, x);
    }
    
    @Deprecated
    public double getY() {
        return this.readDouble(1);
    }
    
    @Deprecated
    public void setY(final double y) {
        this.writeDouble(1, y);
    }
    
    @Deprecated
    public double getZ() {
        return this.readDouble(2);
    }
    
    @Deprecated
    public void setZ(final double z) {
        this.writeDouble(2, z);
    }
    
    @Deprecated
    public boolean isPosition() {
        return this.readBoolean(1);
    }
    
    @Deprecated
    public void setIsPosition(final boolean isPosition) {
        this.writeBoolean(1, isPosition);
    }
    
    @Deprecated
    public boolean isLook() {
        return this.readBoolean(2);
    }
    
    @Deprecated
    public void setIsLook(final boolean isLook) {
        this.writeBoolean(2, isLook);
    }
    
    @Deprecated
    public boolean hasPositionChanged() {
        return this.readBoolean(1);
    }
    
    @Deprecated
    public void setPositionChanged(final boolean positionChanged) {
        this.writeBoolean(1, positionChanged);
    }
    
    @Deprecated
    public boolean hasRotationChanged() {
        return this.readBoolean(2);
    }
    
    @Deprecated
    public void setRotationChanged(final boolean rotationChanged) {
        this.writeBoolean(2, rotationChanged);
    }
    
    public Vector3d getPosition() {
        return new Vector3d(this.readDouble(0), this.readDouble(1), this.readDouble(2));
    }
    
    public void setPosition(final Vector3d position) {
        this.writeDouble(0, position.x);
        this.writeDouble(1, position.y);
        this.writeDouble(2, position.z);
    }
    
    public float getYaw() {
        return this.readFloat(0);
    }
    
    public void setYaw(final float yaw) {
        this.writeFloat(0, yaw);
    }
    
    public float getPitch() {
        return this.readFloat(1);
    }
    
    public void setPitch(final float pitch) {
        this.writeFloat(1, pitch);
    }
    
    public boolean isOnGround() {
        return this.readBoolean(0);
    }
    
    public void setOnGround(final boolean onGround) {
        this.writeBoolean(0, onGround);
    }
    
    public boolean isMoving() {
        return this.readBoolean(1);
    }
    
    public void setMoving(final boolean moving) {
        this.writeBoolean(1, moving);
    }
    
    public boolean isRotating() {
        return this.readBoolean(2);
    }
    
    public void setRotating(final boolean rotating) {
        this.writeBoolean(2, rotating);
    }
}
