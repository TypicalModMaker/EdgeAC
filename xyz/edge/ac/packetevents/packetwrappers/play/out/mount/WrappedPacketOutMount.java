package xyz.edge.ac.packetevents.packetwrappers.play.out.mount;

import xyz.edge.ac.packetevents.PacketEvents;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

public class WrappedPacketOutMount extends WrappedPacketEntityAbstraction implements SendableWrapper
{
    private static boolean v_1_17;
    private static Constructor<?> packetConstructor;
    private int[] passengerIDs;
    
    public WrappedPacketOutMount(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutMount(final int entityID, final int[] passengerIDs) {
        this.setEntityId(entityID);
        this.passengerIDs = passengerIDs;
    }
    
    public WrappedPacketOutMount(final Entity entity, final int[] passengerIDs) {
        this.setEntity(entity);
        this.passengerIDs = passengerIDs;
    }
    
    @Override
    protected void load() {
        WrappedPacketOutMount.v_1_17 = WrappedPacketOutMount.version.isNewerThanOrEquals(ServerVersion.v_1_17);
        try {
            if (WrappedPacketOutMount.v_1_17) {
                WrappedPacketOutMount.packetConstructor = PacketTypeClasses.Play.Server.MOUNT.getConstructor(NMSUtils.packetDataSerializerClass);
            }
            else {
                WrappedPacketOutMount.packetConstructor = PacketTypeClasses.Play.Server.MOUNT.getConstructor((Class<?>[])new Class[0]);
            }
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public int[] getPassengerIds() {
        if (this.packet != null) {
            return this.readIntArray(0);
        }
        return this.passengerIDs;
    }
    
    public void setPassengerIds(final int[] passengerIDs) {
        if (this.packet != null) {
            this.writeIntArray(0, passengerIDs);
        }
        else {
            this.passengerIDs = passengerIDs;
        }
    }
    
    @Override
    public boolean isSupported() {
        return WrappedPacketOutMount.version.isNewerThan(ServerVersion.v_1_8_8);
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance;
        if (WrappedPacketOutMount.v_1_17) {
            final Object byteBuf = PacketEvents.get().getByteBufUtil().newByteBuf(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 });
            final Object packetDataSerializer = NMSUtils.generatePacketDataSerializer(byteBuf);
            packetInstance = WrappedPacketOutMount.packetConstructor.newInstance(packetDataSerializer);
        }
        else {
            packetInstance = WrappedPacketOutMount.packetConstructor.newInstance(new Object[0]);
        }
        final WrappedPacketOutMount wrappedPacketOutMount = new WrappedPacketOutMount(new NMSPacket(packetInstance));
        wrappedPacketOutMount.setEntityId(this.getEntityId());
        wrappedPacketOutMount.setPassengerIds(this.getPassengerIds());
        return packetInstance;
    }
}
