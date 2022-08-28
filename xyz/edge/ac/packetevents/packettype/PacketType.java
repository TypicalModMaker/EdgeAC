package xyz.edge.ac.packetevents.packettype;

import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import java.util.IdentityHashMap;
import java.util.Map;

public class PacketType
{
    public static final byte INVALID = Byte.MIN_VALUE;
    public static final Map<Class<?>, Byte> packetIDMap;
    private static final boolean isNine;
    
    private static void insertPacketID(final Class<?> cls, final byte packetID) {
        if (cls != null) {
            PacketType.packetIDMap.put(cls, packetID);
        }
    }
    
    public static void load() {
        load();
        load();
        load();
        load();
        load();
        load();
        load();
    }
    
    static {
        packetIDMap = new IdentityHashMap<Class<?>, Byte>();
        isNine = ServerVersion.getVersion().isNewerThanOrEquals(ServerVersion.v_1_9);
    }
    
    public static class Status
    {
        public static class Client
        {
            public static final byte START = -127;
            public static final byte PING = -126;
            
            private static void load() {
                insertPacketID(PacketTypeClasses.Status.Client.START, (byte)(-127));
                insertPacketID(PacketTypeClasses.Status.Client.PING, (byte)(-126));
            }
        }
        
        public static class Server
        {
            public static final byte PONG = -125;
            public static final byte SERVER_INFO = -124;
            
            private static void load() {
                insertPacketID(PacketTypeClasses.Status.Server.PONG, (byte)(-125));
                insertPacketID(PacketTypeClasses.Status.Server.SERVER_INFO, (byte)(-124));
            }
        }
    }
    
    public static class Handshaking
    {
        public static class Client
        {
            public static final byte SET_PROTOCOL = -123;
            
            private static void load() {
                insertPacketID(PacketTypeClasses.Handshaking.Client.SET_PROTOCOL, (byte)(-123));
            }
        }
    }
    
    public static class Login
    {
        public static class Client
        {
            public static final byte CUSTOM_PAYLOAD = -122;
            public static final byte START = -121;
            public static final byte ENCRYPTION_BEGIN = -120;
            
            private static void load() {
                insertPacketID(PacketTypeClasses.Login.Client.CUSTOM_PAYLOAD, (byte)(-122));
                insertPacketID(PacketTypeClasses.Login.Client.START, (byte)(-121));
                insertPacketID(PacketTypeClasses.Login.Client.ENCRYPTION_BEGIN, (byte)(-120));
            }
        }
        
        public static class Server
        {
            public static final byte CUSTOM_PAYLOAD = -119;
            public static final byte DISCONNECT = -118;
            public static final byte ENCRYPTION_BEGIN = -117;
            public static final byte SUCCESS = -116;
            public static final byte SET_COMPRESSION = -115;
            
            private static void load() {
                insertPacketID(PacketTypeClasses.Login.Server.CUSTOM_PAYLOAD, (byte)(-119));
                insertPacketID(PacketTypeClasses.Login.Server.DISCONNECT, (byte)(-118));
                insertPacketID(PacketTypeClasses.Login.Server.ENCRYPTION_BEGIN, (byte)(-117));
                insertPacketID(PacketTypeClasses.Login.Server.SUCCESS, (byte)(-116));
                insertPacketID(PacketTypeClasses.Login.Server.SET_COMPRESSION, (byte)(-115));
            }
        }
    }
    
    public static class Play
    {
        public static class Client
        {
            public static final byte TELEPORT_ACCEPT = -114;
            public static final byte TILE_NBT_QUERY = -113;
            public static final byte DIFFICULTY_CHANGE = -112;
            public static final byte CHAT = -111;
            public static final byte CLIENT_COMMAND = -110;
            public static final byte SETTINGS = -109;
            public static final byte TAB_COMPLETE = -108;
            public static final byte TRANSACTION = -107;
            public static final byte ENCHANT_ITEM = -106;
            public static final byte WINDOW_CLICK = -105;
            public static final byte CLOSE_WINDOW = -104;
            public static final byte CUSTOM_PAYLOAD = -103;
            public static final byte B_EDIT = -102;
            public static final byte ENTITY_NBT_QUERY = -101;
            public static final byte USE_ENTITY = -100;
            public static final byte JIGSAW_GENERATE = -99;
            public static final byte KEEP_ALIVE = -98;
            public static final byte DIFFICULTY_LOCK = -97;
            public static final byte POSITION = -96;
            public static final byte POSITION_LOOK = -95;
            public static final byte LOOK = -94;
            public static final byte FLYING = -93;
            public static final byte VEHICLE_MOVE = -92;
            public static final byte BOAT_MOVE = -91;
            public static final byte PICK_ITEM = -90;
            public static final byte AUTO_RECIPE = -89;
            public static final byte ABILITIES = -88;
            public static final byte BLOCK_DIG = -87;
            public static final byte ENTITY_ACTION = -86;
            public static final byte STEER_VEHICLE = -85;
            public static final byte RECIPE_DISPLAYED = -84;
            public static final byte ITEM_NAME = -83;
            public static final byte RESOURCE_PACK_STATUS = -82;
            public static final byte ADVANCEMENTS = -81;
            public static final byte TR_SEL = -80;
            public static final byte BEACON = -79;
            public static final byte HELD_ITEM_SLOT = -78;
            public static final byte SET_COMMAND_BLOCK = -77;
            public static final byte SET_COMMAND_MINECART = -76;
            public static final byte SET_CREATIVE_SLOT = -75;
            public static final byte SET_JIGSAW = -74;
            public static final byte STRUCT = -73;
            public static final byte UPDATE_SIGN = -72;
            public static final byte ARM_ANIMATION = -71;
            public static final byte SPECTATE = -70;
            public static final byte USE_ITEM = -69;
            public static final byte BLOCK_PLACE = -68;
            public static final byte PONG = 28;
            
            private static void load() {
                insertPacketID(PacketTypeClasses.Play.Client.TELEPORT_ACCEPT, (byte)(-114));
                insertPacketID(PacketTypeClasses.Play.Client.TILE_NBT_QUERY, (byte)(-113));
                insertPacketID(PacketTypeClasses.Play.Client.DIFFICULTY_CHANGE, (byte)(-112));
                insertPacketID(PacketTypeClasses.Play.Client.CHAT, (byte)(-111));
                insertPacketID(PacketTypeClasses.Play.Client.CLIENT_COMMAND, (byte)(-110));
                insertPacketID(PacketTypeClasses.Play.Client.SETTINGS, (byte)(-109));
                insertPacketID(PacketTypeClasses.Play.Client.TAB_COMPLETE, (byte)(-108));
                insertPacketID(PacketTypeClasses.Play.Client.TRANSACTION, (byte)(-107));
                insertPacketID(PacketTypeClasses.Play.Client.ENCHANT_ITEM, (byte)(-106));
                insertPacketID(PacketTypeClasses.Play.Client.WINDOW_CLICK, (byte)(-105));
                insertPacketID(PacketTypeClasses.Play.Client.CLOSE_WINDOW, (byte)(-104));
                insertPacketID(PacketTypeClasses.Play.Client.CUSTOM_PAYLOAD, (byte)(-103));
                insertPacketID(PacketTypeClasses.Play.Client.B_EDIT, (byte)(-102));
                insertPacketID(PacketTypeClasses.Play.Client.ENTITY_NBT_QUERY, (byte)(-101));
                insertPacketID(PacketTypeClasses.Play.Client.USE_ENTITY, (byte)(-100));
                insertPacketID(PacketTypeClasses.Play.Client.JIGSAW_GENERATE, (byte)(-99));
                insertPacketID(PacketTypeClasses.Play.Client.KEEP_ALIVE, (byte)(-98));
                insertPacketID(PacketTypeClasses.Play.Client.DIFFICULTY_LOCK, (byte)(-97));
                insertPacketID(PacketTypeClasses.Play.Client.POSITION, (byte)(-96));
                insertPacketID(PacketTypeClasses.Play.Client.POSITION_LOOK, (byte)(-95));
                insertPacketID(PacketTypeClasses.Play.Client.LOOK, (byte)(-94));
                insertPacketID(PacketTypeClasses.Play.Client.GROUND, (byte)(-93));
                insertPacketID(PacketTypeClasses.Play.Client.VEHICLE_MOVE, (byte)(-92));
                insertPacketID(PacketTypeClasses.Play.Client.BOAT_MOVE, (byte)(-91));
                insertPacketID(PacketTypeClasses.Play.Client.PICK_ITEM, (byte)(-90));
                insertPacketID(PacketTypeClasses.Play.Client.AUTO_RECIPE, (byte)(-89));
                insertPacketID(PacketTypeClasses.Play.Client.ABILITIES, (byte)(-88));
                insertPacketID(PacketTypeClasses.Play.Client.BLOCK_DIG, (byte)(-87));
                insertPacketID(PacketTypeClasses.Play.Client.ENTITY_ACTION, (byte)(-86));
                insertPacketID(PacketTypeClasses.Play.Client.STEER_VEHICLE, (byte)(-85));
                insertPacketID(PacketTypeClasses.Play.Client.RECIPE_DISPLAYED, (byte)(-84));
                insertPacketID(PacketTypeClasses.Play.Client.ITEM_NAME, (byte)(-83));
                insertPacketID(PacketTypeClasses.Play.Client.RESOURCE_PACK_STATUS, (byte)(-82));
                insertPacketID(PacketTypeClasses.Play.Client.ADVANCEMENTS, (byte)(-81));
                insertPacketID(PacketTypeClasses.Play.Client.TR_SEL, (byte)(-80));
                insertPacketID(PacketTypeClasses.Play.Client.BEACON, (byte)(-79));
                insertPacketID(PacketTypeClasses.Play.Client.HELD_ITEM_SLOT, (byte)(-78));
                insertPacketID(PacketTypeClasses.Play.Client.SET_COMMAND_BLOCK, (byte)(-77));
                insertPacketID(PacketTypeClasses.Play.Client.SET_COMMAND_MINECART, (byte)(-76));
                insertPacketID(PacketTypeClasses.Play.Client.SET_CREATIVE_SLOT, (byte)(-75));
                insertPacketID(PacketTypeClasses.Play.Client.SET_JIGSAW, (byte)(-74));
                insertPacketID(PacketTypeClasses.Play.Client.STRUCT, (byte)(-73));
                insertPacketID(PacketTypeClasses.Play.Client.UPDATE_SIGN, (byte)(-72));
                insertPacketID(PacketTypeClasses.Play.Client.ARM_ANIMATION, (byte)(-71));
                insertPacketID(PacketTypeClasses.Play.Client.SPECTATE, (byte)(-70));
                insertPacketID(PacketTypeClasses.Play.Client.USE_ITEM, (byte)(-69));
                insertPacketID(PacketTypeClasses.Play.Client.BLOCK_PLACE, (byte)(-68));
                insertPacketID(PacketTypeClasses.Play.Client.PONG, (byte)28);
            }
            
            public static class Util
            {
                public static boolean isInstanceOfFlying(final byte packetID) {
                    return packetID == -93 || packetID == -96 || packetID == -95 || packetID == -94;
                }
                
                public static boolean isBlockPlace(final byte packetID) {
                    return PacketType.isNine ? (packetID == -69) : (packetID == -68);
                }
            }
        }
        
        public static class Server
        {
            public static final byte SPAWN_ENTITY = -67;
            public static final byte SPAWN_ENTITY_EXPERIENCE_ORB = -66;
            public static final byte SPAWN_ENTITY_WEATHER = -65;
            public static final byte SPAWN_ENTITY_LIVING = -64;
            public static final byte SPAWN_ENTITY_PAINTING = -63;
            public static final byte SPAWN_ENTITY_SPAWN = -62;
            public static final byte ANIMATION = -61;
            public static final byte STATISTIC = -60;
            public static final byte BLOCK_BREAK = -59;
            public static final byte BLOCK_BREAK_ANIMATION = -58;
            public static final byte TILE_ENTITY_DATA = -57;
            public static final byte BLOCK_ACTION = -56;
            public static final byte BLOCK_CHANGE = -55;
            public static final byte BOSS = -54;
            public static final byte SERVER_DIFFICULTY = -53;
            public static final byte CHAT = -52;
            public static final byte MULTI_BLOCK_CHANGE = -51;
            public static final byte TAB_COMPLETE = -50;
            public static final byte COMMANDS = -49;
            public static final byte TRANSACTION = -48;
            public static final byte CLOSE_WINDOW = -47;
            public static final byte WINDOW_ITEMS = -46;
            public static final byte WINDOW_DATA = -45;
            public static final byte SET_SLOT = -44;
            public static final byte SET_COOLDOWN = -43;
            public static final byte CUSTOM_PAYLOAD = -42;
            public static final byte CUSTOM_SOUND_EFFECT = -41;
            public static final byte KICK_DISCONNECT = -40;
            public static final byte ENTITY_STATUS = -39;
            public static final byte EXPLOSION = -38;
            public static final byte UNLOAD_CHUNK = -37;
            public static final byte GAME_STATE_CHANGE = -36;
            public static final byte OPEN_WINDOW_HORSE = -35;
            public static final byte KEEP_ALIVE = -34;
            public static final byte MAP_CHUNK = -33;
            public static final byte WORLD_EVENT = -32;
            public static final byte WORLD_PARTICLES = -31;
            public static final byte LIGHT_UPDATE = -30;
            public static final byte LOGIN = -29;
            public static final byte MAP = -28;
            public static final byte OPEN_WINDOW_MERCHANT = -27;
            public static final byte REL_ENTITY_MOVE = -26;
            public static final byte REL_ENTITY_MOVE_LOOK = -25;
            public static final byte ENTITY_LOOK = -24;
            public static final byte ENTITY = -23;
            public static final byte VEHICLE_MOVE = -22;
            public static final byte OPEN_BOOK = -21;
            public static final byte OPEN_WINDOW = -20;
            public static final byte OPEN_SIGN_EDITOR = -19;
            public static final byte AUTO_RECIPE = -18;
            public static final byte ABILITIES = -17;
            public static final byte COMBAT_EVENT = -16;
            public static final byte PLAYER_INFO = -15;
            public static final byte LOOK_AT = -14;
            public static final byte POSITION = -13;
            public static final byte RECIPES = -12;
            public static final byte ENTITY_DESTROY = -11;
            public static final byte REMOVE_ENTITY_EFFECT = -10;
            public static final byte RESOURCE_PACK_SEND = -9;
            public static final byte RESPAWN = -8;
            public static final byte ENTITY_HEAD_ROTATION = -7;
            public static final byte SELECT_ADVANCEMENT_TAB = -6;
            public static final byte WORLD_BORDER = -5;
            public static final byte CAMERA = -4;
            public static final byte HELD_ITEM_SLOT = -3;
            public static final byte VIEW_CENTRE = -2;
            public static final byte VIEW_DISTANCE = -1;
            public static final byte SCOREBOARD_DISPLAY_OBJECTIVE = 0;
            public static final byte ENTITY_METADATA = 1;
            public static final byte ATTACH_ENTITY = 2;
            public static final byte ENTITY_VELOCITY = 3;
            public static final byte ENTITY_EQUIPMENT = 4;
            public static final byte EXPERIENCE = 5;
            public static final byte UPDATE_HEALTH = 6;
            public static final byte SCOREBOARD_OBJECTIVE = 7;
            public static final byte MOUNT = 8;
            public static final byte SCOREBOARD_TEAM = 9;
            public static final byte SCOREBOARD_SCORE = 10;
            public static final byte SPAWN_POSITION = 11;
            public static final byte UPDATE_TIME = 12;
            public static final byte TITLE = 13;
            public static final byte ENTITY_SOUND = 14;
            public static final byte NAMED_SOUND_EFFECT = 15;
            public static final byte STOP_SOUND = 16;
            public static final byte PLAYER_LIST_HEADER_FOOTER = 17;
            public static final byte NBT_QUERY = 18;
            public static final byte COLLECT = 19;
            public static final byte ENTITY_TELEPORT = 20;
            public static final byte ADVANCEMENTS = 21;
            public static final byte UPDATE_ATTRIBUTES = 22;
            public static final byte ENTITY_EFFECT = 23;
            public static final byte RECIPE_UPDATE = 24;
            public static final byte TAGS = 25;
            public static final byte MAP_CHUNK_BULK = 26;
            public static final byte NAMED_ENTITY_SPAWN = 27;
            public static final byte PING = 29;
            public static final byte ADD_VIBRATION_SIGNAL = 30;
            public static final byte CLEAR_TITLES = 31;
            public static final byte INITIALIZE_BORDER = 32;
            public static final byte PLAYER_COMBAT_END = 33;
            public static final byte PLAYER_COMBAT_ENTER = 34;
            public static final byte PLAYER_COMBAT_KILL = 35;
            public static final byte SET_ACTIONBAR_TEXT = 36;
            public static final byte SET_BORDER_CENTER = 37;
            public static final byte SET_BORDER_LERP_SIZE = 38;
            public static final byte SET_BORDER_SIZE = 39;
            public static final byte SET_BORDER_WARNING_DELAY = 40;
            public static final byte SET_BORDER_WARNING_DISTANCE = 41;
            public static final byte SET_SUBTITLE_TEXT = 42;
            public static final byte SET_TITLES_ANIMATION = 43;
            public static final byte SET_TITLE_TEXT = 44;
            public static final byte SYSTEM_CHAT = 45;
            
            private static void load() {
                insertPacketID(PacketTypeClasses.Play.Server.SPAWN_ENTITY, (byte)(-67));
                insertPacketID(PacketTypeClasses.Play.Server.SPAWN_ENTITY_EXPERIENCE_ORB, (byte)(-66));
                insertPacketID(PacketTypeClasses.Play.Server.SPAWN_ENTITY_WEATHER, (byte)(-65));
                insertPacketID(PacketTypeClasses.Play.Server.SPAWN_ENTITY_LIVING, (byte)(-64));
                insertPacketID(PacketTypeClasses.Play.Server.SPAWN_ENTITY_PAINTING, (byte)(-63));
                insertPacketID(PacketTypeClasses.Play.Server.SPAWN_ENTITY_SPAWN, (byte)(-62));
                insertPacketID(PacketTypeClasses.Play.Server.ANIMATION, (byte)(-61));
                insertPacketID(PacketTypeClasses.Play.Server.STATISTIC, (byte)(-60));
                insertPacketID(PacketTypeClasses.Play.Server.BLOCK_BREAK, (byte)(-59));
                insertPacketID(PacketTypeClasses.Play.Server.BLOCK_BREAK_ANIMATION, (byte)(-58));
                insertPacketID(PacketTypeClasses.Play.Server.TILE_ENTITY_DATA, (byte)(-57));
                insertPacketID(PacketTypeClasses.Play.Server.BLOCK_ACTION, (byte)(-56));
                insertPacketID(PacketTypeClasses.Play.Server.BLOCK_CHANGE, (byte)(-55));
                insertPacketID(PacketTypeClasses.Play.Server.BOSS, (byte)(-54));
                insertPacketID(PacketTypeClasses.Play.Server.SERVER_DIFFICULTY, (byte)(-53));
                insertPacketID(PacketTypeClasses.Play.Server.CHAT, (byte)(-52));
                insertPacketID(PacketTypeClasses.Play.Server.MULTI_BLOCK_CHANGE, (byte)(-51));
                insertPacketID(PacketTypeClasses.Play.Server.TAB_COMPLETE, (byte)(-50));
                insertPacketID(PacketTypeClasses.Play.Server.COMMANDS, (byte)(-49));
                insertPacketID(PacketTypeClasses.Play.Server.TRANSACTION, (byte)(-48));
                insertPacketID(PacketTypeClasses.Play.Server.CLOSE_WINDOW, (byte)(-47));
                insertPacketID(PacketTypeClasses.Play.Server.WINDOW_ITEMS, (byte)(-46));
                insertPacketID(PacketTypeClasses.Play.Server.WINDOW_DATA, (byte)(-45));
                insertPacketID(PacketTypeClasses.Play.Server.SET_SLOT, (byte)(-44));
                insertPacketID(PacketTypeClasses.Play.Server.SET_COOLDOWN, (byte)(-43));
                insertPacketID(PacketTypeClasses.Play.Server.CUSTOM_PAYLOAD, (byte)(-42));
                insertPacketID(PacketTypeClasses.Play.Server.CUSTOM_SOUND_EFFECT, (byte)(-41));
                insertPacketID(PacketTypeClasses.Play.Server.KICK_DISCONNECT, (byte)(-40));
                insertPacketID(PacketTypeClasses.Play.Server.ENTITY_STATUS, (byte)(-39));
                insertPacketID(PacketTypeClasses.Play.Server.EXPLOSION, (byte)(-38));
                insertPacketID(PacketTypeClasses.Play.Server.UNLOAD_CHUNK, (byte)(-37));
                insertPacketID(PacketTypeClasses.Play.Server.GAME_STATE_CHANGE, (byte)(-36));
                insertPacketID(PacketTypeClasses.Play.Server.OPEN_WINDOW_HORSE, (byte)(-35));
                insertPacketID(PacketTypeClasses.Play.Server.KEEP_ALIVE, (byte)(-34));
                insertPacketID(PacketTypeClasses.Play.Server.MAP_CHUNK, (byte)(-33));
                insertPacketID(PacketTypeClasses.Play.Server.WORLD_EVENT, (byte)(-32));
                insertPacketID(PacketTypeClasses.Play.Server.WORLD_PARTICLES, (byte)(-31));
                insertPacketID(PacketTypeClasses.Play.Server.LIGHT_UPDATE, (byte)(-30));
                insertPacketID(PacketTypeClasses.Play.Server.LOGIN, (byte)(-29));
                insertPacketID(PacketTypeClasses.Play.Server.MAP, (byte)(-28));
                insertPacketID(PacketTypeClasses.Play.Server.OPEN_WINDOW_MERCHANT, (byte)(-27));
                insertPacketID(PacketTypeClasses.Play.Server.REL_ENTITY_MOVE, (byte)(-26));
                insertPacketID(PacketTypeClasses.Play.Server.REL_ENTITY_MOVE_LOOK, (byte)(-25));
                insertPacketID(PacketTypeClasses.Play.Server.ENTITY_LOOK, (byte)(-24));
                insertPacketID(PacketTypeClasses.Play.Server.ENTITY, (byte)(-23));
                insertPacketID(PacketTypeClasses.Play.Server.VEHICLE_MOVE, (byte)(-22));
                insertPacketID(PacketTypeClasses.Play.Server.OPEN_BOOK, (byte)(-21));
                insertPacketID(PacketTypeClasses.Play.Server.OPEN_WINDOW, (byte)(-20));
                insertPacketID(PacketTypeClasses.Play.Server.OPEN_SIGN_EDITOR, (byte)(-19));
                insertPacketID(PacketTypeClasses.Play.Server.AUTO_RECIPE, (byte)(-18));
                insertPacketID(PacketTypeClasses.Play.Server.ABILITIES, (byte)(-17));
                insertPacketID(PacketTypeClasses.Play.Server.COMBAT_EVENT, (byte)(-16));
                insertPacketID(PacketTypeClasses.Play.Server.PLAYER_INFO, (byte)(-15));
                insertPacketID(PacketTypeClasses.Play.Server.LOOK_AT, (byte)(-14));
                insertPacketID(PacketTypeClasses.Play.Server.POSITION, (byte)(-13));
                insertPacketID(PacketTypeClasses.Play.Server.RECIPES, (byte)(-12));
                insertPacketID(PacketTypeClasses.Play.Server.ENTITY_DESTROY, (byte)(-11));
                insertPacketID(PacketTypeClasses.Play.Server.REMOVE_ENTITY_EFFECT, (byte)(-10));
                insertPacketID(PacketTypeClasses.Play.Server.RESOURCE_PACK_SEND, (byte)(-9));
                insertPacketID(PacketTypeClasses.Play.Server.RESPAWN, (byte)(-8));
                insertPacketID(PacketTypeClasses.Play.Server.ENTITY_HEAD_ROTATION, (byte)(-7));
                insertPacketID(PacketTypeClasses.Play.Server.SELECT_ADVANCEMENT_TAB, (byte)(-6));
                insertPacketID(PacketTypeClasses.Play.Server.WORLD_BORDER, (byte)(-5));
                insertPacketID(PacketTypeClasses.Play.Server.CAMERA, (byte)(-4));
                insertPacketID(PacketTypeClasses.Play.Server.HELD_ITEM_SLOT, (byte)(-3));
                insertPacketID(PacketTypeClasses.Play.Server.VIEW_CENTRE, (byte)(-2));
                insertPacketID(PacketTypeClasses.Play.Server.VIEW_DISTANCE, (byte)(-1));
                insertPacketID(PacketTypeClasses.Play.Server.SCOREBOARD_DISPLAY_OBJECTIVE, (byte)0);
                insertPacketID(PacketTypeClasses.Play.Server.ENTITY_METADATA, (byte)1);
                insertPacketID(PacketTypeClasses.Play.Server.ATTACH_ENTITY, (byte)2);
                insertPacketID(PacketTypeClasses.Play.Server.ENTITY_VELOCITY, (byte)3);
                insertPacketID(PacketTypeClasses.Play.Server.ENTITY_EQUIPMENT, (byte)4);
                insertPacketID(PacketTypeClasses.Play.Server.EXPERIENCE, (byte)5);
                insertPacketID(PacketTypeClasses.Play.Server.UPDATE_HEALTH, (byte)6);
                insertPacketID(PacketTypeClasses.Play.Server.SCOREBOARD_OBJECTIVE, (byte)7);
                insertPacketID(PacketTypeClasses.Play.Server.MOUNT, (byte)8);
                insertPacketID(PacketTypeClasses.Play.Server.SCOREBOARD_TEAM, (byte)9);
                insertPacketID(PacketTypeClasses.Play.Server.SCOREBOARD_SCORE, (byte)10);
                insertPacketID(PacketTypeClasses.Play.Server.SPAWN_POSITION, (byte)11);
                insertPacketID(PacketTypeClasses.Play.Server.UPDATE_TIME, (byte)12);
                insertPacketID(PacketTypeClasses.Play.Server.TITLE, (byte)13);
                insertPacketID(PacketTypeClasses.Play.Server.ENTITY_SOUND, (byte)14);
                insertPacketID(PacketTypeClasses.Play.Server.NAMED_SOUND_EFFECT, (byte)15);
                insertPacketID(PacketTypeClasses.Play.Server.STOP_SOUND, (byte)16);
                insertPacketID(PacketTypeClasses.Play.Server.PLAYER_LIST_HEADER_FOOTER, (byte)17);
                insertPacketID(PacketTypeClasses.Play.Server.NBT_QUERY, (byte)18);
                insertPacketID(PacketTypeClasses.Play.Server.COLLECT, (byte)19);
                insertPacketID(PacketTypeClasses.Play.Server.ENTITY_TELEPORT, (byte)20);
                insertPacketID(PacketTypeClasses.Play.Server.ADVANCEMENTS, (byte)21);
                insertPacketID(PacketTypeClasses.Play.Server.UPDATE_ATTRIBUTES, (byte)22);
                insertPacketID(PacketTypeClasses.Play.Server.ENTITY_EFFECT, (byte)23);
                insertPacketID(PacketTypeClasses.Play.Server.RECIPE_UPDATE, (byte)24);
                insertPacketID(PacketTypeClasses.Play.Server.TAGS, (byte)25);
                insertPacketID(PacketTypeClasses.Play.Server.MAP_CHUNK_BULK, (byte)26);
                insertPacketID(PacketTypeClasses.Play.Server.NAMED_ENTITY_SPAWN, (byte)27);
                insertPacketID(PacketTypeClasses.Play.Server.PING, (byte)29);
                insertPacketID(PacketTypeClasses.Play.Server.ADD_VIBRATION_SIGNAL, (byte)30);
                insertPacketID(PacketTypeClasses.Play.Server.CLEAR_TITLES, (byte)31);
                insertPacketID(PacketTypeClasses.Play.Server.INITIALIZE_BORDER, (byte)32);
                insertPacketID(PacketTypeClasses.Play.Server.PLAYER_COMBAT_END, (byte)33);
                insertPacketID(PacketTypeClasses.Play.Server.PLAYER_COMBAT_ENTER, (byte)34);
                insertPacketID(PacketTypeClasses.Play.Server.PLAYER_COMBAT_KILL, (byte)35);
                insertPacketID(PacketTypeClasses.Play.Server.SET_ACTIONBAR_TEXT, (byte)36);
                insertPacketID(PacketTypeClasses.Play.Server.SET_BORDER_CENTER, (byte)37);
                insertPacketID(PacketTypeClasses.Play.Server.SET_BORDER_LERP_SIZE, (byte)38);
                insertPacketID(PacketTypeClasses.Play.Server.SET_BORDER_SIZE, (byte)39);
                insertPacketID(PacketTypeClasses.Play.Server.SET_BORDER_WARNING_DELAY, (byte)40);
                insertPacketID(PacketTypeClasses.Play.Server.SET_BORDER_WARNING_DISTANCE, (byte)41);
                insertPacketID(PacketTypeClasses.Play.Server.SET_SUBTITLE_TEXT, (byte)42);
                insertPacketID(PacketTypeClasses.Play.Server.SET_TITLES_ANIMATION, (byte)43);
                insertPacketID(PacketTypeClasses.Play.Server.SET_TITLE_TEXT, (byte)44);
                insertPacketID(PacketTypeClasses.Play.Server.SYSTEM_CHAT, (byte)45);
            }
            
            public static class Util
            {
                public static boolean isInstanceOfEntity(final byte packetID) {
                    return packetID == -23 || packetID == -26 || packetID == -25 || packetID == -24;
                }
            }
        }
    }
}
