package xyz.edge.ac.util.mc.reach;

import xyz.edge.ac.util.type.Pair;
import xyz.edge.ac.util.SystemLogsUtil;
import java.util.Arrays;
import xyz.edge.ac.user.User;
import org.bukkit.util.Vector;

public class Ray implements Cloneable
{
    private Vector origin;
    private Vector direction;
    
    public Ray(final Vector origin, final Vector direction) {
        this.origin = origin;
        this.direction = direction;
    }
    
    public Ray(final User player, final double x, final double y, final double z, final float xRot, final float yRot) {
        this.origin = new Vector(x, y, z);
        this.direction = getDirection(player, xRot, yRot);
    }
    
    public static Vector getDirection(final User player, final float xRot, final float yRot) {
        final Vector vector = new Vector();
        final float rotX = (float)Math.toRadians(xRot);
        final float rotY = (float)Math.toRadians(yRot);
        vector.setY(-Math.sin(rotY));
        final double xz = Math.cos(rotY);
        vector.setX(-xz * Math.sin(rotX));
        vector.setZ(xz * Math.cos(rotX));
        return vector;
    }
    
    public Ray clone() {
        try {
            final Ray clone = (Ray)super.clone();
            clone.origin = this.origin.clone();
            clone.direction = this.direction.clone();
            return clone;
        }
        catch (final CloneNotSupportedException e) {
            SystemLogsUtil.createNewLog(Arrays.toString(e.getStackTrace()), "Ray (Clone)", e.getMessage());
            return null;
        }
    }
    
    @Override
    public String toString() {
        return "origin: " + this.origin + " direction: " + this.direction;
    }
    
    public Vector getPointAtDistance(final double distance) {
        final Vector dir = new Vector(this.direction.getX(), this.direction.getY(), this.direction.getZ());
        final Vector orig = new Vector(this.origin.getX(), this.origin.getY(), this.origin.getZ());
        return orig.add(dir.multiply(distance));
    }
    
    public Pair<Vector, Vector> closestPointsBetweenLines(final Ray other) {
        final Vector n1 = this.direction.clone().crossProduct(other.direction.clone().crossProduct(this.direction));
        final Vector n2 = other.direction.clone().crossProduct(this.direction.clone().crossProduct(other.direction));
        final Vector c1 = this.origin.clone().add(this.direction.clone().multiply(other.origin.clone().subtract(this.origin).dot(n2) / this.direction.dot(n2)));
        final Vector c2 = other.origin.clone().add(other.direction.clone().multiply(this.origin.clone().subtract(other.origin).dot(n1) / other.direction.dot(n1)));
        return new Pair<Vector, Vector>(c1, c2);
    }
    
    public Vector getOrigin() {
        return this.origin;
    }
    
    public Vector getDirection() {
        return this.direction;
    }
}
