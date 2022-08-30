package xyz.edge.ac.util.mc.reach;

import java.util.List;
import xyz.edge.ac.util.type.BoundingBox;
import org.bukkit.Location;
import io.github.retrooper.packetevents.utils.vector.Vector3d;
import org.bukkit.util.Vector;
import xyz.edge.ac.util.mc.CollisionBox;

public class SimpleCollisionBox implements CollisionBox
{
    public static final double COLLISION_EPSILON = 1.0E-7;
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;
    boolean isFullBlock;
    
    public SimpleCollisionBox() {
        this(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, false);
    }
    
    public SimpleCollisionBox(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ, final boolean fullBlock) {
        this.isFullBlock = false;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
        this.isFullBlock = fullBlock;
    }
    
    public SimpleCollisionBox(final Vector min, final Vector max) {
        this(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
    }
    
    public SimpleCollisionBox(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ) {
        this.isFullBlock = false;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
        if (minX == 0.0 && minY == 0.0 && minZ == 0.0 && maxX == 1.0 && maxY == 1.0 && maxZ == 1.0) {
            this.isFullBlock = true;
        }
    }
    
    public SimpleCollisionBox(final Vector3d min, final Vector3d max) {
        this(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
    }
    
    public SimpleCollisionBox(final Location loc, final double width, final double height) {
        this(loc.toVector(), width, height);
    }
    
    public SimpleCollisionBox(final Vector vec, final double width, final double height) {
        this(vec.getX(), vec.getY(), vec.getZ(), vec.getX(), vec.getY(), vec.getZ());
        this.expand(width / 2.0, 0.0, width / 2.0);
        this.maxY += height;
    }
    
    public SimpleCollisionBox expand(final double x, final double y, final double z) {
        this.minX -= x;
        this.minY -= y;
        this.minZ -= z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
        return this.sort();
    }
    
    public SimpleCollisionBox(final BoundingBox box) {
        this(box.getMinX(), box.getMinY(), box.getMinZ(), box.getMaxX(), box.getMaxY(), box.getMaxZ());
    }
    
    public SimpleCollisionBox sort() {
        final double minX = Math.min(this.minX, this.maxX);
        final double minY = Math.min(this.minY, this.maxY);
        final double minZ = Math.min(this.minZ, this.maxZ);
        final double maxX = Math.max(this.minX, this.maxX);
        final double maxY = Math.max(this.minY, this.maxY);
        final double maxZ = Math.max(this.minZ, this.maxZ);
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        return this;
    }
    
    public SimpleCollisionBox expandMin(final double x, final double y, final double z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;
        return this;
    }
    
    public SimpleCollisionBox expandMax(final double x, final double y, final double z) {
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
        return this;
    }
    
    public SimpleCollisionBox expand(final double value) {
        this.minX -= value;
        this.minY -= value;
        this.minZ -= value;
        this.maxX += value;
        this.maxY += value;
        this.maxZ += value;
        return this;
    }
    
    public Vector[] corners() {
        final Vector[] vectors = { new Vector(this.minX, this.minY, this.minZ), new Vector(this.minX, this.minY, this.maxZ), new Vector(this.maxX, this.minY, this.minZ), new Vector(this.maxX, this.minY, this.maxZ), new Vector(this.minX, this.maxY, this.minZ), new Vector(this.minX, this.maxY, this.maxZ), new Vector(this.maxX, this.maxY, this.minZ), new Vector(this.maxX, this.maxY, this.maxZ) };
        return vectors;
    }
    
    public SimpleCollisionBox expandToAbsoluteCoordinates(final double x, final double y, final double z) {
        return this.expandToCoordinate(x - (this.minX + this.maxX) / 2.0, y - (this.minY + this.maxY) / 2.0, z - (this.minZ + this.maxZ) / 2.0);
    }
    
    public SimpleCollisionBox expandToCoordinate(final double x, final double y, final double z) {
        if (x < 0.0) {
            this.minX += x;
        }
        else {
            this.maxX += x;
        }
        if (y < 0.0) {
            this.minY += y;
        }
        else {
            this.maxY += y;
        }
        if (z < 0.0) {
            this.minZ += z;
        }
        else {
            this.maxZ += z;
        }
        return this;
    }
    
    @Override
    public boolean isCollided(final SimpleCollisionBox other) {
        return other.maxX >= this.minX && other.minX <= this.maxX && other.maxY >= this.minY && other.minY <= this.maxY && other.maxZ >= this.minZ && other.minZ <= this.maxZ;
    }
    
    @Override
    public boolean isIntersected(final SimpleCollisionBox other) {
        return other.maxX > this.minX && other.minX < this.maxX && other.maxY > this.minY && other.minY < this.maxY && other.maxZ > this.minZ && other.minZ < this.maxZ;
    }
    
    public boolean collidesVertically(final SimpleCollisionBox other) {
        return other.maxX > this.minX && other.minX < this.maxX && other.maxY >= this.minY && other.minY <= this.maxY && other.maxZ > this.minZ && other.minZ < this.maxZ;
    }
    
    @Override
    public SimpleCollisionBox copy() {
        return new SimpleCollisionBox(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ, this.isFullBlock);
    }
    
    @Override
    public SimpleCollisionBox offset(final double x, final double y, final double z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
        return this;
    }
    
    @Override
    public void downCast(final List<SimpleCollisionBox> list) {
        list.add(this);
    }
    
    @Override
    public boolean isNull() {
        return false;
    }
    
    @Override
    public boolean isFullBlock() {
        return this.isFullBlock;
    }
    
    public double collideX(final SimpleCollisionBox other, final double offsetX) {
        if (offsetX == 0.0 || other.minY - this.maxY >= -1.0E-7 || other.maxY - this.minY <= 1.0E-7 || other.minZ - this.maxZ >= -1.0E-7 || other.maxZ - this.minZ <= 1.0E-7) {
            return offsetX;
        }
        if (offsetX >= 0.0) {
            final double max_move = this.minX - other.maxX;
            if (max_move < -1.0E-7) {
                return offsetX;
            }
            return Math.min(max_move, offsetX);
        }
        else {
            final double max_move = this.maxX - other.minX;
            if (max_move > 1.0E-7) {
                return offsetX;
            }
            return Math.max(max_move, offsetX);
        }
    }
    
    public double collideY(final SimpleCollisionBox other, final double offsetY) {
        if (offsetY == 0.0 || other.minX - this.maxX >= -1.0E-7 || other.maxX - this.minX <= 1.0E-7 || other.minZ - this.maxZ >= -1.0E-7 || other.maxZ - this.minZ <= 1.0E-7) {
            return offsetY;
        }
        if (offsetY >= 0.0) {
            final double max_move = this.minY - other.maxY;
            if (max_move < -1.0E-7) {
                return offsetY;
            }
            return Math.min(max_move, offsetY);
        }
        else {
            final double max_move = this.maxY - other.minY;
            if (max_move > 1.0E-7) {
                return offsetY;
            }
            return Math.max(max_move, offsetY);
        }
    }
    
    public double collideZ(final SimpleCollisionBox other, final double offsetZ) {
        if (offsetZ == 0.0 || other.minX - this.maxX >= -1.0E-7 || other.maxX - this.minX <= 1.0E-7 || other.minY - this.maxY >= -1.0E-7 || other.maxY - this.minY <= 1.0E-7) {
            return offsetZ;
        }
        if (offsetZ >= 0.0) {
            final double max_move = this.minZ - other.maxZ;
            if (max_move < -1.0E-7) {
                return offsetZ;
            }
            return Math.min(max_move, offsetZ);
        }
        else {
            final double max_move = this.maxZ - other.minZ;
            if (max_move > 1.0E-7) {
                return offsetZ;
            }
            return Math.max(max_move, offsetZ);
        }
    }
    
    public double distance(final SimpleCollisionBox box) {
        final double xwidth = (this.maxX - this.minX) / 2.0;
        final double zwidth = (this.maxZ - this.minZ) / 2.0;
        final double bxwidth = (box.maxX - box.minX) / 2.0;
        final double bzwidth = (box.maxZ - box.minZ) / 2.0;
        final double hxz = Math.hypot(this.minX - box.minX, this.minZ - box.minZ);
        return hxz - (xwidth + zwidth + bxwidth + bzwidth) / 4.0;
    }
    
    public Vector intersectsRay(final Ray ray, final float minDist, final float maxDist) {
        final Vector invDir = new Vector(1.0 / ray.getDirection().getX(), 1.0 / ray.getDirection().getY(), 1.0 / ray.getDirection().getZ());
        final boolean signDirX = invDir.getX() < 0.0;
        final boolean signDirY = invDir.getY() < 0.0;
        final boolean signDirZ = invDir.getZ() < 0.0;
        Vector bbox = signDirX ? this.max() : this.min();
        double tmin = (bbox.getX() - ray.getOrigin().getX()) * invDir.getX();
        bbox = (signDirX ? this.min() : this.max());
        double tmax = (bbox.getX() - ray.getOrigin().getX()) * invDir.getX();
        bbox = (signDirY ? this.max() : this.min());
        final double tymin = (bbox.getY() - ray.getOrigin().getY()) * invDir.getY();
        bbox = (signDirY ? this.min() : this.max());
        final double tymax = (bbox.getY() - ray.getOrigin().getY()) * invDir.getY();
        if (tmin > tymax || tymin > tmax) {
            return null;
        }
        if (tymin > tmin) {
            tmin = tymin;
        }
        if (tymax < tmax) {
            tmax = tymax;
        }
        bbox = (signDirZ ? this.max() : this.min());
        final double tzmin = (bbox.getZ() - ray.getOrigin().getZ()) * invDir.getZ();
        bbox = (signDirZ ? this.min() : this.max());
        final double tzmax = (bbox.getZ() - ray.getOrigin().getZ()) * invDir.getZ();
        if (tmin > tzmax || tzmin > tmax) {
            return null;
        }
        if (tzmin > tmin) {
            tmin = tzmin;
        }
        if (tzmax < tmax) {
            tmax = tzmax;
        }
        if (tmin < maxDist && tmax > minDist) {
            return ray.getPointAtDistance(tmin);
        }
        return null;
    }
    
    public Vector max() {
        return new Vector(this.maxX, this.maxY, this.maxZ);
    }
    
    public Vector min() {
        return new Vector(this.minX, this.minY, this.minZ);
    }
    
    @Override
    public String toString() {
        return "SimpleCollisionBox{minX=" + this.minX + ", minY=" + this.minY + ", minZ=" + this.minZ + ", maxX=" + this.maxX + ", maxY=" + this.maxY + ", maxZ=" + this.maxZ + ", isFullBlock=" + this.isFullBlock + '}';
    }
}
