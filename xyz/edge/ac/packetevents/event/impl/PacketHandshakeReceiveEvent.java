package xyz.edge.ac.packetevents.event.impl;

import xyz.edge.ac.packetevents.event.PacketListenerAbstract;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.event.eventtypes.CancellableNMSPacketEvent;

public class PacketHandshakeReceiveEvent extends CancellableNMSPacketEvent
{
    public PacketHandshakeReceiveEvent(final Object channel, final NMSPacket packet) {
        super(channel, packet);
    }
    
    @Override
    public void call(final PacketListenerAbstract listener) {
        if (listener.clientSidedLoginAllowance == null || listener.clientSidedLoginAllowance.contains(this.getPacketId())) {
            listener.onPacketHandshakeReceive(this);
        }
    }
}
