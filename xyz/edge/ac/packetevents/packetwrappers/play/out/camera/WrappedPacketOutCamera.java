package xyz.edge.ac.packetevents.packetwrappers.play.out.camera;

import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;
import xyz.edge.ac.packetevents.PacketEvents;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

public class WrappedPacketOutCamera extends WrappedPacketEntityAbstraction implements SendableWrapper
{
    private static boolean v_1_17;
    private static Constructor<?> packetConstructor;
    
    public WrappedPacketOutCamera(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutCamera(final int entityID) {
        this.setEntityId(entityID);
    }
    
    @Override
    protected void load() {
        WrappedPacketOutCamera.v_1_17 = WrappedPacketOutCamera.version.isNewerThanOrEquals(ServerVersion.v_1_17);
        try {
            if (WrappedPacketOutCamera.v_1_17) {
                WrappedPacketOutCamera.packetConstructor = PacketTypeClasses.Play.Server.CAMERA.getConstructor(NMSUtils.packetDataSerializerClass);
            }
            else {
                WrappedPacketOutCamera.packetConstructor = PacketTypeClasses.Play.Server.CAMERA.getConstructor((Class<?>[])new Class[0]);
            }
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    @Deprecated
    public int getCameraId() {
        return this.getEntityId();
    }
    
    @Deprecated
    public void setCameraId(final int cameraID) {
        this.setEntityId(cameraID);
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance;
        if (WrappedPacketOutCamera.v_1_17) {
            final Object byteBuf = PacketEvents.get().getByteBufUtil().newByteBuf(new byte[] { 1 });
            final Object packetDataSerializer = NMSUtils.generatePacketDataSerializer(byteBuf);
            packetInstance = WrappedPacketOutCamera.packetConstructor.newInstance(packetDataSerializer);
        }
        else {
            packetInstance = WrappedPacketOutCamera.packetConstructor.newInstance(new Object[0]);
        }
        final WrappedPacket packetWrapper = new WrappedPacket(new NMSPacket(packetInstance));
        packetWrapper.writeInt(0, this.getEntityId());
        return packetInstance;
    }
    
    @Override
    public boolean isSupported() {
        return WrappedPacketOutCamera.version.isNewerThan(ServerVersion.v_1_7_10);
    }
}
