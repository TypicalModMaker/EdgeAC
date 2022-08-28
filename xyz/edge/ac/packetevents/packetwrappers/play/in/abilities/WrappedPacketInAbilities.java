package xyz.edge.ac.packetevents.packetwrappers.play.in.abilities;

import java.util.Optional;
import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public final class WrappedPacketInAbilities extends WrappedPacket
{
    private static boolean v_1_16_Mode;
    
    public WrappedPacketInAbilities(final NMSPacket packet) {
        super(packet);
    }
    
    @Override
    protected void load() {
        WrappedPacketInAbilities.v_1_16_Mode = (Reflection.getField(PacketTypeClasses.Play.Client.ABILITIES, Boolean.TYPE, 1) == null);
    }
    
    public boolean isFlying() {
        return this.readBoolean(WrappedPacketInAbilities.v_1_16_Mode ? 0 : 1);
    }
    
    public void setFlying(final boolean flying) {
        this.writeBoolean(WrappedPacketInAbilities.v_1_16_Mode ? 0 : 1, flying);
    }
    
    public Optional<Boolean> isVulnerable() {
        if (WrappedPacketInAbilities.v_1_16_Mode) {
            return Optional.empty();
        }
        return Optional.of(this.readBoolean(0));
    }
    
    public void setVulnerable(final boolean vulnerable) {
        if (!WrappedPacketInAbilities.v_1_16_Mode) {
            this.writeBoolean(0, vulnerable);
        }
    }
    
    public Optional<Boolean> isFlightAllowed() {
        if (WrappedPacketInAbilities.v_1_16_Mode) {
            return Optional.empty();
        }
        return Optional.of(this.readBoolean(2));
    }
    
    public void setFlightAllowed(final boolean flightAllowed) {
        if (!WrappedPacketInAbilities.v_1_16_Mode) {
            this.writeBoolean(2, flightAllowed);
        }
    }
    
    public Optional<Boolean> canInstantlyBuild() {
        if (WrappedPacketInAbilities.v_1_16_Mode) {
            return Optional.empty();
        }
        return Optional.of(this.readBoolean(3));
    }
    
    public void setCanInstantlyBuild(final boolean canInstantlyBuild) {
        if (!WrappedPacketInAbilities.v_1_16_Mode) {
            this.writeBoolean(3, canInstantlyBuild);
        }
    }
    
    public Optional<Float> getFlySpeed() {
        if (WrappedPacketInAbilities.v_1_16_Mode) {
            return Optional.empty();
        }
        return Optional.of(this.readFloat(0));
    }
    
    public void setFlySpeed(final float flySpeed) {
        if (!WrappedPacketInAbilities.v_1_16_Mode) {
            this.writeFloat(0, flySpeed);
        }
    }
    
    public Optional<Float> getWalkSpeed() {
        if (WrappedPacketInAbilities.v_1_16_Mode) {
            return Optional.empty();
        }
        return Optional.of(this.readFloat(1));
    }
    
    public void setWalkSpeed(final float walkSpeed) {
        if (!WrappedPacketInAbilities.v_1_16_Mode) {
            this.writeFloat(1, walkSpeed);
        }
    }
}
