package xyz.edge.ac.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Bukkit;
import java.util.Arrays;
import org.bukkit.Server;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class SlotSetter
{
    private static final String CRAFT_BUKKIT_PACKAGE;
    private static final String NET_MINECRAFT_SERVER_PACKAGE;
    private static final Class CRAFT_SERVER_CLASS;
    private static final Method CRAFT_SERVER_GET_HANDLE_METHOD;
    private static final Class PLAYER_LIST_CLASS;
    private static final Field PLAYER_LIST_MAX_PLAYERS_FIELD;
    private static final Class CRAFT_PLAYER_CLASS;
    private static final Method CRAFT_PLAYER_GET_HANDLE_METHOD;
    private static final Class ENTITY_PLAYER_CLASS;
    private static final Field ENTITY_PLAYER_PING_FIELD;
    private static final Class CRAFT_ITEM_STACK_CLASS;
    private static final Method CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD;
    private static final Class ENTITY_ITEM_STACK_CLASS;
    private static final Method ENTITY_ITEM_STACK_GET_NAME;
    private static final Class SPIGOT_CONFIG_CLASS;
    private static final Field SPIGOT_CONFIG_BUNGEE_FIELD;
    
    public static void setMaxPlayers(final Server server, final int slots) {
        try {
            SlotSetter.PLAYER_LIST_MAX_PLAYERS_FIELD.set(SlotSetter.CRAFT_SERVER_GET_HANDLE_METHOD.invoke(server, new Object[0]), slots);
        }
        catch (final Exception e) {
            SystemLogsUtil.createNewLog(Arrays.toString(e.getStackTrace()), "SlotSetter (setMaxPlayers)", e.getMessage());
        }
    }
    
    private SlotSetter() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    static {
        try {
            final String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
            CRAFT_BUKKIT_PACKAGE = "org.bukkit.craftbukkit." + version + ".";
            NET_MINECRAFT_SERVER_PACKAGE = "net.minecraft.server." + version + ".";
            CRAFT_SERVER_CLASS = Class.forName(SlotSetter.CRAFT_BUKKIT_PACKAGE + "CraftServer");
            (CRAFT_SERVER_GET_HANDLE_METHOD = SlotSetter.CRAFT_SERVER_CLASS.getDeclaredMethod("getHandle", (Class[])new Class[0])).setAccessible(true);
            PLAYER_LIST_CLASS = Class.forName(SlotSetter.NET_MINECRAFT_SERVER_PACKAGE + "PlayerList");
            (PLAYER_LIST_MAX_PLAYERS_FIELD = SlotSetter.PLAYER_LIST_CLASS.getDeclaredField("maxPlayers")).setAccessible(true);
            CRAFT_PLAYER_CLASS = Class.forName(SlotSetter.CRAFT_BUKKIT_PACKAGE + "entity.CraftPlayer");
            (CRAFT_PLAYER_GET_HANDLE_METHOD = SlotSetter.CRAFT_PLAYER_CLASS.getDeclaredMethod("getHandle", (Class[])new Class[0])).setAccessible(true);
            ENTITY_PLAYER_CLASS = Class.forName(SlotSetter.NET_MINECRAFT_SERVER_PACKAGE + "EntityPlayer");
            (ENTITY_PLAYER_PING_FIELD = SlotSetter.ENTITY_PLAYER_CLASS.getDeclaredField("ping")).setAccessible(true);
            CRAFT_ITEM_STACK_CLASS = Class.forName(SlotSetter.CRAFT_BUKKIT_PACKAGE + "inventory.CraftItemStack");
            (CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD = SlotSetter.CRAFT_ITEM_STACK_CLASS.getDeclaredMethod("asNMSCopy", ItemStack.class)).setAccessible(true);
            ENTITY_ITEM_STACK_CLASS = Class.forName(SlotSetter.NET_MINECRAFT_SERVER_PACKAGE + "ItemStack");
            ENTITY_ITEM_STACK_GET_NAME = SlotSetter.ENTITY_ITEM_STACK_CLASS.getDeclaredMethod("getName", (Class[])new Class[0]);
            SPIGOT_CONFIG_CLASS = Class.forName("org.spigotmc.SpigotConfig");
            (SPIGOT_CONFIG_BUNGEE_FIELD = SlotSetter.SPIGOT_CONFIG_CLASS.getDeclaredField("bungee")).setAccessible(true);
        }
        catch (final Exception e) {
            SystemLogsUtil.createNewLog(Arrays.toString(e.getStackTrace()), "SlotSetter (Failed to initialize Bukkit/NMS Reflection)", e.getMessage());
            throw new RuntimeException("Failed to initialize Bukkit/NMS Reflection");
        }
    }
}
