package xyz.edge.ac.checks.checks.killaura;

import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "KillAura (BZ)", type = "BZ")
public class KillAuraBZ extends EdgeCheck
{
    public KillAuraBZ(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_USE_ENTITY()) {
            final double hitMissRatio = this.user.getFightHandler().getHitMissRatio();
            final double hitTicks = this.user.getFightHandler().getHitTicks();
            this.user.getRotationHandler().isCinematic();
        }
    }
}
