package xyz.edge.ac.packetevents.event.impl;

import xyz.edge.ac.packetevents.event.PacketListenerAbstract;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import org.bukkit.entity.Player;
import xyz.edge.ac.packetevents.event.eventtypes.PlayerEvent;
import xyz.edge.ac.packetevents.event.eventtypes.NMSPacketEvent;

public class PostPacketPlayReceiveEvent extends NMSPacketEvent implements PlayerEvent
{
    private final Player player;
    
    public PostPacketPlayReceiveEvent(final Player player, final Object channel, final NMSPacket packet) {
        super(channel, packet);
        this.player = player;
    }
    
    @Override
    public Player getPlayer() {
        return this.player;
    }
    
    @Override
    public void call(final PacketListenerAbstract listener) {
        if (listener.clientSidedPlayAllowance == null || listener.clientSidedPlayAllowance.contains(this.getPacketId())) {
            listener.onPostPacketPlayReceive(this);
        }
    }
}
