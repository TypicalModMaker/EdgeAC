package xyz.edge.ac.packetevents.utils.entityfinder;

import java.lang.reflect.InvocationTargetException;
import org.jetbrains.annotations.Nullable;
import java.util.Iterator;
import java.util.ConcurrentModificationException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.World;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import java.lang.reflect.Method;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;

public final class EntityFinderUtils
{
    public static ServerVersion version;
    private static Class<?> worldServerClass;
    private static Method getEntityByIdMethod;
    private static Method craftWorldGetHandle;
    
    public static void load() {
        EntityFinderUtils.worldServerClass = NMSUtils.getNMSClassWithoutException("WorldServer");
        if (EntityFinderUtils.worldServerClass == null) {
            EntityFinderUtils.worldServerClass = NMSUtils.getNMClassWithoutException("server.level.WorldServer");
        }
        try {
            EntityFinderUtils.craftWorldGetHandle = NMSUtils.craftWorldClass.getMethod("getHandle", (Class<?>[])new Class[0]);
            EntityFinderUtils.getEntityByIdMethod = EntityFinderUtils.worldServerClass.getMethod("a", Integer.TYPE);
        }
        catch (final NoSuchMethodException e) {
            try {
                EntityFinderUtils.getEntityByIdMethod = EntityFinderUtils.worldServerClass.getMethod("getEntity", Integer.TYPE);
            }
            catch (final NoSuchMethodException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    @Nullable
    public static Entity getEntityByIdUnsafe(final World origin, final int id) {
        final Entity e = getEntityByIdWithWorldUnsafe(origin, id);
        if (e != null) {
            return e;
        }
        for (final World world : Bukkit.getWorlds()) {
            final Entity entity = getEntityByIdWithWorldUnsafe(world, id);
            if (entity != null) {
                return entity;
            }
        }
        for (final World world : Bukkit.getWorlds()) {
            try {
                for (final Entity entity2 : world.getEntities()) {
                    if (entity2.getEntityId() == id) {
                        return entity2;
                    }
                }
            }
            catch (final ConcurrentModificationException ex) {
                return null;
            }
        }
        return null;
    }
    
    public static Entity getEntityByIdWithWorldUnsafe(final World world, final int id) {
        if (world == null) {
            return null;
        }
        if (NMSUtils.craftWorldClass == null) {
            throw new IllegalStateException("PacketEvents failed to locate the CraftWorld class.");
        }
        final Object craftWorld = NMSUtils.craftWorldClass.cast(world);
        try {
            final Object worldServer = EntityFinderUtils.craftWorldGetHandle.invoke(craftWorld, new Object[0]);
            final Object nmsEntity = EntityFinderUtils.getEntityByIdMethod.invoke(worldServer, id);
            if (nmsEntity == null) {
                return null;
            }
            return NMSUtils.getBukkitEntity(nmsEntity);
        }
        catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
