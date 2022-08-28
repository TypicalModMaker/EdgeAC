package xyz.edge.ac.packetevents.packetwrappers.play.in.clientcommand;

import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public enum ClientCommand
{
    PERFORM_RESPAWN, 
    REQUEST_STATS, 
    @SupportedVersions(ranges = { ServerVersion.v_1_7_10, ServerVersion.v_1_15_2 })
    @Deprecated
    OPEN_INVENTORY_ACHIEVEMENT;
}
