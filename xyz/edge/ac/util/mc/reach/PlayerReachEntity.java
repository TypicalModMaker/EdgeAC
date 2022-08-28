package xyz.edge.ac.util.mc.reach;

import xyz.edge.ac.packetevents.utils.player.ClientVersion;
import xyz.edge.ac.user.User;
import xyz.edge.ac.packetevents.utils.vector.Vector3d;

public class PlayerReachEntity
{
    public Vector3d serverPos;
    public ReachInterpolationData oldPacketLocation;
    public ReachInterpolationData newPacketLocation;
    public int lastTransactionHung;
    public int removeTrans;
    
    public PlayerReachEntity(final double x, final double y, final double z, final User player) {
        this.removeTrans = Integer.MAX_VALUE;
        this.serverPos = new Vector3d(x, y, z);
        this.newPacketLocation = new ReachInterpolationData(GetBoundingBox.getBoundingBoxFromPosAndSize(x, y, z, 0.6, 1.8), this.serverPos.getX(), this.serverPos.getY(), this.serverPos.getZ(), player.getVersion().isNewerThanOrEquals(ClientVersion.v_1_9));
    }
    
    public void onFirstTransaction(final double x, final double y, final double z, final User player) {
        this.oldPacketLocation = this.newPacketLocation;
        this.newPacketLocation = new ReachInterpolationData(this.oldPacketLocation.getPossibleLocationCombined(), x, y, z, player.getVersion().isNewerThanOrEquals(ClientVersion.v_1_9));
    }
    
    public void onSecondTransaction() {
        this.oldPacketLocation = null;
    }
    
    public void onMovement() {
        this.newPacketLocation.tickMovement(this.oldPacketLocation == null);
        if (this.oldPacketLocation != null) {
            this.oldPacketLocation.tickMovement(true);
            this.newPacketLocation.updatePossibleStartingLocation(this.oldPacketLocation.getPossibleLocationCombined());
        }
    }
    
    public SimpleCollisionBox getPossibleCollisionBoxes() {
        if (this.oldPacketLocation == null) {
            return this.newPacketLocation.getPossibleLocationCombined();
        }
        return ReachInterpolationData.combineCollisionBox(this.oldPacketLocation.getPossibleLocationCombined(), this.newPacketLocation.getPossibleLocationCombined());
    }
    
    public void setDestroyed(final int trans) {
        if (this.removeTrans != Integer.MAX_VALUE) {
            return;
        }
        this.removeTrans = trans;
    }
    
    @Override
    public String toString() {
        return "PlayerReachEntity{serverPos=" + this.serverPos + ", oldPacketLocation=" + this.oldPacketLocation + ", newPacketLocation=" + this.newPacketLocation + '}';
    }
}
