package xyz.edge.ac.packetevents.packetwrappers.login.in.custompayload;

import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.PacketEvents;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketLoginInCustomPayload extends WrappedPacket
{
    private static boolean v_1_17;
    
    public WrappedPacketLoginInCustomPayload(final NMSPacket packet) {
        super(packet);
    }
    
    @Override
    protected void load() {
        WrappedPacketLoginInCustomPayload.v_1_17 = WrappedPacketLoginInCustomPayload.version.isNewerThanOrEquals(ServerVersion.v_1_17);
    }
    
    public int getMessageId() {
        return this.readInt(WrappedPacketLoginInCustomPayload.v_1_17 ? 1 : 0);
    }
    
    public void setMessageId(final int id) {
        this.writeInt(WrappedPacketLoginInCustomPayload.v_1_17 ? 1 : 0, id);
    }
    
    public byte[] getData() {
        return PacketEvents.get().getByteBufUtil().getBytes(this.getBuffer());
    }
    
    public void setData(final byte[] data) {
        PacketEvents.get().getByteBufUtil().setBytes(this.getBuffer(), data);
    }
    
    private Object getBuffer() {
        final Object dataSerializer = this.readObject(0, NMSUtils.packetDataSerializerClass);
        final WrappedPacket byteBufWrapper = new WrappedPacket(new NMSPacket(dataSerializer));
        return byteBufWrapper.readObject(0, NMSUtils.byteBufClass);
    }
    
    public void retain() {
        PacketEvents.get().getByteBufUtil().retain(this.getBuffer());
    }
    
    public void release() {
        PacketEvents.get().getByteBufUtil().release(this.getBuffer());
    }
    
    @Override
    public boolean isSupported() {
        return WrappedPacketLoginInCustomPayload.version.isNewerThan(ServerVersion.v_1_12_2);
    }
}
