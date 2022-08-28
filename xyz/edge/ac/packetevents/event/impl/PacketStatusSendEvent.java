package xyz.edge.ac.packetevents.event.impl;

import xyz.edge.ac.packetevents.event.PacketListenerAbstract;
import org.jetbrains.annotations.NotNull;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.event.eventtypes.PostTaskEvent;
import xyz.edge.ac.packetevents.event.eventtypes.CancellableNMSPacketEvent;

public class PacketStatusSendEvent extends CancellableNMSPacketEvent implements PostTaskEvent
{
    private Runnable postTask;
    
    public PacketStatusSendEvent(final Object channel, final NMSPacket packet) {
        super(channel, packet);
    }
    
    @Override
    public boolean isPostTaskAvailable() {
        return this.postTask != null;
    }
    
    @Override
    public Runnable getPostTask() {
        return this.postTask;
    }
    
    @Override
    public void setPostTask(@NotNull final Runnable postTask) {
        this.postTask = postTask;
    }
    
    @Override
    public void call(final PacketListenerAbstract listener) {
        if (listener.serverSidedStatusAllowance == null || listener.serverSidedStatusAllowance.contains(this.getPacketId())) {
            listener.onPacketStatusSend(this);
        }
    }
}
