package xyz.edge.ac.packetevents.packetwrappers.play.out.entity;

import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;

public static class WrappedPacketOutRelEntityMoveLook extends WrappedPacketOutEntity
{
    public WrappedPacketOutRelEntityMoveLook(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutRelEntityMoveLook(final int entityID, final double deltaX, final double deltaY, final double deltaZ, final float yaw, final float pitch, final boolean onGround) {
        super(entityID, deltaX, deltaY, deltaZ, yaw, pitch, onGround, false);
    }
    
    @Override
    protected void load() {
        super.load();
        try {
            if (WrappedPacketOutEntity.access$000()) {
                WrappedPacketOutEntity.access$602(PacketTypeClasses.Play.Server.REL_ENTITY_MOVE_LOOK.getConstructor(Integer.TYPE, Short.TYPE, Short.TYPE, Short.TYPE, Byte.TYPE, Byte.TYPE, Boolean.TYPE));
            }
            else {
                WrappedPacketOutEntity.access$602(PacketTypeClasses.Play.Server.REL_ENTITY_MOVE_LOOK.getConstructor((Class<?>[])new Class[0]));
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
            final short dx = (short)(this.getDeltaX() * WrappedPacketOutEntity.access$400());
            final short dy = (short)(this.getDeltaY() * WrappedPacketOutEntity.access$400());
            final short dz = (short)(this.getDeltaZ() * WrappedPacketOutEntity.access$400());
            final byte angleYaw = (byte)(this.getYaw() * 0.7111111f);
            final byte anglePitch = (byte)(this.getPitch() * 0.7111111f);
            packetInstance = WrappedPacketOutEntity.access$600().newInstance(this.getEntityId(), dx, dy, dz, angleYaw, anglePitch, this.isOnGround());
        }
        else {
            packetInstance = WrappedPacketOutEntity.access$600().newInstance(new Object[0]);
            final WrappedPacketOutRelEntityMoveLook wrapper = new WrappedPacketOutRelEntityMoveLook(new NMSPacket(packetInstance));
            wrapper.setEntityId(this.getEntityId());
            wrapper.setDeltaX(this.getDeltaX());
            wrapper.setDeltaY(this.getDeltaY());
            wrapper.setDeltaZ(this.getDeltaZ());
            wrapper.setYaw(this.getYaw());
            wrapper.setPitch(this.getPitch());
            wrapper.setOnGround(this.isOnGround());
            if (WrappedPacketOutEntity.access$200()) {
                wrapper.setRotating(true);
                if (WrappedPacketOutEntity.access$500()) {
                    wrapper.setMoving(true);
                }
            }
        }
        return packetInstance;
    }
}
