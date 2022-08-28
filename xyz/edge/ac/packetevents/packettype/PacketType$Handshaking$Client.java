package xyz.edge.ac.packetevents.packettype;

public static class Client
{
    public static final byte SET_PROTOCOL = -123;
    
    private static void load() {
        PacketType.access$700(PacketTypeClasses.Handshaking.Client.SET_PROTOCOL, (byte)(-123));
    }
}
