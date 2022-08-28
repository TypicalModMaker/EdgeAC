package xyz.edge.ac.packetevents.event.impl;

import xyz.edge.ac.packetevents.event.PacketListenerAbstract;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.event.eventtypes.CancellableNMSPacketEvent;

public class PacketStatusReceiveEvent extends CancellableNMSPacketEvent
{
    public PacketStatusReceiveEvent(final Object channel, final NMSPacket packet) {
        super(channel, packet);
    }
    
    @Override
    public void call(final PacketListenerAbstract listener) {
        if (listener.clientSidedStatusAllowance == null || listener.clientSidedStatusAllowance.contains(this.getPacketId())) {
            listener.onPacketStatusReceive(this);
        }
    }
}
