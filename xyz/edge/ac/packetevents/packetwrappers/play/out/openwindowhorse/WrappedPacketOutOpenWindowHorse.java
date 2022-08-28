package xyz.edge.ac.packetevents.packetwrappers.play.out.openwindowhorse;

import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

public final class WrappedPacketOutOpenWindowHorse extends WrappedPacketEntityAbstraction implements SendableWrapper
{
    private static Constructor<?> packetConstructor;
    private int windowID;
    private int slotCount;
    
    public WrappedPacketOutOpenWindowHorse(final NMSPacket packet) {
        super(packet, 2);
    }
    
    @Override
    protected void load() {
        try {
            WrappedPacketOutOpenWindowHorse.packetConstructor = PacketTypeClasses.Play.Server.OPEN_WINDOW_HORSE.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE);
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public int getWindowId() {
        if (this.packet != null) {
            return this.readInt(0);
        }
        return this.windowID;
    }
    
    public void setWindowId(final int windowID) {
        if (this.packet != null) {
            this.writeInt(0, windowID);
        }
        else {
            this.windowID = windowID;
        }
    }
    
    public int getSlotCount() {
        if (this.packet != null) {
            return this.readInt(1);
        }
        return this.slotCount;
    }
    
    public void setSlotCount(final int slotCount) {
        if (this.packet != null) {
            this.writeInt(1, slotCount);
        }
        else {
            this.slotCount = slotCount;
        }
    }
    
    @Override
    public boolean isSupported() {
        return WrappedPacketOutOpenWindowHorse.version.isNewerThan(ServerVersion.v_1_15_2);
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        return WrappedPacketOutOpenWindowHorse.packetConstructor.newInstance(this.getWindowId(), this.getSlotCount(), this.getEntityId());
    }
}
