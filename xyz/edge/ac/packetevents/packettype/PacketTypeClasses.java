package xyz.edge.ac.packetevents.packettype;

import xyz.edge.ac.packetevents.utils.reflection.SubclassUtil;
import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.PacketEvents;

public class PacketTypeClasses
{
    public static void load() {
        Status.Client.load();
        Status.Server.load();
        Handshaking.Client.load();
        Login.Client.load();
        Login.Server.load();
        Play.Client.load();
        Play.Server.load();
    }
    
    public static class Status
    {
        public static class Client
        {
            private static String PREFIX;
            public static Class<?> START;
            public static Class<?> PING;
            
            public static void load() {
                if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_17)) {
                    Client.PREFIX = "net.minecraft.network.protocol.status.";
                }
                else {
                    Client.PREFIX = ServerVersion.getNMSDirectory() + ".";
                }
                Client.START = Reflection.getClassByNameWithoutException(Client.PREFIX + "PacketStatusInStart");
                Client.PING = Reflection.getClassByNameWithoutException(Client.PREFIX + "PacketStatusInPing");
            }
        }
        
        public static class Server
        {
            private static String PREFIX;
            public static Class<?> PONG;
            public static Class<?> SERVER_INFO;
            
            public static void load() {
                if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_17)) {
                    Server.PREFIX = "net.minecraft.network.protocol.status.";
                }
                else {
                    Server.PREFIX = ServerVersion.getNMSDirectory() + ".";
                }
                Server.PONG = Reflection.getClassByNameWithoutException(Server.PREFIX + "PacketStatusOutPong");
                Server.SERVER_INFO = Reflection.getClassByNameWithoutException(Server.PREFIX + "PacketStatusOutServerInfo");
            }
        }
    }
    
    public static class Handshaking
    {
        public static class Client
        {
            private static String PREFIX;
            public static Class<?> SET_PROTOCOL;
            
            public static void load() {
                if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_17)) {
                    Client.PREFIX = "net.minecraft.network.protocol.handshake.";
                }
                else {
                    Client.PREFIX = ServerVersion.getNMSDirectory() + ".";
                }
                Client.SET_PROTOCOL = Reflection.getClassByNameWithoutException(Client.PREFIX + "PacketHandshakingInSetProtocol");
            }
        }
    }
    
    public static class Login
    {
        public static class Client
        {
            private static String PREFIX;
            public static Class<?> CUSTOM_PAYLOAD;
            public static Class<?> START;
            public static Class<?> ENCRYPTION_BEGIN;
            
            public static void load() {
                if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_17)) {
                    Client.PREFIX = "net.minecraft.network.protocol.login.";
                }
                else {
                    Client.PREFIX = ServerVersion.getNMSDirectory() + ".";
                }
                if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_13)) {
                    Client.CUSTOM_PAYLOAD = Reflection.getClassByNameWithoutException(Client.PREFIX + "PacketLoginInCustomPayload");
                }
                Client.START = Reflection.getClassByNameWithoutException(Client.PREFIX + "PacketLoginInStart");
                Client.ENCRYPTION_BEGIN = Reflection.getClassByNameWithoutException(Client.PREFIX + "PacketLoginInEncryptionBegin");
            }
        }
        
        public static class Server
        {
            private static String PREFIX;
            public static Class<?> CUSTOM_PAYLOAD;
            public static Class<?> DISCONNECT;
            public static Class<?> ENCRYPTION_BEGIN;
            public static Class<?> SUCCESS;
            public static Class<?> SET_COMPRESSION;
            
            public static void load() {
                if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_17)) {
                    Server.PREFIX = "net.minecraft.network.protocol.login.";
                }
                else {
                    Server.PREFIX = ServerVersion.getNMSDirectory() + ".";
                }
                if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_13)) {
                    Server.CUSTOM_PAYLOAD = Reflection.getClassByNameWithoutException(Server.PREFIX + "PacketLoginOutCustomPayload");
                }
                Server.DISCONNECT = Reflection.getClassByNameWithoutException(Server.PREFIX + "PacketLoginOutDisconnect");
                Server.ENCRYPTION_BEGIN = Reflection.getClassByNameWithoutException(Server.PREFIX + "PacketLoginOutEncryptionBegin");
                Server.SUCCESS = Reflection.getClassByNameWithoutException(Server.PREFIX + "PacketLoginOutSuccess");
                if (PacketEvents.get().getServerUtils().getVersion().isNewerThan(ServerVersion.v_1_7_10)) {
                    Server.SET_COMPRESSION = Reflection.getClassByNameWithoutException(Server.PREFIX + "PacketLoginOutSetCompression");
                }
            }
        }
    }
    
    public static class Play
    {
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
        
        public static class Server
        {
            private static String COMMON_PREFIX;
            private static String PREFIX;
            public static Class<?> SPAWN_ENTITY;
            public static Class<?> SPAWN_ENTITY_EXPERIENCE_ORB;
            public static Class<?> SPAWN_ENTITY_WEATHER;
            public static Class<?> SPAWN_ENTITY_LIVING;
            public static Class<?> SPAWN_ENTITY_PAINTING;
            public static Class<?> SPAWN_ENTITY_SPAWN;
            public static Class<?> ANIMATION;
            public static Class<?> STATISTIC;
            public static Class<?> BLOCK_BREAK;
            public static Class<?> BLOCK_BREAK_ANIMATION;
            public static Class<?> TILE_ENTITY_DATA;
            public static Class<?> BLOCK_ACTION;
            public static Class<?> BLOCK_CHANGE;
            public static Class<?> BOSS;
            public static Class<?> SERVER_DIFFICULTY;
            public static Class<?> CHAT;
            public static Class<?> MULTI_BLOCK_CHANGE;
            public static Class<?> TAB_COMPLETE;
            public static Class<?> COMMANDS;
            public static Class<?> TRANSACTION;
            public static Class<?> CLOSE_WINDOW;
            public static Class<?> WINDOW_ITEMS;
            public static Class<?> WINDOW_DATA;
            public static Class<?> SET_SLOT;
            public static Class<?> SET_COOLDOWN;
            public static Class<?> CUSTOM_PAYLOAD;
            public static Class<?> CUSTOM_SOUND_EFFECT;
            public static Class<?> KICK_DISCONNECT;
            public static Class<?> ENTITY_STATUS;
            public static Class<?> EXPLOSION;
            public static Class<?> UNLOAD_CHUNK;
            public static Class<?> GAME_STATE_CHANGE;
            public static Class<?> OPEN_WINDOW_HORSE;
            public static Class<?> KEEP_ALIVE;
            public static Class<?> MAP_CHUNK;
            public static Class<?> WORLD_EVENT;
            public static Class<?> WORLD_PARTICLES;
            public static Class<?> LIGHT_UPDATE;
            public static Class<?> LOGIN;
            public static Class<?> MAP;
            public static Class<?> OPEN_WINDOW_MERCHANT;
            public static Class<?> REL_ENTITY_MOVE;
            public static Class<?> REL_ENTITY_MOVE_LOOK;
            public static Class<?> ENTITY_LOOK;
            public static Class<?> ENTITY;
            public static Class<?> VEHICLE_MOVE;
            public static Class<?> OPEN_BOOK;
            public static Class<?> OPEN_WINDOW;
            public static Class<?> OPEN_SIGN_EDITOR;
            public static Class<?> AUTO_RECIPE;
            public static Class<?> ABILITIES;
            public static Class<?> COMBAT_EVENT;
            public static Class<?> PLAYER_INFO;
            public static Class<?> LOOK_AT;
            public static Class<?> POSITION;
            public static Class<?> RECIPES;
            public static Class<?> ENTITY_DESTROY;
            public static Class<?> REMOVE_ENTITY_EFFECT;
            public static Class<?> RESOURCE_PACK_SEND;
            public static Class<?> RESPAWN;
            public static Class<?> ENTITY_HEAD_ROTATION;
            public static Class<?> SELECT_ADVANCEMENT_TAB;
            public static Class<?> WORLD_BORDER;
            public static Class<?> CAMERA;
            public static Class<?> HELD_ITEM_SLOT;
            public static Class<?> VIEW_CENTRE;
            public static Class<?> VIEW_DISTANCE;
            public static Class<?> SCOREBOARD_DISPLAY_OBJECTIVE;
            public static Class<?> ENTITY_METADATA;
            public static Class<?> ATTACH_ENTITY;
            public static Class<?> ENTITY_VELOCITY;
            public static Class<?> ENTITY_EQUIPMENT;
            public static Class<?> EXPERIENCE;
            public static Class<?> UPDATE_HEALTH;
            public static Class<?> SCOREBOARD_OBJECTIVE;
            public static Class<?> MOUNT;
            public static Class<?> SCOREBOARD_TEAM;
            public static Class<?> SCOREBOARD_SCORE;
            public static Class<?> SPAWN_POSITION;
            public static Class<?> UPDATE_TIME;
            public static Class<?> TITLE;
            public static Class<?> ENTITY_SOUND;
            public static Class<?> NAMED_SOUND_EFFECT;
            public static Class<?> STOP_SOUND;
            public static Class<?> PLAYER_LIST_HEADER_FOOTER;
            public static Class<?> NBT_QUERY;
            public static Class<?> COLLECT;
            public static Class<?> ENTITY_TELEPORT;
            public static Class<?> ADVANCEMENTS;
            public static Class<?> UPDATE_ATTRIBUTES;
            public static Class<?> ENTITY_EFFECT;
            public static Class<?> RECIPE_UPDATE;
            public static Class<?> TAGS;
            public static Class<?> MAP_CHUNK_BULK;
            public static Class<?> NAMED_ENTITY_SPAWN;
            public static Class<?> PING;
            public static Class<?> ADD_VIBRATION_SIGNAL;
            public static Class<?> CLEAR_TITLES;
            public static Class<?> INITIALIZE_BORDER;
            public static Class<?> PLAYER_COMBAT_END;
            public static Class<?> PLAYER_COMBAT_ENTER;
            public static Class<?> PLAYER_COMBAT_KILL;
            public static Class<?> SET_ACTIONBAR_TEXT;
            public static Class<?> SET_BORDER_CENTER;
            public static Class<?> SET_BORDER_LERP_SIZE;
            public static Class<?> SET_BORDER_SIZE;
            public static Class<?> SET_BORDER_WARNING_DELAY;
            public static Class<?> SET_BORDER_WARNING_DISTANCE;
            public static Class<?> SET_SUBTITLE_TEXT;
            public static Class<?> SET_TITLES_ANIMATION;
            public static Class<?> SET_TITLE_TEXT;
            public static Class<?> SYSTEM_CHAT;
            
            public static void load() {
                if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_17)) {
                    Server.PREFIX = "net.minecraft.network.protocol.game.";
                }
                else {
                    Server.PREFIX = ServerVersion.getNMSDirectory() + ".";
                }
                Server.COMMON_PREFIX = Server.PREFIX + "PacketPlayOut";
                Server.SPAWN_ENTITY = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "SpawnEntity");
                Server.SPAWN_ENTITY_EXPERIENCE_ORB = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "SpawnEntityExperienceOrb");
                Server.SPAWN_ENTITY_WEATHER = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "SpawnEntityWeather");
                Server.SPAWN_ENTITY_LIVING = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "SpawnEntityLiving");
                Server.SPAWN_ENTITY_PAINTING = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "SpawnEntityPainting");
                Server.SPAWN_ENTITY_SPAWN = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "SpawnEntitySpawn");
                Server.ANIMATION = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "Animation");
                Server.STATISTIC = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "Statistic");
                Server.BLOCK_BREAK = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "BlockBreak");
                Server.BLOCK_BREAK_ANIMATION = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "BlockBreakAnimation");
                Server.TILE_ENTITY_DATA = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "TileEntityData");
                Server.BLOCK_ACTION = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "BlockAction");
                Server.BLOCK_CHANGE = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "BlockChange");
                Server.BOSS = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "Boss");
                Server.SERVER_DIFFICULTY = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "ServerDifficulty");
                Server.CHAT = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "Chat");
                if (Server.CHAT == null) {
                    Server.CHAT = Reflection.getClassByNameWithoutException(Server.PREFIX + "ClientboundPlayerChatPacket");
                }
                Server.MULTI_BLOCK_CHANGE = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "MultiBlockChange");
                Server.TAB_COMPLETE = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "TabComplete");
                Server.COMMANDS = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "Commands");
                Server.TRANSACTION = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "Transaction");
                Server.CLOSE_WINDOW = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "CloseWindow");
                Server.WINDOW_ITEMS = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "WindowItems");
                Server.WINDOW_DATA = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "WindowData");
                Server.SET_SLOT = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "SetSlot");
                Server.SET_COOLDOWN = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "SetCooldown");
                Server.CUSTOM_PAYLOAD = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "CustomPayload");
                Server.CUSTOM_SOUND_EFFECT = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "CustomSoundEffect");
                Server.KICK_DISCONNECT = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "KickDisconnect");
                Server.ENTITY_STATUS = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "EntityStatus");
                Server.EXPLOSION = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "Explosion");
                Server.UNLOAD_CHUNK = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "UnloadChunk");
                Server.GAME_STATE_CHANGE = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "GameStateChange");
                Server.OPEN_WINDOW_HORSE = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "OpenWindowHorse");
                Server.KEEP_ALIVE = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "KeepAlive");
                Server.MAP_CHUNK = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "MapChunk");
                Server.WORLD_EVENT = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "WorldEvent");
                Server.WORLD_PARTICLES = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "WorldParticles");
                Server.LIGHT_UPDATE = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "LightUpdate");
                Server.LOGIN = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "Login");
                Server.MAP = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "Map");
                Server.OPEN_WINDOW_MERCHANT = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "OpenWindowMerchant");
                Server.ENTITY = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "Entity");
                Server.REL_ENTITY_MOVE = SubclassUtil.getSubClass(Server.ENTITY, "PacketPlayOutRelEntityMove");
                Server.REL_ENTITY_MOVE_LOOK = SubclassUtil.getSubClass(Server.ENTITY, "PacketPlayOutRelEntityMoveLook");
                Server.ENTITY_LOOK = SubclassUtil.getSubClass(Server.ENTITY, "PacketPlayOutEntityLook");
                if (Server.REL_ENTITY_MOVE == null) {
                    Server.REL_ENTITY_MOVE = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "RelEntityMove");
                    Server.REL_ENTITY_MOVE_LOOK = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "RelEntityMoveLook");
                    Server.ENTITY_LOOK = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "RelEntityLook");
                }
                Server.VEHICLE_MOVE = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "VehicleMove");
                Server.OPEN_BOOK = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "OpenBook");
                Server.OPEN_WINDOW = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "OpenWindow");
                Server.OPEN_SIGN_EDITOR = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "OpenSignEditor");
                Server.AUTO_RECIPE = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "AutoRecipe");
                Server.ABILITIES = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "Abilities");
                Server.COMBAT_EVENT = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "CombatEvent");
                Server.PLAYER_INFO = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "PlayerInfo");
                Server.LOOK_AT = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "LookAt");
                Server.POSITION = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "Position");
                Server.RECIPES = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "Recipes");
                Server.ENTITY_DESTROY = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "EntityDestroy");
                Server.REMOVE_ENTITY_EFFECT = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "RemoveEntityEffect");
                Server.RESOURCE_PACK_SEND = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "ResourcePackSend");
                Server.RESPAWN = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "Respawn");
                Server.ENTITY_HEAD_ROTATION = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "EntityHeadRotation");
                Server.SELECT_ADVANCEMENT_TAB = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "SelectAdvancementTab");
                Server.WORLD_BORDER = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "WorldBorder");
                Server.CAMERA = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "Camera");
                Server.HELD_ITEM_SLOT = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "HeldItemSlot");
                Server.VIEW_CENTRE = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "ViewCentre");
                Server.VIEW_DISTANCE = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "ViewDistance");
                Server.SCOREBOARD_DISPLAY_OBJECTIVE = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "ScoreboardDisplayObjective");
                Server.ENTITY_METADATA = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "EntityMetadata");
                Server.ATTACH_ENTITY = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "AttachEntity");
                Server.ENTITY_VELOCITY = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "EntityVelocity");
                Server.ENTITY_EQUIPMENT = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "EntityEquipment");
                Server.EXPERIENCE = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "Experience");
                Server.UPDATE_HEALTH = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "UpdateHealth");
                Server.SCOREBOARD_OBJECTIVE = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "ScoreboardObjective");
                Server.MOUNT = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "Mount");
                Server.SCOREBOARD_TEAM = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "ScoreboardTeam");
                Server.SCOREBOARD_SCORE = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "ScoreboardScore");
                Server.SPAWN_POSITION = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "SpawnPosition");
                Server.UPDATE_TIME = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "UpdateTime");
                Server.TITLE = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "Title");
                Server.ENTITY_SOUND = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "EntitySound");
                Server.NAMED_SOUND_EFFECT = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "NamedSoundEffect");
                Server.STOP_SOUND = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "StopSound");
                Server.PLAYER_LIST_HEADER_FOOTER = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "PlayerListHeaderFooter");
                Server.NBT_QUERY = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "NBTQuery");
                Server.COLLECT = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "Collect");
                Server.ENTITY_TELEPORT = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "EntityTeleport");
                Server.ADVANCEMENTS = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "Advancements");
                Server.UPDATE_ATTRIBUTES = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "UpdateAttributes");
                Server.ENTITY_EFFECT = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "EntityEffect");
                Server.RECIPE_UPDATE = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "RecipeUpdate");
                Server.TAGS = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "Tags");
                Server.MAP_CHUNK_BULK = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "MapChunkBulk");
                Server.NAMED_ENTITY_SPAWN = Reflection.getClassByNameWithoutException(Server.COMMON_PREFIX + "NamedEntitySpawn");
                Server.PING = Reflection.getClassByNameWithoutException(Server.PREFIX + "ClientboundPingPacket");
                Server.ADD_VIBRATION_SIGNAL = Reflection.getClassByNameWithoutException(Server.PREFIX + "ClientboundAddVibrationSignalPacket");
                Server.CLEAR_TITLES = Reflection.getClassByNameWithoutException(Server.PREFIX + "ClientboundClearTitlesPacket");
                Server.INITIALIZE_BORDER = Reflection.getClassByNameWithoutException(Server.PREFIX + "ClientboundInitializeBorderPacket");
                Server.PLAYER_COMBAT_END = Reflection.getClassByNameWithoutException(Server.PREFIX + "ClientboundPlayerCombatEndPacket");
                Server.PLAYER_COMBAT_ENTER = Reflection.getClassByNameWithoutException(Server.PREFIX + "ClientboundPlayerCombatEnterPacket");
                Server.PLAYER_COMBAT_KILL = Reflection.getClassByNameWithoutException(Server.PREFIX + "ClientboundPlayerCombatKillPacket");
                Server.SET_ACTIONBAR_TEXT = Reflection.getClassByNameWithoutException(Server.PREFIX + "ClientboundSetActionBarTextPacket");
                Server.SET_BORDER_CENTER = Reflection.getClassByNameWithoutException(Server.PREFIX + "ClientboundSetBorderCenterPacket");
                Server.SET_BORDER_LERP_SIZE = Reflection.getClassByNameWithoutException(Server.PREFIX + "ClientboundSetBorderLerpSizePacket");
                Server.SET_BORDER_SIZE = Reflection.getClassByNameWithoutException(Server.PREFIX + "ClientboundSetBorderSizePacket");
                Server.SET_BORDER_WARNING_DELAY = Reflection.getClassByNameWithoutException(Server.PREFIX + "ClientboundSetBorderWarningDelayPacket");
                Server.SET_BORDER_WARNING_DISTANCE = Reflection.getClassByNameWithoutException(Server.PREFIX + "ClientboundSetBorderWarningDistancePacket");
                Server.SET_SUBTITLE_TEXT = Reflection.getClassByNameWithoutException(Server.PREFIX + "ClientboundSetSubtitleTextPacket");
                Server.SET_TITLES_ANIMATION = Reflection.getClassByNameWithoutException(Server.PREFIX + "ClientboundSetTitlesAnimationPacket");
                Server.SET_TITLE_TEXT = Reflection.getClassByNameWithoutException(Server.PREFIX + "ClientboundSetTitleTextPacket");
                Server.SYSTEM_CHAT = Reflection.getClassByNameWithoutException(Server.PREFIX + "ClientboundSystemChatPacket");
                if (Server.MAP_CHUNK == null) {
                    Server.MAP_CHUNK = Reflection.getClassByNameWithoutException("net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket");
                }
            }
        }
    }
}
