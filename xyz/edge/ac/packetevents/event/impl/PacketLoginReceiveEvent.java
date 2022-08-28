package xyz.edge.ac.packetevents.event.impl;

import xyz.edge.ac.packetevents.event.PacketListenerAbstract;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.event.eventtypes.CancellableNMSPacketEvent;

public class PacketLoginReceiveEvent extends CancellableNMSPacketEvent
{
    public PacketLoginReceiveEvent(final Object channel, final NMSPacket packet) {
        super(channel, packet);
    }
    
    @Override
    public void call(final PacketListenerAbstract listener) {
        if (listener.clientSidedLoginAllowance == null || listener.clientSidedLoginAllowance.contains(this.getPacketId())) {
            listener.onPacketLoginReceive(this);
        }
    }
}
