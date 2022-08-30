package xyz.edge.ac.checks.checks.aimbot;

import xyz.edge.ac.user.impl.Rotations;
import xyz.edge.ac.util.utils.MathUtil;
import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "AimBot (H)", type = "H")
public class AimBotH extends EdgeCheck
{
    public AimBotH(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_ROTATION() && System.currentTimeMillis() - this.user.getFightHandler().getLastAttack() < 850L) {
            final Rotations processor = this.user.getRotationHandler();
            final float deltaPitch = processor.getDeltaPitch();
            final float lastDeltaPitch = processor.getLastDeltaPitch();
            if (deltaPitch > 1.0f && !this.isExempt(Exempts.TELEPORT)) {
                final long expanded = (long)(deltaPitch * MathUtil.EXPANDER);
                final long lastExpanded = (long)(lastDeltaPitch * MathUtil.EXPANDER);
                final long gcd = MathUtil.getGcd(expanded, lastExpanded);
                final double divisor = gcd / MathUtil.EXPANDER;
                final double moduloPitch = Math.abs(processor.getPitch() % divisor);
                this.debug("GCD Invalid &8[&cMP=" + moduloPitch + " : E=" + expanded + "&8]");
                if (moduloPitch < 1.2E-5) {
                    final double buffer = this.buffer;
                    this.buffer = buffer + 1.0;
                    if (buffer > 2.0) {
                        this.fail("GCD Invalid", "MP=" + moduloPitch + " : E=" + expanded);
                    }
                }
            }
            else {
                this.decreaseBufferBy(0.5);
            }
        }
    }
}
