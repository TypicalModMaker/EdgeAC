package xyz.edge.ac.packetevents.packetwrappers.play.out.experience;

import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutExperience extends WrappedPacket implements SendableWrapper
{
    private static Constructor<?> packetConstructor;
    private float experienceBar;
    private int experienceLevel;
    private int totalExperience;
    
    public WrappedPacketOutExperience(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutExperience(final float experienceBar, final int experienceLevel, final int totalExperience) {
        this.experienceBar = experienceBar;
        this.experienceLevel = experienceLevel;
        this.totalExperience = totalExperience;
    }
    
    @Override
    protected void load() {
        try {
            WrappedPacketOutExperience.packetConstructor = PacketTypeClasses.Play.Server.EXPERIENCE.getConstructor(Float.TYPE, Integer.TYPE, Integer.TYPE);
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public float getExperienceBar() {
        if (this.packet != null) {
            return this.readFloat(0);
        }
        return this.experienceBar;
    }
    
    public void setExperienceBar(final float experienceBar) {
        if (this.packet != null) {
            this.writeFloat(0, experienceBar);
        }
        else {
            this.experienceBar = experienceBar;
        }
    }
    
    public int getExperienceLevel() {
        if (this.packet != null) {
            return this.readInt(0);
        }
        return this.experienceLevel;
    }
    
    public void setExperienceLevel(final int experienceLevel) {
        if (this.packet != null) {
            this.writeInt(0, experienceLevel);
        }
        else {
            this.experienceLevel = experienceLevel;
        }
    }
    
    public int getTotalExperience() {
        if (this.packet != null) {
            return this.readInt(1);
        }
        return this.totalExperience;
    }
    
    public void setTotalExperience(final int totalExperience) {
        if (this.packet != null) {
            this.writeInt(1, totalExperience);
        }
        else {
            this.totalExperience = totalExperience;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        return WrappedPacketOutExperience.packetConstructor.newInstance(this.getExperienceBar(), this.getExperienceLevel(), this.getTotalExperience());
    }
}
