package xyz.edge.ac.checks.checks.flight;

import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Flight (C)", type = "C")
public final class FlightC extends EdgeCheck
{
    private double lastDeltaY;
    private double buffer;
    private int airTicks;
    
    public FlightC(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_FLYING()) {
            final double deltaY = this.user.getMovementHandler().getDeltaY();
            final double accelY = this.user.getMovementHandler().getAccelY();
            final double ticks = this.user.getMovementHandler().getAirTicks();
            final double motionY = this.user.getMovementHandler().getMotionY();
            if (this.isExempt(Exempts.FLYING, Exempts.CREATIVE, Exempts.TELEPORT, Exempts.ICE, Exempts.INSIDE_VEHICLE, Exempts.CLIMBABLE, Exempts.UNDER_BLOCK, Exempts.GLIDING, Exempts.SLOW_FALL)) {
                return;
            }
            if (this.user.getMovementHandler().isLevitation()) {
                return;
            }
            if (this.user.getVelocityHandler().isTakingVelocity()) {
                return;
            }
            final boolean touchingAir = this.user.getMovementHandler().isInAir();
            if (touchingAir) {
                ++this.airTicks;
                if (this.airTicks > 9) {
                    final double estimation = this.lastDeltaY * 0.9800000190734863 - 0.08;
                    this.debug("Impossible air accel prediction &8[&cP=" + estimation + " : Y=" + deltaY + "&8]");
                    if (Math.abs(deltaY + 0.0980000019) < 0.005) {
                        this.buffer = 0.0;
                        return;
                    }
                    if (estimation == -0.08 && deltaY == 0.0) {
                        return;
                    }
                    if (Math.abs(estimation - deltaY) > 0.002) {
                        this.buffer += 1.5;
                        if (this.buffer > 8.0) {
                            this.fail("Impossible air accel prediction", "P=" + estimation + " : Y=" + deltaY);
                        }
                    }
                    else {
                        this.buffer = Math.max(0.0, this.buffer - 1.25);
                    }
                }
            }
            else {
                this.airTicks = 0;
                this.buffer = Math.max(0.0, this.buffer - 10.0);
            }
            this.lastDeltaY = deltaY;
        }
    }
}
