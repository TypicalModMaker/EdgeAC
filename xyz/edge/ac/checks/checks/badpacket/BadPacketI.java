package xyz.edge.ac.checks.checks.badpacket;

import xyz.edge.ac.packetevents.packetwrappers.play.in.windowclick.WrappedPacketInWindowClick;
import xyz.edge.ac.packetevents.utils.player.ClientVersion;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "BadPacket (I)", type = "I")
public class BadPacketI extends EdgeCheck
{
    public BadPacketI(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_WINDOW_CLICK() && !this.isExempt(Exempts.VELOCITY, Exempts.LIQUID, Exempts.INSIDE_VEHICLE, Exempts.TELEPORT_TIME, Exempts.ICE, Exempts.PISTON)) {
            final double deltaXZ = this.user.getMovementHandler().getDeltaXZ();
            if (deltaXZ > 0.22 && this.user.getMovementHandler().isOnGround()) {
                if (this.buffer > 3.0) {
                    this.fail("Tried to move too fast in inventory", "DXZ=" + deltaXZ);
                }
            }
            else {
                this.decreaseBufferBy(1.0);
            }
            if ((ServerVersion.getVersion().isNewerThanOrEquals(ServerVersion.v_1_9) && this.user.getVersion().isOlderThan(ClientVersion.v_1_9)) || this.user.getVersion().isNewerThanOrEquals(ClientVersion.v_1_9)) {
                return;
            }
            final WrappedPacketInWindowClick wrappedPacketInWindowClick = new WrappedPacketInWindowClick(packet.getRawPacket());
            if (wrappedPacketInWindowClick.getWindowId() == 0 && !this.user.getPacketActionHandler().isInventory()) {
                this.fail("Invalid window client", "WID=0");
            }
        }
    }
}
