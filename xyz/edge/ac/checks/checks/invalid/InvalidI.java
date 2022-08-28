package xyz.edge.ac.checks.checks.invalid;

import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.ac.checks.EdgeCheck;

public class InvalidI extends EdgeCheck
{
    public InvalidI(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_FLYING()) {}
    }
}
