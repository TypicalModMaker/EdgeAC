package xyz.edge.ac.packetevents.event.eventtypes;

public interface CancellableEvent
{
    boolean isCancelled();
    
    void setCancelled(final boolean p0);
}
