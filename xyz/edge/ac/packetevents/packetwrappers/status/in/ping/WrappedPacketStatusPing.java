package xyz.edge.ac.packetevents.packetwrappers.status.in.ping;

import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketStatusPing extends WrappedPacket
{
    public WrappedPacketStatusPing(final NMSPacket packet) {
        super(packet);
    }
    
    public long getPayload() {
        return this.readLong(0);
    }
    
    public void setPayload(final long payload) {
        this.writeLong(0, payload);
    }
}
