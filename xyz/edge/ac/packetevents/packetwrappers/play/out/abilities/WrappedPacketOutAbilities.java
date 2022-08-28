package xyz.edge.ac.packetevents.packetwrappers.play.out.abilities;

import java.lang.reflect.InvocationTargetException;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public final class WrappedPacketOutAbilities extends WrappedPacket implements SendableWrapper
{
    private static Constructor<?> packetConstructor;
    private static Constructor<?> playerAbilitiesConstructor;
    private static Class<?> playerAbilitiesClass;
    private boolean vulnerable;
    private boolean flying;
    private boolean allowFlight;
    private boolean instantBuild;
    private float flySpeed;
    private float walkSpeed;
    
    public WrappedPacketOutAbilities(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutAbilities(final boolean isVulnerable, final boolean isFlying, final boolean allowFlight, final boolean canBuildInstantly, final float flySpeed, final float walkSpeed) {
        this.vulnerable = isVulnerable;
        this.flying = isFlying;
        this.allowFlight = allowFlight;
        this.instantBuild = canBuildInstantly;
        this.flySpeed = flySpeed;
        this.walkSpeed = walkSpeed;
    }
    
    @Override
    protected void load() {
        try {
            WrappedPacketOutAbilities.playerAbilitiesClass = NMSUtils.getNMSClass("PlayerAbilities");
        }
        catch (final ClassNotFoundException e) {
            WrappedPacketOutAbilities.playerAbilitiesClass = NMSUtils.getNMClassWithoutException("world.entity.player.PlayerAbilities");
        }
        if (WrappedPacketOutAbilities.playerAbilitiesClass != null) {
            try {
                WrappedPacketOutAbilities.playerAbilitiesConstructor = WrappedPacketOutAbilities.playerAbilitiesClass.getConstructor((Class<?>[])new Class[0]);
            }
            catch (final NoSuchMethodException e2) {
                e2.printStackTrace();
            }
        }
        try {
            WrappedPacketOutAbilities.packetConstructor = PacketTypeClasses.Play.Server.ABILITIES.getConstructor(WrappedPacketOutAbilities.playerAbilitiesClass);
        }
        catch (final NoSuchMethodException e2) {
            e2.printStackTrace();
        }
    }
    
    public boolean isVulnerable() {
        if (this.packet != null) {
            return this.readBoolean(0);
        }
        return this.vulnerable;
    }
    
    public void setVulnerable(final boolean isVulnerable) {
        if (this.packet != null) {
            this.writeBoolean(0, isVulnerable);
        }
        else {
            this.vulnerable = isVulnerable;
        }
    }
    
    public boolean isFlying() {
        if (this.packet != null) {
            return this.readBoolean(1);
        }
        return this.flying;
    }
    
    public void setFlying(final boolean isFlying) {
        if (this.packet != null) {
            this.writeBoolean(1, isFlying);
        }
        else {
            this.flying = isFlying;
        }
    }
    
    public boolean isFlightAllowed() {
        if (this.packet != null) {
            return this.readBoolean(2);
        }
        return this.allowFlight;
    }
    
    public void setFlightAllowed(final boolean isFlightAllowed) {
        if (this.packet != null) {
            this.writeBoolean(2, isFlightAllowed);
        }
        else {
            this.allowFlight = isFlightAllowed;
        }
    }
    
    public boolean canBuildInstantly() {
        if (this.packet != null) {
            return this.readBoolean(3);
        }
        return this.instantBuild;
    }
    
    public void setCanBuildInstantly(final boolean canBuildInstantly) {
        if (this.packet != null) {
            this.writeBoolean(3, canBuildInstantly);
        }
        else {
            this.instantBuild = canBuildInstantly;
        }
    }
    
    public float getFlySpeed() {
        if (this.packet != null) {
            return this.readFloat(0);
        }
        return this.flySpeed;
    }
    
    public void setFlySpeed(final float flySpeed) {
        if (this.packet != null) {
            this.writeFloat(0, flySpeed);
        }
        else {
            this.flySpeed = flySpeed;
        }
    }
    
    public float getWalkSpeed() {
        if (this.packet != null) {
            return this.readFloat(1);
        }
        return this.walkSpeed;
    }
    
    public void setWalkSpeed(final float walkSpeed) {
        if (this.packet != null) {
            this.writeFloat(1, walkSpeed);
        }
        else {
            this.walkSpeed = walkSpeed;
        }
    }
    
    private Object getPlayerAbilities(final boolean vulnerable, final boolean flying, final boolean flightAllowed, final boolean canBuildInstantly, final float flySpeed, final float walkSpeed) {
        Object instance = null;
        try {
            instance = WrappedPacketOutAbilities.playerAbilitiesConstructor.newInstance(new Object[0]);
        }
        catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        final WrappedPacket wrapper = new WrappedPacket(new NMSPacket(instance));
        wrapper.writeBoolean(0, vulnerable);
        wrapper.writeBoolean(1, flying);
        wrapper.writeBoolean(2, flightAllowed);
        wrapper.writeBoolean(3, canBuildInstantly);
        wrapper.writeFloat(0, flySpeed);
        wrapper.writeFloat(1, walkSpeed);
        return instance;
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        return WrappedPacketOutAbilities.packetConstructor.newInstance(this.getPlayerAbilities(this.isVulnerable(), this.isFlying(), this.isFlightAllowed(), this.canBuildInstantly(), this.getFlySpeed(), this.getWalkSpeed()));
    }
}
