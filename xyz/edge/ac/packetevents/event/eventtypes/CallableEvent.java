package xyz.edge.ac.packetevents.event.eventtypes;

import xyz.edge.ac.packetevents.event.PacketListenerAbstract;

public interface CallableEvent
{
    void call(final PacketListenerAbstract p0);
}
