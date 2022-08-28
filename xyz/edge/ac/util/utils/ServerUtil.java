package xyz.edge.ac.util.utils;

import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.PacketEvents;

public final class ServerUtil
{
    public static double getTPS() {
        return PacketEvents.get().getServerUtils().getTPS();
    }
    
    public static ServerVersion getServerVersion() {
        return PacketEvents.get().getServerUtils().getVersion();
    }
    
    public static boolean isLowerThan1_8() {
        return getServerVersion().isLowerThan(ServerVersion.v_1_8);
    }
    
    public static boolean isHigherThan1_13_2() {
        return getServerVersion().isHigherThan(ServerVersion.v_1_13_2);
    }
    
    private ServerUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
