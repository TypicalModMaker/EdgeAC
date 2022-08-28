package xyz.edge.ac.packetevents.event.eventtypes;

import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;

public abstract class CancellableNMSPacketEvent extends NMSPacketEvent implements CancellableEvent
{
    private boolean cancelled;
    
    public CancellableNMSPacketEvent(final Object channel, final NMSPacket packet) {
        super(channel, packet);
    }
    
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    @Override
    public void setCancelled(final boolean value) {
        this.cancelled = value;
    }
}
