package xyz.edge.ac.packetevents.packetwrappers.play.in.difficultychange;

import xyz.edge.ac.packetevents.utils.world.Difficulty;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public final class WrappedPacketInDifficultyChange extends WrappedPacket
{
    public WrappedPacketInDifficultyChange(final NMSPacket packet) {
        super(packet);
    }
    
    public Difficulty getDifficulty() {
        return this.readDifficulty(0);
    }
    
    public void setDifficulty(final Difficulty difficulty) {
        this.writeDifficulty(0, difficulty);
    }
}
