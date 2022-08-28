package xyz.edge.ac.packetevents.packetwrappers.play.in.entityaction;

import org.jetbrains.annotations.Nullable;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;
import xyz.edge.ac.packetevents.utils.enums.EnumUtil;
import xyz.edge.ac.packetevents.utils.reflection.SubclassUtil;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

public final class WrappedPacketInEntityAction extends WrappedPacketEntityAbstraction
{
    private static Class<? extends Enum<?>> enumPlayerActionClass;
    private static boolean newerThan_v_1_8_8;
    
    public WrappedPacketInEntityAction(final NMSPacket packet) {
        super(packet);
    }
    
    @Override
    protected void load() {
        WrappedPacketInEntityAction.newerThan_v_1_8_8 = WrappedPacketInEntityAction.version.isNewerThan(ServerVersion.v_1_8_8);
        WrappedPacketInEntityAction.enumPlayerActionClass = NMSUtils.getNMSEnumClassWithoutException("EnumPlayerAction");
        if (WrappedPacketInEntityAction.enumPlayerActionClass == null) {
            WrappedPacketInEntityAction.enumPlayerActionClass = SubclassUtil.getEnumSubClass(PacketTypeClasses.Play.Client.ENTITY_ACTION, "EnumPlayerAction");
        }
    }
    
    public PlayerAction getAction() {
        if (WrappedPacketInEntityAction.enumPlayerActionClass == null) {
            final int animationIndex = this.readInt(1) - 1;
            return PlayerAction.getByActionValue((byte)animationIndex);
        }
        final Enum<?> enumConst = this.readEnumConstant(0, WrappedPacketInEntityAction.enumPlayerActionClass);
        if (WrappedPacketInEntityAction.newerThan_v_1_8_8) {
            return PlayerAction.getByActionValue((byte)enumConst.ordinal());
        }
        return PlayerAction.getByName(enumConst.name());
    }
    
    public void setAction(final PlayerAction action) throws UnsupportedOperationException {
        if (WrappedPacketInEntityAction.enumPlayerActionClass == null) {
            final byte animationIndex = action.actionID;
            this.writeInt(1, animationIndex + 1);
        }
        else {
            Enum<?> enumConst;
            if (WrappedPacketInEntityAction.newerThan_v_1_8_8) {
                if (action == PlayerAction.RIDING_JUMP) {
                    this.throwUnsupportedOperation(action);
                }
                enumConst = EnumUtil.valueByIndex(WrappedPacketInEntityAction.enumPlayerActionClass, action.getActionValue());
            }
            else {
                enumConst = EnumUtil.valueOf(WrappedPacketInEntityAction.enumPlayerActionClass, action.name());
                if (enumConst == null) {
                    enumConst = EnumUtil.valueOf(WrappedPacketInEntityAction.enumPlayerActionClass, action.alias);
                }
            }
            this.writeEnumConstant(0, enumConst);
        }
    }
    
    public int getJumpBoost() {
        if (WrappedPacketInEntityAction.enumPlayerActionClass == null) {
            return this.readInt(2);
        }
        return this.readInt(1);
    }
    
    public void setJumpBoost(final int jumpBoost) {
        if (WrappedPacketInEntityAction.enumPlayerActionClass == null) {
            this.writeInt(2, jumpBoost);
        }
        else {
            this.writeInt(1, jumpBoost);
        }
    }
    
    public enum PlayerAction
    {
        START_SNEAKING((byte)0, "PRESS_SHIFT_KEY"), 
        STOP_SNEAKING((byte)1, "RELEASE_SHIFT_KEY"), 
        STOP_SLEEPING((byte)2), 
        START_SPRINTING((byte)3), 
        STOP_SPRINTING((byte)4), 
        START_RIDING_JUMP((byte)5), 
        STOP_RIDING_JUMP((byte)6), 
        OPEN_INVENTORY((byte)7), 
        START_FALL_FLYING((byte)8), 
        @SupportedVersions(ranges = { ServerVersion.v_1_7_10, ServerVersion.v_1_8_8 })
        @Deprecated
        RIDING_JUMP((byte)5);
        
        final byte actionID;
        final String alias;
        
        private PlayerAction(final byte actionID) {
            this.actionID = actionID;
            this.alias = "empty";
        }
        
        private PlayerAction(final byte actionID, final String alias) {
            this.actionID = actionID;
            this.alias = alias;
        }
        
        @Nullable
        public static PlayerAction getByActionValue(final byte value) {
            if (!WrappedPacket.version.isOlderThan(ServerVersion.v_1_9)) {
                return values()[value];
            }
            if (value == PlayerAction.RIDING_JUMP.actionID) {
                return PlayerAction.RIDING_JUMP;
            }
            for (final PlayerAction action : values()) {
                if (action.actionID == value) {
                    return action;
                }
            }
            return null;
        }
        
        @Nullable
        public static PlayerAction getByName(final String name) {
            for (final PlayerAction action : values()) {
                if (action.name().equals(name) || action.alias.equals(name)) {
                    return action;
                }
            }
            return null;
        }
        
        public byte getActionValue() {
            return this.actionID;
        }
    }
}
