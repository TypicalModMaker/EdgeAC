package xyz.edge.ac.packetevents.packetwrappers.play.out.entity;

import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;

public static class WrappedPacketOutRelEntityMove extends WrappedPacketOutEntity
{
    public WrappedPacketOutRelEntityMove(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutRelEntityMove(final int entityID, final double deltaX, final double deltaY, final double deltaZ, final boolean onGround) {
        super(entityID, deltaX, deltaY, deltaZ, 0.0f, 0.0f, onGround, false);
    }
    
    @Override
    protected void load() {
        super.load();
        try {
            if (WrappedPacketOutEntity.access$000()) {
                WrappedPacketOutEntity.access$302(PacketTypeClasses.Play.Server.REL_ENTITY_MOVE.getConstructor(Integer.TYPE, Short.TYPE, Short.TYPE, Short.TYPE, Boolean.TYPE));
            }
            else {
                WrappedPacketOutEntity.access$302(PacketTypeClasses.Play.Server.REL_ENTITY_MOVE.getConstructor((Class<?>[])new Class[0]));
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
            packetInstance = WrappedPacketOutEntity.access$300().newInstance(this.getEntityId(), dx, dy, dz, this.isOnGround());
        }
        else {
            packetInstance = WrappedPacketOutEntity.access$300().newInstance(new Object[0]);
            final WrappedPacketOutRelEntityMove wrapper = new WrappedPacketOutRelEntityMove(new NMSPacket(packetInstance));
            wrapper.setEntityId(this.getEntityId());
            wrapper.setDeltaX(this.getDeltaX());
            wrapper.setDeltaY(this.getDeltaY());
            wrapper.setDeltaZ(this.getDeltaZ());
            wrapper.setOnGround(this.isOnGround());
            if (WrappedPacketOutEntity.access$500()) {
                wrapper.setMoving(true);
            }
        }
        return packetInstance;
    }
}
