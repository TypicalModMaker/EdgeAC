package xyz.edge.ac.checks.checks.badpacket;

import xyz.edge.ac.packetevents.utils.player.ClientVersion;
import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetevents.packetwrappers.play.in.steervehicle.WrappedPacketInSteerVehicle;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "BadPacket (C)", type = "C")
public final class BadPacketC extends EdgeCheck
{
    public BadPacketC(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_STEER_VEHICLE()) {
            final WrappedPacketInSteerVehicle wrapper = new WrappedPacketInSteerVehicle(packet.getRawPacket());
            final boolean exempt = this.isExempt(Exempts.INSIDE_VEHICLE);
            if (!this.user.getMovementHandler().isTeleporting() && !this.user.getVersion().isOlderThanOrEquals(ClientVersion.v_1_7_10) && !this.user.getVersion().isNewerThanOrEquals(ClientVersion.v_1_9) && !exempt) {
                this.fail("Vehicle spoof [1]", "IV=false");
            }
            final float forward = wrapper.getForwardValue();
            final float side = wrapper.getSideValue();
            final boolean invalid = Math.abs(forward) > 0.98f || Math.abs(side) > 0.98f;
            this.debug("Vehicle spoof &8[&cF=" + forward + "&8]");
            if (invalid) {
                this.fail("Vehicle spoof [2]", "F=" + forward + " : S=" + side + " : OV=true");
            }
        }
    }
}
