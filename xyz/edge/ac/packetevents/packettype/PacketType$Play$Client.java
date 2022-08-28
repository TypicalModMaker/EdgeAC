package xyz.edge.ac.packetevents.packettype;

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
        PacketType.access$700(PacketTypeClasses.Play.Client.TELEPORT_ACCEPT, (byte)(-114));
        PacketType.access$700(PacketTypeClasses.Play.Client.TILE_NBT_QUERY, (byte)(-113));
        PacketType.access$700(PacketTypeClasses.Play.Client.DIFFICULTY_CHANGE, (byte)(-112));
        PacketType.access$700(PacketTypeClasses.Play.Client.CHAT, (byte)(-111));
        PacketType.access$700(PacketTypeClasses.Play.Client.CLIENT_COMMAND, (byte)(-110));
        PacketType.access$700(PacketTypeClasses.Play.Client.SETTINGS, (byte)(-109));
        PacketType.access$700(PacketTypeClasses.Play.Client.TAB_COMPLETE, (byte)(-108));
        PacketType.access$700(PacketTypeClasses.Play.Client.TRANSACTION, (byte)(-107));
        PacketType.access$700(PacketTypeClasses.Play.Client.ENCHANT_ITEM, (byte)(-106));
        PacketType.access$700(PacketTypeClasses.Play.Client.WINDOW_CLICK, (byte)(-105));
        PacketType.access$700(PacketTypeClasses.Play.Client.CLOSE_WINDOW, (byte)(-104));
        PacketType.access$700(PacketTypeClasses.Play.Client.CUSTOM_PAYLOAD, (byte)(-103));
        PacketType.access$700(PacketTypeClasses.Play.Client.B_EDIT, (byte)(-102));
        PacketType.access$700(PacketTypeClasses.Play.Client.ENTITY_NBT_QUERY, (byte)(-101));
        PacketType.access$700(PacketTypeClasses.Play.Client.USE_ENTITY, (byte)(-100));
        PacketType.access$700(PacketTypeClasses.Play.Client.JIGSAW_GENERATE, (byte)(-99));
        PacketType.access$700(PacketTypeClasses.Play.Client.KEEP_ALIVE, (byte)(-98));
        PacketType.access$700(PacketTypeClasses.Play.Client.DIFFICULTY_LOCK, (byte)(-97));
        PacketType.access$700(PacketTypeClasses.Play.Client.POSITION, (byte)(-96));
        PacketType.access$700(PacketTypeClasses.Play.Client.POSITION_LOOK, (byte)(-95));
        PacketType.access$700(PacketTypeClasses.Play.Client.LOOK, (byte)(-94));
        PacketType.access$700(PacketTypeClasses.Play.Client.GROUND, (byte)(-93));
        PacketType.access$700(PacketTypeClasses.Play.Client.VEHICLE_MOVE, (byte)(-92));
        PacketType.access$700(PacketTypeClasses.Play.Client.BOAT_MOVE, (byte)(-91));
        PacketType.access$700(PacketTypeClasses.Play.Client.PICK_ITEM, (byte)(-90));
        PacketType.access$700(PacketTypeClasses.Play.Client.AUTO_RECIPE, (byte)(-89));
        PacketType.access$700(PacketTypeClasses.Play.Client.ABILITIES, (byte)(-88));
        PacketType.access$700(PacketTypeClasses.Play.Client.BLOCK_DIG, (byte)(-87));
        PacketType.access$700(PacketTypeClasses.Play.Client.ENTITY_ACTION, (byte)(-86));
        PacketType.access$700(PacketTypeClasses.Play.Client.STEER_VEHICLE, (byte)(-85));
        PacketType.access$700(PacketTypeClasses.Play.Client.RECIPE_DISPLAYED, (byte)(-84));
        PacketType.access$700(PacketTypeClasses.Play.Client.ITEM_NAME, (byte)(-83));
        PacketType.access$700(PacketTypeClasses.Play.Client.RESOURCE_PACK_STATUS, (byte)(-82));
        PacketType.access$700(PacketTypeClasses.Play.Client.ADVANCEMENTS, (byte)(-81));
        PacketType.access$700(PacketTypeClasses.Play.Client.TR_SEL, (byte)(-80));
        PacketType.access$700(PacketTypeClasses.Play.Client.BEACON, (byte)(-79));
        PacketType.access$700(PacketTypeClasses.Play.Client.HELD_ITEM_SLOT, (byte)(-78));
        PacketType.access$700(PacketTypeClasses.Play.Client.SET_COMMAND_BLOCK, (byte)(-77));
        PacketType.access$700(PacketTypeClasses.Play.Client.SET_COMMAND_MINECART, (byte)(-76));
        PacketType.access$700(PacketTypeClasses.Play.Client.SET_CREATIVE_SLOT, (byte)(-75));
        PacketType.access$700(PacketTypeClasses.Play.Client.SET_JIGSAW, (byte)(-74));
        PacketType.access$700(PacketTypeClasses.Play.Client.STRUCT, (byte)(-73));
        PacketType.access$700(PacketTypeClasses.Play.Client.UPDATE_SIGN, (byte)(-72));
        PacketType.access$700(PacketTypeClasses.Play.Client.ARM_ANIMATION, (byte)(-71));
        PacketType.access$700(PacketTypeClasses.Play.Client.SPECTATE, (byte)(-70));
        PacketType.access$700(PacketTypeClasses.Play.Client.USE_ITEM, (byte)(-69));
        PacketType.access$700(PacketTypeClasses.Play.Client.BLOCK_PLACE, (byte)(-68));
        PacketType.access$700(PacketTypeClasses.Play.Client.PONG, (byte)28);
    }
    
    public static class Util
    {
        public static boolean isInstanceOfFlying(final byte packetID) {
            return packetID == -93 || packetID == -96 || packetID == -95 || packetID == -94;
        }
        
        public static boolean isBlockPlace(final byte packetID) {
            return PacketType.access$800() ? (packetID == -69) : (packetID == -68);
        }
    }
}
