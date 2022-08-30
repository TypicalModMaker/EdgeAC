package xyz.edge.ac.checks.checks.aimbot;

import xyz.edge.ac.util.utils.MathUtil;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "AimBot (B)", type = "B", exemptBedrock = true)
public final class AimBotB extends EdgeCheck
{
    private static final double MODULO_THRESHOLD = 90.0;
    private static final double LINEAR_THRESHOLD = 0.10000000149011612;
    
    public AimBotB(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_ROTATION()) {
            final float yaw = this.user.getRotationHandler().getYaw();
            final float pitch = this.user.getRotationHandler().getPitch();
            final float deltaYaw = this.user.getRotationHandler().getDeltaYaw();
            final float lastDeltaYaw = this.user.getRotationHandler().getLastDeltaYaw();
            final float deltaPitch = this.user.getRotationHandler().getDeltaPitch();
            final float lastDeltaPitch = this.user.getRotationHandler().getLastDeltaPitch();
            final double divX = MathUtil.getGcd(deltaPitch, lastDeltaPitch);
            final double divY = MathUtil.getGcd(deltaYaw, lastDeltaYaw);
            final double deltaX = deltaYaw / divX;
            final double deltaY = deltaPitch / divY;
            final double lastDeltaX = lastDeltaYaw / divX;
            final double lastDeltaY = lastDeltaPitch / divY;
            if (this.user.getRotationHandler().isCinematic()) {
                return;
            }
            if (deltaYaw > 0.0 && deltaPitch > 0.0 && deltaYaw < 20.0f && deltaPitch < 20.0f) {
                final double moduloX = deltaX % lastDeltaX;
                final double moduloY = deltaY % lastDeltaY;
                final double floorModuloX = Math.abs(Math.floor(moduloX) - moduloX);
                final double floorModuloY = Math.abs(Math.floor(moduloY) - moduloY);
                final boolean invalidX = moduloX > 90.0 && floorModuloX > 0.10000000149011612;
                final boolean invalidY = moduloY > 90.0 && floorModuloY > 0.10000000149011612;
                this.debug("Impossible modulo while rotation &8[&cFMX=" + floorModuloX + " : FMY=" + floorModuloY + "&8]");
                if (invalidX && invalidY) {
                    if (this.increaseBuffer() > 8.0) {
                        this.fail("Impossible modulo while rotation", "FMX=" + floorModuloX + " : FMY=" + floorModuloY);
                    }
                }
                else {
                    this.decreaseBuffer();
                }
            }
        }
    }
}
