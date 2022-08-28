package xyz.edge.ac.packetevents.utils.nms;

import java.util.function.Supplier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.UUID;
import xyz.edge.ac.packetevents.utils.vector.Vector3d;
import xyz.edge.ac.packetevents.utils.vector.Vector3f;
import xyz.edge.ac.packetevents.utils.vector.Vector3i;
import java.util.Iterator;
import java.util.List;
import org.bukkit.entity.Player;
import java.lang.reflect.InvocationTargetException;
import xyz.edge.ac.packetevents.PacketEvents;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.bukkit.World;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import org.bukkit.Server;
import java.util.Optional;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.edge.ac.packetevents.utils.reflection.SubclassUtil;
import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import java.util.Random;

public final class NMSUtils
{
    public static final String NMS_DIR;
    public static final String OBC_DIR;
    private static final ThreadLocal<Random> randomThreadLocal;
    public static boolean legacyNettyImportMode;
    public static ServerVersion version;
    public static Constructor<?> blockPosConstructor;
    public static Constructor<?> minecraftKeyConstructor;
    public static Constructor<?> vec3DConstructor;
    public static Constructor<?> dataWatcherConstructor;
    public static Constructor<?> packetDataSerializerConstructor;
    public static Constructor<?> itemStackConstructor;
    public static Class<?> mobEffectListClass;
    public static Class<?> nmsEntityClass;
    public static Class<?> minecraftServerClass;
    public static Class<?> craftWorldClass;
    public static Class<?> playerInteractManagerClass;
    public static Class<?> entityPlayerClass;
    public static Class<?> playerConnectionClass;
    public static Class<?> craftServerClass;
    public static Class<?> craftPlayerClass;
    public static Class<?> serverConnectionClass;
    public static Class<?> craftEntityClass;
    public static Class<?> nmsItemStackClass;
    public static Class<?> networkManagerClass;
    public static Class<?> nettyChannelClass;
    public static Class<?> gameProfileClass;
    public static Class<?> iChatBaseComponentClass;
    public static Class<?> blockPosClass;
    public static Class<?> sectionPositionClass;
    public static Class<?> vec3DClass;
    public static Class<?> channelFutureClass;
    public static Class<?> blockClass;
    public static Class<?> iBlockDataClass;
    public static Class<?> nmsWorldClass;
    public static Class<?> craftItemStackClass;
    public static Class<?> soundEffectClass;
    public static Class<?> minecraftKeyClass;
    public static Class<?> chatSerializerClass;
    public static Class<?> craftMagicNumbersClass;
    public static Class<?> worldSettingsClass;
    public static Class<?> worldServerClass;
    public static Class<?> dataWatcherClass;
    public static Class<?> dedicatedServerClass;
    public static Class<?> entityHumanClass;
    public static Class<?> packetDataSerializerClass;
    public static Class<?> byteBufClass;
    public static Class<?> dimensionManagerClass;
    public static Class<?> nmsItemClass;
    public static Class<?> iMaterialClass;
    public static Class<?> movingObjectPositionBlockClass;
    public static Class<?> boundingBoxClass;
    public static Class<?> tileEntityCommandClass;
    public static Class<?> mojangEitherClass;
    public static Class<? extends Enum<?>> enumDirectionClass;
    public static Class<? extends Enum<?>> enumHandClass;
    public static Class<? extends Enum<?>> enumGameModeClass;
    public static Class<? extends Enum<?>> enumDifficultyClass;
    public static Class<? extends Enum<?>> tileEntityCommandTypeClass;
    public static Method getBlockPosX;
    public static Method getBlockPosY;
    public static Method getBlockPosZ;
    public static Method mojangEitherLeft;
    public static Method mojangEitherRight;
    private static String nettyPrefix;
    private static Method getCraftPlayerHandle;
    private static Method getCraftEntityHandle;
    private static Method getCraftWorldHandle;
    private static Method asBukkitCopy;
    private static Method asNMSCopy;
    private static Method getMessageMethod;
    private static Method chatFromStringMethod;
    private static Method getMaterialFromNMSBlock;
    private static Method getNMSBlockFromMaterial;
    private static Method getMobEffectListId;
    private static Method getMobEffectListById;
    private static Method getItemId;
    private static Method getItemById;
    private static Method getBukkitEntity;
    private static Field entityPlayerPingField;
    private static Field entityBoundingBoxField;
    private static Object minecraftServer;
    private static Object minecraftServerConnection;
    
    public static void load() {
        final String legacyNettyPrefix = "net.minecraft.util.io.netty.";
        final String newNettyPrefix = "io.netty.";
        if (NMSUtils.version.isNewerThan(ServerVersion.v_1_7_10)) {
            NMSUtils.legacyNettyImportMode = false;
            NMSUtils.nettyPrefix = newNettyPrefix;
        }
        else {
            NMSUtils.legacyNettyImportMode = true;
            NMSUtils.nettyPrefix = legacyNettyPrefix;
        }
        try {
            getNettyClass("channel.Channel");
        }
        catch (final Exception ex) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "[packetevents] Failed to locate the netty package location for your server version. Searching...");
            if (NMSUtils.legacyNettyImportMode) {
                NMSUtils.legacyNettyImportMode = false;
                NMSUtils.nettyPrefix = newNettyPrefix;
            }
            else {
                NMSUtils.legacyNettyImportMode = true;
                NMSUtils.nettyPrefix = legacyNettyPrefix;
            }
        }
        try {
            NMSUtils.byteBufClass = getNettyClass("buffer.ByteBuf");
            NMSUtils.nettyChannelClass = getNettyClass("channel.Channel");
            NMSUtils.channelFutureClass = getNettyClass("channel.ChannelFuture");
            NMSUtils.craftWorldClass = getOBCClass("CraftWorld");
            NMSUtils.craftPlayerClass = getOBCClass("entity.CraftPlayer");
            NMSUtils.craftServerClass = getOBCClass("CraftServer");
            NMSUtils.craftEntityClass = getOBCClass("entity.CraftEntity");
            NMSUtils.craftItemStackClass = getOBCClass("inventory.CraftItemStack");
        }
        catch (final ClassNotFoundException e) {
            e.printStackTrace();
        }
        NMSUtils.nmsEntityClass = getNMSClassWithoutException("Entity");
        if (NMSUtils.nmsEntityClass == null) {
            NMSUtils.nmsEntityClass = getNMClassWithoutException("world.entity.Entity");
        }
        NMSUtils.boundingBoxClass = getNMSClassWithoutException("AxisAlignedBB");
        if (NMSUtils.boundingBoxClass == null) {
            NMSUtils.boundingBoxClass = getNMClassWithoutException("world.phys.AxisAlignedBB");
        }
        NMSUtils.entityBoundingBoxField = Reflection.getField(NMSUtils.nmsEntityClass, NMSUtils.boundingBoxClass, 0, true);
        if (NMSUtils.nmsEntityClass != null) {
            NMSUtils.getBukkitEntity = Reflection.getMethod(NMSUtils.nmsEntityClass, NMSUtils.craftEntityClass, 0);
        }
        NMSUtils.minecraftServerClass = getNMSClassWithoutException("MinecraftServer");
        if (NMSUtils.minecraftServerClass == null) {
            NMSUtils.minecraftServerClass = getNMClassWithoutException("server.MinecraftServer");
        }
        NMSUtils.entityPlayerClass = getNMSClassWithoutException("EntityPlayer");
        if (NMSUtils.entityPlayerClass == null) {
            NMSUtils.entityPlayerClass = getNMClassWithoutException("server.level.EntityPlayer");
        }
        NMSUtils.entityHumanClass = getNMSClassWithoutException("EntityHuman");
        if (NMSUtils.entityHumanClass == null) {
            NMSUtils.entityHumanClass = getNMClassWithoutException("world.entity.player.EntityHuman");
        }
        NMSUtils.playerConnectionClass = getNMSClassWithoutException("PlayerConnection");
        if (NMSUtils.playerConnectionClass == null) {
            NMSUtils.playerConnectionClass = getNMClassWithoutException("server.network.PlayerConnection");
        }
        NMSUtils.serverConnectionClass = getNMSClassWithoutException("ServerConnection");
        if (NMSUtils.serverConnectionClass == null) {
            NMSUtils.serverConnectionClass = getNMClassWithoutException("server.network.ServerConnection");
        }
        NMSUtils.nmsItemStackClass = getNMSClassWithoutException("ItemStack");
        if (NMSUtils.nmsItemStackClass == null) {
            NMSUtils.nmsItemStackClass = getNMClassWithoutException("world.item.ItemStack");
        }
        NMSUtils.networkManagerClass = getNMSClassWithoutException("NetworkManager");
        if (NMSUtils.networkManagerClass == null) {
            NMSUtils.networkManagerClass = getNMClassWithoutException("network.NetworkManager");
        }
        NMSUtils.mobEffectListClass = getNMSClassWithoutException("MobEffectList");
        if (NMSUtils.mobEffectListClass == null) {
            NMSUtils.mobEffectListClass = getNMClassWithoutException("world.effect.MobEffectList");
        }
        NMSUtils.playerInteractManagerClass = getNMSClassWithoutException("PlayerInteractManager");
        if (NMSUtils.playerInteractManagerClass == null) {
            NMSUtils.playerInteractManagerClass = getNMClassWithoutException("server.level.PlayerInteractManager");
        }
        NMSUtils.blockClass = getNMSClassWithoutException("Block");
        if (NMSUtils.blockClass == null) {
            NMSUtils.blockClass = getNMClassWithoutException("world.level.block.Block");
        }
        NMSUtils.iBlockDataClass = getNMSClassWithoutException("IBlockData");
        if (NMSUtils.iBlockDataClass == null) {
            NMSUtils.iBlockDataClass = getNMClassWithoutException("world.level.block.state.IBlockData");
        }
        NMSUtils.nmsWorldClass = getNMSClassWithoutException("World");
        if (NMSUtils.nmsWorldClass == null) {
            NMSUtils.nmsWorldClass = getNMClassWithoutException("world.level.World");
        }
        NMSUtils.soundEffectClass = getNMSClassWithoutException("SoundEffect");
        if (NMSUtils.soundEffectClass == null) {
            NMSUtils.soundEffectClass = getNMClassWithoutException("sounds.SoundEffect");
        }
        NMSUtils.minecraftKeyClass = getNMSClassWithoutException("MinecraftKey");
        if (NMSUtils.minecraftKeyClass == null) {
            NMSUtils.minecraftKeyClass = getNMClassWithoutException("resources.MinecraftKey");
        }
        NMSUtils.worldServerClass = getNMSClassWithoutException("WorldServer");
        if (NMSUtils.worldServerClass == null) {
            NMSUtils.worldServerClass = getNMClassWithoutException("server.level.WorldServer");
        }
        NMSUtils.dataWatcherClass = getNMSClassWithoutException("DataWatcher");
        if (NMSUtils.dataWatcherClass == null) {
            NMSUtils.dataWatcherClass = getNMClassWithoutException("network.syncher.DataWatcher");
        }
        NMSUtils.nmsItemClass = getNMSClassWithoutException("Item");
        if (NMSUtils.nmsItemClass == null) {
            NMSUtils.nmsItemClass = getNMClassWithoutException("world.item.Item");
        }
        NMSUtils.iMaterialClass = getNMSClassWithoutException("IMaterial");
        if (NMSUtils.iMaterialClass == null) {
            NMSUtils.iMaterialClass = getNMClassWithoutException("world.level.IMaterial");
        }
        NMSUtils.dedicatedServerClass = getNMSClassWithoutException("DedicatedServer");
        if (NMSUtils.dedicatedServerClass == null) {
            NMSUtils.dedicatedServerClass = getNMClassWithoutException("server.dedicated.DedicatedServer");
        }
        NMSUtils.packetDataSerializerClass = getNMSClassWithoutException("PacketDataSerializer");
        if (NMSUtils.packetDataSerializerClass == null) {
            NMSUtils.packetDataSerializerClass = getNMClassWithoutException("network.PacketDataSerializer");
        }
        if (NMSUtils.packetDataSerializerClass != null) {
            try {
                NMSUtils.packetDataSerializerConstructor = NMSUtils.packetDataSerializerClass.getConstructor(NMSUtils.byteBufClass);
            }
            catch (final NoSuchMethodException e2) {
                e2.printStackTrace();
            }
        }
        NMSUtils.dimensionManagerClass = getNMSClassWithoutException("DimensionManager");
        if (NMSUtils.dimensionManagerClass == null) {
            NMSUtils.dimensionManagerClass = getNMClassWithoutException("world.level.dimension.DimensionManager");
        }
        try {
            NMSUtils.gameProfileClass = Class.forName("net.minecraft.util.com.mojang.authlib.GameProfile");
        }
        catch (final ClassNotFoundException e) {
            try {
                NMSUtils.gameProfileClass = Class.forName("com.mojang.authlib.GameProfile");
            }
            catch (final ClassNotFoundException e3) {
                e3.printStackTrace();
            }
        }
        NMSUtils.iChatBaseComponentClass = getNMSClassWithoutException("IChatBaseComponent");
        if (NMSUtils.iChatBaseComponentClass == null) {
            NMSUtils.iChatBaseComponentClass = getNMClassWithoutException("network.chat.IChatBaseComponent");
        }
        NMSUtils.tileEntityCommandClass = getNMSClassWithoutException("TileEntityCommand");
        if (NMSUtils.tileEntityCommandClass == null) {
            NMSUtils.tileEntityCommandClass = getNMClassWithoutException("world.level.block.entity.TileEntityCommand");
        }
        NMSUtils.tileEntityCommandTypeClass = SubclassUtil.getEnumSubClass(NMSUtils.tileEntityCommandClass, 0);
        NMSUtils.mojangEitherClass = Reflection.getClassByNameWithoutException("com.mojang.datafixers.util.Either");
        NMSUtils.vec3DClass = getNMSClassWithoutException("Vec3D");
        if (NMSUtils.vec3DClass == null) {
            NMSUtils.vec3DClass = getNMClassWithoutException("world.phys.Vec3D");
        }
        if (NMSUtils.version.isNewerThan(ServerVersion.v_1_7_10)) {
            NMSUtils.blockPosClass = getNMSClassWithoutException("BlockPosition");
            if (NMSUtils.blockPosClass == null) {
                NMSUtils.blockPosClass = getNMClassWithoutException("core.BlockPosition");
            }
        }
        NMSUtils.sectionPositionClass = getNMSClassWithoutException("SectionPosition");
        if (NMSUtils.sectionPositionClass == null) {
            NMSUtils.sectionPositionClass = getNMClassWithoutException("core.SectionPosition");
        }
        if (NMSUtils.version.isNewerThanOrEquals(ServerVersion.v_1_14)) {
            NMSUtils.movingObjectPositionBlockClass = getNMSClassWithoutException("MovingObjectPositionBlock");
            if (NMSUtils.movingObjectPositionBlockClass == null) {
                NMSUtils.movingObjectPositionBlockClass = getNMClassWithoutException("world.phys.MovingObjectPositionBlock");
            }
        }
        try {
            if (NMSUtils.blockPosClass != null) {
                NMSUtils.blockPosConstructor = NMSUtils.blockPosClass.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE);
            }
            if (NMSUtils.vec3DClass != null) {
                (NMSUtils.vec3DConstructor = NMSUtils.vec3DClass.getDeclaredConstructor(Double.TYPE, Double.TYPE, Double.TYPE)).setAccessible(true);
            }
            if (NMSUtils.dataWatcherClass != null) {
                NMSUtils.dataWatcherConstructor = NMSUtils.dataWatcherClass.getConstructor(NMSUtils.nmsEntityClass);
            }
            if (NMSUtils.nmsItemStackClass != null && NMSUtils.iMaterialClass != null) {
                NMSUtils.itemStackConstructor = NMSUtils.nmsItemStackClass.getDeclaredConstructor(NMSUtils.iMaterialClass);
            }
        }
        catch (final NoSuchMethodException e2) {
            e2.printStackTrace();
        }
        try {
            NMSUtils.getItemId = NMSUtils.nmsItemClass.getMethod("getId", NMSUtils.nmsItemClass);
            NMSUtils.getItemById = NMSUtils.nmsItemClass.getMethod("getById", Integer.TYPE);
        }
        catch (final Exception ex2) {}
        NMSUtils.enumDirectionClass = getNMSEnumClassWithoutException("EnumDirection");
        if (NMSUtils.enumDirectionClass == null) {
            NMSUtils.enumDirectionClass = getNMEnumClassWithoutException("core.EnumDirection");
        }
        try {
            NMSUtils.getCraftPlayerHandle = NMSUtils.craftPlayerClass.getMethod("getHandle", (Class<?>[])new Class[0]);
            NMSUtils.getCraftEntityHandle = NMSUtils.craftEntityClass.getMethod("getHandle", (Class<?>[])new Class[0]);
            NMSUtils.getCraftWorldHandle = NMSUtils.craftWorldClass.getMethod("getHandle", (Class<?>[])new Class[0]);
            NMSUtils.asBukkitCopy = NMSUtils.craftItemStackClass.getMethod("asBukkitCopy", NMSUtils.nmsItemStackClass);
            NMSUtils.asNMSCopy = NMSUtils.craftItemStackClass.getMethod("asNMSCopy", ItemStack.class);
            NMSUtils.getMessageMethod = Reflection.getMethodCheckContainsString(NMSUtils.iChatBaseComponentClass, "c", String.class);
            if (NMSUtils.getMessageMethod == null) {
                NMSUtils.getMessageMethod = Reflection.getMethodCheckContainsString(NMSUtils.iChatBaseComponentClass, "Plain", String.class);
                if (NMSUtils.getMessageMethod == null) {
                    NMSUtils.getMessageMethod = Reflection.getMethodCheckContainsString(NMSUtils.iChatBaseComponentClass, "String", String.class);
                }
            }
            try {
                NMSUtils.chatSerializerClass = getNMSClass("ChatSerializer");
            }
            catch (final ClassNotFoundException e) {
                NMSUtils.chatSerializerClass = SubclassUtil.getSubClass(NMSUtils.iChatBaseComponentClass, "ChatSerializer");
            }
            NMSUtils.craftMagicNumbersClass = getOBCClass("util.CraftMagicNumbers");
            NMSUtils.chatFromStringMethod = Reflection.getMethod(NMSUtils.chatSerializerClass, 0, String.class);
            NMSUtils.getMaterialFromNMSBlock = Reflection.getMethod(NMSUtils.craftMagicNumbersClass, "getMaterial", Material.class, NMSUtils.blockClass);
            NMSUtils.getNMSBlockFromMaterial = Reflection.getMethod(NMSUtils.craftMagicNumbersClass, "getBlock", NMSUtils.blockClass, Material.class);
            if (NMSUtils.minecraftKeyClass != null) {
                NMSUtils.minecraftKeyConstructor = NMSUtils.minecraftKeyClass.getConstructor(String.class);
            }
        }
        catch (final Exception e4) {
            e4.printStackTrace();
        }
        try {
            if (NMSUtils.mobEffectListClass != null) {
                NMSUtils.getMobEffectListId = Reflection.getMethod(NMSUtils.mobEffectListClass, 0, NMSUtils.mobEffectListClass);
                NMSUtils.getMobEffectListById = Reflection.getMethod(NMSUtils.mobEffectListClass, 0, Integer.TYPE);
            }
        }
        catch (final Exception ex) {
            ex.printStackTrace();
        }
        try {
            NMSUtils.entityPlayerPingField = NMSUtils.entityPlayerClass.getField("ping");
        }
        catch (final NoSuchFieldException ex3) {}
        if (NMSUtils.blockPosClass != null) {
            NMSUtils.getBlockPosX = Reflection.getMethod(NMSUtils.blockPosClass, "getX", Integer.TYPE);
            NMSUtils.getBlockPosY = Reflection.getMethod(NMSUtils.blockPosClass, "getY", Integer.TYPE);
            NMSUtils.getBlockPosZ = Reflection.getMethod(NMSUtils.blockPosClass, "getZ", Integer.TYPE);
            if (NMSUtils.getBlockPosX == null) {
                NMSUtils.getBlockPosX = Reflection.getMethod(NMSUtils.blockPosClass, "u", Integer.TYPE);
            }
            if (NMSUtils.getBlockPosY == null) {
                NMSUtils.getBlockPosY = Reflection.getMethod(NMSUtils.blockPosClass, "v", Integer.TYPE);
            }
            if (NMSUtils.getBlockPosZ == null) {
                NMSUtils.getBlockPosZ = Reflection.getMethod(NMSUtils.blockPosClass, "w", Integer.TYPE);
            }
        }
        if (NMSUtils.mojangEitherClass != null) {
            NMSUtils.mojangEitherLeft = Reflection.getMethod(NMSUtils.mojangEitherClass, "left", Optional.class);
            NMSUtils.mojangEitherRight = Reflection.getMethod(NMSUtils.mojangEitherClass, "right", Optional.class);
        }
        NMSUtils.worldSettingsClass = getNMSClassWithoutException("WorldSettings");
        if (NMSUtils.worldServerClass == null) {
            NMSUtils.worldServerClass = getNMClassWithoutException("world.level.WorldSettings");
        }
        NMSUtils.enumHandClass = getNMSEnumClassWithoutException("EnumHand");
        if (NMSUtils.enumHandClass == null) {
            NMSUtils.enumHandClass = getNMEnumClassWithoutException("world.EnumHand");
        }
        NMSUtils.enumDifficultyClass = getNMSEnumClassWithoutException("EnumDifficulty");
        if (NMSUtils.enumDifficultyClass == null) {
            NMSUtils.enumDifficultyClass = getNMEnumClassWithoutException("world.EnumDifficulty");
        }
        NMSUtils.enumGameModeClass = getNMSEnumClassWithoutException("EnumGamemode");
        if (NMSUtils.enumGameModeClass == null) {
            NMSUtils.enumGameModeClass = SubclassUtil.getEnumSubClass(NMSUtils.worldSettingsClass, "EnumGamemode");
        }
        if (NMSUtils.enumGameModeClass == null) {
            NMSUtils.enumGameModeClass = getNMEnumClassWithoutException("world.level.EnumGamemode");
        }
    }
    
    public static Object getMinecraftServerInstance(final Server server) {
        if (NMSUtils.minecraftServer == null) {
            try {
                NMSUtils.minecraftServer = Reflection.getField(NMSUtils.craftServerClass, NMSUtils.minecraftServerClass, 0).get(server);
            }
            catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return NMSUtils.minecraftServer;
    }
    
    public static Object getMinecraftServerConnection() {
        if (NMSUtils.minecraftServerConnection == null) {
            try {
                NMSUtils.minecraftServerConnection = Reflection.getField(NMSUtils.minecraftServerClass, NMSUtils.serverConnectionClass, 0).get(getMinecraftServerInstance(Bukkit.getServer()));
            }
            catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return NMSUtils.minecraftServerConnection;
    }
    
    public static double[] recentTPS() {
        final WrappedPacket minecraftServerInstanceReader = new WrappedPacket(new NMSPacket(getMinecraftServerInstance(Bukkit.getServer())), NMSUtils.minecraftServerClass);
        return minecraftServerInstanceReader.readDoubleArray(0);
    }
    
    public static Class<?> getNMSClass(final String name) throws ClassNotFoundException {
        return Class.forName(NMSUtils.NMS_DIR + name);
    }
    
    public static Class<?> getNMClass(final String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft." + name);
    }
    
    public static Class<?> getNMClassWithoutException(final String name) {
        try {
            return Class.forName("net.minecraft." + name);
        }
        catch (final ClassNotFoundException ex) {
            return null;
        }
    }
    
    public static Class<? extends Enum<?>> getNMEnumClassWithoutException(final String name) {
        try {
            return (Class<? extends Enum<?>>)Class.forName("net.minecraft." + name);
        }
        catch (final ClassNotFoundException ex) {
            return null;
        }
    }
    
    public static Class<? extends Enum<?>> getNMSEnumClass(final String name) throws ClassNotFoundException {
        return (Class<? extends Enum<?>>)Class.forName(NMSUtils.NMS_DIR + name);
    }
    
    public static Class<? extends Enum<?>> getNMSEnumClassWithoutException(final String name) {
        try {
            return (Class<? extends Enum<?>>)getNMSClass(name);
        }
        catch (final Exception e) {
            return null;
        }
    }
    
    public static Class<?> getNMSClassWithoutException(final String name) {
        try {
            return getNMSClass(name);
        }
        catch (final Exception e) {
            return null;
        }
    }
    
    public static Class<?> getOBCClass(final String name) throws ClassNotFoundException {
        return Class.forName(NMSUtils.OBC_DIR + name);
    }
    
    public static Class<?> getNettyClass(final String name) throws ClassNotFoundException {
        return Class.forName(NMSUtils.nettyPrefix + name);
    }
    
    @Deprecated
    @Nullable
    public static Entity getEntityById(@Nullable final World world, final int id) {
        return PacketEvents.get().getServerUtils().getEntityById(world, id);
    }
    
    public static Entity getBukkitEntity(final Object nmsEntity) {
        Object craftEntity = null;
        try {
            craftEntity = NMSUtils.getBukkitEntity.invoke(nmsEntity, new Object[0]);
        }
        catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return (Entity)craftEntity;
    }
    
    public static Object getNMSEntity(final Entity entity) {
        final Object craftEntity = NMSUtils.craftEntityClass.cast(entity);
        try {
            return NMSUtils.getCraftEntityHandle.invoke(craftEntity, new Object[0]);
        }
        catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object getNMSAxisAlignedBoundingBox(final Object nmsEntity) {
        try {
            return NMSUtils.entityBoundingBoxField.get(NMSUtils.nmsEntityClass.cast(nmsEntity));
        }
        catch (final IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object getCraftPlayer(final Player player) {
        return NMSUtils.craftPlayerClass.cast(player);
    }
    
    public static Object getEntityPlayer(final Player player) {
        final Object craftPlayer = getCraftPlayer(player);
        try {
            return NMSUtils.getCraftPlayerHandle.invoke(craftPlayer, new Object[0]);
        }
        catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object getPlayerConnection(final Player player) {
        final Object entityPlayer = getEntityPlayer(player);
        if (entityPlayer == null) {
            return null;
        }
        final WrappedPacket wrappedEntityPlayer = new WrappedPacket(new NMSPacket(entityPlayer));
        return wrappedEntityPlayer.readObject(0, NMSUtils.playerConnectionClass);
    }
    
    public static Object getGameProfile(final Player player) {
        final Object entityPlayer = getEntityPlayer(player);
        final WrappedPacket entityHumanWrapper = new WrappedPacket(new NMSPacket(entityPlayer), NMSUtils.entityHumanClass);
        return entityHumanWrapper.readObject(0, NMSUtils.gameProfileClass);
    }
    
    public static Object getNetworkManager(final Player player) {
        Object playerConnection = getPlayerConnection(player);
        if (playerConnection == null) {
            return null;
        }
        WrappedPacket wrapper = new WrappedPacket(new NMSPacket(playerConnection), NMSUtils.playerConnectionClass);
        try {
            return wrapper.readObject(0, NMSUtils.networkManagerClass);
        }
        catch (final Exception ex) {
            wrapper = new WrappedPacket(new NMSPacket(playerConnection));
            try {
                return wrapper.readObject(0, NMSUtils.networkManagerClass);
            }
            catch (final Exception ex2) {
                playerConnection = wrapper.read(0, NMSUtils.playerConnectionClass);
                wrapper = new WrappedPacket(new NMSPacket(playerConnection), NMSUtils.playerConnectionClass);
                return wrapper.readObject(0, NMSUtils.networkManagerClass);
            }
        }
    }
    
    public static Object getChannel(final Player player) {
        final Object networkManager = getNetworkManager(player);
        if (networkManager == null) {
            return null;
        }
        final WrappedPacket wrapper = new WrappedPacket(new NMSPacket(networkManager), NMSUtils.networkManagerClass);
        return wrapper.readObject(0, NMSUtils.nettyChannelClass);
    }
    
    public static int getPlayerPing(final Player player) {
        if (NMSUtils.entityPlayerPingField == null) {
            return PlayerAPIModern.getPing(player);
        }
        final Object entityPlayer = getEntityPlayer(player);
        try {
            return NMSUtils.entityPlayerPingField.getInt(entityPlayer);
        }
        catch (final IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    public static List<Object> getNetworkManagers() {
        final WrappedPacket serverConnectionWrapper = new WrappedPacket(new NMSPacket(getMinecraftServerConnection()));
        int i = 0;
        while (true) {
            try {
                final List<?> list = serverConnectionWrapper.readObject(i, (Class<? extends List<?>>)List.class);
                for (final Object obj : list) {
                    if (obj.getClass().isAssignableFrom(NMSUtils.networkManagerClass)) {
                        return (List<Object>)list;
                    }
                }
            }
            catch (final Exception ex) {
                break;
            }
            ++i;
        }
        return serverConnectionWrapper.readObject(1, (Class<? extends List<Object>>)List.class);
    }
    
    public static ItemStack toBukkitItemStack(final Object nmsItemStack) {
        try {
            return (ItemStack)NMSUtils.asBukkitCopy.invoke(null, nmsItemStack);
        }
        catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object toNMSItemStack(final ItemStack stack) {
        try {
            return NMSUtils.asNMSCopy.invoke(null, stack);
        }
        catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object convertBukkitServerToNMSServer(final Server server) {
        final Object craftServer = NMSUtils.craftServerClass.cast(server);
        final WrappedPacket wrapper = new WrappedPacket(new NMSPacket(craftServer));
        try {
            return wrapper.readObject(0, NMSUtils.minecraftServerClass);
        }
        catch (final Exception ex) {
            wrapper.readObject(0, NMSUtils.dedicatedServerClass);
            return null;
        }
    }
    
    public static Object convertBukkitWorldToWorldServer(final World world) {
        final Object craftWorld = NMSUtils.craftWorldClass.cast(world);
        try {
            return NMSUtils.getCraftWorldHandle.invoke(craftWorld, new Object[0]);
        }
        catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Nullable
    public static Object generateIChatBaseComponent(final String text) {
        if (text == null) {
            return null;
        }
        try {
            return NMSUtils.chatFromStringMethod.invoke(null, text);
        }
        catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object[] generateIChatBaseComponents(final String... texts) {
        final Object[] components = new Object[texts.length];
        for (int i = 0; i < components.length; ++i) {
            components[i] = generateIChatBaseComponent(texts[i]);
        }
        return components;
    }
    
    @Nullable
    public static String readIChatBaseComponent(final Object iChatBaseComponent) {
        if (iChatBaseComponent == null) {
            return null;
        }
        try {
            return NMSUtils.getMessageMethod.invoke(iChatBaseComponent, new Object[0]).toString();
        }
        catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String[] readIChatBaseComponents(final Object... components) {
        final String[] texts = new String[components.length];
        for (int i = 0; i < texts.length; ++i) {
            texts[i] = readIChatBaseComponent(components[i]);
        }
        return texts;
    }
    
    public static String fromStringToJSON(final String message) {
        if (message == null) {
            return null;
        }
        return "{\"text\": \"" + message + "\"}";
    }
    
    public static Object generateNMSBlockPos(final Vector3i blockPosition) {
        try {
            return NMSUtils.blockPosConstructor.newInstance(blockPosition.x, blockPosition.y, blockPosition.z);
        }
        catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String getStringFromMinecraftKey(final Object minecraftKey) {
        final WrappedPacket minecraftKeyWrapper = new WrappedPacket(new NMSPacket(minecraftKey));
        return minecraftKeyWrapper.readString(1);
    }
    
    public static String[] splitMinecraftKey(final String var0, final char var1) {
        final String[] array = { "minecraft", var0 };
        final int index = var0.indexOf(var1);
        if (index >= 0) {
            array[1] = var0.substring(index + 1);
            if (index >= 1) {
                array[0] = var0.substring(0, index);
            }
        }
        return array;
    }
    
    public static Object generateMinecraftKeyNew(final String text) {
        try {
            return NMSUtils.minecraftKeyConstructor.newInstance(text);
        }
        catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object generateVec3D(final Vector3f vector) {
        return generateVec3D(vector.x, vector.y, vector.z);
    }
    
    public static Object generateVec3D(final Vector3d vector) {
        return generateVec3D(vector.x, vector.y, vector.z);
    }
    
    public static Object generateVec3D(final double x, final double y, final double z) {
        try {
            return NMSUtils.vec3DConstructor.newInstance(x, y, z);
        }
        catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Material getMaterialFromNMSBlock(final Object nmsBlock) {
        try {
            return (Material)NMSUtils.getMaterialFromNMSBlock.invoke(null, nmsBlock);
        }
        catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object getNMSBlockFromMaterial(final Material material) {
        try {
            return NMSUtils.getNMSBlockFromMaterial.invoke(null, material);
        }
        catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object generateDataWatcher(final Object nmsEntity) {
        try {
            return NMSUtils.dataWatcherConstructor.newInstance(nmsEntity);
        }
        catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static UUID generateUUID() {
        final long var1 = (NMSUtils.randomThreadLocal.get().nextLong() & 0xFFFFFFFFFFFF0FFFL) | 0x4000L;
        final long var2 = (NMSUtils.randomThreadLocal.get().nextLong() & 0x3FFFFFFFFFFFFFFFL) | Long.MIN_VALUE;
        return new UUID(var1, var2);
    }
    
    public static int generateEntityId() {
        Field field = Reflection.getField(NMSUtils.nmsEntityClass, "entityCount");
        if (field == null) {
            field = Reflection.getField(NMSUtils.nmsEntityClass, AtomicInteger.class, 0);
        }
        try {
            if (field.getType().equals(AtomicInteger.class)) {
                final AtomicInteger atomicInteger = (AtomicInteger)field.get(null);
                return atomicInteger.incrementAndGet();
            }
            final int id = field.getInt(null) + 1;
            field.set(null, id);
            return id;
        }
        catch (final IllegalAccessException ex) {
            ex.printStackTrace();
            throw new IllegalStateException("Failed to generate a new unique entity ID!");
        }
    }
    
    public static int getEffectId(final Object nmsMobEffectList) {
        try {
            return (int)NMSUtils.getMobEffectListId.invoke(null, nmsMobEffectList);
        }
        catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    public static Object getMobEffectListById(final int effectID) {
        try {
            return NMSUtils.getMobEffectListById.invoke(null, effectID);
        }
        catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static int getNMSItemId(final Object nmsItem) {
        try {
            return (int)NMSUtils.getItemId.invoke(null, nmsItem);
        }
        catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    public static Object getNMSItemById(final int id) {
        try {
            return NMSUtils.getItemById.invoke(null, id);
        }
        catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object generatePacketDataSerializer(final Object byteBuf) {
        try {
            return NMSUtils.packetDataSerializerConstructor.newInstance(byteBuf);
        }
        catch (final InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Optional<Object> getEitherLeft(final Object either) {
        try {
            return (Optional)NMSUtils.mojangEitherLeft.invoke(either, new Object[0]);
        }
        catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Optional<Object> getEitherRight(final Object either) {
        try {
            return (Optional)NMSUtils.mojangEitherRight.invoke(either, new Object[0]);
        }
        catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    static {
        NMS_DIR = ServerVersion.getNMSDirectory() + ".";
        OBC_DIR = ServerVersion.getOBCDirectory() + ".";
        randomThreadLocal = ThreadLocal.withInitial((Supplier<? extends Random>)Random::new);
    }
}
