package xyz.edge.ac.packetevents.event.impl;

import xyz.edge.ac.packetevents.event.PacketListenerAbstract;
import org.jetbrains.annotations.Nullable;
import xyz.edge.ac.packetevents.utils.netty.channel.ChannelUtils;
import xyz.edge.ac.packetevents.PacketEvents;
import java.net.InetSocketAddress;
import org.bukkit.entity.Player;
import xyz.edge.ac.packetevents.event.eventtypes.PlayerEvent;
import xyz.edge.ac.packetevents.event.eventtypes.CancellableEvent;
import xyz.edge.ac.packetevents.event.PacketEvent;

public final class PlayerInjectEvent extends PacketEvent implements CancellableEvent, PlayerEvent
{
    private final Player player;
    private final InetSocketAddress address;
    private boolean cancelled;
    
    public PlayerInjectEvent(final Player player) {
        this.player = player;
        this.address = ChannelUtils.getSocketAddress(PacketEvents.get().getPlayerUtils().getChannel(player));
    }
    
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    @Override
    public void setCancelled(final boolean value) {
        this.cancelled = value;
    }
    
    @Nullable
    @Override
    public Player getPlayer() {
        return this.player;
    }
    
    @Nullable
    public InetSocketAddress getSocketAddress() {
        return this.address;
    }
    
    @Deprecated
    public boolean isAsync() {
        return false;
    }
    
    @Override
    public void call(final PacketListenerAbstract listener) {
        listener.onPlayerInject(this);
    }
    
    @Override
    public boolean isInbuilt() {
        return true;
    }
}
