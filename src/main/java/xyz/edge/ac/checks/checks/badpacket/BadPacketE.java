package xyz.edge.ac.checks.checks.badpacket;

import xyz.edge.ac.util.exempts.type.Exempts;
import io.github.retrooper.packetevents.packetwrappers.play.in.keepalive.WrappedPacketInKeepAlive;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.license.LicenseType;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "BadPacket (E)", type = "E", licenseType = LicenseType.ENTERPRISE)
public final class BadPacketE extends EdgeCheck
{
    private long lastId;
    
    public BadPacketE(final User user) {
        super(user);
        this.lastId = -1L;
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_INCOMING_KEEPALIVE()) {
            final WrappedPacketInKeepAlive wrapper = new WrappedPacketInKeepAlive(packet.getRawPacket());
            this.debug("Duplicate KeepAlive ID &8[&cCID=" + wrapper.getId() + " : LID=" + this.lastId + "&8]");
            if (wrapper.getId() == this.lastId && !this.isExempt(Exempts.TELEPORT, Exempts.JOINED)) {
                this.fail("Duplicate KeepAlive ID", "KID" + wrapper.getId() + " : LKID=" + this.lastId);
            }
            this.lastId = wrapper.getId();
        }
    }
}
