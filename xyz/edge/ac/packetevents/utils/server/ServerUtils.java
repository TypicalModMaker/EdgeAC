package xyz.edge.ac.packetevents.utils.server;

import java.util.ArrayList;
import java.lang.reflect.InvocationTargetException;
import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import java.util.List;
import xyz.edge.ac.packetevents.utils.entityfinder.EntityFinderUtils;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;
import java.util.Iterator;
import xyz.edge.ac.packetevents.PacketEvents;
import org.jetbrains.annotations.NotNull;
import org.bukkit.World;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.utils.boundingbox.BoundingBox;
import org.spigotmc.SpigotConfig;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import org.bukkit.entity.Entity;
import java.util.Map;
import xyz.edge.ac.packetevents.utils.npc.NPCManager;
import java.lang.reflect.Method;

public final class ServerUtils
{
    private static Method getLevelEntityGetterIterable;
    private static Class<?> persistentEntitySectionManagerClass;
    private static Class<?> levelEntityGetterClass;
    private static byte v_1_17;
    private static Class<?> geyserClass;
    private boolean geyserClassChecked;
    private final NPCManager npcManager;
    public Map<Integer, Entity> entityCache;
    
    public ServerUtils() {
        this.npcManager = new NPCManager();
    }
    
    public ServerVersion getVersion() {
        return ServerVersion.getVersion();
    }
    
    public double[] getRecentTPS() {
        return NMSUtils.recentTPS();
    }
    
    public double getTPS() {
        return this.getRecentTPS()[0];
    }
    
    public SystemOS getOS() {
        return SystemOS.getOS();
    }
    
    public NPCManager getNPCManager() {
        return this.npcManager;
    }
    
    public boolean isBungeeCordEnabled() {
        return SpigotConfig.bungee;
    }
    
    public BoundingBox getEntityBoundingBox(final Entity entity) {
        final Object nmsEntity = NMSUtils.getNMSEntity(entity);
        final Object aabb = NMSUtils.getNMSAxisAlignedBoundingBox(nmsEntity);
        final WrappedPacket wrappedBoundingBox = new WrappedPacket(new NMSPacket(aabb));
        final double minX = wrappedBoundingBox.readDouble(0);
        final double minY = wrappedBoundingBox.readDouble(1);
        final double minZ = wrappedBoundingBox.readDouble(2);
        final double maxX = wrappedBoundingBox.readDouble(3);
        final double maxY = wrappedBoundingBox.readDouble(4);
        final double maxZ = wrappedBoundingBox.readDouble(5);
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }
    
    @Nullable
    private Entity getEntityByIdIterateWorld(@NotNull final World world, final int entityID) {
        for (final Entity entity : PacketEvents.get().getServerUtils().getEntityList(world)) {
            if (entity.getEntityId() == entityID) {
                this.entityCache.putIfAbsent(entity.getEntityId(), entity);
                return entity;
            }
        }
        return null;
    }
    
    @Nullable
    public Entity getEntityById(@Nullable final World world, final int entityID) {
        final Entity e = this.entityCache.get(entityID);
        if (e != null) {
            return e;
        }
        if (ServerUtils.v_1_17 == -1) {
            ServerUtils.v_1_17 = (byte)(this.getVersion().isNewerThanOrEquals(ServerVersion.v_1_17) ? 1 : 0);
        }
        if (ServerUtils.v_1_17 == 1) {
            try {
                if (world != null) {
                    final Entity newEntity = this.getEntityByIdIterateWorld(world, entityID);
                    if (newEntity != null) {
                        return newEntity;
                    }
                }
            }
            catch (final Exception ex2) {}
            try {
                for (final World w : Bukkit.getWorlds()) {
                    final Entity newEntity2 = this.getEntityByIdIterateWorld(w, entityID);
                    if (newEntity2 != null) {
                        return newEntity2;
                    }
                }
                return null;
            }
            catch (final Exception ex) {
                return null;
            }
            return EntityFinderUtils.getEntityByIdUnsafe(world, entityID);
        }
        return EntityFinderUtils.getEntityByIdUnsafe(world, entityID);
    }
    
    @Nullable
    public Entity getEntityById(final int entityID) {
        return this.getEntityById(null, entityID);
    }
    
    public List<Entity> getEntityList(final World world) {
        if (ServerUtils.v_1_17 == -1) {
            ServerUtils.v_1_17 = (byte)(this.getVersion().isNewerThanOrEquals(ServerVersion.v_1_17) ? 1 : 0);
        }
        if (ServerUtils.v_1_17 == 1) {
            if (ServerUtils.persistentEntitySectionManagerClass == null) {
                ServerUtils.persistentEntitySectionManagerClass = NMSUtils.getNMClassWithoutException("world.level.entity.PersistentEntitySectionManager");
            }
            if (ServerUtils.levelEntityGetterClass == null) {
                ServerUtils.levelEntityGetterClass = NMSUtils.getNMClassWithoutException("world.level.entity.LevelEntityGetter");
            }
            if (ServerUtils.getLevelEntityGetterIterable == null) {
                ServerUtils.getLevelEntityGetterIterable = Reflection.getMethod(ServerUtils.levelEntityGetterClass, Iterable.class, 0);
            }
            final Object worldServer = NMSUtils.convertBukkitWorldToWorldServer(world);
            final WrappedPacket wrappedWorldServer = new WrappedPacket(new NMSPacket(worldServer));
            final Object persistentEntitySectionManager = wrappedWorldServer.readObject(0, ServerUtils.persistentEntitySectionManagerClass);
            final WrappedPacket wrappedPersistentEntitySectionManager = new WrappedPacket(new NMSPacket(persistentEntitySectionManager));
            final Object levelEntityGetter = wrappedPersistentEntitySectionManager.readObject(0, ServerUtils.levelEntityGetterClass);
            Iterable<Object> nmsEntitiesIterable = null;
            try {
                nmsEntitiesIterable = (Iterable)ServerUtils.getLevelEntityGetterIterable.invoke(levelEntityGetter, new Object[0]);
            }
            catch (final IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            final List<Entity> entityList = new ArrayList<Entity>();
            if (nmsEntitiesIterable != null) {
                for (final Object nmsEntity : nmsEntitiesIterable) {
                    final Entity bukkitEntity = NMSUtils.getBukkitEntity(nmsEntity);
                    entityList.add(bukkitEntity);
                }
            }
            return entityList;
        }
        return world.getEntities();
    }
    
    public boolean isGeyserAvailable() {
        if (!this.geyserClassChecked) {
            this.geyserClassChecked = true;
            try {
                ServerUtils.geyserClass = Class.forName("org.geysermc.connector.GeyserConnector");
                return true;
            }
            catch (final ClassNotFoundException e) {
                return false;
            }
        }
        return ServerUtils.geyserClass != null;
    }
    
    static {
        ServerUtils.v_1_17 = -1;
    }
}
