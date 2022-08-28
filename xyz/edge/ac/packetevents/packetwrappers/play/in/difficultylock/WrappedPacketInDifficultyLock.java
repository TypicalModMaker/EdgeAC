package xyz.edge.ac.packetevents.packetwrappers.play.in.difficultylock;

import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public final class WrappedPacketInDifficultyLock extends WrappedPacket
{
    public WrappedPacketInDifficultyLock(final NMSPacket packet) {
        super(packet);
    }
    
    public boolean isLocked() {
        return this.readBoolean(0);
    }
    
    public void setLocked(final boolean locked) {
        this.writeBoolean(0, locked);
    }
    
    @Override
    public boolean isSupported() {
        return WrappedPacketInDifficultyLock.version.isNewerThan(ServerVersion.v_1_15_2);
    }
}
