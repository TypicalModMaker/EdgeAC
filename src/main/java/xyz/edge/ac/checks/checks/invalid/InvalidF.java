package xyz.edge.ac.checks.checks.invalid;

import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.license.LicenseType;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Invalid (F)", type = "F", exemptBedrock = true, experimental = true, licenseType = LicenseType.ENTERPRISE)
public class InvalidF extends EdgeCheck
{
    public InvalidF(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_FLYING()) {
            final boolean exemption = this.isExempt(Exempts.CHUNK, Exempts.FLYING, Exempts.CREATIVE, Exempts.TELEPORT_TIME);
            final boolean inAirClient = this.user.getMovementHandler().isInAir();
            final boolean isMOG = this.user.getMovementHandler().isMathematicallyOnGround();
            if (inAirClient && isMOG && !exemption) {
                this.debug("HeHe Cheater... I hope", "IAC=" + inAirClient + " : isMOG=" + isMOG);
                final double buffer = this.buffer + 1.0;
                this.buffer = buffer;
                if (buffer > 5.0) {
                    this.fail("Tried to make impossible Y level movements", "IA=" + inAirClient + " : MOG=" + isMOG);
                }
            }
            else {
                this.decreaseBufferBy(0.25);
            }
        }
    }
}
