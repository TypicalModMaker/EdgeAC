package xyz.edge.ac.packetevents.utils.netty.channel;

import io.netty.channel.Channel;
import java.net.InetSocketAddress;

public final class ChannelUtils8
{
    public static InetSocketAddress getSocketAddress(final Object ch) {
        final Channel channel = (Channel)ch;
        return (InetSocketAddress)channel.remoteAddress();
    }
}
