package xyz.edge.ac.packetevents.packetwrappers.play.in.clientcommand;

import xyz.edge.ac.packetevents.utils.enums.EnumUtil;
import xyz.edge.ac.packetevents.utils.reflection.SubclassUtil;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public final class WrappedPacketInClientCommand extends WrappedPacket
{
    private static boolean v_1_16;
    private static Class<? extends Enum<?>> enumClientCommandClass;
    
    public WrappedPacketInClientCommand(final NMSPacket packet) {
        super(packet);
    }
    
    @Override
    protected void load() {
        WrappedPacketInClientCommand.v_1_16 = WrappedPacketInClientCommand.version.isNewerThanOrEquals(ServerVersion.v_1_16);
        WrappedPacketInClientCommand.enumClientCommandClass = NMSUtils.getNMSEnumClassWithoutException("EnumClientCommand");
        if (WrappedPacketInClientCommand.enumClientCommandClass == null) {
            WrappedPacketInClientCommand.enumClientCommandClass = SubclassUtil.getEnumSubClass(PacketTypeClasses.Play.Client.CLIENT_COMMAND, "EnumClientCommand");
        }
    }
    
    public ClientCommand getClientCommand() {
        final Enum<?> enumConst = this.readEnumConstant(0, WrappedPacketInClientCommand.enumClientCommandClass);
        return ClientCommand.values()[enumConst.ordinal()];
    }
    
    public void setClientCommand(final ClientCommand command) throws UnsupportedOperationException {
        if (command == ClientCommand.OPEN_INVENTORY_ACHIEVEMENT && WrappedPacketInClientCommand.v_1_16) {
            this.throwUnsupportedOperation(command);
        }
        final Enum<?> enumConst = EnumUtil.valueByIndex(WrappedPacketInClientCommand.enumClientCommandClass, command.ordinal());
        this.writeEnumConstant(0, enumConst);
    }
    
    public enum ClientCommand
    {
        PERFORM_RESPAWN, 
        REQUEST_STATS, 
        @SupportedVersions(ranges = { ServerVersion.v_1_7_10, ServerVersion.v_1_15_2 })
        @Deprecated
        OPEN_INVENTORY_ACHIEVEMENT;
    }
}
