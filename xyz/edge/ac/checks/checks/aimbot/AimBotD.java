package xyz.edge.ac.checks.checks.aimbot;

import java.util.Collection;
import xyz.edge.ac.util.utils.MathUtil;
import xyz.edge.ac.packetutils.PacketUtil;
import java.util.ArrayList;
import xyz.edge.ac.user.User;
import java.util.List;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "AimBot (D)", type = "D")
public class AimBotD extends EdgeCheck
{
    private double lastSTD;
    private double lastDeltaYaw;
    private final List<Double> deltaYawList;
    
    public AimBotD(final User user) {
        super(user);
        this.deltaYawList = new ArrayList<Double>();
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_POSITION() && packet.PACKET_USE_ENTITY()) {
            final double yaw = MathUtil.wrapAngleTo180_float(this.user.getRotationHandler().getYaw());
            if (yaw > 150.0) {
                this.deltaYawList.add(yaw);
                if (this.deltaYawList.size() >= 25) {
                    final double std = MathUtil.getStandardDeviation(this.deltaYawList);
                    this.debug("Rotation movements &8[&cSTD=" + std + " : LS=" + this.deltaYawList.size() + "&8]");
                    if (std < 0.03 || (Math.abs(std - this.lastSTD) < 0.001 && yaw > 155.0)) {
                        this.fail("Rotation movements", "STD=" + std + " : LS=" + this.deltaYawList.size());
                    }
                    this.lastSTD = std;
                    this.deltaYawList.clear();
                }
            }
            this.lastDeltaYaw = yaw;
        }
    }
}
