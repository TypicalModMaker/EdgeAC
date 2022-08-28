package xyz.edge.ac.packetevents.injector.legacy.late;

import net.minecraft.util.io.netty.channel.ChannelHandler;
import net.minecraft.util.io.netty.channel.socket.nio.NioSocketChannel;
import xyz.edge.ac.packetevents.PacketEvents;
import net.minecraft.util.io.netty.channel.Channel;
import xyz.edge.ac.packetevents.injector.legacy.PlayerChannelHandlerLegacy;
import org.bukkit.entity.Player;
import xyz.edge.ac.packetevents.injector.LateInjector;

public class LateChannelInjectorLegacy implements LateInjector
{
    @Override
    public void inject() {
    }
    
    @Override
    public void eject() {
    }
    
    @Override
    public void injectPlayer(final Player player) {
        final PlayerChannelHandlerLegacy playerChannelHandlerLegacy = new PlayerChannelHandlerLegacy();
        playerChannelHandlerLegacy.player = player;
        final Channel channel = (Channel)PacketEvents.get().getPlayerUtils().getChannel(player);
        if (channel.getClass().equals(NioSocketChannel.class)) {
            channel.pipeline().addBefore("packet_handler", PacketEvents.get().getHandlerName(), playerChannelHandlerLegacy);
        }
    }
    
    @Override
    public void ejectPlayer(final Player player) {
        final Object channel = PacketEvents.get().getPlayerUtils().getChannel(player);
        if (channel != null) {
            try {
                ((Channel)channel).pipeline().remove(PacketEvents.get().getHandlerName());
            }
            catch (final Exception ex) {}
        }
    }
    
    @Override
    public boolean hasInjected(final Player player) {
        final Channel channel = (Channel)PacketEvents.get().getPlayerUtils().getChannel(player);
        return channel.pipeline().get(PacketEvents.get().getHandlerName()) != null;
    }
    
    @Override
    public void writePacket(final Object ch, final Object rawNMSPacket) {
        final Channel channel = (Channel)ch;
        channel.write(rawNMSPacket);
    }
    
    @Override
    public void flushPackets(final Object ch) {
        final Channel channel = (Channel)ch;
        channel.flush();
    }
    
    @Override
    public void sendPacket(final Object rawChannel, final Object packet) {
        final Channel channel = (Channel)rawChannel;
        channel.pipeline().writeAndFlush(packet);
    }
}
