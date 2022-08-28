package xyz.edge.ac.packetevents.injector.legacy.early;

import net.minecraft.util.io.netty.channel.ChannelHandler;
import xyz.edge.ac.packetevents.PacketEvents;
import net.minecraft.util.io.netty.channel.socket.nio.NioSocketChannel;
import xyz.edge.ac.packetevents.injector.legacy.PlayerChannelHandlerLegacy;
import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import java.lang.reflect.Method;
import net.minecraft.util.io.netty.channel.Channel;
import net.minecraft.util.io.netty.channel.ChannelInitializer;

public class PEChannelInitializerLegacy extends ChannelInitializer<Channel>
{
    private final ChannelInitializer<?> oldChannelInitializer;
    private Method initChannelMethod;
    
    public PEChannelInitializerLegacy(final ChannelInitializer<?> oldChannelInitializer) {
        this.oldChannelInitializer = oldChannelInitializer;
        this.load();
    }
    
    private void load() {
        this.initChannelMethod = Reflection.getMethod(this.oldChannelInitializer.getClass(), "initChannel", 0);
    }
    
    public ChannelInitializer<?> getOldChannelInitializer() {
        return this.oldChannelInitializer;
    }
    
    protected void initChannel(final Channel channel) throws Exception {
        this.initChannelMethod.invoke(this.oldChannelInitializer, channel);
        final PlayerChannelHandlerLegacy channelHandler = new PlayerChannelHandlerLegacy();
        if (channel.getClass().equals(NioSocketChannel.class) && channel.pipeline().get("packet_handler") != null) {
            final String handlerName = PacketEvents.get().getHandlerName();
            if (channel.pipeline().get(handlerName) != null) {
                PacketEvents.get().getPlugin().getLogger().warning("[PacketEvents] Attempted to initialize a channel twice!");
            }
            else {
                channel.pipeline().addBefore("packet_handler", handlerName, channelHandler);
            }
        }
    }
}
