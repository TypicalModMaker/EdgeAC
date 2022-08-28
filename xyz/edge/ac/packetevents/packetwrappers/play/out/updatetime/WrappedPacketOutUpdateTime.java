package xyz.edge.ac.packetevents.packetwrappers.play.out.updatetime;

import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutUpdateTime extends WrappedPacket implements SendableWrapper
{
    private static Constructor<?> packetConstructor;
    private long worldAgeTicks;
    private long timeOfDayTicks;
    
    public WrappedPacketOutUpdateTime(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutUpdateTime(final long worldAgeTicks, final long timeOfDayTicks) {
        this.worldAgeTicks = worldAgeTicks;
        this.timeOfDayTicks = timeOfDayTicks;
    }
    
    @Override
    protected void load() {
        try {
            WrappedPacketOutUpdateTime.packetConstructor = PacketTypeClasses.Play.Server.UPDATE_TIME.getConstructor(Long.TYPE, Long.TYPE, Boolean.TYPE);
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public long getWorldAgeTicks() {
        if (this.packet != null) {
            return this.readLong(0);
        }
        return this.worldAgeTicks;
    }
    
    public void setWorldAgeTicks(final long ticks) {
        if (this.packet != null) {
            this.writeLong(0, ticks);
        }
        else {
            this.worldAgeTicks = ticks;
        }
    }
    
    public long getTimeOfDayTicks() {
        if (this.packet != null) {
            return this.readLong(1);
        }
        return this.timeOfDayTicks;
    }
    
    public void setTimeOfDayTicks(final long ticks) {
        if (this.packet != null) {
            this.writeLong(1, ticks);
        }
        else {
            this.timeOfDayTicks = ticks;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        return WrappedPacketOutUpdateTime.packetConstructor.newInstance(this.getWorldAgeTicks(), this.getTimeOfDayTicks(), true);
    }
}
