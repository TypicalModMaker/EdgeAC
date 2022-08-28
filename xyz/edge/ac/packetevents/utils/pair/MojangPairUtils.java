package xyz.edge.ac.packetevents.utils.pair;

public class MojangPairUtils
{
    public static Pair<Object, Object> extractPair(final Object mojangPairObj) {
        final com.mojang.datafixers.util.Pair<Object, Object> mojangPair = (com.mojang.datafixers.util.Pair<Object, Object>)mojangPairObj;
        return Pair.of(mojangPair.getFirst(), mojangPair.getSecond());
    }
    
    public static Object getMojangPair(final Object first, final Object second) {
        return new com.mojang.datafixers.util.Pair(first, second);
    }
    
    public static Object getMojangPair(final Pair<Object, Object> pair) {
        return getMojangPair(pair.getFirst(), pair.getSecond());
    }
}
