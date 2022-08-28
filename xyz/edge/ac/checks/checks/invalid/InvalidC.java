package xyz.edge.ac.checks.checks.invalid;

import xyz.edge.ac.packetevents.utils.player.ClientVersion;
import xyz.edge.ac.util.mc.VanillaMath;
import xyz.edge.ac.user.impl.Movement;
import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.util.utils.MathUtil;
import org.bukkit.util.Vector;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.util.utils.ServerUtil;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Invalid (C)", type = "C")
public class InvalidC extends EdgeCheck
{
    private int fireWorkTick;
    
    public InvalidC(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_POSITION()) {
            if (ServerUtil.getServerVersion().isOlderThan(ServerVersion.v_1_9)) {
                return;
            }
            if (this.user.getGliding().isGliding()) {
                final Movement process = this.user.getMovementHandler();
                final Vector prediction = this.computePrediction(this.user.getRotationHandler().getPitch(), new Vector(process.getLastDeltaX(), process.getLastDeltaY(), process.getLastDeltaZ()), this.getVectorForRotation()).multiply(new Vector(0.99, 0.98, 0.99));
                final double deltaX = process.getDeltaX();
                final double deltaY = process.getDeltaY();
                final double deltaZ = process.getDeltaZ();
                final double deltas = MathUtil.magnitude(deltaX, deltaY, deltaZ);
                final double predictions = MathUtil.magnitude(prediction.getX(), prediction.getY(), prediction.getZ());
                final double offset = Math.abs(predictions - deltas);
                final boolean exempt = this.isExempt(Exempts.FLYING, Exempts.NEAR_VEHICLE) || process.isNearWall() || process.isLevitation();
                final String debug = String.format("offset=%s", offset);
                this.debug("Tried to move incorrectly with elytra &7[&cO=" + offset + "&7]");
                if (!exempt && process.isInAir() && this.user.getTicks() - this.fireWorkTick > 100 && offset > 0.001) {
                    if (this.increaseBuffer() > 6.0) {
                        this.fail("Tried to move incorrectly with elytra", "O=" + offset);
                    }
                }
                else {
                    this.resetBuffer();
                }
            }
        }
    }
    
    private Vector getVectorForRotation() {
        final float f = this.user.getRotationHandler().getPitch() * 0.017453292f;
        final float f2 = -this.user.getRotationHandler().getYaw() * 0.017453292f;
        final float f3 = VanillaMath.cos(f2);
        final float f4 = VanillaMath.sin(f2);
        final float f5 = VanillaMath.cos(f);
        final float f6 = VanillaMath.sin(f);
        return new Vector(f4 * f5, -f6, f3 * f5);
    }
    
    private Vector computePrediction(final float pitch, final Vector vector, final Vector lookVector) {
        final float yRotRadians = pitch * 0.017453292f;
        final double horizontalSqrt = Math.sqrt(lookVector.getX() * lookVector.getX() + lookVector.getZ() * lookVector.getZ());
        final double horizontalLength = vector.clone().setY(0).length();
        final double length = lookVector.length();
        double vertCosRotation = this.user.getVersion().isNewerThanOrEquals(ClientVersion.v_1_18_2) ? Math.cos(yRotRadians) : VanillaMath.cos(yRotRadians);
        vertCosRotation = (float)(vertCosRotation * vertCosRotation * Math.min(1.0, length / 0.4));
        vector.add(new Vector(0.0, 0.08 * (-1.0 + vertCosRotation * 0.75), 0.0));
        if (vector.getY() < 0.0 && horizontalSqrt > 0.0) {
            final double d5 = vector.getY() * -0.1 * vertCosRotation;
            vector.add(new Vector(lookVector.getX() * d5 / horizontalSqrt, d5, lookVector.getZ() * d5 / horizontalSqrt));
        }
        if (yRotRadians < 0.0f && horizontalSqrt > 0.0) {
            final double d5 = horizontalLength * -VanillaMath.sin(yRotRadians) * 0.04;
            vector.add(new Vector(-lookVector.getX() * d5 / horizontalSqrt, d5 * 3.2, -lookVector.getZ() * d5 / horizontalSqrt));
        }
        if (horizontalSqrt > 0.0) {
            vector.add(new Vector((lookVector.getX() / horizontalSqrt * horizontalLength - vector.getX()) * 0.1, 0.0, (lookVector.getZ() / horizontalSqrt * horizontalLength - vector.getZ()) * 0.1));
        }
        return vector;
    }
}
