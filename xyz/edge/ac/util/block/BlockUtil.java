package xyz.edge.ac.util.block;

import org.bukkit.plugin.Plugin;
import xyz.edge.ac.Edge;
import org.bukkit.Bukkit;
import java.util.concurrent.FutureTask;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.Location;

public final class BlockUtil
{
    public static double getBlockFriction(final Location to) {
        try {
            return (getBlockAsync(to).getType() == Material.PACKED_ICE || getBlockAsync(to).getType() == Material.ICE) ? 0.9800000190734863 : (getBlockAsync(to).getType().toString().toLowerCase().contains("slime") ? 0.800000011920929 : 0.6000000238418579);
        }
        catch (final Exception ignored) {
            return 0.6000000238418579;
        }
    }
    
    public static double getBlockFriction2(final Location to) {
        try {
            return (getBlockAsync(to).getType() == Material.PACKED_ICE || getBlockAsync(to).getType() == Material.ICE) ? 0.9800000190734863 : (getBlockAsync(to).getType().toString().toLowerCase().contains("slime") ? 0.800000011920929 : 0.6000000238418579);
        }
        catch (final Exception ignored) {
            return 0.6000000238418579;
        }
    }
    
    public static double getBlockFriction(final Block block) {
        return (block.getType() == Material.PACKED_ICE || block.getType() == Material.ICE) ? 0.9800000190734863 : (block.getType().toString().contains("SLIME") ? 0.800000011920929 : 0.6000000238418579);
    }
    
    public double getBlockFriction2(final Block block) {
        return (block.getType() == Material.PACKED_ICE || block.getType() == Material.ICE) ? 0.9800000190734863 : (block.getType().toString().contains("SLIME") ? 0.800000011920929 : 0.6000000238418579);
    }
    
    public static boolean isLiquid(final Block block) {
        return block.getType().toString().contains("WATER") || block.getType().toString().contains("LAVA");
    }
    
    public static Block getBlockAsync(final Location location) {
        if (location.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4)) {
            return location.getWorld().getBlockAt(location);
        }
        return null;
    }
    
    public static Block shitAssBlockGetterThatCrashServer(final Location location) {
        if (location.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4)) {
            return location.getBlock();
        }
        final FutureTask<Block> futureTask = new FutureTask<Block>(() -> {
            location.getWorld().loadChunk(location.getBlockX() >> 4, location.getBlockZ() >> 4);
            return location.getBlock();
        });
        Bukkit.getScheduler().runTask(Edge.getInstance(), futureTask);
        try {
            return futureTask.get();
        }
        catch (final Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
    
    private BlockUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
