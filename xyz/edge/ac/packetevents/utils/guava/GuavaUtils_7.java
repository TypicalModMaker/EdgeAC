package xyz.edge.ac.packetevents.utils.guava;

import net.minecraft.util.com.google.common.collect.MapMaker;
import java.util.concurrent.ConcurrentMap;

class GuavaUtils_7
{
    static <T, K> ConcurrentMap<T, K> makeMap() {
        return new MapMaker().weakValues().makeMap();
    }
}
