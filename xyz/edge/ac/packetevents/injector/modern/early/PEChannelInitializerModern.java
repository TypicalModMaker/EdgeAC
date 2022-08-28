package xyz.edge.ac.packetevents.injector.modern.early;

import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import io.netty.channel.ChannelHandler;
import xyz.edge.ac.packetevents.PacketEvents;
import xyz.edge.ac.packetevents.injector.modern.PlayerChannelHandlerModern;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.lang.reflect.Method;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class PEChannelInitializerModern extends ChannelInitializer<Channel>
{
    private final ChannelInitializer<?> oldChannelInitializer;
    private Method initChannelMethod;
    
    public PEChannelInitializerModern(final ChannelInitializer<?> oldChannelInitializer) {
        this.oldChannelInitializer = oldChannelInitializer;
        this.load();
    }
    
    public static void postInitChannel(final Channel channel) {
        if (channel.getClass().equals(NioSocketChannel.class) || channel.getClass().equals(EpollSocketChannel.class)) {
            final PlayerChannelHandlerModern channelHandler = new PlayerChannelHandlerModern();
            if (channel.pipeline().get("packet_handler") != null) {
                final String handlerName = PacketEvents.get().getHandlerName();
                if (channel.pipeline().get(handlerName) == null) {
                    channel.pipeline().addBefore("packet_handler", handlerName, channelHandler);
                }
            }
        }
    }
    
    private void load() {
        this.initChannelMethod = Reflection.getMethod(this.oldChannelInitializer.getClass(), "initChannel", 0);
    }
    
    public ChannelInitializer<?> getOldChannelInitializer() {
        return this.oldChannelInitializer;
    }
    
    protected void initChannel(final Channel channel) throws Exception {
        this.initChannelMethod.invoke(this.oldChannelInitializer, channel);
        postInitChannel(channel);
    }
}
