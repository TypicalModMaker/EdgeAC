package xyz.edge.ac.packetevents.packetwrappers.play.in.teleportaccept;

import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInTeleportAccept extends WrappedPacket
{
    public WrappedPacketInTeleportAccept(final NMSPacket packet) {
        super(packet);
    }
    
    public int getTeleportId() {
        return this.readInt(0);
    }
    
    public void setTeleportId(final int teleportId) {
        this.writeInt(0, teleportId);
    }
}
