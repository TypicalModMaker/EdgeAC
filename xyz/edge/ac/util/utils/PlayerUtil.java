package xyz.edge.ac.util.utils;

import java.util.Iterator;
import org.bukkit.entity.Vehicle;
import org.bukkit.World;
import java.util.LinkedList;
import org.bukkit.entity.Entity;
import java.util.List;
import org.bukkit.util.Vector;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import java.util.function.Function;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.enchantments.Enchantment;
import xyz.edge.ac.packetevents.PacketEvents;
import xyz.edge.ac.packetevents.utils.player.ClientVersion;
import org.bukkit.entity.Player;

public final class PlayerUtil
{
    public static ClientVersion getClientVersion(final Player player) {
        return PacketEvents.get().getPlayerUtils().getClientVersion(player);
    }
    
    public static int getPing(final Player player) {
        return PacketEvents.get().getPlayerUtils().getPing(player);
    }
    
    public static int getDepthStriderLevel(final Player player) {
        if (player.getInventory().getBoots() != null && !ServerUtil.isLowerThan1_8()) {
            return player.getInventory().getBoots().getEnchantmentLevel(Enchantment.DEPTH_STRIDER);
        }
        return 0;
    }
    
    public static boolean isHoldingSword(final Player player) {
        return player.getItemInHand().getType().toString().toLowerCase().contains("sword");
    }
    
    public static double getBaseSpeed(final Player player) {
        return 0.36 + getPotionLevel(player, PotionEffectType.SPEED) * 0.062f + (player.getWalkSpeed() - 0.2f) * 1.6f;
    }
    
    public static double getBaseGroundSpeed(final Player player) {
        return 0.288 + getPotionLevel(player, PotionEffectType.SPEED) * 0.062f + (player.getWalkSpeed() - 0.2f) * 1.6f;
    }
    
    public static double getScaffoldSpeed(final Player player) {
        return 0.28 + getPotionLevel(player, PotionEffectType.SPEED) * 0.06f + (player.getWalkSpeed() - 0.2f) * 1.6f;
    }
    
    public static int getPotionLevel(final Player player, final PotionEffectType effect) {
        final int effectId = effect.getId();
        if (!player.hasPotionEffect(effect)) {
            return 0;
        }
        return player.getActivePotionEffects().stream().filter(potionEffect -> potionEffect.getType().getId() == effectId).map((Function<? super Object, ? extends Integer>)PotionEffect::getAmplifier).findAny().orElse(0) + 1;
    }
    
    public static Block getLookingBlock(final Player player, final int distance) {
        final Location loc = player.getEyeLocation();
        final Vector v = loc.getDirection().normalize();
        for (int i = 1; i <= distance; ++i) {
            loc.add(v);
            final Block b = loc.getBlock();
            if (b == null) {
                break;
            }
            if (b.getType() != Material.AIR) {
                return b;
            }
        }
        return null;
    }
    
    public static List<Entity> getEntitiesWithinRadius(final Location location, final double radius) {
        final double expander = 16.0;
        final double x = location.getX();
        final double z = location.getZ();
        final int minX = (int)Math.floor((x - radius) / 16.0);
        final int maxX = (int)Math.floor((x + radius) / 16.0);
        final int minZ = (int)Math.floor((z - radius) / 16.0);
        final int maxZ = (int)Math.floor((z + radius) / 16.0);
        final World world = location.getWorld();
        final List<Entity> entities = new LinkedList<Entity>();
        for (int xVal = minX; xVal <= maxX; ++xVal) {
            for (int zVal = minZ; zVal <= maxZ; ++zVal) {
                if (world.isChunkLoaded(xVal, zVal)) {
                    for (final Entity entity : world.getChunkAt(xVal, zVal).getEntities()) {
                        if (entity != null) {
                            if (entity.getLocation().distanceSquared(location) <= radius * radius) {
                                entities.add(entity);
                            }
                        }
                    }
                }
            }
        }
        return entities;
    }
    
    public static boolean isNearVehicle(final Player player) {
        for (final Entity entity : getEntitiesWithinRadius(player.getLocation(), 2.0)) {
            if (entity instanceof Vehicle) {
                return true;
            }
        }
        return false;
    }
    
    private PlayerUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
