package xyz.edge.ac.packetevents.utils.guava;

import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.PacketEvents;
import java.util.concurrent.ConcurrentMap;

public class GuavaUtils
{
    public static <T, K> ConcurrentMap<T, K> makeMap() {
        if (PacketEvents.get().getServerUtils().getVersion().isNewerThan(ServerVersion.v_1_7_10)) {
            return GuavaUtils_8.makeMap();
        }
        return GuavaUtils_7.makeMap();
    }
}
