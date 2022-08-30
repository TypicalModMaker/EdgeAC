package xyz.edge.ac.checks.checks.speed;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import xyz.edge.ac.util.utils.MathUtil;
import org.bukkit.util.Vector;
import xyz.edge.ac.util.block.BlockUtil;
import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.ac.config.ConfigValue;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Speed (C)", type = "C", exemptBedrock = true)
public class SpeedC extends EdgeCheck
{
    private static final ConfigValue thresholdMove;
    private final double THRESHOLD;
    private boolean hitSlow;
    private float lastFriction;
    private int lastIdleTick;
    private int buffer2;
    private int buffer3;
    
    public SpeedC(final User user) {
        super(user);
        this.THRESHOLD = Math.toRadians(0.5);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_FLYING()) {
            if (this.user.getMovementHandler().isNearWall()) {
                return;
            }
            if (this.user.getVelocityHandler().isTakingVelocity()) {
                return;
            }
            if (this.user.getMovementHandler().isOnWallOrFence()) {
                return;
            }
            if (this.user.getMovementHandler().isNearCarpet() && this.user.getVelocityHandler().getTicksSinceVelocity() < 20) {
                return;
            }
            if (this.isExempt(Exempts.CREATIVE, Exempts.GLIDING, Exempts.FLYING, Exempts.TELEPORT_TIME)) {
                return;
            }
            final Block footBlock = BlockUtil.getBlockAsync(this.user.getPlayer().getLocation().clone().add(0.0, -0.2, 0.0));
            if (footBlock == null) {
                return;
            }
            double dX = this.user.getMovementHandler().getX() - this.user.getMovementHandler().getLastX();
            double dZ = this.user.getMovementHandler().getZ() - this.user.getMovementHandler().getLastZ();
            final Vector vector = new Vector(this.user.getMovementHandler().getLastDeltaX(), 0.0, this.user.getMovementHandler().getLastDeltaZ());
            if (Math.abs(vector.getX() * this.lastFriction) < 0.005) {
                vector.setX(0);
            }
            if (Math.abs(vector.getZ() * this.lastFriction) < 0.005) {
                vector.setZ(0);
            }
            if (this.hitSlow) {
                vector.multiply(0.6);
            }
            this.hitSlow = false;
            dX /= this.lastFriction;
            dZ /= this.lastFriction;
            dX -= vector.getX();
            dZ -= vector.getZ();
            final float lastFriction = this.user.getMovementHandler().getFriction();
            if (this.lastFriction != lastFriction) {
                this.lastIdleTick = this.user.getCurrentTick();
            }
            this.lastFriction = lastFriction;
            final Vector accelDir = new Vector(dX, 0.0, dZ);
            if (accelDir.lengthSquared() < 0.03) {
                return;
            }
            if (this.user.getTicks() - this.lastIdleTick <= 2 || this.user.getMovementHandler().testJump() || this.user.getVelocityHandler().getTicksSinceVelocity() < 2) {
                return;
            }
            final Vector yaw = MathUtil.getDirection(this.user.getRotationHandler().getYaw(), 0.0f);
            final boolean vectorDir = accelDir.clone().crossProduct(yaw).dot(new Vector(0, 1, 0)) >= 0.0;
            final double angle = (vectorDir ? 1 : -1) * MathUtil.angle(accelDir, yaw);
            if (Double.isNaN(angle)) {
                return;
            }
            this.user.getMovementHandler().setLastMoveAngle(angle);
            if (!this.isValidStrafe(angle)) {
                this.debug("Tried to move incorrectly &8[&cAT=" + Math.round(angle) + " : C=" + this.hitSlow + "&8]");
                final double buffer = this.buffer;
                this.buffer = buffer + 1.0;
                if (buffer > 8.0) {
                    this.fail("Tried to move incorrectly", "A=" + angle + " : HS=" + this.hitSlow);
                    this.buffer = Math.max(20.0, this.buffer - 5.0);
                }
            }
            else {
                this.buffer = 0.0;
            }
        }
        else if (packet.PACKET_USE_ENTITY() && this.user.getPacketActionHandler().isSprinting()) {
            final WrappedPacketInUseEntity entity = new WrappedPacketInUseEntity(packet.getRawPacket());
            if (entity.getEntity() == null) {
                return;
            }
            if (entity.getEntity() instanceof Player) {
                this.hitSlow = true;
            }
        }
        else if (packet.PACKET_FLYING()) {
            this.lastIdleTick = this.user.getCurrentTick();
        }
    }
    
    private boolean isValidStrafe(final double angle) {
        final double modulo = angle % 0.7853981633974483 * 1.2732395447351628;
        final double error = Math.abs(modulo - Math.round(modulo)) * 0.7853981633974483;
        return error <= this.THRESHOLD;
    }
    
    static {
        thresholdMove = new ConfigValue(ConfigValue.ValueType.DOUBLE, "settings.threshold");
    }
}
