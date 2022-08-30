package xyz.edge.ac.checks.checks.aimbot;

import xyz.edge.ac.util.utils.MathUtil;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.ac.config.ConfigValue;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "AimBot (J)", type = "J", exemptBedrock = true, experimental = true)
public class AimBotJ extends EdgeCheck
{
    private static final ConfigValue threshold;
    private float lastPitchDifference;
    private int verbose;
    
    public AimBotJ(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_ROTATION()) {
            final float deltaPitch = this.user.getRotationHandler().getDeltaPitch();
            final float deltaYaw = this.user.getRotationHandler().getDeltaYaw();
            final float pitch = this.user.getRotationHandler().getPitch();
            if (deltaYaw == 0.0f && deltaPitch > 0.0f && deltaPitch < 1.0f && pitch != 90.0f) {
                final long gcd = MathUtil.getGcd((long)(deltaPitch * MathUtil.EXPANDER), (long)(this.lastPitchDifference * MathUtil.EXPANDER));
                this.debug("Baritone like rotations &7[&cG=" + gcd + "&7]");
                if (gcd < 131072L) {
                    this.verbose = Math.min(this.verbose + 1, 20);
                    if (this.verbose > AimBotJ.threshold.getInt()) {
                        this.fail("Baritone like rotations [3]", "GCD=" + gcd + " : P=" + pitch);
                        this.verbose = 0;
                    }
                }
                else {
                    this.verbose = Math.max(0, this.verbose - 1);
                }
            }
            this.lastPitchDifference = deltaPitch;
        }
    }
    
    static {
        threshold = new ConfigValue(ConfigValue.ValueType.INTEGER, "settings.threshold");
    }
}
