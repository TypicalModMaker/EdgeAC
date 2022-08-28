package xyz.edge.ac.packetevents.utils.vector;

import org.bukkit.Location;

public class Vector3i
{
    public static final Vector3i INVALID;
    public int x;
    public int y;
    public int z;
    
    public Vector3i() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }
    
    public Vector3i(final Location location) {
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }
    
    public Vector3i(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3i(final int[] array) {
        if (array.length <= 0) {
            this.x = 0;
            this.y = 0;
            this.z = 0;
            return;
        }
        this.x = array[0];
        if (array.length > 1) {
            this.y = array[1];
            if (array.length > 2) {
                this.z = array[2];
            }
            else {
                this.z = 0;
            }
            return;
        }
        this.y = 0;
        this.z = 0;
    }
    
    public int getX() {
        return this.x;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public int getZ() {
        return this.z;
    }
    
    public void setZ(final int z) {
        this.z = z;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Vector3i) {
            final Vector3i vec = (Vector3i)obj;
            return this.x == vec.x && this.y == vec.y && this.z == vec.z;
        }
        if (obj instanceof Vector3d) {
            final Vector3d vec2 = (Vector3d)obj;
            return this.x == vec2.x && this.y == vec2.y && this.z == vec2.z;
        }
        if (obj instanceof Vector3f) {
            final Vector3f vec3 = (Vector3f)obj;
            return this.x == vec3.x && this.y == vec3.y && this.z == vec3.z;
        }
        return false;
    }
    
    public Vector3i clone() {
        return new Vector3i(this.getX(), this.getY(), this.getZ());
    }
    
    @Override
    public String toString() {
        return "X: " + this.x + ", Y: " + this.y + ", Z: " + this.z;
    }
    
    static {
        INVALID = new Vector3i(-1, -1, -1);
    }
}
