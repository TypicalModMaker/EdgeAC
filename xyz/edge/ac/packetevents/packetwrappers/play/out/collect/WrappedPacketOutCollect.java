package xyz.edge.ac.packetevents.packetwrappers.play.out.collect;

import java.util.Optional;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutCollect extends WrappedPacket implements SendableWrapper
{
    private static boolean v_1_11;
    private static Constructor<?> packetConstructor;
    private int collectedEntityId;
    private int collectorEntityId;
    private int itemCount;
    
    public WrappedPacketOutCollect(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutCollect(final int collectedEntityId, final int collectorEntityId, final int itemCount) {
        this.collectedEntityId = collectedEntityId;
        this.collectorEntityId = collectorEntityId;
        this.itemCount = itemCount;
    }
    
    @Override
    protected void load() {
        WrappedPacketOutCollect.v_1_11 = WrappedPacketOutCollect.version.isNewerThanOrEquals(ServerVersion.v_1_11);
        try {
            if (WrappedPacketOutCollect.v_1_11) {
                WrappedPacketOutCollect.packetConstructor = PacketTypeClasses.Play.Server.COLLECT.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE);
            }
            else {
                WrappedPacketOutCollect.packetConstructor = PacketTypeClasses.Play.Server.COLLECT.getConstructor(Integer.TYPE, Integer.TYPE);
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public int getCollectedEntityId() {
        if (this.packet != null) {
            return this.readInt(0);
        }
        return this.collectedEntityId;
    }
    
    public void setCollectedEntityId(final int id) {
        if (this.packet != null) {
            this.writeInt(0, id);
        }
        else {
            this.collectedEntityId = id;
        }
    }
    
    public int getCollectorEntityId() {
        if (this.packet != null) {
            return this.readInt(1);
        }
        return this.collectorEntityId;
    }
    
    public void setCollectorEntityId(final int id) {
        if (this.packet != null) {
            this.writeInt(1, id);
        }
        else {
            this.collectorEntityId = id;
        }
    }
    
    public Optional<Integer> getItemCount() {
        if (!WrappedPacketOutCollect.v_1_11) {
            return Optional.empty();
        }
        if (this.packet != null) {
            return Optional.of(this.readInt(2));
        }
        return Optional.of(this.itemCount);
    }
    
    public void setItemCount(final int count) {
        if (WrappedPacketOutCollect.v_1_11) {
            if (this.packet != null) {
                this.writeInt(2, count);
            }
            else {
                this.itemCount = count;
            }
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        return WrappedPacketOutCollect.v_1_11 ? WrappedPacketOutCollect.packetConstructor.newInstance(this.getCollectedEntityId(), this.getCollectorEntityId(), this.getItemCount().get()) : WrappedPacketOutCollect.packetConstructor.newInstance(this.getCollectedEntityId(), this.getCollectorEntityId());
    }
}
