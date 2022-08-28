package xyz.edge.ac.packetevents.event.impl;

import xyz.edge.ac.packetevents.event.PacketListenerAbstract;
import org.jetbrains.annotations.NotNull;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import org.bukkit.entity.Player;
import xyz.edge.ac.packetevents.event.eventtypes.PlayerEvent;
import xyz.edge.ac.packetevents.event.eventtypes.CancellableNMSPacketEvent;

public final class PacketPlayReceiveEvent extends CancellableNMSPacketEvent implements PlayerEvent
{
    private final Player player;
    
    public PacketPlayReceiveEvent(final Player player, final Object channel, final NMSPacket packet) {
        super(channel, packet);
        this.player = player;
    }
    
    @NotNull
    @Override
    public Player getPlayer() {
        return this.player;
    }
    
    @Override
    public void call(final PacketListenerAbstract listener) {
        if (listener.clientSidedPlayAllowance == null || listener.clientSidedPlayAllowance.contains(this.getPacketId())) {
            listener.onPacketPlayReceive(this);
        }
    }
}
