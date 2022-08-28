package xyz.edge.ac.packetevents.event.eventtypes;

import xyz.edge.ac.packetevents.utils.reflection.ClassUtil;
import xyz.edge.ac.packetevents.packettype.PacketType;
import xyz.edge.ac.packetevents.utils.netty.channel.ChannelUtils;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.net.InetSocketAddress;
import xyz.edge.ac.packetevents.event.PacketEvent;

public abstract class NMSPacketEvent extends PacketEvent implements CallableEvent
{
    private final Object channel;
    private final InetSocketAddress socketAddress;
    private final byte packetID;
    protected NMSPacket packet;
    
    public NMSPacketEvent(final Object channel, final NMSPacket packet) {
        this.channel = channel;
        this.socketAddress = ChannelUtils.getSocketAddress(channel);
        this.packet = packet;
        this.packetID = PacketType.packetIDMap.getOrDefault(packet.getRawNMSPacket().getClass(), (Byte)(-128));
    }
    
    public final InetSocketAddress getSocketAddress() {
        return this.socketAddress;
    }
    
    public Object getChannel() {
        return this.channel;
    }
    
    @Deprecated
    public final String getPacketName() {
        return ClassUtil.getClassSimpleName(this.packet.getRawNMSPacket().getClass());
    }
    
    public final NMSPacket getNMSPacket() {
        return this.packet;
    }
    
    public final void setNMSPacket(final NMSPacket packet) {
        this.packet = packet;
    }
    
    public byte getPacketId() {
        return this.packetID;
    }
    
    @Override
    public boolean isInbuilt() {
        return true;
    }
}
