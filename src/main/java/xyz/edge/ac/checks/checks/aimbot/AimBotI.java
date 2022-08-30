package xyz.edge.ac.checks.checks.aimbot;

import xyz.edge.ac.util.utils.MathUtil;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "AimBot (I)", type = "I")
public class AimBotI extends EdgeCheck
{
    private float lastDeltaPitch;
    private boolean applied;
    private int rotations;
    private final long[] grid;
    
    public AimBotI(final User user) {
        super(user);
        this.lastDeltaPitch = 0.0f;
        this.applied = false;
        this.rotations = 0;
        this.grid = new long[10];
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_ROTATION()) {
            final float deltaYaw = this.user.getRotationHandler().getDeltaYaw();
            final float deltaPitch = this.user.getRotationHandler().getDeltaPitch();
            final boolean cinematic = this.user.getRotationHandler().isCinematic();
            final boolean attacking = this.user.getFightHandler().getLastAttackTick() < 10;
            final long deviation = this.getDeviation(deltaPitch);
            ++this.rotations;
            this.grid[this.rotations % this.grid.length] = deviation;
            if (deltaYaw > 0.0 && deltaPitch > 0.0 && deltaYaw < 30.0f && deltaPitch < 30.0f && !cinematic && attacking) {
                final boolean reached = this.rotations > this.grid.length;
                if (reached) {
                    double deviationMax = 0.0;
                    final long[] grid = this.grid;
                    for (int length = grid.length, i = 0; i < length; ++i) {
                        final double l = (double)grid[i];
                        if (deviation != 0L && l != 0.0) {
                            deviationMax = Math.max(Math.max(l, (double)deviation) % Math.min(l, (double)deviation), deviationMax);
                        }
                    }
                    this.debug("Constant GCD &8[&cD=" + deviation + "/" + deviationMax + "&8]");
                    if (deviationMax > 0.0 && deviation > 0.0) {
                        this.fail("Constant GCD", "D=" + deviation + "/" + deviationMax);
                        this.applied = false;
                    }
                }
            }
            this.lastDeltaPitch = deltaPitch;
        }
    }
    
    private long getDeviation(final float deltaPitch) {
        final long expandedPitch = (long)(deltaPitch * MathUtil.EXPANDER);
        final long previousExpandedPitch = (long)(this.lastDeltaPitch * MathUtil.EXPANDER);
        final long result = this.applied ? MathUtil.getGcd(expandedPitch, previousExpandedPitch) : 0L;
        if (this.applied) {
            this.applied = false;
            return result;
        }
        return 0L;
    }
}
