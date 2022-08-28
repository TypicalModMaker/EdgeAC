package xyz.edge.ac.packetevents.packetwrappers.play.in.vehiclemove;

import xyz.edge.ac.packetevents.utils.vector.Vector3d;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInVehicleMove extends WrappedPacket
{
    public WrappedPacketInVehicleMove(final NMSPacket packet) {
        super(packet);
    }
    
    public Vector3d getPosition() {
        return new Vector3d(this.readDouble(0), this.readDouble(1), this.readDouble(2));
    }
    
    public void setPosition(final Vector3d position) {
        this.writeDouble(0, position.x);
        this.writeDouble(1, position.y);
        this.writeDouble(2, position.z);
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
}
