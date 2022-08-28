package xyz.edge.ac.packetutils.inout;

import xyz.edge.ac.checks.EdgeCheck;
import xyz.edge.ac.packetevents.utils.player.ClientVersion;
import xyz.edge.ac.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import xyz.edge.ac.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import xyz.edge.ac.packetevents.packetwrappers.play.in.helditemslot.WrappedPacketInHeldItemSlot;
import xyz.edge.ac.packetevents.packetwrappers.play.in.clientcommand.WrappedPacketInClientCommand;
import xyz.edge.ac.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig;
import xyz.edge.ac.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.ac.packetevents.event.impl.PacketPlayReceiveEvent;

public final class Receiving
{
    public void handle(final PacketPlayReceiveEvent event, final User user, final PacketUtil packet) {
        user.getTransactionsHandler().handleReceive(packet);
        if (packet.PACKET_ENTITY_ACTION()) {
            final WrappedPacketInEntityAction wrapper = new WrappedPacketInEntityAction(packet.getRawPacket());
            user.getGliding().onAction(wrapper);
            user.getPacketActionHandler().handleEntityAction(wrapper);
        }
        else if (packet.PACKET_BLOCK_DIG()) {
            final WrappedPacketInBlockDig wrapper2 = new WrappedPacketInBlockDig(packet.getRawPacket());
            user.getPacketActionHandler().handleBlockDig(wrapper2);
        }
        else if (packet.PACKET_CLIENT_COMMAND()) {
            final WrappedPacketInClientCommand wrapper3 = new WrappedPacketInClientCommand(packet.getRawPacket());
            user.getPacketActionHandler().handleClientCommand(wrapper3);
        }
        else if (packet.PACKET_BLOCKPLACE()) {
            user.getPacketActionHandler().handleBlockPlace();
        }
        else if (packet.PACKET_HELD_ITEM_SLOT()) {
            final WrappedPacketInHeldItemSlot wrapper4 = new WrappedPacketInHeldItemSlot(packet.getRawPacket());
            user.getPacketActionHandler().handleHeldItemSlot(wrapper4);
        }
        else if (packet.PACKET_CLOSE_WINDOW()) {
            user.getPacketActionHandler().handleCloseWindow();
        }
        else if (packet.PACKET_USE_ENTITY()) {
            final WrappedPacketInUseEntity wrapper5 = new WrappedPacketInUseEntity(packet.getRawPacket());
            user.getFightHandler().handleUseEntity(wrapper5);
        }
        else if (packet.PACKET_FLYING()) {
            final WrappedPacketInFlying wrapper6 = new WrappedPacketInFlying(packet.getRawPacket());
            user.getMovementHandler().handleMyFlyingForTeleportSoWeCanGoFixThisStuff(wrapper6);
            user.getLol().onFlying();
            if (wrapper6.isLook() && wrapper6.isPosition() && user.getMovementHandler().getX() == wrapper6.getX() && user.getMovementHandler().getY() == wrapper6.getY() && user.getMovementHandler().getZ() == wrapper6.getZ() && user.getVersion().isNewerThanOrEquals(ClientVersion.v_1_17)) {
                event.setCancelled(true);
                user.getPlayer().sendMessage("EXEMPTED 1.18");
                return;
            }
            user.setTicks(user.getTicks() + 1);
            user.getPacketActionHandler().handleFlying();
            user.getVelocityHandler().handleFlying();
            user.getFightHandler().handleFlying();
            if (wrapper6.isPosition()) {
                user.getMovementHandler().handle(wrapper6.getPosition().getX(), wrapper6.getPosition().getY(), wrapper6.getPosition().getZ(), wrapper6.isOnGround());
            }
            if (wrapper6.isLook()) {
                user.getRotationHandler().handle(wrapper6.getYaw(), wrapper6.getPitch());
            }
        }
        else if (packet.PACKET_ARM_ANIMATION()) {
            user.getClickHandler().handleArmAnimation();
            user.getPacketActionHandler().handleArmAnimation();
            user.getFightHandler().handleArmAnimation();
        }
        else if (packet.isTick()) {
            user.getVelocityHandler().handleTransaction((short)packet.getTransactionID());
        }
        if (!user.getPlayer().hasPermission("edge.bypass") || user.getPlayer().isOp()) {
            user.getChecks().stream().filter(EdgeCheck::isEnabled).forEach(check -> check.handle(packet));
        }
    }
}
