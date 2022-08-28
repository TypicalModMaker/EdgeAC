package xyz.edge.ac.packetevents.event.impl;

import xyz.edge.ac.packetevents.event.PacketListenerAbstract;
import xyz.edge.ac.packetevents.utils.player.ClientVersion;
import xyz.edge.ac.packetevents.utils.netty.channel.ChannelUtils;
import java.net.InetSocketAddress;
import xyz.edge.ac.packetevents.PacketEvents;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Player;
import xyz.edge.ac.packetevents.event.eventtypes.PlayerEvent;
import xyz.edge.ac.packetevents.event.PacketEvent;

public class PostPlayerInjectEvent extends PacketEvent implements PlayerEvent
{
    private final Player player;
    private final boolean async;
    
    public PostPlayerInjectEvent(final Player player, final boolean async) {
        this.player = player;
        this.async = async;
    }
    
    @NotNull
    @Override
    public Player getPlayer() {
        return this.player;
    }
    
    @NotNull
    public Object getChannel() {
        return PacketEvents.get().getPlayerUtils().getChannel(this.player);
    }
    
    @NotNull
    public InetSocketAddress getSocketAddress() {
        return ChannelUtils.getSocketAddress(this.getChannel());
    }
    
    @NotNull
    public ClientVersion getClientVersion() {
        return PacketEvents.get().getPlayerUtils().getClientVersion(this.player);
    }
    
    public boolean isAsync() {
        return this.async;
    }
    
    @Override
    public void call(final PacketListenerAbstract listener) {
        listener.onPostPlayerInject(this);
    }
    
    @Override
    public boolean isInbuilt() {
        return true;
    }
}
