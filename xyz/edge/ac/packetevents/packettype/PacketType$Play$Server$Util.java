package xyz.edge.ac.packetevents.packettype;

public static class Util
{
    public static boolean isInstanceOfEntity(final byte packetID) {
        return packetID == -23 || packetID == -26 || packetID == -25 || packetID == -24;
    }
}
