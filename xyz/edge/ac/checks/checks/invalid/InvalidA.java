package xyz.edge.ac.checks.checks.invalid;

import xyz.edge.ac.user.impl.Movement;
import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.util.utils.PlayerUtil;
import org.bukkit.potion.PotionEffectType;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Invalid (A)", type = "A", exemptBedrock = true)
public class InvalidA extends EdgeCheck
{
    private int ticks;
    private double expectedJumpMotion;
    
    public InvalidA(final User user) {
        super(user);
        this.ticks = Integer.MAX_VALUE;
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_POSITION()) {
            final Movement process = this.user.getMovementHandler();
            final boolean onGround = process.isOnGround();
            final boolean lastOnGround = process.isLastOnGround();
            final double deltaY = process.getDeltaY();
            final double y = process.getY();
            final double lastY = process.getLastY();
            final boolean step = y % 0.015625 == 0.0 && lastY % 0.015625 == 0.0;
            final double modifierJump = PlayerUtil.getPotionLevel(this.user.getPlayer(), PotionEffectType.JUMP) * 0.1f;
            final boolean exempt = this.isExempt(Exempts.TPS, Exempts.PLACING, Exempts.NEAR_VEHICLE, Exempts.CLIMBABLE, Exempts.PISTON, Exempts.VELOCITY_TICK, Exempts.LIQUID, Exempts.TELEPORT_TIME, Exempts.WEB, Exempts.FLYING, Exempts.SLIME, Exempts.UNDER_BLOCK, Exempts.CREATIVE, Exempts.ELYTA_TICK, Exempts.GLIDING) || this.user.getMovementHandler().isNearScaffolding() || process.getSinceBlockNearHeadTicks() < 10 || this.user.getVelocityHandler().isWaitingForTransaction();
            final boolean jumped = deltaY > 0.0 && !onGround && lastY % 0.015625 == 0.0 && !step && lastOnGround;
            if (exempt) {
                this.ticks = Integer.MAX_VALUE;
            }
            if (jumped && !exempt) {
                this.ticks = 0;
                this.expectedJumpMotion = 0.41999998688697815 + modifierJump;
            }
            boolean flagged = false;
            if (onGround) {
                this.ticks = Integer.MAX_VALUE;
            }
            if (this.ticks < 5) {
                if (this.user.getMovementHandler().isRiptide() || this.user.getMovementHandler().isLeashed() || this.user.getMovementHandler().isNearPowderSnow() || this.user.getMovementHandler().isWaterLogged()) {
                    return;
                }
                if (this.user.getMovementHandler().isInLiquid() && this.ticks >= 2) {
                    return;
                }
                ++this.ticks;
                final String debug = String.format("deltaY=%s, diff=%s, tick=%s", deltaY, deltaY - this.expectedJumpMotion, this.ticks);
                final double calcedEJMq = deltaY - this.expectedJumpMotion;
                if (Math.abs(deltaY - this.expectedJumpMotion) > 1.0E-5) {
                    flagged = true;
                    if (this.increaseBufferBy(100.0) > 100.0) {
                        if (Math.abs(deltaY - 0.2 + modifierJump) > 1.0E-4 && Math.abs(deltaY - 0.0338907 + modifierJump) > 1.0E-4 && Math.abs(deltaY - 0.034999) > 1.0E-4) {
                            this.fail("Tried to accede allowed EJP", "DY=" + deltaY + " : P=" + calcedEJMq + " : T=" + this.ticks);
                        }
                        else {
                            this.user.dragDown();
                        }
                        this.fixGhostBlocks();
                    }
                    this.ticks = Integer.MAX_VALUE;
                }
                else {
                    this.expectedJumpMotion = ((Math.abs((this.expectedJumpMotion - 0.08) * 0.9800000190734863) < 0.005) ? 0.0 : ((this.expectedJumpMotion - 0.08) * 0.9800000190734863));
                }
            }
            if (!flagged) {
                this.decreaseBuffer();
            }
            if (step && deltaY > 0.6000000238418579 && !exempt) {
                this.fail("Acceded set Y value", "SYV=" + deltaY + " : SA=" + step);
            }
        }
    }
}
