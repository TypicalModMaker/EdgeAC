package xyz.edge.ac.packetutils.inout;

import xyz.edge.ac.checks.EdgeCheck;
import io.github.retrooper.packetevents.packetwrappers.play.out.gamestatechange.WrappedPacketOutGameStateChange;
import io.github.retrooper.packetevents.packetwrappers.play.out.abilities.WrappedPacketOutAbilities;
import io.github.retrooper.packetevents.packetwrappers.play.out.entitymetadata.WrappedPacketOutEntityMetadata;
import io.github.retrooper.packetevents.packetwrappers.play.out.position.WrappedPacketOutPosition;
import io.github.retrooper.packetevents.packetwrappers.play.out.explosion.WrappedPacketOutExplosion;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityvelocity.WrappedPacketOutEntityVelocity;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;

public final class Sending
{
    public void handle(final PacketPlaySendEvent event, final User user, final PacketUtil packet) {
        user.getTransactionsHandler().handleSent(packet);
        if (packet.PACKET_ENTITY_VELOCITY()) {
            final WrappedPacketOutEntityVelocity wrapper = new WrappedPacketOutEntityVelocity(packet.getRawPacket());
            if (wrapper.getEntity() == user.getPlayer()) {
                user.getVelocityHandler().handle(wrapper.getVelocityX(), wrapper.getVelocityY(), wrapper.getVelocityZ());
            }
        }
        if (packet.PACKET_EXPLOSION()) {
            final WrappedPacketOutExplosion wrapper2 = new WrappedPacketOutExplosion(packet.getRawPacket());
            user.getVelocityHandler().handle(wrapper2.getPlayerMotionX(), wrapper2.getPlayerMotionY(), wrapper2.getPlayerMotionZ());
        }
        if (packet.PACKET_OUT_POSITION()) {
            final WrappedPacketOutPosition wrapper3 = new WrappedPacketOutPosition(packet.getRawPacket());
            user.getMovementHandler().handleServerPosition(wrapper3);
        }
        if (packet.isEntityData()) {
            user.getGliding().onEntityData(event, new WrappedPacketOutEntityMetadata(packet.getRawPacket()));
        }
        if (packet.PACKET_INCOMING_ABILITIES()) {
            final WrappedPacketOutAbilities abilities = new WrappedPacketOutAbilities(packet.getRawPacket());
            user.getLol().handleAbilities(abilities);
        }
        if (packet.isGamemode()) {
            final WrappedPacketOutGameStateChange wrappedPacketOutGameStateChange = new WrappedPacketOutGameStateChange(packet.getRawPacket());
            user.getLol().handleGamemode(wrappedPacketOutGameStateChange);
        }
        if (!user.getPlayer().hasPermission("edge.bypass") || user.getPlayer().isOp()) {
            user.getChecks().stream().filter(EdgeCheck::isEnabled).forEach(check -> check.handle(packet));
        }
    }
}
