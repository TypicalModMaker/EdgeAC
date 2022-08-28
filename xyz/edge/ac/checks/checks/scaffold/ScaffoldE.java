package xyz.edge.ac.checks.checks.scaffold;

import org.bukkit.util.Vector;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.ac.packetevents.packetwrappers.play.in.blockplace.WrappedPacketInBlockPlace;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Scaffold (E)", type = "E")
public class ScaffoldE extends EdgeCheck
{
    private WrappedPacketInBlockPlace wrapper;
    
    public ScaffoldE(final User user) {
        super(user);
        this.wrapper = null;
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_BLOCKPLACE()) {
            this.wrapper = new WrappedPacketInBlockPlace(packet.getRawPacket());
        }
        else if (packet.PACKET_FLYING()) {
            if (this.wrapper != null) {
                final Vector eyeLocation = this.user.getPlayer().getEyeLocation().toVector();
                final Vector blockLocation = new Vector(this.wrapper.getBlockPosition().x, this.wrapper.getBlockPosition().y, this.wrapper.getBlockPosition().z);
                final Vector directionToDestination = blockLocation.clone().subtract(eyeLocation);
                final Vector playerDirection = this.user.getPlayer().getEyeLocation().getDirection();
                final float angle = directionToDestination.angle(playerDirection);
                final float distance = (float)eyeLocation.distance(blockLocation);
                final boolean exempt = blockLocation.getX() == -1.0 && blockLocation.getY() == -1.0 && blockLocation.getZ() == -1.0;
                final boolean invalid = angle > 1.0f && distance > 1.5;
                this.debug("Tried to place too many blocks in a tick");
                if (invalid && !exempt) {
                    if (this.increaseBuffer() > 2.0) {
                        this.fail("Tried to expand", "A=" + angle + " : ES" + distance);
                    }
                }
                else {
                    this.decreaseBuffer();
                }
            }
            this.wrapper = null;
        }
    }
}
