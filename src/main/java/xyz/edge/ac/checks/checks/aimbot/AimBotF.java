package xyz.edge.ac.checks.checks.aimbot;

import java.util.Collection;
import xyz.edge.ac.util.utils.MathUtil;
import xyz.edge.ac.packetutils.PacketUtil;
import com.google.common.collect.Lists;
import xyz.edge.ac.user.User;
import java.util.Deque;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "AimBot (F)", type = "F")
public class AimBotF extends EdgeCheck
{
    private final Deque<Float> samples;
    
    public AimBotF(final User user) {
        super(user);
        this.samples = Lists.newLinkedList();
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_ROTATION()) {
            final float deltaYaw = this.user.getRotationHandler().getDeltaYaw();
            final float deltaPitch = this.user.getRotationHandler().getDeltaPitch();
            final boolean cinematic = this.user.getRotationHandler().isCinematic();
            final boolean attacking = this.user.getFightHandler().getLastAttackTick() < 2;
            if (deltaYaw > 0.0 && deltaPitch > 0.0 && deltaYaw < 30.0f && deltaPitch < 30.0f && !cinematic && attacking) {
                this.samples.add(deltaPitch);
            }
            if (this.samples.size() == 120) {
                final int distinct = MathUtil.getDistinct(this.samples);
                final int duplicates = this.samples.size() - distinct;
                final double average = this.samples.stream().mapToDouble(d -> d).average().orElse(0.0);
                this.debug("Duplicated rotations &8[&cD=" + duplicates + "&8]");
                if (duplicates <= 9 && average < 30.0 && distinct > 130) {
                    final double buffer = this.buffer + 1.0;
                    this.buffer = buffer;
                    if (buffer > 3.0) {
                        this.fail("Duplicated rotations", "D=" + duplicates);
                    }
                }
                else {
                    this.buffer = Math.max(this.buffer - 3.0, 0.0);
                }
                this.samples.clear();
            }
        }
    }
}
