package xyz.edge.ac.packetevents.packettype;

public static class Client
{
    public static final byte START = -127;
    public static final byte PING = -126;
    
    private static void load() {
        PacketType.access$700(PacketTypeClasses.Status.Client.START, (byte)(-127));
        PacketType.access$700(PacketTypeClasses.Status.Client.PING, (byte)(-126));
    }
}
