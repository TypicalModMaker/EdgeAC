package xyz.edge.ac.packetevents.utils.vector;

import org.bukkit.Location;

public class Vector3d
{
    public static final Vector3d INVALID;
    public double x;
    public double y;
    public double z;
    
    public Vector3d() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }
    
    public Vector3d(final Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }
    
    public Vector3d(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3d(final double[] array) {
        if (array.length <= 0) {
            this.x = 0.0;
            this.y = 0.0;
            this.z = 0.0;
            return;
        }
        this.x = array[0];
        if (array.length > 1) {
            this.y = array[1];
            if (array.length > 2) {
                this.z = array[2];
            }
            else {
                this.z = 0.0;
            }
            return;
        }
        this.y = 0.0;
        this.z = 0.0;
    }
    
    public double getX() {
        return this.x;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public void setZ(final double z) {
        this.z = z;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Vector3d) {
            final Vector3d vec = (Vector3d)obj;
            return this.x == vec.x && this.y == vec.y && this.z == vec.z;
        }
        if (obj instanceof Vector3f) {
            final Vector3f vec2 = (Vector3f)obj;
            return this.x == vec2.x && this.y == vec2.y && this.z == vec2.z;
        }
        if (obj instanceof Vector3i) {
            final Vector3i vec3 = (Vector3i)obj;
            return this.x == vec3.x && this.y == vec3.y && this.z == vec3.z;
        }
        return false;
    }
    
    public Vector3d clone() {
        return new Vector3d(this.getX(), this.getY(), this.getZ());
    }
    
    public Vector3d add(final Vector3d target) {
        final Vector3d vector3d;
        final Vector3d result = vector3d = new Vector3d(this.x, this.y, this.z);
        vector3d.x += target.x;
        final Vector3d vector3d2 = result;
        vector3d2.y += target.y;
        final Vector3d vector3d3 = result;
        vector3d3.z += target.z;
        return result;
    }
    
    public Vector3d subtract(final Vector3d target) {
        final Vector3d vector3d;
        final Vector3d result = vector3d = new Vector3d(this.x, this.y, this.z);
        vector3d.x -= target.x;
        final Vector3d vector3d2 = result;
        vector3d2.y -= target.y;
        final Vector3d vector3d3 = result;
        vector3d3.z -= target.z;
        return result;
    }
    
    public double distance(final Vector3d target) {
        return Math.sqrt(this.distanceSquared(target));
    }
    
    public double distanceSquared(final Vector3d target) {
        final double distX = (this.x - target.x) * (this.x - target.x);
        final double distY = (this.y - target.y) * (this.y - target.y);
        final double distZ = (this.z - target.z) * (this.z - target.z);
        return distX + distY + distZ;
    }
    
    @Override
    public String toString() {
        return "X: " + this.x + ", Y: " + this.y + ", Z: " + this.z;
    }
    
    static {
        INVALID = new Vector3d(-1.0, -1.0, -1.0);
    }
}
