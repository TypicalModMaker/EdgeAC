package xyz.edge.ac.packetevents.packettype;

public static class Server
{
    public static final byte PONG = -125;
    public static final byte SERVER_INFO = -124;
    
    private static void load() {
        PacketType.access$700(PacketTypeClasses.Status.Server.PONG, (byte)(-125));
        PacketType.access$700(PacketTypeClasses.Status.Server.SERVER_INFO, (byte)(-124));
    }
}
