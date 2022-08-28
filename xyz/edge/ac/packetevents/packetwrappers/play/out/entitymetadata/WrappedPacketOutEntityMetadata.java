package xyz.edge.ac.packetevents.packetwrappers.play.out.entitymetadata;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

public class WrappedPacketOutEntityMetadata extends WrappedPacketEntityAbstraction
{
    public WrappedPacketOutEntityMetadata(final NMSPacket packet) {
        super(packet);
    }
    
    public List<WrappedWatchableObject> getWatchableObjects() {
        final List<Object> nmsWatchableObjectList = this.readList(0);
        if (nmsWatchableObjectList == null) {
            return new ArrayList<WrappedWatchableObject>();
        }
        final List<WrappedWatchableObject> wrappedWatchableObjects = new ArrayList<WrappedWatchableObject>(nmsWatchableObjectList.size());
        for (final Object watchableObject : nmsWatchableObjectList) {
            wrappedWatchableObjects.add(new WrappedWatchableObject(new NMSPacket(watchableObject)));
        }
        return wrappedWatchableObjects;
    }
}
