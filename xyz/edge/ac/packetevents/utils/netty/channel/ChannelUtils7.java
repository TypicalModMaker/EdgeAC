package xyz.edge.ac.packetevents.utils.netty.channel;

import net.minecraft.util.io.netty.channel.Channel;
import java.net.InetSocketAddress;

public final class ChannelUtils7
{
    public static InetSocketAddress getSocketAddress(final Object ch) {
        final Channel channel = (Channel)ch;
        return (InetSocketAddress)channel.remoteAddress();
    }
}
