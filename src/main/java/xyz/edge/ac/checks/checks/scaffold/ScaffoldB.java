package xyz.edge.ac.checks.checks.scaffold;

import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Scaffold (B)", type = "B")
public final class ScaffoldB extends EdgeCheck
{
    private boolean placedBlock;
    
    public ScaffoldB(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_FLYING()) {
            if (this.placedBlock && this.isBridging()) {
                final double deltaXZ = this.user.getMovementHandler().getDeltaXZ();
                final double lastDeltaXZ = this.user.getMovementHandler().getLastDeltaXZ();
                final double accel = Math.abs(deltaXZ - lastDeltaXZ);
                final float deltaYaw = this.user.getRotationHandler().getDeltaYaw() % 360.0f;
                final float deltaPitch = this.user.getRotationHandler().getDeltaPitch();
                final boolean invalid = deltaYaw > 75.0f && deltaPitch > 15.0f && accel < 0.15;
                final boolean exempt = this.isExempt(Exempts.FLYING, Exempts.CREATIVE);
                this.debug("Impossible placement movements &8[&cA=" + accel + " : XZ=" + deltaXZ + "&8]");
                if (invalid && this.isBridging() && !exempt) {
                    if (this.increaseBuffer() > 2.0) {
                        this.fail("Impossible placement movements", "A=" + accel + " : DXZ=" + deltaXZ);
                    }
                }
                else {
                    this.decreaseBufferBy(0.5);
                }
            }
            this.placedBlock = false;
        }
        else if (packet.PACKET_BLOCKPLACE() && this.user.getPlayer().getItemInHand().getType().isBlock()) {
            this.placedBlock = true;
        }
    }
}
