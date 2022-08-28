package xyz.edge.ac.packetevents.packetwrappers.play.out.updatehealth;

import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public final class WrappedPacketOutUpdateHealth extends WrappedPacket implements SendableWrapper
{
    private static Constructor<?> packetConstructor;
    private float health;
    private float foodSaturation;
    private int food;
    
    public WrappedPacketOutUpdateHealth(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutUpdateHealth(final float health, final int food, final float foodSaturation) {
        this.health = health;
        this.food = food;
        this.foodSaturation = foodSaturation;
    }
    
    @Override
    protected void load() {
        try {
            WrappedPacketOutUpdateHealth.packetConstructor = PacketTypeClasses.Play.Server.UPDATE_HEALTH.getConstructor(Float.TYPE, Integer.TYPE, Float.TYPE);
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public float getHealth() {
        if (this.packet != null) {
            return this.readFloat(0);
        }
        return this.health;
    }
    
    public void setHealth(final float health) {
        if (this.packet != null) {
            this.writeFloat(0, health);
        }
        else {
            this.health = health;
        }
    }
    
    public float getFoodSaturation() {
        if (this.packet != null) {
            return this.readFloat(1);
        }
        return this.foodSaturation;
    }
    
    public void setFoodSaturation(final float foodSaturation) {
        if (this.packet != null) {
            this.writeFloat(0, foodSaturation);
        }
        else {
            this.foodSaturation = foodSaturation;
        }
    }
    
    public int getFood() {
        if (this.packet != null) {
            return this.readInt(0);
        }
        return this.food;
    }
    
    public void setFood(final int food) {
        if (this.packet != null) {
            this.writeInt(0, food);
        }
        else {
            this.food = food;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        return WrappedPacketOutUpdateHealth.packetConstructor.newInstance(this.getHealth(), this.getFood(), this.getFoodSaturation());
    }
}
