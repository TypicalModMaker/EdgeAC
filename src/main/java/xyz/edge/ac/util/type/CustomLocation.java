package xyz.edge.ac.util.type;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.bukkit.World;

public final class CustomLocation
{
    private World world;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private boolean onGround;
    private long timeStamp;
    
    public CustomLocation(final World world, final double x, final double y, final double z, final float yaw, final float pitch, final boolean onGround) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.timeStamp = System.currentTimeMillis();
    }
    
    public Vector toVector() {
        return new Vector(this.x, this.y, this.z);
    }
    
    public CustomLocation offset(final double x, final double y, final double z, final float yaw, final float pitch) {
        return new CustomLocation(this.world, this.x + x, this.y + y, this.z + z, this.yaw + yaw, this.pitch + pitch, this.onGround);
    }
    
    public CustomLocation clone() {
        return new CustomLocation(this.world, this.x, this.y, this.z, this.yaw, this.pitch, this.onGround);
    }
    
    public static CustomLocation fromBukkit(final Location location) {
        return new CustomLocation(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), false);
    }
    
    public Location toBukkit() {
        return new Location(this.world, this.x, this.y, this.z);
    }
    
    public Vector getDirection() {
        final Vector vector = new Vector();
        final double rotX = this.getYaw();
        final double rotY = this.getPitch();
        vector.setY(-Math.sin(Math.toRadians(rotY)));
        final double xz = Math.cos(Math.toRadians(rotY));
        vector.setX(-xz * Math.sin(Math.toRadians(rotX)));
        vector.setZ(xz * Math.cos(Math.toRadians(rotX)));
        return vector;
    }
    
    public World getWorld() {
        return this.world;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    public long getTimeStamp() {
        return this.timeStamp;
    }
    
    public void setWorld(final World world) {
        this.world = world;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public void setZ(final double z) {
        this.z = z;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }
    
    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
    }
    
    public void setTimeStamp(final long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
