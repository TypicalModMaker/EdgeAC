package xyz.edge.ac.packetevents.injector.legacy;

import net.minecraft.util.io.netty.util.concurrent.Future;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import net.minecraft.util.io.netty.channel.ChannelPromise;
import xyz.edge.ac.packetevents.processor.PacketProcessorInternal;
import xyz.edge.ac.packetevents.PacketEvents;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import org.bukkit.entity.Player;
import net.minecraft.util.io.netty.channel.ChannelHandler;
import net.minecraft.util.io.netty.channel.ChannelDuplexHandler;

@ChannelHandler.Sharable
public class PlayerChannelHandlerLegacy extends ChannelDuplexHandler
{
    public volatile Player player;
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object packet) throws Exception {
        final PacketProcessorInternal.PacketData data = PacketEvents.get().getInternalPacketProcessor().read(this.player, ctx.channel(), packet);
        if (data.packet != null) {
            super.channelRead(ctx, data.packet);
            PacketEvents.get().getInternalPacketProcessor().postRead(this.player, ctx.channel(), data.packet);
        }
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object packet, final ChannelPromise promise) throws Exception {
        if (packet instanceof ByteBuf) {
            super.write(ctx, packet, promise);
            return;
        }
        final PacketProcessorInternal.PacketData data = PacketEvents.get().getInternalPacketProcessor().write(this.player, ctx.channel(), packet);
        if (data.postAction != null) {
            promise.addListener(f -> data.postAction.run());
        }
        if (data.packet != null) {
            super.write(ctx, data.packet, promise);
            PacketEvents.get().getInternalPacketProcessor().postWrite(this.player, ctx.channel(), data.packet);
        }
    }
}
