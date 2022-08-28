package xyz.edge.ac.packetevents.packetwrappers.play.in.updatesign;

import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.utils.vector.Vector3i;
import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInUpdateSign extends WrappedPacket
{
    private static boolean v_1_7_mode;
    private static boolean strArrayMode;
    
    public WrappedPacketInUpdateSign(final NMSPacket packet) {
        super(packet);
    }
    
    @Override
    protected void load() {
        WrappedPacketInUpdateSign.v_1_7_mode = (Reflection.getField(PacketTypeClasses.Play.Client.UPDATE_SIGN, Integer.TYPE, 0) != null);
        WrappedPacketInUpdateSign.strArrayMode = (Reflection.getField(PacketTypeClasses.Play.Client.UPDATE_SIGN, String[].class, 0) != null);
    }
    
    public Vector3i getBlockPosition() {
        if (WrappedPacketInUpdateSign.v_1_7_mode) {
            final int x = this.readInt(0);
            final int y = this.readInt(1);
            final int z = this.readInt(2);
            return new Vector3i(x, y, z);
        }
        return this.readBlockPosition(0);
    }
    
    public void setBlockPosition(final Vector3i blockPos) {
        if (WrappedPacketInUpdateSign.v_1_7_mode) {
            this.writeInt(0, blockPos.x);
            this.writeInt(1, blockPos.y);
            this.writeInt(2, blockPos.z);
        }
        else {
            this.writeBlockPosition(0, blockPos);
        }
    }
    
    public String[] getTextLines() {
        if (WrappedPacketInUpdateSign.strArrayMode) {
            return this.readStringArray(0);
        }
        final Object[] iChatComponents = (Object[])this.readAnyObject(1);
        return NMSUtils.readIChatBaseComponents(iChatComponents);
    }
    
    public void setTextLines(final String[] lines) {
        if (WrappedPacketInUpdateSign.strArrayMode) {
            this.writeStringArray(0, lines);
        }
        else {
            final Object[] iChatComponents = NMSUtils.generateIChatBaseComponents(lines);
            this.writeAnyObject(1, iChatComponents);
        }
    }
}
