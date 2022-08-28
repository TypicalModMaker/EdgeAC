package xyz.edge.ac.packetevents.packettype;

public static class Status
{
    public static class Client
    {
        public static final byte START = -127;
        public static final byte PING = -126;
        
        private static void load() {
            PacketType.access$700(PacketTypeClasses.Status.Client.START, (byte)(-127));
            PacketType.access$700(PacketTypeClasses.Status.Client.PING, (byte)(-126));
        }
    }
    
    public static class Server
    {
        public static final byte PONG = -125;
        public static final byte SERVER_INFO = -124;
        
        private static void load() {
            PacketType.access$700(PacketTypeClasses.Status.Server.PONG, (byte)(-125));
            PacketType.access$700(PacketTypeClasses.Status.Server.SERVER_INFO, (byte)(-124));
        }
    }
}
