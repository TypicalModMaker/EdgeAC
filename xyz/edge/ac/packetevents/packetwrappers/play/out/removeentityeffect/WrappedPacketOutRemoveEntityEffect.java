package xyz.edge.ac.packetevents.packetwrappers.play.out.removeentityeffect;

import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

public class WrappedPacketOutRemoveEntityEffect extends WrappedPacketEntityAbstraction implements SendableWrapper
{
    private static boolean v_1_8_x;
    private static boolean v_1_17;
    private static Constructor<?> packetConstructor;
    private int effectID;
    
    public WrappedPacketOutRemoveEntityEffect(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutRemoveEntityEffect(final Entity entity, final int effectID) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.effectID = effectID;
    }
    
    public WrappedPacketOutRemoveEntityEffect(final int entityID, final int effectID) {
        this.entityID = entityID;
        this.effectID = effectID;
    }
    
    @Override
    protected void load() {
        WrappedPacketOutRemoveEntityEffect.v_1_8_x = WrappedPacketOutRemoveEntityEffect.version.isOlderThan(ServerVersion.v_1_9);
        WrappedPacketOutRemoveEntityEffect.v_1_17 = WrappedPacketOutRemoveEntityEffect.version.isNewerThanOrEquals(ServerVersion.v_1_17);
        try {
            if (WrappedPacketOutRemoveEntityEffect.v_1_17) {
                WrappedPacketOutRemoveEntityEffect.packetConstructor = PacketTypeClasses.Play.Server.REMOVE_ENTITY_EFFECT.getConstructor(Integer.TYPE, NMSUtils.mobEffectListClass);
            }
            else {
                WrappedPacketOutRemoveEntityEffect.packetConstructor = PacketTypeClasses.Play.Server.REMOVE_ENTITY_EFFECT.getConstructor((Class<?>[])new Class[0]);
            }
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public int getEffectId() {
        if (this.packet == null) {
            return this.effectID;
        }
        if (WrappedPacketOutRemoveEntityEffect.v_1_8_x) {
            return this.readInt(1);
        }
        final Object nmsMobEffectList = this.readObject(0, NMSUtils.mobEffectListClass);
        return NMSUtils.getEffectId(nmsMobEffectList);
    }
    
    public void setEffectId(final int effectID) {
        if (this.packet != null) {
            if (WrappedPacketOutRemoveEntityEffect.v_1_8_x) {
                this.writeInt(1, effectID);
            }
            else {
                final Object nmsMobEffectList = NMSUtils.getMobEffectListById(effectID);
                this.write(NMSUtils.mobEffectListClass, 0, nmsMobEffectList);
            }
        }
        else {
            this.effectID = effectID;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance;
        if (WrappedPacketOutRemoveEntityEffect.v_1_17) {
            final Object nmsMobEffectList = NMSUtils.getMobEffectListById(this.getEffectId());
            packetInstance = WrappedPacketOutRemoveEntityEffect.packetConstructor.newInstance(this.getEntityId(), nmsMobEffectList);
        }
        else {
            packetInstance = WrappedPacketOutRemoveEntityEffect.packetConstructor.newInstance(new Object[0]);
            final WrappedPacketOutRemoveEntityEffect wrappedPacketOutRemoveEntityEffect = new WrappedPacketOutRemoveEntityEffect(new NMSPacket(packetInstance));
            wrappedPacketOutRemoveEntityEffect.setEntityId(this.getEntityId());
            wrappedPacketOutRemoveEntityEffect.setEffectId(this.getEffectId());
        }
        return packetInstance;
    }
}
