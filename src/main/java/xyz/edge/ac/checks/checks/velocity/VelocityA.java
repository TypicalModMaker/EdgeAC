package xyz.edge.ac.checks.checks.velocity;

import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.util.utils.MathUtil;
import java.util.Arrays;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Velocity (A)", experimental = true, type = "A", exemptBedrock = true)
public final class VelocityA extends EdgeCheck
{
    public VelocityA(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_FLYING() && this.user.getVelocityHandler().getTicksSinceVelocity() == 1) {
            final boolean isNethite = Arrays.toString(this.user.getPlayer().getInventory().getArmorContents()).contains("NETHERITE");
            final double velocityY = this.user.getVelocityHandler().getVelocityY();
            final double deltaY = this.user.getMovementHandler().getDeltaY();
            final double takenVelocity = MathUtil.preciseRound(deltaY / velocityY, 3);
            this.debug("Took lower then possible adjustment to vertical &8[&cR=" + Math.max(0.0, takenVelocity * 100.0) + "&8]");
            if (this.isExempt(Exempts.CREATIVE, Exempts.WEB, Exempts.JUMP, Exempts.NEAR_VEHICLE, Exempts.TELEPORT)) {
                return;
            }
            if (Math.abs(this.user.getVelocityHandler().getVelocityX()) < 0.005 || Math.abs(this.user.getVelocityHandler().getVelocityY()) < 0.005 || Math.abs(this.user.getVelocityHandler().getVelocityZ()) < 0.005) {
                return;
            }
            if (isNethite && Math.max(0.0, takenVelocity * 100.0) == 0.0) {
                return;
            }
            if (takenVelocity < 1.0 && !this.user.getMovementHandler().isBlockNearHead()) {
                final double buffer = this.buffer;
                this.buffer = buffer + 1.0;
                if (buffer > 2.0) {
                    this.fail("Took lower then possible adjustment to vertical", "R=" + Math.max(0.0, takenVelocity * 100.0) + " : TDY=" + deltaY);
                }
            }
            else {
                this.buffer = Math.max(0.0, this.buffer - 0.5);
            }
        }
    }
}
