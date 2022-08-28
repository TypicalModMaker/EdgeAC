package xyz.edge.ac.packetevents.utils.vector;

public class Vector3f
{
    public static final Vector3f INVALID;
    public float x;
    public float y;
    public float z;
    
    public Vector3f() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }
    
    public Vector3f(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3f(final float[] array) {
        if (array.length <= 0) {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 0.0f;
            return;
        }
        this.x = array[0];
        if (array.length > 1) {
            this.y = array[1];
            if (array.length > 2) {
                this.z = array[2];
            }
            else {
                this.z = 0.0f;
            }
            return;
        }
        this.y = 0.0f;
        this.z = 0.0f;
    }
    
    public float getX() {
        return this.x;
    }
    
    public void setX(final float x) {
        this.x = x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public void setY(final float y) {
        this.y = y;
    }
    
    public float getZ() {
        return this.z;
    }
    
    public void setZ(final float z) {
        this.z = z;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Vector3f) {
            final Vector3f vec = (Vector3f)obj;
            return this.x == vec.x && this.y == vec.y && this.z == vec.z;
        }
        if (obj instanceof Vector3d) {
            final Vector3d vec2 = (Vector3d)obj;
            return this.x == vec2.x && this.y == vec2.y && this.z == vec2.z;
        }
        if (obj instanceof Vector3i) {
            final Vector3i vec3 = (Vector3i)obj;
            return this.x == (double)vec3.x && this.y == (double)vec3.y && this.z == (double)vec3.z;
        }
        return false;
    }
    
    public Vector3f clone() {
        return new Vector3f(this.getX(), this.getY(), this.getZ());
    }
    
    @Override
    public String toString() {
        return "X: " + this.x + ", Y: " + this.y + ", Z: " + this.z;
    }
    
    static {
        INVALID = new Vector3f(-1.0f, -1.0f, -1.0f);
    }
}
