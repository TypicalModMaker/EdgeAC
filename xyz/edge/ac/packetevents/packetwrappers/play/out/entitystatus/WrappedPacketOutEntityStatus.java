package xyz.edge.ac.packetevents.packetwrappers.play.out.entitystatus;

import xyz.edge.ac.packetevents.PacketEvents;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

public class WrappedPacketOutEntityStatus extends WrappedPacketEntityAbstraction implements SendableWrapper
{
    private static boolean v_1_17;
    private static Constructor<?> packetConstructor;
    private byte status;
    
    public WrappedPacketOutEntityStatus(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutEntityStatus(final Entity entity, final byte status) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.status = status;
    }
    
    public WrappedPacketOutEntityStatus(final int entityID, final byte status) {
        this.entityID = entityID;
        this.status = status;
    }
    
    @Override
    protected void load() {
        WrappedPacketOutEntityStatus.v_1_17 = WrappedPacketOutEntityStatus.version.isNewerThanOrEquals(ServerVersion.v_1_17);
        try {
            if (WrappedPacketOutEntityStatus.v_1_17) {
                WrappedPacketOutEntityStatus.packetConstructor = PacketTypeClasses.Play.Server.ENTITY_STATUS.getConstructor(NMSUtils.packetDataSerializerClass);
            }
            else {
                WrappedPacketOutEntityStatus.packetConstructor = PacketTypeClasses.Play.Server.ENTITY_STATUS.getConstructor((Class<?>[])new Class[0]);
            }
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public byte getEntityStatus() {
        if (this.packet != null) {
            return this.readByte(0);
        }
        return this.status;
    }
    
    public void setEntityStatus(final byte status) {
        if (this.packet != null) {
            this.writeByte(0, status);
        }
        else {
            this.status = status;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance;
        if (WrappedPacketOutEntityStatus.v_1_17) {
            final Object packetDataSerializer = NMSUtils.generatePacketDataSerializer(PacketEvents.get().getByteBufUtil().newByteBuf(new byte[] { 0, 0, 0, 0, 0 }));
            packetInstance = WrappedPacketOutEntityStatus.packetConstructor.newInstance(packetDataSerializer);
        }
        else {
            packetInstance = WrappedPacketOutEntityStatus.packetConstructor.newInstance(new Object[0]);
        }
        final WrappedPacketOutEntityStatus entityStatus = new WrappedPacketOutEntityStatus(new NMSPacket(packetInstance));
        entityStatus.setEntityId(this.getEntityId());
        entityStatus.setEntityStatus(this.getEntityStatus());
        return packetInstance;
    }
}
