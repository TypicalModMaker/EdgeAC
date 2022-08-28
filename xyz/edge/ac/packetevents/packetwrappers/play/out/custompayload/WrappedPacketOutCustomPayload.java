package xyz.edge.ac.packetevents.packetwrappers.play.out.custompayload;

import xyz.edge.ac.packetevents.PacketEvents;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutCustomPayload extends WrappedPacket implements SendableWrapper
{
    private static Constructor<?> constructor;
    private static Constructor<?> packetDataSerializerConstructor;
    private static int minecraftKeyIndexInClass;
    private static byte constructorMode;
    private String channelName;
    private byte[] data;
    
    public WrappedPacketOutCustomPayload(final String channelName, final byte[] data) {
        this.channelName = channelName;
        this.data = data;
    }
    
    public WrappedPacketOutCustomPayload(final NMSPacket packet) {
        super(packet);
    }
    
    @Override
    protected void load() {
        final Class<?> packetClass = PacketTypeClasses.Play.Server.CUSTOM_PAYLOAD;
        try {
            WrappedPacketOutCustomPayload.packetDataSerializerConstructor = NMSUtils.packetDataSerializerClass.getConstructor(NMSUtils.byteBufClass);
        }
        catch (final NullPointerException | NoSuchMethodException ex) {}
        try {
            WrappedPacketOutCustomPayload.constructor = packetClass.getConstructor(String.class, byte[].class);
            WrappedPacketOutCustomPayload.constructorMode = 0;
        }
        catch (final NoSuchMethodException e) {
            try {
                WrappedPacketOutCustomPayload.constructor = packetClass.getConstructor(String.class, NMSUtils.packetDataSerializerClass);
                WrappedPacketOutCustomPayload.constructorMode = 1;
            }
            catch (final NoSuchMethodException e2) {
                try {
                    for (int i = 0; i < packetClass.getDeclaredFields().length; ++i) {
                        final Field f = packetClass.getDeclaredFields()[i];
                        if (!Modifier.isStatic(f.getModifiers())) {
                            WrappedPacketOutCustomPayload.minecraftKeyIndexInClass = i;
                            break;
                        }
                    }
                    WrappedPacketOutCustomPayload.constructor = packetClass.getConstructor(NMSUtils.minecraftKeyClass, NMSUtils.packetDataSerializerClass);
                    WrappedPacketOutCustomPayload.constructorMode = 2;
                }
                catch (final NoSuchMethodException e3) {
                    throw new IllegalStateException("PacketEvents is unable to resolve the PacketPlayOutCustomPayload constructor.");
                }
            }
        }
    }
    
    public String getChannelName() {
        if (this.packet == null) {
            return this.channelName;
        }
        switch (WrappedPacketOutCustomPayload.constructorMode) {
            case 0:
            case 1: {
                return this.readString(0);
            }
            case 2: {
                return this.readMinecraftKey(WrappedPacketOutCustomPayload.minecraftKeyIndexInClass);
            }
            default: {
                return null;
            }
        }
    }
    
    public void setChannelName(final String channelName) {
        if (this.packet != null) {
            switch (WrappedPacketOutCustomPayload.constructorMode) {
                case 0:
                case 1: {
                    this.writeString(0, channelName);
                }
                case 2: {
                    this.writeMinecraftKey(0, channelName);
                    break;
                }
            }
        }
        else {
            this.channelName = channelName;
        }
    }
    
    public byte[] getData() {
        if (this.packet == null) {
            return this.data;
        }
        switch (WrappedPacketOutCustomPayload.constructorMode) {
            case 0: {
                return this.readByteArray(0);
            }
            case 1:
            case 2: {
                return PacketEvents.get().getByteBufUtil().getBytes(this.getBuffer());
            }
            default: {
                return new byte[0];
            }
        }
    }
    
    public void setData(final byte[] data) {
        if (this.packet != null) {
            switch (WrappedPacketOutCustomPayload.constructorMode) {
                case 0: {
                    this.writeByteArray(0, data);
                    break;
                }
                case 1:
                case 2: {
                    PacketEvents.get().getByteBufUtil().setBytes(this.getBuffer(), data);
                    break;
                }
            }
        }
        else {
            this.data = data;
        }
    }
    
    private Object getBuffer() {
        final Object dataSerializer = this.readObject(0, NMSUtils.packetDataSerializerClass);
        final WrappedPacket dataSerializerWrapper = new WrappedPacket(new NMSPacket(dataSerializer));
        return dataSerializerWrapper.readObject(0, NMSUtils.byteBufClass);
    }
    
    public void retain() {
        if (this.packet != null && WrappedPacketOutCustomPayload.constructorMode != 0) {
            PacketEvents.get().getByteBufUtil().retain(this.getBuffer());
        }
    }
    
    public void release() {
        if (this.packet != null && WrappedPacketOutCustomPayload.constructorMode != 0) {
            PacketEvents.get().getByteBufUtil().release(this.getBuffer());
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        final byte[] data = this.getData();
        switch (WrappedPacketOutCustomPayload.constructorMode) {
            case 0: {
                return WrappedPacketOutCustomPayload.constructor.newInstance(this.getChannelName(), data);
            }
            case 1: {
                final Object dataSerializer = WrappedPacketOutCustomPayload.packetDataSerializerConstructor.newInstance(PacketEvents.get().getByteBufUtil().newByteBuf(data));
                return WrappedPacketOutCustomPayload.constructor.newInstance(this.getChannelName(), dataSerializer);
            }
            case 2: {
                final Object minecraftKey = NMSUtils.generateMinecraftKeyNew(this.getChannelName());
                final Object dataSerializer = WrappedPacketOutCustomPayload.packetDataSerializerConstructor.newInstance(PacketEvents.get().getByteBufUtil().newByteBuf(data));
                return WrappedPacketOutCustomPayload.constructor.newInstance(minecraftKey, dataSerializer);
            }
            default: {
                return null;
            }
        }
    }
}
