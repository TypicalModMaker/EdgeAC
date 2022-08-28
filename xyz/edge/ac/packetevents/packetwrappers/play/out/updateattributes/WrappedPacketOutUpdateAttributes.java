package xyz.edge.ac.packetevents.packetwrappers.play.out.updateattributes;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import org.bukkit.entity.Entity;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.utils.attributesnapshot.AttributeSnapshotWrapper;
import java.util.List;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

public class WrappedPacketOutUpdateAttributes extends WrappedPacketEntityAbstraction implements SendableWrapper
{
    private static Constructor<?> packetConstructor;
    private List<AttributeSnapshotWrapper> properties;
    
    public WrappedPacketOutUpdateAttributes(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutUpdateAttributes(final int entityID, final List<AttributeSnapshotWrapper> properties) {
        this.entityID = entityID;
        this.properties = properties;
    }
    
    public WrappedPacketOutUpdateAttributes(final Entity entity, final List<AttributeSnapshotWrapper> properties) {
        this.entityID = entity.getEntityId();
        this.properties = properties;
    }
    
    @Override
    protected void load() {
        try {
            WrappedPacketOutUpdateAttributes.packetConstructor = PacketTypeClasses.Play.Server.UPDATE_ATTRIBUTES.getConstructor(Integer.TYPE, Collection.class);
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public List<AttributeSnapshotWrapper> getProperties() {
        if (this.packet != null) {
            final List<Object> list = this.readList(0);
            final List<AttributeSnapshotWrapper> attributeSnapshotWrappers = new ArrayList<AttributeSnapshotWrapper>(list.size());
            for (final Object nmsAttributeSnapshot : list) {
                attributeSnapshotWrappers.add(new AttributeSnapshotWrapper(new NMSPacket(nmsAttributeSnapshot)));
            }
            return attributeSnapshotWrappers;
        }
        return this.properties;
    }
    
    public void setProperties(final List<AttributeSnapshotWrapper> properties) {
        if (this.packet != null) {
            final List<Object> list = new ArrayList<Object>(properties.size());
            for (final AttributeSnapshotWrapper attributeSnapshotWrapper : properties) {
                list.add(attributeSnapshotWrapper.getNMSPacket().getRawNMSPacket());
            }
            this.writeList(0, list);
        }
        else {
            this.properties = properties;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        final List<AttributeSnapshotWrapper> properties = this.getProperties();
        final List<Object> nmsProperties = new ArrayList<Object>(properties.size());
        for (final AttributeSnapshotWrapper property : properties) {
            nmsProperties.add(property.getNMSPacket().getRawNMSPacket());
        }
        return WrappedPacketOutUpdateAttributes.packetConstructor.newInstance(this.getEntityId(), nmsProperties);
    }
}
