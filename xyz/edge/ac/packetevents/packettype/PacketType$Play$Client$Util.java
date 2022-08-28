package xyz.edge.ac.packetevents.packettype;

public static class Util
{
    public static boolean isInstanceOfFlying(final byte packetID) {
        return packetID == -93 || packetID == -96 || packetID == -95 || packetID == -94;
    }
    
    public static boolean isBlockPlace(final byte packetID) {
        return PacketType.access$800() ? (packetID == -69) : (packetID == -68);
    }
}
