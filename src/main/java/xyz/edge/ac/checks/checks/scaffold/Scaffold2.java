package xyz.edge.ac.checks.checks.scaffold;

import io.github.retrooper.packetevents.packetwrappers.play.in.blockplace.WrappedPacketInBlockPlace;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.license.LicenseType;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Scaffold (2)", type = "2", experimental = true, exemptBedrock = true, licenseType = LicenseType.ENTERPRISE, punish = false)
public class Scaffold2 extends EdgeCheck
{
    public Scaffold2(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_BLOCKPLACE()) {
            final WrappedPacketInBlockPlace wrapper = new WrappedPacketInBlockPlace(packet.getRawPacket());
            final boolean onGround = this.user.getMovementHandler().isOnGround();
            final boolean mathGround = this.user.getMovementHandler().isMathematicallyOnGround();
            final double accelerationXZ = this.user.getMovementHandler().getAccelXZ();
            final double accelerationY = this.user.getMovementHandler().getAccelY();
            final double wrappedFaceValue = wrapper.getDirection().getFaceValue();
            final double blockX = wrapper.getBlockPosition().getX();
            final double blockZ = wrapper.getBlockPosition().getX();
            final double blockHypotXZ = Math.hypot(blockX, blockZ);
            if (!onGround && !mathGround && accelerationY == 0.52408036398) {
                final double buffer = this.buffer + 1.0;
                this.buffer = buffer;
                if (buffer > 3.0) {
                    this.fail("A", "A=" + accelerationY);
                }
            }
            else {
                this.decreaseBufferBy(0.25);
            }
            this.debug("AY=" + accelerationY + ", G=" + onGround + ", MG=" + mathGround);
        }
    }
}
