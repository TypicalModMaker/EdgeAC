package xyz.edge.ac.packetevents.event.impl;

import xyz.edge.ac.packetevents.event.PacketListenerAbstract;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Player;
import xyz.edge.ac.packetevents.event.eventtypes.PlayerEvent;
import xyz.edge.ac.packetevents.event.eventtypes.CancellableEvent;
import xyz.edge.ac.packetevents.event.PacketEvent;

public final class PlayerEjectEvent extends PacketEvent implements CancellableEvent, PlayerEvent
{
    private final Player player;
    private boolean cancelled;
    
    public PlayerEjectEvent(final Player player) {
        this.player = player;
    }
    
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    @Override
    public void setCancelled(final boolean value) {
        this.cancelled = value;
    }
    
    @NotNull
    @Override
    public Player getPlayer() {
        return this.player;
    }
    
    @Deprecated
    public boolean isAsync() {
        return false;
    }
    
    @Override
    public void call(final PacketListenerAbstract listener) {
        listener.onPlayerEject(this);
    }
    
    @Override
    public boolean isInbuilt() {
        return true;
    }
}
