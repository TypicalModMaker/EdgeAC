package xyz.edge.ac.checks.checks.invalid;

import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Invalid (G)", type = "G", experimental = true, exemptBedrock = true)
public class InvalidG extends EdgeCheck
{
    public InvalidG(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_FLYING()) {
            final boolean exemption = this.isExempt(Exempts.CHUNK, Exempts.TELEPORT_TIME, Exempts.CREATIVE, Exempts.FLYING) || this.user.getMovementHandler().isNearShulker();
            final boolean onGround = this.user.getMovementHandler().isOnGround();
            final boolean mathGround = this.user.getMovementHandler().isMathematicallyOnGround();
            this.debug("Shulker: " + this.user.getMovementHandler().isNearShulker() + " : Scaffolding: " + this.user.getMovementHandler().isNearScaffolding() + " : Honey: " + this.user.getMovementHandler().isNearHoneyBlock());
            if (onGround && !mathGround && !exemption) {
                final double buffer = this.buffer + 1.0;
                this.buffer = buffer;
                if (buffer > 3.0) {
                    this.fail("Tried to obey ground math", "OG=" + onGround + " : MG=" + mathGround + " : E=" + exemption);
                }
            }
            else {
                this.decreaseBufferBy(0.15);
            }
        }
    }
}
