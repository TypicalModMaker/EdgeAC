package xyz.edge.ac.checks.checks.scaffold;

import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.license.LicenseType;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Scaffold (X)", type = "X", exemptBedrock = true, punish = false, experimental = true, licenseType = LicenseType.ENTERPRISE)
public class ScaffoldX extends EdgeCheck
{
    public ScaffoldX(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_BLOCKPLACE()) {
            final double accelXZ = this.user.getMovementHandler().getAccelXZ();
            final double lastAccelXZ = this.user.getMovementHandler().getLastAccelXZ();
            final double prediction = (lastAccelXZ - 0.08) * 0.9800000190734863;
            if (this.isBridging()) {
                this.debug("P=" + prediction);
                if (prediction == -0.0784000015258789) {
                    this.fail("", "P= " + prediction);
                }
            }
        }
    }
}
