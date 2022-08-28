package xyz.edge.ac.packetevents.packetwrappers.play.out.respawn;

import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import org.bukkit.WorldType;
import xyz.edge.ac.packetevents.utils.player.GameMode;
import xyz.edge.ac.packetevents.utils.world.Difficulty;
import xyz.edge.ac.packetevents.utils.world.Dimension;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

class WrappedPacketOutRespawn extends WrappedPacket
{
    private Dimension dimension;
    private Difficulty difficulty;
    private GameMode gameMode;
    private WorldType levelType;
    
    public WrappedPacketOutRespawn(final NMSPacket packet) {
        super(packet);
    }
    
    @Override
    protected void load() {
    }
    
    public Dimension getDimension() {
        if (this.packet != null) {
            return this.readDimension(0, 0);
        }
        return this.dimension;
    }
    
    public void setDimension(final Dimension dimension) {
        if (this.packet != null) {
            this.writeDimension(0, 0, dimension);
        }
        else {
            this.dimension = dimension;
        }
    }
    
    public GameMode getGameMode() {
        if (this.packet != null) {
            return this.readGameMode(0);
        }
        return this.gameMode;
    }
    
    public void setGameMode(final GameMode gameMode) {
        if (this.packet != null) {
            this.writeGameMode(0, gameMode);
        }
        else {
            this.gameMode = gameMode;
        }
    }
}
