package xyz.edge.ac.packetevents.packetwrappers.play.in.spectate;

import java.util.UUID;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInSpectate extends WrappedPacket
{
    public WrappedPacketInSpectate(final NMSPacket packet) {
        super(packet);
    }
    
    public UUID getUUID() {
        return this.readObject(0, (Class<? extends UUID>)UUID.class);
    }
    
    public void setUUID(final UUID uuid) {
        this.writeObject(0, uuid);
    }
}
