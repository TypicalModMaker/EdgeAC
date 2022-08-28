package xyz.edge.ac.packetevents.packettype;

public static class Login
{
    public static class Client
    {
        public static final byte CUSTOM_PAYLOAD = -122;
        public static final byte START = -121;
        public static final byte ENCRYPTION_BEGIN = -120;
        
        private static void load() {
            PacketType.access$700(PacketTypeClasses.Login.Client.CUSTOM_PAYLOAD, (byte)(-122));
            PacketType.access$700(PacketTypeClasses.Login.Client.START, (byte)(-121));
            PacketType.access$700(PacketTypeClasses.Login.Client.ENCRYPTION_BEGIN, (byte)(-120));
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
            PacketType.access$700(PacketTypeClasses.Login.Server.CUSTOM_PAYLOAD, (byte)(-119));
            PacketType.access$700(PacketTypeClasses.Login.Server.DISCONNECT, (byte)(-118));
            PacketType.access$700(PacketTypeClasses.Login.Server.ENCRYPTION_BEGIN, (byte)(-117));
            PacketType.access$700(PacketTypeClasses.Login.Server.SUCCESS, (byte)(-116));
            PacketType.access$700(PacketTypeClasses.Login.Server.SET_COMPRESSION, (byte)(-115));
        }
    }
}
