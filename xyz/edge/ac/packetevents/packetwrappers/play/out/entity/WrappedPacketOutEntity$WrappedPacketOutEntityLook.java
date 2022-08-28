package xyz.edge.ac.packetevents.packetwrappers.play.out.entity;

import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;

public static class WrappedPacketOutEntityLook extends WrappedPacketOutEntity
{
    public WrappedPacketOutEntityLook(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutEntityLook(final int entityID, final float yaw, final float pitch, final boolean onGround) {
        super(entityID, 0.0, 0.0, 0.0, yaw, pitch, onGround, true);
    }
    
    @Override
    protected void load() {
        super.load();
        try {
            if (WrappedPacketOutEntity.access$000()) {
                WrappedPacketOutEntity.access$102(PacketTypeClasses.Play.Server.ENTITY_LOOK.getConstructor(Integer.TYPE, Byte.TYPE, Byte.TYPE, Boolean.TYPE));
            }
            else {
                WrappedPacketOutEntity.access$102(PacketTypeClasses.Play.Server.ENTITY_LOOK.getConstructor((Class<?>[])new Class[0]));
            }
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance;
        if (WrappedPacketOutEntity.access$000()) {
            final byte angleYaw = (byte)(this.getYaw() * 0.7111111f);
            final byte anglePitch = (byte)(this.getPitch() * 0.7111111f);
            packetInstance = WrappedPacketOutEntity.access$100().newInstance(this.getEntityId(), angleYaw, anglePitch, this.isOnGround());
        }
        else {
            packetInstance = WrappedPacketOutEntity.access$100().newInstance(new Object[0]);
            final WrappedPacketOutEntityLook wrapper = new WrappedPacketOutEntityLook(new NMSPacket(packetInstance));
            wrapper.setEntityId(this.getEntityId());
            wrapper.setYaw(this.getYaw());
            wrapper.setPitch(this.getPitch());
            wrapper.setOnGround(this.isOnGround());
            if (WrappedPacketOutEntity.access$200()) {
                wrapper.setRotating(true);
            }
        }
        return packetInstance;
    }
}
