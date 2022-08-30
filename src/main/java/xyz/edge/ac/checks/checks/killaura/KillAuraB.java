package xyz.edge.ac.checks.checks.killaura;

import org.bukkit.entity.Player;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "KillAura (B)", type = "B")
public final class KillAuraB extends EdgeCheck
{
    private double lastDeltaX;
    private double lastDeltaZ;
    private double lastCombined;
    
    public KillAuraB(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_POSITION()) {
            final boolean sprinting = this.user.getPacketActionHandler().isSprinting() && this.user.getMovementHandler().isOnGround();
            final boolean target = this.user.getFightHandler().getEntity() != null && this.user.getFightHandler().getEntity().get() instanceof Player;
            final boolean attacking = this.user.getFightHandler().getLastAttackTick() > 1;
            final double deltaX = this.user.getMovementHandler().getDeltaX();
            final double deltaZ = this.user.getMovementHandler().getDeltaZ();
            if (attacking && target) {
                if (sprinting) {
                    final double estX = this.lastDeltaX * 0.6000000238418579;
                    final double estZ = this.lastDeltaZ * 0.6000000238418579;
                    final double offsetX = Math.abs(estX - deltaX);
                    final double offsetZ = Math.abs(estZ - deltaZ);
                    final double combined = offsetX + offsetZ;
                    final double accel = combined - this.lastCombined;
                    this.debug("Attack acceleration &8[&cA=" + accel + " : C=" + combined + " : S=" + sprinting + "&8]");
                    if (accel < 0.001 && Math.hypot(deltaX, deltaZ) > 0.0) {
                        if (this.increaseBufferBy(1.0) > 4.0) {
                            this.fail("Attack acceleration", "A=" + accel + " : C=" + combined + " : S=" + sprinting);
                        }
                    }
                    else {
                        this.resetBuffer();
                    }
                    this.lastCombined = combined;
                }
            }
            this.lastDeltaX = deltaX;
            this.lastDeltaZ = deltaZ;
        }
    }
}
