package xyz.edge.ac.checks.checks.badpacket.payload;

import io.github.retrooper.packetevents.packetwrappers.play.in.custompayload.WrappedPacketInCustomPayload;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "BadPacket (D1)", type = "D1")
public class BadPacketD1 extends EdgeCheck
{
    public BadPacketD1(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_CUSTOM_PAYLOAD()) {
            final WrappedPacketInCustomPayload wrappedPacketInCustomPayload = new WrappedPacketInCustomPayload(packet.getRawPacket());
            final String payload = wrappedPacketInCustomPayload.getChannelName();
            if (payload.equalsIgnoreCase("Remix") || payload.contains("CRYSTAL") || payload.contains("matrix") || payload.equalsIgnoreCase("LOLIMAHCKER") || payload.equalsIgnoreCase("MCnetHandler") || payload.equalsIgnoreCase("XDSMKDKFDKSDAKDFkEJF") || payload.equalsIgnoreCase("CRYSTAL|6LAKS0TRIES") || payload.equalsIgnoreCase("CrystalWare") || payload.equalsIgnoreCase("CRYSTAL|KZ1LM9TO") || payload.equalsIgnoreCase("Misplace") || payload.equalsIgnoreCase("reach") || payload.equalsIgnoreCase("lmaohax") || payload.equalsIgnoreCase("Reach Mod") || payload.equalsIgnoreCase("cock") || payload.equalsIgnoreCase("Vape v3") || payload.equalsIgnoreCase("1946203560") || payload.equalsIgnoreCase("#unbanearwax") || payload.equalsIgnoreCase("EARWAXWASHERE") || payload.equalsIgnoreCase("Cracked Vape") || payload.equalsIgnoreCase("EROUAXWASHERE") || payload.equalsIgnoreCase("Vape") || payload.equalsIgnoreCase("Bspkrs Client")) {
                this.fail("joined with blacklisted payload", "PL=" + payload);
                this.kick();
            }
        }
    }
}
