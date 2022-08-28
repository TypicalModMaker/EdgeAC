package xyz.edge.ac.packetevents.utils.boundingbox;

import java.util.LinkedHashMap;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import org.bukkit.block.BlockFace;
import org.bukkit.util.NumberConversions;
import org.bukkit.block.Block;
import java.util.Objects;
import org.bukkit.Location;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.bukkit.util.Vector;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

@SerializableAs("BoundingBox")
public class BoundingBox implements Cloneable, ConfigurationSerializable
{
    private double minX;
    private double minY;
    private double minZ;
    private double maxX;
    private double maxY;
    private double maxZ;
    
    @NotNull
    public static BoundingBox of(@NotNull final Vector corner1, @NotNull final Vector corner2) {
        Validate.notNull(corner1, "Corner1 is null!");
        Validate.notNull(corner2, "Corner2 is null!");
        return new BoundingBox(corner1.getX(), corner1.getY(), corner1.getZ(), corner2.getX(), corner2.getY(), corner2.getZ());
    }
    
    @NotNull
    public static BoundingBox of(@NotNull final Location corner1, @NotNull final Location corner2) {
        Validate.notNull(corner1, "Corner1 is null!");
        Validate.notNull(corner2, "Corner2 is null!");
        Validate.isTrue(Objects.equals(corner1.getWorld(), corner2.getWorld()), "Locations from different worlds!");
        return new BoundingBox(corner1.getX(), corner1.getY(), corner1.getZ(), corner2.getX(), corner2.getY(), corner2.getZ());
    }
    
    @NotNull
    public static BoundingBox of(@NotNull final Block corner1, @NotNull final Block corner2) {
        Validate.notNull(corner1, "Corner1 is null!");
        Validate.notNull(corner2, "Corner2 is null!");
        Validate.isTrue(Objects.equals(corner1.getWorld(), corner2.getWorld()), "Blocks from different worlds!");
        final int x1 = corner1.getX();
        final int y1 = corner1.getY();
        final int z1 = corner1.getZ();
        final int x2 = corner2.getX();
        final int y2 = corner2.getY();
        final int z2 = corner2.getZ();
        final int minX = Math.min(x1, x2);
        final int minY = Math.min(y1, y2);
        final int minZ = Math.min(z1, z2);
        final int maxX = Math.max(x1, x2) + 1;
        final int maxY = Math.max(y1, y2) + 1;
        final int maxZ = Math.max(z1, z2) + 1;
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }
    
    @NotNull
    public static BoundingBox of(@NotNull final Block block) {
        Validate.notNull(block, "Block is null!");
        return new BoundingBox(block.getX(), block.getY(), block.getZ(), block.getX() + 1, block.getY() + 1, block.getZ() + 1);
    }
    
    @NotNull
    public static BoundingBox of(@NotNull final Vector center, final double x, final double y, final double z) {
        Validate.notNull(center, "Center is null!");
        return new BoundingBox(center.getX() - x, center.getY() - y, center.getZ() - z, center.getX() + x, center.getY() + y, center.getZ() + z);
    }
    
    @NotNull
    public static BoundingBox of(@NotNull final Location center, final double x, final double y, final double z) {
        Validate.notNull(center, "Center is null!");
        return new BoundingBox(center.getX() - x, center.getY() - y, center.getZ() - z, center.getX() + x, center.getY() + y, center.getZ() + z);
    }
    
    public BoundingBox() {
        this.resize(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }
    
    public BoundingBox(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        this.resize(x1, y1, z1, x2, y2, z2);
    }
    
    @NotNull
    public BoundingBox resize(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        NumberConversions.checkFinite(x1, "x1 not finite");
        NumberConversions.checkFinite(y1, "y1 not finite");
        NumberConversions.checkFinite(z1, "z1 not finite");
        NumberConversions.checkFinite(x2, "x2 not finite");
        NumberConversions.checkFinite(y2, "y2 not finite");
        NumberConversions.checkFinite(z2, "z2 not finite");
        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
        return this;
    }
    
    public double getMinX() {
        return this.minX;
    }
    
    public double getMinY() {
        return this.minY;
    }
    
    public double getMinZ() {
        return this.minZ;
    }
    
    @NotNull
    public Vector getMin() {
        return new Vector(this.minX, this.minY, this.minZ);
    }
    
    public double getMaxX() {
        return this.maxX;
    }
    
    public double getMaxY() {
        return this.maxY;
    }
    
    public double getMaxZ() {
        return this.maxZ;
    }
    
    @NotNull
    public Vector getMax() {
        return new Vector(this.maxX, this.maxY, this.maxZ);
    }
    
    public double getWidthX() {
        return this.maxX - this.minX;
    }
    
    public double getWidthZ() {
        return this.maxZ - this.minZ;
    }
    
    public double getHeight() {
        return this.maxY - this.minY;
    }
    
    public double getVolume() {
        return this.getHeight() * this.getWidthX() * this.getWidthZ();
    }
    
    public double getCenterX() {
        return this.minX + this.getWidthX() * 0.5;
    }
    
    public double getCenterY() {
        return this.minY + this.getHeight() * 0.5;
    }
    
    public double getCenterZ() {
        return this.minZ + this.getWidthZ() * 0.5;
    }
    
    @NotNull
    public Vector getCenter() {
        return new Vector(this.getCenterX(), this.getCenterY(), this.getCenterZ());
    }
    
    @NotNull
    public BoundingBox copy(@NotNull final BoundingBox other) {
        Validate.notNull(other, "Other bounding box is null!");
        return this.resize(other.getMinX(), other.getMinY(), other.getMinZ(), other.getMaxX(), other.getMaxY(), other.getMaxZ());
    }
    
    @NotNull
    public BoundingBox expand(final double negativeX, final double negativeY, final double negativeZ, final double positiveX, final double positiveY, final double positiveZ) {
        if (negativeX == 0.0 && negativeY == 0.0 && negativeZ == 0.0 && positiveX == 0.0 && positiveY == 0.0 && positiveZ == 0.0) {
            return this;
        }
        double newMinX = this.minX - negativeX;
        double newMinY = this.minY - negativeY;
        double newMinZ = this.minZ - negativeZ;
        double newMaxX = this.maxX + positiveX;
        double newMaxY = this.maxY + positiveY;
        double newMaxZ = this.maxZ + positiveZ;
        if (newMinX > newMaxX) {
            final double centerZ = this.getCenterX();
            if (newMaxX >= centerZ) {
                newMinX = newMaxX;
            }
            else if (newMinX <= centerZ) {
                newMaxX = newMinX;
            }
            else {
                newMinX = centerZ;
                newMaxX = centerZ;
            }
        }
        if (newMinY > newMaxY) {
            final double centerZ = this.getCenterY();
            if (newMaxY >= centerZ) {
                newMinY = newMaxY;
            }
            else if (newMinY <= centerZ) {
                newMaxY = newMinY;
            }
            else {
                newMinY = centerZ;
                newMaxY = centerZ;
            }
        }
        if (newMinZ > newMaxZ) {
            final double centerZ = this.getCenterZ();
            if (newMaxZ >= centerZ) {
                newMinZ = newMaxZ;
            }
            else if (newMinZ <= centerZ) {
                newMaxZ = newMinZ;
            }
            else {
                newMinZ = centerZ;
                newMaxZ = centerZ;
            }
        }
        return this.resize(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }
    
    @NotNull
    public BoundingBox expand(final double x, final double y, final double z) {
        return this.expand(x, y, z, x, y, z);
    }
    
    @NotNull
    public BoundingBox expand(@NotNull final Vector expansion) {
        Validate.notNull(expansion, "Expansion is null!");
        final double x = expansion.getX();
        final double y = expansion.getY();
        final double z = expansion.getZ();
        return this.expand(x, y, z, x, y, z);
    }
    
    @NotNull
    public BoundingBox expand(final double expansion) {
        return this.expand(expansion, expansion, expansion, expansion, expansion, expansion);
    }
    
    @NotNull
    public BoundingBox expand(final double dirX, final double dirY, final double dirZ, final double expansion) {
        if (expansion == 0.0) {
            return this;
        }
        if (dirX == 0.0 && dirY == 0.0 && dirZ == 0.0) {
            return this;
        }
        final double negativeX = (dirX < 0.0) ? (-dirX * expansion) : 0.0;
        final double negativeY = (dirY < 0.0) ? (-dirY * expansion) : 0.0;
        final double negativeZ = (dirZ < 0.0) ? (-dirZ * expansion) : 0.0;
        final double positiveX = (dirX > 0.0) ? (dirX * expansion) : 0.0;
        final double positiveY = (dirY > 0.0) ? (dirY * expansion) : 0.0;
        final double positiveZ = (dirZ > 0.0) ? (dirZ * expansion) : 0.0;
        return this.expand(negativeX, negativeY, negativeZ, positiveX, positiveY, positiveZ);
    }
    
    @NotNull
    public BoundingBox expand(@NotNull final Vector direction, final double expansion) {
        Validate.notNull(direction, "Direction is null!");
        return this.expand(direction.getX(), direction.getY(), direction.getZ(), expansion);
    }
    
    @NotNull
    public BoundingBox expand(@NotNull final BlockFace blockFace, final double expansion) {
        Validate.notNull(blockFace, "Block face is null!");
        return (blockFace == BlockFace.SELF) ? this : this.expand(blockFace.getDirection(), expansion);
    }
    
    @NotNull
    public BoundingBox expandDirectional(final double dirX, final double dirY, final double dirZ) {
        return this.expand(dirX, dirY, dirZ, 1.0);
    }
    
    @NotNull
    public BoundingBox expandDirectional(@NotNull final Vector direction) {
        Validate.notNull(direction, "Expansion is null!");
        return this.expand(direction.getX(), direction.getY(), direction.getZ(), 1.0);
    }
    
    @NotNull
    public BoundingBox union(final double posX, final double posY, final double posZ) {
        final double newMinX = Math.min(this.minX, posX);
        final double newMinY = Math.min(this.minY, posY);
        final double newMinZ = Math.min(this.minZ, posZ);
        final double newMaxX = Math.max(this.maxX, posX);
        final double newMaxY = Math.max(this.maxY, posY);
        final double newMaxZ = Math.max(this.maxZ, posZ);
        return (newMinX == this.minX && newMinY == this.minY && newMinZ == this.minZ && newMaxX == this.maxX && newMaxY == this.maxY && newMaxZ == this.maxZ) ? this : this.resize(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }
    
    @NotNull
    public BoundingBox union(@NotNull final Vector position) {
        Validate.notNull(position, "Position is null!");
        return this.union(position.getX(), position.getY(), position.getZ());
    }
    
    @NotNull
    public BoundingBox union(@NotNull final Location position) {
        Validate.notNull(position, "Position is null!");
        return this.union(position.getX(), position.getY(), position.getZ());
    }
    
    @NotNull
    public BoundingBox union(@NotNull final BoundingBox other) {
        Validate.notNull(other, "Other bounding box is null!");
        if (this.contains(other)) {
            return this;
        }
        final double newMinX = Math.min(this.minX, other.minX);
        final double newMinY = Math.min(this.minY, other.minY);
        final double newMinZ = Math.min(this.minZ, other.minZ);
        final double newMaxX = Math.max(this.maxX, other.maxX);
        final double newMaxY = Math.max(this.maxY, other.maxY);
        final double newMaxZ = Math.max(this.maxZ, other.maxZ);
        return this.resize(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }
    
    @NotNull
    public BoundingBox intersection(@NotNull final BoundingBox other) {
        Validate.notNull(other, "Other bounding box is null!");
        Validate.isTrue(this.overlaps(other), "The bounding boxes do not overlap!");
        final double newMinX = Math.max(this.minX, other.minX);
        final double newMinY = Math.max(this.minY, other.minY);
        final double newMinZ = Math.max(this.minZ, other.minZ);
        final double newMaxX = Math.min(this.maxX, other.maxX);
        final double newMaxY = Math.min(this.maxY, other.maxY);
        final double newMaxZ = Math.min(this.maxZ, other.maxZ);
        return this.resize(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }
    
    @NotNull
    public BoundingBox shift(final double shiftX, final double shiftY, final double shiftZ) {
        return (shiftX == 0.0 && shiftY == 0.0 && shiftZ == 0.0) ? this : this.resize(this.minX + shiftX, this.minY + shiftY, this.minZ + shiftZ, this.maxX + shiftX, this.maxY + shiftY, this.maxZ + shiftZ);
    }
    
    @NotNull
    public BoundingBox shift(@NotNull final Vector shift) {
        Validate.notNull(shift, "Shift is null!");
        return this.shift(shift.getX(), shift.getY(), shift.getZ());
    }
    
    @NotNull
    public BoundingBox shift(@NotNull final Location shift) {
        Validate.notNull(shift, "Shift is null!");
        return this.shift(shift.getX(), shift.getY(), shift.getZ());
    }
    
    private boolean overlaps(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ) {
        return this.minX < maxX && this.maxX > minX && this.minY < maxY && this.maxY > minY && this.minZ < maxZ && this.maxZ > minZ;
    }
    
    public boolean overlaps(@NotNull final BoundingBox other) {
        Validate.notNull(other, "Other bounding box is null!");
        return this.overlaps(other.minX, other.minY, other.minZ, other.maxX, other.maxY, other.maxZ);
    }
    
    public boolean overlaps(@NotNull final Vector min, @NotNull final Vector max) {
        Validate.notNull(min, "Min is null!");
        Validate.notNull(max, "Max is null!");
        final double x1 = min.getX();
        final double y1 = min.getY();
        final double z1 = min.getZ();
        final double x2 = max.getX();
        final double y2 = max.getY();
        final double z2 = max.getZ();
        return this.overlaps(Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2), Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2));
    }
    
    public boolean contains(final double x, final double y, final double z) {
        return x >= this.minX && x < this.maxX && y >= this.minY && y < this.maxY && z >= this.minZ && z < this.maxZ;
    }
    
    public boolean contains(@NotNull final Vector position) {
        Validate.notNull(position, "Position is null!");
        return this.contains(position.getX(), position.getY(), position.getZ());
    }
    
    private boolean contains(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ) {
        return this.minX <= minX && this.maxX >= maxX && this.minY <= minY && this.maxY >= maxY && this.minZ <= minZ && this.maxZ >= maxZ;
    }
    
    public boolean contains(@NotNull final BoundingBox other) {
        Validate.notNull(other, "Other bounding box is null!");
        return this.contains(other.minX, other.minY, other.minZ, other.maxX, other.maxY, other.maxZ);
    }
    
    public boolean contains(@NotNull final Vector min, @NotNull final Vector max) {
        Validate.notNull(min, "Min is null!");
        Validate.notNull(max, "Max is null!");
        final double x1 = min.getX();
        final double y1 = min.getY();
        final double z1 = min.getZ();
        final double x2 = max.getX();
        final double y2 = max.getY();
        final double z2 = max.getZ();
        return this.contains(Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2), Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2));
    }
    
    @Nullable
    public RayTraceResult rayTrace(@NotNull final Vector start, @NotNull final Vector direction, final double maxDistance) {
        Validate.notNull(start, "Start is null!");
        start.checkFinite();
        Validate.notNull(direction, "Direction is null!");
        direction.checkFinite();
        Validate.isTrue(direction.lengthSquared() > 0.0, "Direction's magnitude is 0!");
        if (maxDistance < 0.0) {
            return null;
        }
        final double startX = start.getX();
        final double startY = start.getY();
        final double startZ = start.getZ();
        final Vector normalizedZeroDir = direction.clone();
        if (normalizedZeroDir.getX() == -0.0) {
            normalizedZeroDir.setX(0.0);
        }
        if (normalizedZeroDir.getY() == -0.0) {
            normalizedZeroDir.setY(0.0);
        }
        if (normalizedZeroDir.getZ() == -0.0) {
            normalizedZeroDir.setZ(0.0);
        }
        final Vector dir = normalizedZeroDir.normalize();
        final double dirX = dir.getX();
        final double dirY = dir.getY();
        final double dirZ = dir.getZ();
        final double divX = 1.0 / dirX;
        final double divY = 1.0 / dirY;
        final double divZ = 1.0 / dirZ;
        double tMin;
        double tMax;
        BlockFace hitBlockFaceMin;
        BlockFace hitBlockFaceMax;
        if (dirX >= 0.0) {
            tMin = (this.minX - startX) * divX;
            tMax = (this.maxX - startX) * divX;
            hitBlockFaceMin = BlockFace.WEST;
            hitBlockFaceMax = BlockFace.EAST;
        }
        else {
            tMin = (this.maxX - startX) * divX;
            tMax = (this.minX - startX) * divX;
            hitBlockFaceMin = BlockFace.EAST;
            hitBlockFaceMax = BlockFace.WEST;
        }
        double tyMin;
        double tyMax;
        BlockFace hitBlockFaceYMin;
        BlockFace hitBlockFaceYMax;
        if (dirY >= 0.0) {
            tyMin = (this.minY - startY) * divY;
            tyMax = (this.maxY - startY) * divY;
            hitBlockFaceYMin = BlockFace.DOWN;
            hitBlockFaceYMax = BlockFace.UP;
        }
        else {
            tyMin = (this.maxY - startY) * divY;
            tyMax = (this.minY - startY) * divY;
            hitBlockFaceYMin = BlockFace.UP;
            hitBlockFaceYMax = BlockFace.DOWN;
        }
        if (tMin > tyMax || tMax < tyMin) {
            return null;
        }
        if (tyMin > tMin) {
            tMin = tyMin;
            hitBlockFaceMin = hitBlockFaceYMin;
        }
        if (tyMax < tMax) {
            tMax = tyMax;
            hitBlockFaceMax = hitBlockFaceYMax;
        }
        double tzMin;
        double tzMax;
        BlockFace hitBlockFaceZMin;
        BlockFace hitBlockFaceZMax;
        if (dirZ >= 0.0) {
            tzMin = (this.minZ - startZ) * divZ;
            tzMax = (this.maxZ - startZ) * divZ;
            hitBlockFaceZMin = BlockFace.NORTH;
            hitBlockFaceZMax = BlockFace.SOUTH;
        }
        else {
            tzMin = (this.maxZ - startZ) * divZ;
            tzMax = (this.minZ - startZ) * divZ;
            hitBlockFaceZMin = BlockFace.SOUTH;
            hitBlockFaceZMax = BlockFace.NORTH;
        }
        if (tMin > tzMax || tMax < tzMin) {
            return null;
        }
        if (tzMin > tMin) {
            tMin = tzMin;
            hitBlockFaceMin = hitBlockFaceZMin;
        }
        if (tzMax < tMax) {
            tMax = tzMax;
            hitBlockFaceMax = hitBlockFaceZMax;
        }
        if (tMax < 0.0) {
            return null;
        }
        if (tMin > maxDistance) {
            return null;
        }
        double t;
        BlockFace hitBlockFace;
        if (tMin < 0.0) {
            t = tMax;
            hitBlockFace = hitBlockFaceMax;
        }
        else {
            t = tMin;
            hitBlockFace = hitBlockFaceMin;
        }
        final Vector hitPosition = dir.multiply(t).add(start);
        return new RayTraceResult(hitPosition, hitBlockFace);
    }
    
    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(this.maxX);
        int result = 31 + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.maxY);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.maxZ);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.minX);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.minY);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.minZ);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BoundingBox)) {
            return false;
        }
        final BoundingBox other = (BoundingBox)obj;
        return Double.doubleToLongBits(this.maxX) == Double.doubleToLongBits(other.maxX) && Double.doubleToLongBits(this.maxY) == Double.doubleToLongBits(other.maxY) && Double.doubleToLongBits(this.maxZ) == Double.doubleToLongBits(other.maxZ) && Double.doubleToLongBits(this.minX) == Double.doubleToLongBits(other.minX) && Double.doubleToLongBits(this.minY) == Double.doubleToLongBits(other.minY) && Double.doubleToLongBits(this.minZ) == Double.doubleToLongBits(other.minZ);
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BoundingBox [minX=");
        builder.append(this.minX);
        builder.append(", minY=");
        builder.append(this.minY);
        builder.append(", minZ=");
        builder.append(this.minZ);
        builder.append(", maxX=");
        builder.append(this.maxX);
        builder.append(", maxY=");
        builder.append(this.maxY);
        builder.append(", maxZ=");
        builder.append(this.maxZ);
        builder.append("]");
        return builder.toString();
    }
    
    @NotNull
    public BoundingBox clone() {
        try {
            return (BoundingBox)super.clone();
        }
        catch (final CloneNotSupportedException var2) {
            throw new Error(var2);
        }
    }
    
    @NotNull
    public Map<String, Object> serialize() {
        final Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("minX", this.minX);
        result.put("minY", this.minY);
        result.put("minZ", this.minZ);
        result.put("maxX", this.maxX);
        result.put("maxY", this.maxY);
        result.put("maxZ", this.maxZ);
        return result;
    }
    
    @NotNull
    public static BoundingBox deserialize(@NotNull final Map<String, Object> args) {
        double minX = 0.0;
        double minY = 0.0;
        double minZ = 0.0;
        double maxX = 0.0;
        double maxY = 0.0;
        double maxZ = 0.0;
        if (args.containsKey("minX")) {
            minX = args.get("minX").doubleValue();
        }
        if (args.containsKey("minY")) {
            minY = args.get("minY").doubleValue();
        }
        if (args.containsKey("minZ")) {
            minZ = args.get("minZ").doubleValue();
        }
        if (args.containsKey("maxX")) {
            maxX = args.get("maxX").doubleValue();
        }
        if (args.containsKey("maxY")) {
            maxY = args.get("maxY").doubleValue();
        }
        if (args.containsKey("maxZ")) {
            maxZ = args.get("maxZ").doubleValue();
        }
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }
}
