package xyz.edge.ac.packetevents.packetwrappers.api.helper;

import org.jetbrains.annotations.NotNull;
import xyz.edge.ac.packetevents.PacketEvents;
import org.jetbrains.annotations.Nullable;
import org.bukkit.World;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import org.bukkit.entity.Entity;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public abstract class WrappedPacketEntityAbstraction extends WrappedPacket
{
    private final int entityIDFieldIndex;
    protected Entity entity;
    protected int entityID;
    
    public WrappedPacketEntityAbstraction(final NMSPacket packet, final int entityIDFieldIndex) {
        super(packet);
        this.entityID = -1;
        this.entityIDFieldIndex = entityIDFieldIndex;
    }
    
    public WrappedPacketEntityAbstraction(final NMSPacket packet) {
        super(packet);
        this.entityID = -1;
        this.entityIDFieldIndex = 0;
    }
    
    public WrappedPacketEntityAbstraction(final int entityIDFieldIndex) {
        this.entityID = -1;
        this.entityIDFieldIndex = entityIDFieldIndex;
    }
    
    public WrappedPacketEntityAbstraction() {
        this.entityID = -1;
        this.entityIDFieldIndex = 0;
    }
    
    public int getEntityId() {
        if (this.entityID != -1 || this.packet == null) {
            return this.entityID;
        }
        return this.entityID = this.readInt(this.entityIDFieldIndex);
    }
    
    public void setEntityId(final int entityID) {
        if (this.packet != null) {
            this.writeInt(this.entityIDFieldIndex, this.entityID = entityID);
        }
        else {
            this.entityID = entityID;
        }
        this.entity = null;
    }
    
    @Nullable
    public Entity getEntity(@Nullable final World world) {
        if (this.entity != null) {
            return this.entity;
        }
        return PacketEvents.get().getServerUtils().getEntityById(world, this.getEntityId());
    }
    
    @Nullable
    public Entity getEntity() {
        if (this.entity != null) {
            return this.entity;
        }
        return PacketEvents.get().getServerUtils().getEntityById(this.getEntityId());
    }
    
    public void setEntity(@NotNull final Entity entity) {
        this.setEntityId(entity.getEntityId());
        this.entity = entity;
    }
}
