package xyz.edge.ac.util.mc.reach;

import xyz.edge.ac.util.mc.VanillaMath;
import org.bukkit.util.Vector;

public class ReachUtils
{
    public static Vector calculateIntercept(final SimpleCollisionBox self, final Vector origin, final Vector end) {
        Vector minX = getIntermediateWithXValue(origin, end, self.minX);
        Vector maxX = getIntermediateWithXValue(origin, end, self.maxX);
        Vector minY = getIntermediateWithYValue(origin, end, self.minY);
        Vector maxY = getIntermediateWithYValue(origin, end, self.maxY);
        Vector minZ = getIntermediateWithZValue(origin, end, self.minZ);
        Vector maxZ = getIntermediateWithZValue(origin, end, self.maxZ);
        if (!isVecInYZ(self, minX)) {
            minX = null;
        }
        if (!isVecInYZ(self, maxX)) {
            maxX = null;
        }
        if (!isVecInXZ(self, minY)) {
            minY = null;
        }
        if (!isVecInXZ(self, maxY)) {
            maxY = null;
        }
        if (!isVecInXY(self, minZ)) {
            minZ = null;
        }
        if (!isVecInXY(self, maxZ)) {
            maxZ = null;
        }
        Vector best = null;
        if (minX != null) {
            best = minX;
        }
        if (maxX != null && (best == null || origin.distanceSquared(maxX) < origin.distanceSquared(best))) {
            best = maxX;
        }
        if (minY != null && (best == null || origin.distanceSquared(minY) < origin.distanceSquared(best))) {
            best = minY;
        }
        if (maxY != null && (best == null || origin.distanceSquared(maxY) < origin.distanceSquared(best))) {
            best = maxY;
        }
        if (minZ != null && (best == null || origin.distanceSquared(minZ) < origin.distanceSquared(best))) {
            best = minZ;
        }
        if (maxZ != null && (best == null || origin.distanceSquared(maxZ) < origin.distanceSquared(best))) {
            best = maxZ;
        }
        return best;
    }
    
    public static Vector getIntermediateWithXValue(final Vector self, final Vector other, final double x) {
        final double d0 = other.getX() - self.getX();
        final double d2 = other.getY() - self.getY();
        final double d3 = other.getZ() - self.getZ();
        if (d0 * d0 < 1.0000000116860974E-7) {
            return null;
        }
        final double d4 = (x - self.getX()) / d0;
        return (d4 >= 0.0 && d4 <= 1.0) ? new Vector(self.getX() + d0 * d4, self.getY() + d2 * d4, self.getZ() + d3 * d4) : null;
    }
    
    public static Vector getIntermediateWithYValue(final Vector self, final Vector other, final double y) {
        final double d0 = other.getX() - self.getX();
        final double d2 = other.getY() - self.getY();
        final double d3 = other.getZ() - self.getZ();
        if (d2 * d2 < 1.0000000116860974E-7) {
            return null;
        }
        final double d4 = (y - self.getY()) / d2;
        return (d4 >= 0.0 && d4 <= 1.0) ? new Vector(self.getX() + d0 * d4, self.getY() + d2 * d4, self.getZ() + d3 * d4) : null;
    }
    
    public static Vector getIntermediateWithZValue(final Vector self, final Vector other, final double z) {
        final double d0 = other.getX() - self.getX();
        final double d2 = other.getY() - self.getY();
        final double d3 = other.getZ() - self.getZ();
        if (d3 * d3 < 1.0000000116860974E-7) {
            return null;
        }
        final double d4 = (z - self.getZ()) / d3;
        return (d4 >= 0.0 && d4 <= 1.0) ? new Vector(self.getX() + d0 * d4, self.getY() + d2 * d4, self.getZ() + d3 * d4) : null;
    }
    
    private static boolean isVecInYZ(final SimpleCollisionBox self, final Vector vec) {
        return vec != null && vec.getY() >= self.minY && vec.getY() <= self.maxY && vec.getZ() >= self.minZ && vec.getZ() <= self.maxZ;
    }
    
    private static boolean isVecInXZ(final SimpleCollisionBox self, final Vector vec) {
        return vec != null && vec.getX() >= self.minX && vec.getX() <= self.maxX && vec.getZ() >= self.minZ && vec.getZ() <= self.maxZ;
    }
    
    private static boolean isVecInXY(final SimpleCollisionBox self, final Vector vec) {
        return vec != null && vec.getX() >= self.minX && vec.getX() <= self.maxX && vec.getY() >= self.minY && vec.getY() <= self.maxY;
    }
    
    public static Vector getLook(final float xRot, final float yRot) {
        final float f = VanillaMath.cos(-xRot * 0.017453292f - 3.1415927f);
        final float f2 = VanillaMath.sin(-xRot * 0.017453292f - 3.1415927f);
        final float f3 = -VanillaMath.cos(-yRot * 0.017453292f);
        final float f4 = VanillaMath.sin(-yRot * 0.017453292f);
        return new Vector(f2 * f3, f4, f * f3);
    }
    
    public static boolean isVecInside(final SimpleCollisionBox self, final Vector vec) {
        return vec.getX() > self.minX && vec.getX() < self.maxX && vec.getY() > self.minY && vec.getY() < self.maxY && vec.getZ() > self.minZ && vec.getZ() < self.maxZ;
    }
}
