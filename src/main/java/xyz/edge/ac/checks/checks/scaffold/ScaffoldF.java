package xyz.edge.ac.checks.checks.scaffold;

import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Scaffold (F)", type = "F", exemptBedrock = true)
public class ScaffoldF extends EdgeCheck
{
    public ScaffoldF(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_BLOCKPLACE() && this.isBridging()) {
            final boolean isSneeking = this.user.getPacketActionHandler().isSneaking();
            final boolean onGround = this.user.getMovementHandler().isOnGround();
            final boolean jump = this.user.getMovementHandler().testJump();
            final double deltaXZ = this.user.getMovementHandler().getDeltaXZ();
            this.debug("Tried to move fast while sneaking &8[&cXZ=" + deltaXZ + "&8]");
            if (isSneeking && onGround && deltaXZ >= 0.22 && !jump) {
                this.fail("Tried to move fast while sneaking", "DXZ=" + deltaXZ);
            }
        }
    }
}
