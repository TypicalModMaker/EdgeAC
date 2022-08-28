package xyz.edge.ac.checks.checks.badpacket;

import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "BadPacket (F", type = "F")
public final class BadPacketF extends EdgeCheck
{
    private int streak;
    private boolean teleported;
    
    public BadPacketF(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
    }
}
