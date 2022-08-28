package xyz.edge.ac.checks.checks.aimbot;

import xyz.edge.ac.util.utils.MathUtil;
import xyz.edge.ac.packetevents.utils.player.ClientVersion;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "AimBot (C)", type = "C")
public final class AimBotC extends EdgeCheck
{
    private float lastDeltaYaw;
    private float lastDeltaPitch;
    
    public AimBotC(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_ROTATION()) {
            if (this.user.getRotationHandler().isCinematic()) {
                return;
            }
            if (this.user.getVersion().isNewerThanOrEquals(ClientVersion.v_1_9)) {
                return;
            }
            final float deltaYaw = this.user.getRotationHandler().getDeltaYaw() % 360.0f;
            final float deltaPitch = this.user.getRotationHandler().getDeltaPitch();
            final double divisorYaw = (double)MathUtil.getGcd((long)(deltaYaw * MathUtil.EXPANDER), (long)(this.lastDeltaYaw * MathUtil.EXPANDER));
            final double divisorPitch = (double)MathUtil.getGcd((long)(deltaPitch * MathUtil.EXPANDER), (long)(this.lastDeltaPitch * MathUtil.EXPANDER));
            final double constantYaw = divisorYaw / MathUtil.EXPANDER;
            final double constantPitch = divisorPitch / MathUtil.EXPANDER;
            final double currentX = deltaYaw / constantYaw;
            final double currentY = deltaPitch / constantPitch;
            final double previousX = this.lastDeltaYaw / constantYaw;
            final double previousY = this.lastDeltaPitch / constantPitch;
            if (this.user.getRotationHandler().isCinematic()) {
                return;
            }
            if (deltaYaw > 0.0 && deltaPitch > 0.0 && deltaYaw < 20.0f && deltaPitch < 20.0f) {
                final double moduloX = currentX % previousX;
                final double moduloY = currentY % previousY;
                final double floorModuloX = Math.abs(Math.floor(moduloX) - moduloX);
                final double floorModuloY = Math.abs(Math.floor(moduloY) - moduloY);
                final boolean invalidX = moduloX > 90.0 && floorModuloX > 0.1;
                final boolean invalidY = moduloY > 90.0 && floorModuloY > 0.1;
                this.debug("Incorrect divisor &8[&cMX=" + moduloX + " : MY=" + moduloY + "&8]");
                if (invalidX && invalidY) {
                    final double buffer = this.buffer + 1.0;
                    this.buffer = buffer;
                    if (buffer > 8.0) {
                        this.fail("Incorrect divisor", "MX=" + moduloX + " : MY=" + moduloY);
                    }
                }
                else {
                    this.buffer -= ((this.buffer > 0.0) ? 1.0 : 0.0);
                }
            }
            this.lastDeltaYaw = deltaYaw;
            this.lastDeltaPitch = deltaPitch;
        }
    }
}
