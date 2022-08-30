package xyz.edge.ac.checks.checks.badpacket;

import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "BadPacket (H)", type = "H")
public class BadPacketH extends EdgeCheck
{
    private int predictedID;
    
    public BadPacketH(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.isTick()) {
            final int actionNumber = packet.getTransactionID();
            final boolean joined = this.isExempt(Exempts.JOINED, Exempts.TPS);
            if (this.predictedID != -91 && Math.abs(actionNumber - this.predictedID) > 3 && !joined) {
                this.fail("Invalid predicted ID", "PID=" + this.predictedID + " != " + actionNumber);
            }
            this.predictedID = actionNumber;
            --this.predictedID;
            if (this.predictedID < -32768) {
                this.predictedID = -1;
            }
        }
    }
}
