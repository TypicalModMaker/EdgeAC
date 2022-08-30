package xyz.edge.ac.checks.checks.badpacket;

import io.github.retrooper.packetevents.packetwrappers.play.in.abilities.WrappedPacketInAbilities;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "BadPacket (J)", type = "J")
public class BadPacketJ extends EdgeCheck
{
    public BadPacketJ(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_ABILITIES()) {
            final WrappedPacketInAbilities wrappedPacketInAbilities = new WrappedPacketInAbilities(packet.getRawPacket());
            if (wrappedPacketInAbilities.isFlying() && !this.user.getLol().isFlying()) {
                if (this.increaseBuffer() > 3.0) {
                    this.fail("Spoof abilities", "AF=" + wrappedPacketInAbilities.isFlying());
                }
            }
            else {
                this.resetBuffer();
            }
        }
    }
}
