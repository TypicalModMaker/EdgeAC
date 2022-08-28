package xyz.edge.ac.packetevents.utils.netty.channel;

import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.PacketEvents;
import java.net.InetSocketAddress;

public final class ChannelUtils
{
    public static InetSocketAddress getSocketAddress(final Object ch) {
        if (ch == null) {
            return null;
        }
        if (PacketEvents.get().getServerUtils().getVersion() == ServerVersion.v_1_7_10) {
            return ChannelUtils7.getSocketAddress(ch);
        }
        return ChannelUtils8.getSocketAddress(ch);
    }
}
