package xyz.edge.ac.packetevents.packetwrappers.login.out.custompayload;

import xyz.edge.ac.packetevents.PacketEvents;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketLoginOutCustomPayload extends WrappedPacket implements SendableWrapper
{
    private static boolean v_1_17;
    private static Constructor<?> constructor;
    private static Constructor<?> packetDataSerializerConstructor;
    private int messageID;
    private String channelName;
    private byte[] data;
    
    public WrappedPacketLoginOutCustomPayload(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketLoginOutCustomPayload(final int messageID, final String channelName, final byte[] data) {
        this.messageID = messageID;
        this.channelName = channelName;
        this.data = data;
    }
    
    @Override
    protected void load() {
        WrappedPacketLoginOutCustomPayload.v_1_17 = WrappedPacketLoginOutCustomPayload.version.isNewerThanOrEquals(ServerVersion.v_1_17);
        final Class<?> packetClass = PacketTypeClasses.Login.Server.CUSTOM_PAYLOAD;
        if (packetClass != null) {
            try {
                if (NMSUtils.packetDataSerializerClass != null) {
                    WrappedPacketLoginOutCustomPayload.packetDataSerializerConstructor = NMSUtils.packetDataSerializerClass.getConstructor(NMSUtils.byteBufClass);
                }
            }
            catch (final NoSuchMethodException e) {
                e.printStackTrace();
            }
            try {
                WrappedPacketLoginOutCustomPayload.constructor = packetClass.getConstructor(Integer.TYPE, NMSUtils.minecraftKeyClass, NMSUtils.packetDataSerializerClass);
            }
            catch (final NoSuchMethodException e2) {
                throw new IllegalStateException("PacketEvents is unable to resolve the PacketPlayOutCustomPayload constructor.");
            }
        }
    }
    
    public int getMessageId() {
        if (this.packet != null) {
            return this.readInt(WrappedPacketLoginOutCustomPayload.v_1_17 ? 1 : 0);
        }
        return this.messageID;
    }
    
    public void setMessageId(final int messageID) {
        if (this.packet != null) {
            this.writeInt(WrappedPacketLoginOutCustomPayload.v_1_17 ? 1 : 0, messageID);
        }
        else {
            this.messageID = messageID;
        }
    }
    
    public String getChannelName() {
        if (this.packet != null) {
            return this.readMinecraftKey(0);
        }
        return this.channelName;
    }
    
    public void setChannelName(final String channelName) {
        if (this.packet != null) {
            this.writeMinecraftKey(0, channelName);
        }
        else {
            this.channelName = channelName;
        }
    }
    
    public byte[] getData() {
        if (this.packet != null) {
            final Object dataSerializer = this.readObject(0, NMSUtils.packetDataSerializerClass);
            final WrappedPacket byteBufWrapper = new WrappedPacket(new NMSPacket(dataSerializer));
            final Object byteBuf = byteBufWrapper.readObject(0, NMSUtils.byteBufClass);
            return PacketEvents.get().getByteBufUtil().getBytes(byteBuf);
        }
        return this.data;
    }
    
    public void setData(final byte[] data) {
        if (this.packet != null) {
            PacketEvents.get().getByteBufUtil().setBytes(this.getBuffer(), data);
        }
        else {
            this.data = data;
        }
    }
    
    private Object getBuffer() {
        final Object dataSerializer = this.readObject(0, NMSUtils.packetDataSerializerClass);
        final WrappedPacket byteBufWrapper = new WrappedPacket(new NMSPacket(dataSerializer));
        return byteBufWrapper.readObject(0, NMSUtils.byteBufClass);
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        final Object byteBufObject = PacketEvents.get().getByteBufUtil().newByteBuf(this.data);
        final Object minecraftKey = NMSUtils.generateMinecraftKeyNew(this.channelName);
        final Object dataSerializer = WrappedPacketLoginOutCustomPayload.packetDataSerializerConstructor.newInstance(byteBufObject);
        return WrappedPacketLoginOutCustomPayload.constructor.newInstance(this.getMessageId(), minecraftKey, dataSerializer);
    }
    
    @Override
    public boolean isSupported() {
        return PacketTypeClasses.Login.Server.CUSTOM_PAYLOAD != null;
    }
}
