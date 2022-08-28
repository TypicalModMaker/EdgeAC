package xyz.edge.ac.packetevents.packetwrappers.play.out.login;

import xyz.edge.ac.packetevents.utils.player.GameMode;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

public class WrappedPacketOutLogin extends WrappedPacketEntityAbstraction
{
    private static boolean v_1_13_2;
    private static boolean v_1_17;
    
    public WrappedPacketOutLogin(final NMSPacket packet) {
        super(packet);
    }
    
    @Override
    protected void load() {
        WrappedPacketOutLogin.v_1_13_2 = WrappedPacketOutLogin.version.isNewerThanOrEquals(ServerVersion.v_1_13_2);
        WrappedPacketOutLogin.v_1_17 = WrappedPacketOutLogin.version.isNewerThanOrEquals(ServerVersion.v_1_17);
    }
    
    public boolean isHardcore() {
        return this.packet != null && this.readBoolean(0);
    }
    
    public void setHardcore(final boolean value) {
        if (this.packet != null) {
            this.writeBoolean(0, value);
        }
    }
    
    public GameMode getGameMode() {
        if (this.packet != null) {
            return this.readGameMode(0);
        }
        return null;
    }
    
    public void setGameMode(final GameMode gameMode) {
        if (this.packet != null) {
            this.writeGameMode(0, gameMode);
        }
    }
    
    public int getMaxPlayers() {
        if (this.packet != null) {
            final int index = (WrappedPacketOutLogin.v_1_13_2 && !WrappedPacketOutLogin.v_1_17) ? 1 : 2;
            return this.readInt(index);
        }
        return -1;
    }
}
