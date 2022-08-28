package xyz.edge.ac.listener;

import xyz.edge.ac.packetevents.event.impl.PacketPlaySendEvent;
import xyz.edge.ac.user.User;
import xyz.edge.ac.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import xyz.edge.ac.packetevents.packettype.PacketType;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.Edge;
import xyz.edge.ac.packetevents.event.impl.PacketPlayReceiveEvent;
import xyz.edge.ac.packetevents.event.PacketListenerDynamic;

public final class NetworkListener extends PacketListenerDynamic
{
    @Override
    public void onPacketPlayReceive(final PacketPlayReceiveEvent event) {
        final User user = Edge.getInstance().getUserManager().getPlayeruser(event.getPlayer());
        if (user == null) {
            return;
        }
        Edge.getInstance().getPacketExecutor().execute(() -> Edge.getInstance().getReceivingPacketProcessor().handle(event, user, new PacketUtil(PacketUtil.Direction.RECEIVE, event.getNMSPacket(), event.getPacketId())));
        if (PacketType.Play.Client.Util.isInstanceOfFlying(event.getPacketId())) {
            final WrappedPacketInFlying wrapper = new WrappedPacketInFlying(event.getNMSPacket());
            if (Math.abs(wrapper.getPosition().getX()) > 1.0E7 || Math.abs(wrapper.getPosition().getY()) > 1.0E7 || Math.abs(wrapper.getPosition().getZ()) > 1.0E7 || Math.abs(wrapper.getPitch()) > 1.0E7 || Math.abs(wrapper.getYaw()) > 1.0E7) {
                user.fuckOff();
            }
        }
    }
    
    @Override
    public void onPacketPlaySend(final PacketPlaySendEvent event) {
        final User user = Edge.getInstance().getUserManager().getPlayeruser(event.getPlayer());
        if (user == null) {
            return;
        }
        Edge.getInstance().getPacketExecutor().execute(() -> Edge.getInstance().getSendingPacketProcessor().handle(event, user, new PacketUtil(PacketUtil.Direction.SEND, event.getNMSPacket(), event.getPacketId())));
    }
}
