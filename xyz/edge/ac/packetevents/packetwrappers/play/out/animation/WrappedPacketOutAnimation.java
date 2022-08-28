package xyz.edge.ac.packetevents.packetwrappers.play.out.animation;

import xyz.edge.ac.packetevents.PacketEvents;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

public final class WrappedPacketOutAnimation extends WrappedPacketEntityAbstraction implements SendableWrapper
{
    private static boolean v_1_17;
    private static Constructor<?> packetConstructor;
    private EntityAnimationType type;
    
    public WrappedPacketOutAnimation(final NMSPacket packet) {
        super(packet, WrappedPacketOutAnimation.v_1_17 ? 6 : 0);
    }
    
    public WrappedPacketOutAnimation(final Entity target, final EntityAnimationType type) {
        super(WrappedPacketOutAnimation.v_1_17 ? 6 : 0);
        this.entityID = target.getEntityId();
        this.entity = target;
        this.type = type;
    }
    
    public WrappedPacketOutAnimation(final int entityID, final EntityAnimationType type) {
        super(WrappedPacketOutAnimation.v_1_17 ? 6 : 0);
        this.entityID = entityID;
        this.entity = null;
        this.type = type;
    }
    
    @Override
    protected void load() {
        WrappedPacketOutAnimation.v_1_17 = WrappedPacketOutAnimation.version.isNewerThanOrEquals(ServerVersion.v_1_17);
        try {
            if (WrappedPacketOutAnimation.v_1_17) {
                WrappedPacketOutAnimation.packetConstructor = PacketTypeClasses.Play.Server.ANIMATION.getConstructor(NMSUtils.packetDataSerializerClass);
            }
            else {
                WrappedPacketOutAnimation.packetConstructor = PacketTypeClasses.Play.Server.ANIMATION.getConstructor((Class<?>[])new Class[0]);
            }
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public EntityAnimationType getAnimationType() {
        if (this.packet != null) {
            final byte id = (byte)this.readInt(WrappedPacketOutAnimation.v_1_17 ? 7 : 1);
            return EntityAnimationType.values()[id];
        }
        return this.type;
    }
    
    public void setAnimationType(final EntityAnimationType type) {
        if (this.packet != null) {
            this.writeInt(WrappedPacketOutAnimation.v_1_17 ? 7 : 1, type.ordinal());
        }
        else {
            this.type = type;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance;
        if (WrappedPacketOutAnimation.v_1_17) {
            final Object packetDataSerializer = NMSUtils.generatePacketDataSerializer(PacketEvents.get().getByteBufUtil().newByteBuf(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 }));
            packetInstance = WrappedPacketOutAnimation.packetConstructor.newInstance(packetDataSerializer);
        }
        else {
            packetInstance = WrappedPacketOutAnimation.packetConstructor.newInstance(new Object[0]);
        }
        final WrappedPacketOutAnimation animation = new WrappedPacketOutAnimation(new NMSPacket(packetInstance));
        animation.setEntityId(this.getEntityId());
        animation.setAnimationType(this.getAnimationType());
        return packetInstance;
    }
    
    public enum EntityAnimationType
    {
        SWING_MAIN_ARM, 
        TAKE_DAMAGE, 
        LEAVE_BED, 
        SWING_OFFHAND, 
        CRITICAL_EFFECT, 
        MAGIC_CRITICAL_EFFECT;
    }
}
