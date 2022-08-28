package xyz.edge.ac.checks.checks.velocity;

import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Velocity (B)", type = "B", exemptBedrock = true, experimental = true)
public class VelocityB extends EdgeCheck
{
    private int predictedID;
    
    public VelocityB(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
    }
}
