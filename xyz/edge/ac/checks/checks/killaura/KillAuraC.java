package xyz.edge.ac.checks.checks.killaura;

import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "KillAura (C)", type = "C")
public final class KillAuraC extends EdgeCheck
{
    public KillAuraC(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_USE_ENTITY()) {
            final float deltaYaw = this.user.getRotationHandler().getDeltaYaw();
            final double deltaXZ = this.user.getMovementHandler().getDeltaXZ();
            final double squaredAccel = this.user.getMovementHandler().getAccelXZ() * 100.0;
            final boolean invalid = deltaYaw > 6.5f && squaredAccel < 0.02 && deltaXZ > 0.28 + (this.user.getVelocityHandler().isTakingVelocity() ? Math.abs(this.user.getVelocityHandler().getVelocityXZ()) : 0.0);
            final boolean isExempt = this.isExempt(Exempts.CLIMBABLE, Exempts.INSIDE_VEHICLE, Exempts.NEAR_VEHICLE);
            this.debug("Attack movement invalid &8[&cDXZ=" + deltaXZ + " : SA=" + squaredAccel + "&8]");
            if (invalid && !isExempt) {
                final double buffer = this.buffer + ((this.buffer < 175.0) ? 5.0 : 0.0);
                this.buffer = buffer;
                if (buffer > 5.0) {
                    this.fail("Attack movement invalid", "SA=" + squaredAccel + " : XZ=" + deltaXZ);
                }
            }
            else {
                this.buffer = Math.max(this.buffer - 0.5, 0.0);
            }
        }
    }
}
