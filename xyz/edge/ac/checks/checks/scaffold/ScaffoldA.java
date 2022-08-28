package xyz.edge.ac.checks.checks.scaffold;

import xyz.edge.ac.packetevents.utils.vector.Vector3f;
import xyz.edge.ac.packetevents.utils.player.Direction;
import xyz.edge.ac.packetevents.packetwrappers.play.in.blockplace.WrappedPacketInBlockPlace;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.util.utils.ServerUtil;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Scaffold (A)", type = "A", exemptBedrock = true)
public final class ScaffoldA extends EdgeCheck
{
    public ScaffoldA(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_BLOCKPLACE()) {
            if (ServerUtil.getServerVersion().isHigherThanOrEquals(ServerVersion.v_1_9)) {
                return;
            }
            final WrappedPacketInBlockPlace wrapper = new WrappedPacketInBlockPlace(packet.getRawPacket());
            if (wrapper.getDirection() == Direction.INVALID) {
                this.fail("Placed at invalid direction", "D=INVALID");
            }
            final float x = wrapper.getCursorPosition().get().getX();
            final float y = wrapper.getCursorPosition().get().getY();
            final float z = wrapper.getCursorPosition().get().getZ();
            for (final float value : new float[] { x, y, z }) {
                this.debug("Tried to illegally place a block &8[&cV=" + value + "&8]");
                if (value > 1.0 || value < 0.0) {
                    this.fail("Tried to illegally place a block", "BV=" + value);
                }
            }
        }
    }
}
