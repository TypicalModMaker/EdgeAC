package xyz.edge.ac.packetevents.packettype;

import xyz.edge.ac.packetevents.utils.reflection.SubclassUtil;
import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.PacketEvents;

public static class Client
{
    private static String COMMON_PREFIX;
    private static String PREFIX;
    public static Class<?> FLYING;
    public static Class<?> POSITION;
    public static Class<?> POSITION_LOOK;
    public static Class<?> LOOK;
    public static Class<?> GROUND;
    public static Class<?> CLIENT_COMMAND;
    public static Class<?> TRANSACTION;
    public static Class<?> BLOCK_DIG;
    public static Class<?> ENTITY_ACTION;
    public static Class<?> USE_ENTITY;
    public static Class<?> WINDOW_CLICK;
    public static Class<?> STEER_VEHICLE;
    public static Class<?> CUSTOM_PAYLOAD;
    public static Class<?> ARM_ANIMATION;
    public static Class<?> BLOCK_PLACE;
    public static Class<?> USE_ITEM;
    public static Class<?> ABILITIES;
    public static Class<?> HELD_ITEM_SLOT;
    public static Class<?> CLOSE_WINDOW;
    public static Class<?> TAB_COMPLETE;
    public static Class<?> CHAT;
    public static Class<?> SET_CREATIVE_SLOT;
    public static Class<?> KEEP_ALIVE;
    public static Class<?> SETTINGS;
    public static Class<?> ENCHANT_ITEM;
    public static Class<?> TELEPORT_ACCEPT;
    public static Class<?> TILE_NBT_QUERY;
    public static Class<?> DIFFICULTY_CHANGE;
    public static Class<?> B_EDIT;
    public static Class<?> ENTITY_NBT_QUERY;
    public static Class<?> JIGSAW_GENERATE;
    public static Class<?> DIFFICULTY_LOCK;
    public static Class<?> VEHICLE_MOVE;
    public static Class<?> BOAT_MOVE;
    public static Class<?> PICK_ITEM;
    public static Class<?> AUTO_RECIPE;
    public static Class<?> RECIPE_DISPLAYED;
    public static Class<?> ITEM_NAME;
    public static Class<?> RESOURCE_PACK_STATUS;
    public static Class<?> ADVANCEMENTS;
    public static Class<?> TR_SEL;
    public static Class<?> BEACON;
    public static Class<?> SET_COMMAND_BLOCK;
    public static Class<?> SET_COMMAND_MINECART;
    public static Class<?> SET_JIGSAW;
    public static Class<?> STRUCT;
    public static Class<?> UPDATE_SIGN;
    public static Class<?> SPECTATE;
    public static Class<?> PONG;
    
    public static void load() {
        if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_17)) {
            Client.PREFIX = "net.minecraft.network.protocol.game.";
        }
        else {
            Client.PREFIX = ServerVersion.getNMSDirectory() + ".";
        }
        Client.COMMON_PREFIX = Client.PREFIX + "PacketPlayIn";
        Client.FLYING = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "Flying");
        try {
            Client.POSITION = Class.forName(Client.COMMON_PREFIX + "Position");
            Client.POSITION_LOOK = Class.forName(Client.COMMON_PREFIX + "PositionLook");
            Client.LOOK = Class.forName(Client.COMMON_PREFIX + "Look");
        }
        catch (final ClassNotFoundException ex) {
            Client.POSITION = SubclassUtil.getSubClass(Client.FLYING, "PacketPlayInPosition");
            Client.POSITION_LOOK = SubclassUtil.getSubClass(Client.FLYING, "PacketPlayInPositionLook");
            Client.LOOK = SubclassUtil.getSubClass(Client.FLYING, "PacketPlayInLook");
        }
        if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_17)) {
            Client.GROUND = SubclassUtil.getSubClass(Client.FLYING, "d");
        }
        else {
            Client.GROUND = Client.FLYING;
        }
        Client.TRANSACTION = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "Transaction");
        Client.PONG = Reflection.getClassByNameWithoutException(Client.PREFIX + "ServerboundPongPacket");
        try {
            Client.SETTINGS = Class.forName(Client.COMMON_PREFIX + "Settings");
            Client.ENCHANT_ITEM = Class.forName(Client.COMMON_PREFIX + "EnchantItem");
            Client.CLIENT_COMMAND = Class.forName(Client.COMMON_PREFIX + "ClientCommand");
            Client.BLOCK_DIG = Class.forName(Client.COMMON_PREFIX + "BlockDig");
            Client.ENTITY_ACTION = Class.forName(Client.COMMON_PREFIX + "EntityAction");
            Client.USE_ENTITY = Class.forName(Client.COMMON_PREFIX + "UseEntity");
            Client.WINDOW_CLICK = Class.forName(Client.COMMON_PREFIX + "WindowClick");
            Client.STEER_VEHICLE = Class.forName(Client.COMMON_PREFIX + "SteerVehicle");
            Client.CUSTOM_PAYLOAD = Class.forName(Client.COMMON_PREFIX + "CustomPayload");
            Client.ARM_ANIMATION = Class.forName(Client.COMMON_PREFIX + "ArmAnimation");
            Client.ABILITIES = Class.forName(Client.COMMON_PREFIX + "Abilities");
            Client.HELD_ITEM_SLOT = Class.forName(Client.COMMON_PREFIX + "HeldItemSlot");
            Client.CLOSE_WINDOW = Class.forName(Client.COMMON_PREFIX + "CloseWindow");
            Client.TAB_COMPLETE = Class.forName(Client.COMMON_PREFIX + "TabComplete");
            Client.CHAT = Class.forName(Client.COMMON_PREFIX + "Chat");
            Client.SET_CREATIVE_SLOT = Class.forName(Client.COMMON_PREFIX + "SetCreativeSlot");
            Client.KEEP_ALIVE = Class.forName(Client.COMMON_PREFIX + "KeepAlive");
            Client.UPDATE_SIGN = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "UpdateSign");
            Client.TELEPORT_ACCEPT = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "TeleportAccept");
            Client.TILE_NBT_QUERY = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "TileNBTQuery");
            Client.DIFFICULTY_CHANGE = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "DifficultyChange");
            Client.B_EDIT = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "BEdit");
            Client.ENTITY_NBT_QUERY = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "EntityNBTQuery");
            Client.JIGSAW_GENERATE = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "JigsawGenerate");
            Client.DIFFICULTY_LOCK = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "DifficultyLock");
            Client.VEHICLE_MOVE = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "VehicleMove");
            Client.BOAT_MOVE = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "BoatMove");
            Client.PICK_ITEM = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "PickItem");
            Client.AUTO_RECIPE = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "AutoRecipe");
            Client.RECIPE_DISPLAYED = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "RecipeDisplayed");
            Client.ITEM_NAME = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "ItemName");
            Client.RESOURCE_PACK_STATUS = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "ResourcePackStatus");
            Client.ADVANCEMENTS = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "Advancements");
            Client.TR_SEL = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "TrSel");
            Client.BEACON = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "Beacon");
            Client.SET_COMMAND_BLOCK = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "SetCommandBlock");
            Client.SET_COMMAND_MINECART = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "SetCommandMinecart");
            Client.SET_JIGSAW = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "SetJigsaw");
            Client.STRUCT = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "Struct");
            Client.SPECTATE = Reflection.getClassByNameWithoutException(Client.COMMON_PREFIX + "Spectate");
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        try {
            Client.BLOCK_PLACE = Class.forName(Client.COMMON_PREFIX + "BlockPlace");
            Client.USE_ITEM = Class.forName(Client.COMMON_PREFIX + "UseItem");
        }
        catch (final Exception ex2) {}
    }
}
