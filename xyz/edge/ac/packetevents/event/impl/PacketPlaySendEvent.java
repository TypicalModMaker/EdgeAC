package xyz.edge.ac.packetevents.event.impl;

import xyz.edge.ac.packetevents.event.PacketListenerAbstract;
import org.jetbrains.annotations.NotNull;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import org.bukkit.entity.Player;
import xyz.edge.ac.packetevents.event.eventtypes.PostTaskEvent;
import xyz.edge.ac.packetevents.event.eventtypes.PlayerEvent;
import xyz.edge.ac.packetevents.event.eventtypes.CancellableNMSPacketEvent;

public final class PacketPlaySendEvent extends CancellableNMSPacketEvent implements PlayerEvent, PostTaskEvent
{
    private final Player player;
    private Runnable postTask;
    
    public PacketPlaySendEvent(final Player player, final Object channel, final NMSPacket packet) {
        super(channel, packet);
        this.player = player;
    }
    
    @NotNull
    @Override
    public Player getPlayer() {
        return this.player;
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
        if (listener.serverSidedPlayAllowance == null || listener.serverSidedPlayAllowance.contains(this.getPacketId())) {
            listener.onPacketPlaySend(this);
        }
    }
}
