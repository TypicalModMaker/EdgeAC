package xyz.edge.ac.util.ticking;

import java.util.Iterator;
import io.github.retrooper.packetevents.packetwrappers.play.out.entitymetadata.WrappedWatchableObject;
import java.util.List;

public class WatchableIndexUtil
{
    public static WrappedWatchableObject getIndex(final List<WrappedWatchableObject> objects, final int index) {
        for (final WrappedWatchableObject object : objects) {
            if (object.getIndex() == index) {
                return object;
            }
        }
        return null;
    }
}
