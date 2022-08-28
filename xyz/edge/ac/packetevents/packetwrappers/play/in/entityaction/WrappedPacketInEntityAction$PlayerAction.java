package xyz.edge.ac.packetevents.packetwrappers.play.in.entityaction;

import org.jetbrains.annotations.Nullable;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

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
