package xyz.edge.ac.packetevents.packetwrappers.play.out.position;

public enum PlayerTeleportFlags
{
    X(1), 
    Y(2), 
    Z(4), 
    Y_ROT(8), 
    X_ROT(16);
    
    final byte maskFlag;
    
    private PlayerTeleportFlags(final int maskFlag) {
        this.maskFlag = (byte)maskFlag;
    }
}
