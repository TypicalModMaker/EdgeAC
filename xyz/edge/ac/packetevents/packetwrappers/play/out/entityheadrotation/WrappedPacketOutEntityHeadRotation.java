package xyz.edge.ac.packetevents.packetwrappers.play.out.entityheadrotation;

import xyz.edge.ac.packetevents.PacketEvents;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

public class WrappedPacketOutEntityHeadRotation extends WrappedPacketEntityAbstraction implements SendableWrapper
{
    private static boolean v_1_17;
    private static Constructor<?> packetConstructor;
    private static final float ROTATION_FACTOR = 0.7111111f;
    private float yaw;
    
    public WrappedPacketOutEntityHeadRotation(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutEntityHeadRotation(final int entityID, final float yaw) {
        this.entityID = entityID;
        this.yaw = yaw;
    }
    
    public WrappedPacketOutEntityHeadRotation(final Entity entity, final float yaw) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.yaw = yaw;
    }
    
    @Override
    protected void load() {
        WrappedPacketOutEntityHeadRotation.v_1_17 = WrappedPacketOutEntityHeadRotation.version.isNewerThanOrEquals(ServerVersion.v_1_17);
        try {
            if (WrappedPacketOutEntityHeadRotation.v_1_17) {
                WrappedPacketOutEntityHeadRotation.packetConstructor = PacketTypeClasses.Play.Server.ENTITY_HEAD_ROTATION.getConstructor(NMSUtils.packetDataSerializerClass);
            }
            else {
                WrappedPacketOutEntityHeadRotation.packetConstructor = PacketTypeClasses.Play.Server.ENTITY_HEAD_ROTATION.getConstructor((Class<?>[])new Class[0]);
            }
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public float getYaw() {
        if (this.packet != null) {
            return this.readByte(0) / 0.7111111f;
        }
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        if (this.packet != null) {
            this.writeByte(0, (byte)(yaw * 0.7111111f));
        }
        else {
            this.yaw = yaw;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance;
        if (WrappedPacketOutEntityHeadRotation.v_1_17) {
            final Object packetDataSerializer = NMSUtils.generatePacketDataSerializer(PacketEvents.get().getByteBufUtil().newByteBuf(new byte[] { 0, 0 }));
            packetInstance = WrappedPacketOutEntityHeadRotation.packetConstructor.newInstance(packetDataSerializer);
        }
        else {
            packetInstance = WrappedPacketOutEntityHeadRotation.packetConstructor.newInstance(new Object[0]);
        }
        final WrappedPacketOutEntityHeadRotation wrappedPacketOutEntityHeadRotation = new WrappedPacketOutEntityHeadRotation(new NMSPacket(packetInstance));
        wrappedPacketOutEntityHeadRotation.setEntityId(this.getEntityId());
        wrappedPacketOutEntityHeadRotation.setYaw(this.getYaw());
        return packetInstance;
    }
}
