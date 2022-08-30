package xyz.edge.ac.util.mc;

import org.bukkit.Location;
import java.util.ArrayList;
import org.bukkit.block.Block;
import java.util.List;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public final class BoundingBox
{
    private double minX;
    private double minY;
    private double minZ;
    private double maxX;
    private double maxY;
    private double maxZ;
    
    public BoundingBox(final double minX, final double maxX, final double minY, final double maxY, final double minZ, final double maxZ) {
        if (minX < maxX) {
            this.minX = minX;
            this.maxX = maxX;
        }
        else {
            this.minX = maxX;
            this.maxX = minX;
        }
        if (minY < maxY) {
            this.minY = minY;
            this.maxY = maxY;
        }
        else {
            this.minY = maxY;
            this.maxY = minY;
        }
        if (minZ < maxZ) {
            this.minZ = minZ;
            this.maxZ = maxZ;
        }
        else {
            this.minZ = maxZ;
            this.maxZ = minZ;
        }
    }
    
    public BoundingBox(final Vector min, final Vector max) {
        this.minX = min.getX();
        this.minY = min.getY();
        this.minZ = min.getZ();
        this.maxX = max.getX();
        this.maxY = max.getY();
        this.maxZ = max.getZ();
    }
    
    public BoundingBox(final Player player) {
        this.minX = player.getLocation().getX() - 0.3;
        this.minY = player.getLocation().getY();
        this.minZ = player.getLocation().getZ() - 0.3;
        this.maxX = player.getLocation().getX() + 0.3;
        this.maxY = player.getLocation().getY() + 1.8;
        this.maxZ = player.getLocation().getZ() + 0.3;
    }
    
    public BoundingBox(final Vector data) {
        this.minX = data.getX() - 0.4;
        this.minY = data.getY();
        this.minZ = data.getZ() - 0.4;
        this.maxX = data.getX() + 0.4;
        this.maxY = data.getY() + 1.9;
        this.maxZ = data.getZ() + 0.4;
    }
    
    public BoundingBox move(final double x, final double y, final double z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
        return this;
    }
    
    public List<Block> getBlocks(final World world) {
        final List<Block> blockList = new ArrayList<Block>();
        final double minX = this.minX;
        final double minY = this.minY;
        final double minZ = this.minZ;
        final double maxX = this.maxX;
        final double maxY = this.maxY;
        final double maxZ = this.maxZ;
        for (double x = minX; x <= maxX; x += maxX - minX) {
            for (double y = minY; y <= maxY + 0.01; y += maxY - minY) {
                for (double z = minZ; z <= maxZ; z += maxZ - minZ) {
                    final Block block = world.getBlockAt(new Location(world, x, y, z));
                    blockList.add(block);
                }
            }
        }
        return blockList;
    }
    
    public BoundingBox expand(final double x, final double y, final double z) {
        this.minX -= x;
        this.minY -= y;
        this.minZ -= z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
        return this;
    }
    
    public BoundingBox expandSpecific(final double minX, final double maxX, final double minY, final double maxY, final double minZ, final double maxZ) {
        this.minX -= minX;
        this.minY -= minY;
        this.minZ -= minZ;
        this.maxX += maxX;
        this.maxY += maxY;
        this.maxZ += maxZ;
        return this;
    }
    
    public BoundingBox union(final BoundingBox other) {
        final double minX = Math.min(this.minX, other.minX);
        final double minY = Math.min(this.minY, other.minY);
        final double minZ = Math.min(this.minZ, other.minZ);
        final double maxX = Math.max(this.maxX, other.maxX);
        final double maxY = Math.max(this.maxY, other.maxY);
        final double maxZ = Math.max(this.maxZ, other.maxZ);
        return new BoundingBox(minX, maxX, minY, maxY, minZ, maxZ);
    }
    
    public double getSize() {
        final Vector min = new Vector(this.minX, this.minY, this.minZ);
        final Vector max = new Vector(this.maxX, this.maxY, this.maxZ);
        return min.distance(max);
    }
    
    public double min(final int i) {
        switch (i) {
            case 0: {
                return this.minX;
            }
            case 1: {
                return this.minY;
            }
            case 2: {
                return this.minZ;
            }
            default: {
                return 0.0;
            }
        }
    }
    
    public double max(final int i) {
        switch (i) {
            case 0: {
                return this.maxX;
            }
            case 1: {
                return this.maxY;
            }
            case 2: {
                return this.maxZ;
            }
            default: {
                return 0.0;
            }
        }
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
    
    public double getMaxX() {
        return this.maxX;
    }
    
    public double getMaxY() {
        return this.maxY;
    }
    
    public double getMaxZ() {
        return this.maxZ;
    }
}
