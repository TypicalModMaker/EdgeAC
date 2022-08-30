package xyz.edge.ac.packetutils;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.play.in.pong.WrappedPacketInPong;
import io.github.retrooper.packetevents.packetwrappers.play.in.transaction.WrappedPacketInTransaction;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;

public final class PacketUtil
{
    private final Direction direction;
    private final NMSPacket rawPacket;
    private boolean cancelled;
    private final byte packetId;
    
    public PacketUtil(final Direction direction, final NMSPacket rawPacket, final byte packetId) {
        this.direction = direction;
        this.rawPacket = rawPacket;
        this.packetId = packetId;
    }
    
    public boolean STATE_RECEIVING() {
        return this.direction == Direction.RECEIVE;
    }
    
    public boolean STATE_SENDING() {
        return this.direction == Direction.SEND;
    }
    
    public boolean PACKET_SET_CREATIVE_SLOT() {
        return this.packetId == -75;
    }
    
    public boolean PACKET_SPECTATE() {
        return this.packetId == -70;
    }
    
    public boolean PACKET_ITEM_NAME() {
        return this.packetId == -83;
    }
    
    public boolean PACKET_UPDATE_SIGN() {
        return this.packetId == -72;
    }
    
    public boolean PACKET_SETTING_CHANGE() {
        return this.packetId == -109;
    }
    
    public boolean PACKET_FLYING() {
        return this.STATE_RECEIVING() && PacketType.Play.Client.Util.isInstanceOfFlying(this.packetId);
    }
    
    public boolean PACKET_LOGIN_START() {
        return this.packetId == -121;
    }
    
    public boolean PACKET_USE_ENTITY() {
        return this.STATE_RECEIVING() && this.packetId == -100;
    }
    
    public boolean PACKET_ROTATION() {
        return this.STATE_RECEIVING() && (this.packetId == -94 || this.packetId == -95);
    }
    
    public boolean PACKET_CHAT() {
        return this.STATE_RECEIVING() && this.packetId == -111;
    }
    
    public boolean PACKET_TAB_COMPLETE() {
        return this.STATE_RECEIVING() && this.packetId == -108;
    }
    
    public boolean isTick() {
        return this.PACKET_PING_PONG() && this.getTransactionID() < 0;
    }
    
    public int getTransactionID() {
        if (this.packetId == -107) {
            return new WrappedPacketInTransaction(this.getRawPacket()).getActionNumber();
        }
        if (this.packetId == 28) {
            return new WrappedPacketInPong(this.getRawPacket()).getId();
        }
        return 1;
    }
    
    public boolean PACKET_SLOT() {
        return this.packetId == -78;
    }
    
    public boolean PACKET_PING_PONG() {
        return (this.STATE_RECEIVING() && this.packetId == 28) || this.packetId == -107;
    }
    
    public boolean PACKET_POSITION() {
        return this.STATE_RECEIVING() && (this.packetId == -96 || this.packetId == -95);
    }
    
    public boolean PACKET_STOP_SPRINT() {
        return this.isAction() && new WrappedPacketInEntityAction(this.rawPacket).getAction() == WrappedPacketInEntityAction.PlayerAction.STOP_SPRINTING;
    }
    
    public boolean isAction() {
        return this.packetId == -86;
    }
    
    public boolean PACKET_USE_ENTITY_INTERACT() {
        return this.PACKET_USE_ENTITY() && new WrappedPacketInUseEntity(this.rawPacket).getAction() == WrappedPacketInUseEntity.EntityUseAction.INTERACT;
    }
    
    public boolean PACKET_KEEPALIVE() {
        return this.STATE_RECEIVING() && this.packetId == -98;
    }
    
    public boolean PACKET_ABILITIES() {
        return this.STATE_RECEIVING() && this.packetId == -88;
    }
    
    public boolean PACKET_USE_ENTITY_INTERACT_AT() {
        return this.PACKET_USE_ENTITY() && new WrappedPacketInUseEntity(this.rawPacket).getAction() == WrappedPacketInUseEntity.EntityUseAction.INTERACT_AT;
    }
    
    public boolean isTransaction() {
        return this.packetId == -107 && new WrappedPacketInTransaction(this.rawPacket).getActionNumber() < 0;
    }
    
    public boolean PACKET_FLYING_TYPE() {
        return PacketType.Play.Client.Util.isInstanceOfFlying(this.packetId);
    }
    
    public boolean PACKET_LOOK() {
        return this.packetId == -94 || this.packetId == -95;
    }
    
    public boolean PACKET_POSITION_LOOK() {
        return this.packetId == -95;
    }
    
    public boolean PACKET_ARM_ANIMATION() {
        return this.STATE_RECEIVING() && this.packetId == -71;
    }
    
    public boolean PACKET_INCOMING_ABILITIES() {
        return this.STATE_SENDING() && this.packetId == -17;
    }
    
    public boolean PACKET_OUTGOING_ABILITIES() {
        return this.STATE_RECEIVING() && this.packetId == -88;
    }
    
    public boolean isEntityData() {
        return this.STATE_SENDING() && this.packetId == 1;
    }
    
    public boolean PACKET_BLOCKPLACE() {
        return this.STATE_RECEIVING() && PacketType.Play.Client.Util.isBlockPlace(this.packetId);
    }
    
    public boolean isGamemode() {
        return this.STATE_SENDING() && this.packetId == -36;
    }
    
    public boolean PACKET_BLOCK_DIG() {
        return this.STATE_RECEIVING() && this.packetId == -87;
    }
    
    public boolean PACKET_WINDOW_CLICK() {
        return this.STATE_RECEIVING() && this.packetId == -105;
    }
    
    public boolean PACKET_ENTITY_ACTION() {
        return this.STATE_RECEIVING() && this.packetId == -86;
    }
    
    public boolean PACKET_CLOSE_WINDOW() {
        return this.STATE_RECEIVING() && this.packetId == -104;
    }
    
    public boolean PACKET_INCOMING_KEEPALIVE() {
        return this.STATE_RECEIVING() && this.packetId == -98;
    }
    
    public boolean PACKET_OUTGOING_KEEPALIVE() {
        return this.STATE_SENDING() && this.packetId == -34;
    }
    
    public boolean PACKET_STEER_VEHICLE() {
        return this.STATE_RECEIVING() && this.packetId == -85;
    }
    
    public boolean PACKET_INCOMING_HELD_ITEM_SLOT() {
        return this.STATE_RECEIVING() && this.packetId == -78;
    }
    
    public boolean PACKET_OUTGOING_HELD_ITEM_SLOT() {
        return this.STATE_SENDING() && this.packetId == -3;
    }
    
    public boolean PACKET_CLIENT_COMMAND() {
        return this.STATE_RECEIVING() && this.packetId == -110;
    }
    
    public boolean PACKET_CUSTOM_PAYLOAD() {
        return this.STATE_RECEIVING() && this.packetId == -103;
    }
    
    public boolean PACKET_INCOMING_TRANSACTION() {
        return this.STATE_RECEIVING() && this.packetId == -107;
    }
    
    public boolean PACKET_OUTGOING_TRANSACTION() {
        return this.STATE_SENDING() && this.packetId == -48;
    }
    
    public boolean PACKET_HELD_ITEM_SLOT() {
        return this.STATE_RECEIVING() && this.packetId == -78;
    }
    
    public boolean PACKET_OUT_POSITION() {
        return this.STATE_SENDING() && this.packetId == -13;
    }
    
    public boolean PACKET_EXPLOSION() {
        return this.STATE_SENDING() && this.packetId == -38;
    }
    
    public boolean PACKET_ENTITY_VELOCITY() {
        return this.STATE_SENDING() && this.packetId == 3;
    }
    
    public boolean PACKET_NAMED_ENTITY_SPAWN() {
        return this.STATE_SENDING() && this.packetId == 27;
    }
    
    public boolean PACKET_ENTITY_DESTROY() {
        return this.STATE_SENDING() && this.packetId == -11;
    }
    
    public boolean PACKET_ENTITY_TELEPORT() {
        return this.STATE_SENDING() && this.packetId == 20;
    }
    
    public boolean PACKET_REL_ENTITY_MOVE() {
        return this.STATE_SENDING() && this.packetId == -26;
    }
    
    public boolean PACKET_REL_MOVE() {
        return this.STATE_SENDING() && (this.packetId == -26 || this.packetId == -25 || this.packetId == -24);
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public Direction getDirection() {
        return this.direction;
    }
    
    public NMSPacket getRawPacket() {
        return this.rawPacket;
    }
    
    public byte getPacketId() {
        return this.packetId;
    }
    
    public enum Direction
    {
        SEND, 
        RECEIVE;
    }
}
