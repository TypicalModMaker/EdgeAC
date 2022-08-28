package xyz.edge.ac.packetevents.packetwrappers.play.in.resourcepackstatus;

import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.utils.enums.EnumUtil;
import xyz.edge.ac.packetevents.utils.reflection.SubclassUtil;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInResourcePackStatus extends WrappedPacket
{
    private static Class<? extends Enum<?>> enumResourcePackStatusClass;
    
    public WrappedPacketInResourcePackStatus(final NMSPacket packet) {
        super(packet);
    }
    
    @Override
    protected void load() {
        WrappedPacketInResourcePackStatus.enumResourcePackStatusClass = SubclassUtil.getEnumSubClass(PacketTypeClasses.Play.Client.RESOURCE_PACK_STATUS, "EnumResourcePackStatus");
    }
    
    public ResourcePackStatus getStatus() {
        final Enum<?> enumConst = this.readEnumConstant(0, WrappedPacketInResourcePackStatus.enumResourcePackStatusClass);
        return ResourcePackStatus.values()[enumConst.ordinal()];
    }
    
    public void setStatus(final ResourcePackStatus status) {
        final Enum<?> enumConst = EnumUtil.valueByIndex(WrappedPacketInResourcePackStatus.enumResourcePackStatusClass, status.ordinal());
        this.writeEnumConstant(0, enumConst);
    }
    
    @Override
    public boolean isSupported() {
        return WrappedPacketInResourcePackStatus.version.isNewerThan(ServerVersion.v_1_7_10);
    }
    
    public enum ResourcePackStatus
    {
        SUCCESSFULLY_LOADED, 
        DECLINED, 
        FAILED_DOWNLOAD, 
        ACCEPTED;
    }
}
