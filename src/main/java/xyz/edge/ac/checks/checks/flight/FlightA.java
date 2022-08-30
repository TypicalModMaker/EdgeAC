package xyz.edge.ac.checks.checks.flight;

import xyz.edge.ac.util.utils.MathUtil;
import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Flight (A)", type = "A")
public final class FlightA extends EdgeCheck
{
    private int streak;
    
    public FlightA(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_FLYING()) {
            final boolean exempt1 = this.isExempt(Exempts.FLYING, Exempts.TELEPORT, Exempts.VELOCITY, Exempts.NEAR_VEHICLE, Exempts.CHUNK, Exempts.CREATIVE);
            final double dy = this.user.getMovementHandler().getDeltaY();
            final boolean ground = this.user.getMovementHandler().isMathematicallyOnGround();
            this.debug("Tried to fly at invalid Y &8[&cDY=" + dy + "&8]");
            if (dy == 0.0 && !ground && !exempt1) {
                if (++this.streak > 10) {
                    this.fail("Tried to fly at invalid Y", "DY=" + dy);
                }
            }
            else {
                this.streak = 0;
            }
            final double lastDeltaY = this.user.getMovementHandler().getLastDeltaY();
            final double deltaY = this.user.getMovementHandler().getDeltaY();
            final double airTicks = this.user.getMovementHandler().getAirTicks();
            final double prediction = (lastDeltaY - 0.08) * 0.9800000190734863;
            this.debug("Tried to make invalid air movement &8[&cP=" + prediction + "&8]");
            if (this.user.getMovementHandler().getAirTicks() > 6) {
                final boolean exempt2 = this.isExempt(Exempts.FLYING, Exempts.CREATIVE, Exempts.TELEPORT_TIME, Exempts.SLAB, Exempts.SLIME, Exempts.NEAR_VEHICLE, Exempts.GLIDING, Exempts.ELYTA_TICK, Exempts.SLOW_FALL);
                final boolean exempt3 = this.user.getMovementHandler().isNearTrapDoor() || this.user.getMovementHandler().isOnSnow() || this.user.getMovementHandler().isLevitation();
                if (this.user.getMovementHandler().isWaterLogged()) {
                    return;
                }
                if (!MathUtil.isRoughlyEqual(deltaY, prediction, 0.001) && !exempt2 && !exempt3) {
                    final double buffer = this.buffer + 1.0;
                    this.buffer = buffer;
                    if (buffer > 3.0) {
                        this.fail("Tried to make invalid air movement", "P=" + prediction);
                    }
                }
                else {
                    this.buffer = Math.max(this.buffer - 0.5, 0.0);
                }
            }
            else {
                this.buffer = Math.max(this.buffer - 0.5, 0.0);
            }
        }
    }
}
