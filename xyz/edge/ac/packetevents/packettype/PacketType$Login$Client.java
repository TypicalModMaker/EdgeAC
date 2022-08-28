package xyz.edge.ac.packetevents.packettype;

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
