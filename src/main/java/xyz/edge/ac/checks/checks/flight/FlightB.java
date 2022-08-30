package xyz.edge.ac.checks.checks.flight;

import xyz.edge.ac.util.exempts.type.Exempts;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.ac.config.ConfigValue;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Flight (B)", type = "B", exemptBedrock = true)
public final class FlightB extends EdgeCheck
{
    private static final ConfigValue threshold;
    private static final ConfigValue decay;
    private double lastDeltaY;
    private double buffer;
    private int airTicks;
    
    public FlightB(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_POSITION()) {
            final WrappedPacketInFlying wrapper = new WrappedPacketInFlying(packet.getRawPacket());
            final boolean onGround = this.user.getMovementHandler().isOnGround();
            final boolean inAir = this.user.getMovementHandler().getAirTicks() > 3;
            final boolean mathGround = this.user.getMovementHandler().isMathematicallyOnGround();
            final boolean invalid = onGround && inAir && !mathGround;
            final boolean positionGround = wrapper.getPosition().getY() % 0.015625 == 0.0;
            final boolean packetGround = wrapper.isOnGround();
            final boolean exempt = this.isExempt(Exempts.NEAR_VEHICLE, Exempts.FLYING, Exempts.JOINED, Exempts.SLIME, Exempts.STAIRS, Exempts.SLAB, Exempts.CLIMBABLE, Exempts.GLIDING, Exempts.CREATIVE, Exempts.TELEPORT_TIME);
            if (this.user.getMovementHandler().isOnVines()) {
                return;
            }
            this.debug("Server and client location doesn't match &8[&cG=" + onGround + " : PG=" + packetGround + " : MG=" + mathGround + "&8]");
            if (!exempt && positionGround != packetGround) {
                final double buffer = this.buffer + 1.0;
                this.buffer = buffer;
                if (buffer > FlightB.threshold.getInt()) {
                    this.fail("Server and client location doesn't match", "G=" + onGround + " : PG=" + packetGround + " : MG=" + mathGround);
                }
            }
            else {
                this.buffer = Math.max(this.buffer - FlightB.decay.getDouble(), 0.0);
            }
        }
    }
    
    static {
        threshold = new ConfigValue(ConfigValue.ValueType.INTEGER, "settings.threshold");
        decay = new ConfigValue(ConfigValue.ValueType.DOUBLE, "settings.decay");
    }
}
