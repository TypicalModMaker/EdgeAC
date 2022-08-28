package xyz.edge.ac.packetevents.packetwrappers.play.out.position;

import xyz.edge.ac.packetevents.utils.enums.EnumUtil;
import java.util.Iterator;
import java.util.Optional;
import xyz.edge.ac.packetevents.utils.reflection.SubclassUtil;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import java.util.HashSet;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.util.Set;
import xyz.edge.ac.packetevents.utils.vector.Vector3d;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public final class WrappedPacketOutPosition extends WrappedPacket implements SendableWrapper
{
    private static boolean v_1_8;
    private static boolean v_1_17;
    private static Constructor<?> packetConstructor;
    private static byte constructorMode;
    private static Class<? extends Enum<?>> enumPlayerTeleportFlagsClass;
    private Vector3d position;
    private float yaw;
    private float pitch;
    private Set<PlayerTeleportFlags> relativeFlags;
    private int teleportID;
    private boolean onGround;
    
    public WrappedPacketOutPosition(final NMSPacket packet) {
        super(packet);
        this.relativeFlags = new HashSet<PlayerTeleportFlags>();
    }
    
    @Deprecated
    public WrappedPacketOutPosition(final double x, final double y, final double z, final float yaw, final float pitch, final boolean onGround, final Set<PlayerTeleportFlags> relativeFlags) {
        this.position = new Vector3d(x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.relativeFlags = relativeFlags;
    }
    
    @Deprecated
    public WrappedPacketOutPosition(final double x, final double y, final double z, final float yaw, final float pitch, final Set<PlayerTeleportFlags> relativeFlags) {
        this.position = new Vector3d(x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
        this.relativeFlags = relativeFlags;
    }
    
    public WrappedPacketOutPosition(final double x, final double y, final double z, final float yaw, final float pitch, final Set<PlayerTeleportFlags> relativeFlags, final int teleportID) {
        this.position = new Vector3d(x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
        this.relativeFlags = relativeFlags;
        this.teleportID = teleportID;
    }
    
    public WrappedPacketOutPosition(final Vector3d position, final float yaw, final float pitch, final Set<PlayerTeleportFlags> relativeFlags, final int teleportID) {
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
        this.relativeFlags = relativeFlags;
        this.teleportID = teleportID;
    }
    
    public WrappedPacketOutPosition(final Vector3d position, final float yaw, final float pitch, final Set<PlayerTeleportFlags> relativeFlags, final int teleportID, final boolean onGround) {
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
        this.relativeFlags = relativeFlags;
        this.teleportID = teleportID;
        this.onGround = onGround;
    }
    
    @Override
    protected void load() {
        WrappedPacketOutPosition.v_1_8 = WrappedPacketOutPosition.version.isNewerThanOrEquals(ServerVersion.v_1_8);
        WrappedPacketOutPosition.v_1_17 = WrappedPacketOutPosition.version.isNewerThanOrEquals(ServerVersion.v_1_17);
        WrappedPacketOutPosition.enumPlayerTeleportFlagsClass = SubclassUtil.getEnumSubClass(PacketTypeClasses.Play.Server.POSITION, "EnumPlayerTeleportFlags");
        try {
            WrappedPacketOutPosition.packetConstructor = PacketTypeClasses.Play.Server.POSITION.getConstructor(Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE, Boolean.TYPE, Byte.TYPE);
        }
        catch (final NoSuchMethodException e) {
            WrappedPacketOutPosition.constructorMode = 1;
            try {
                WrappedPacketOutPosition.packetConstructor = PacketTypeClasses.Play.Server.POSITION.getConstructor(Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE, Set.class);
            }
            catch (final NoSuchMethodException e2) {
                WrappedPacketOutPosition.constructorMode = 2;
                try {
                    WrappedPacketOutPosition.packetConstructor = PacketTypeClasses.Play.Server.POSITION.getConstructor(Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE, Set.class, Integer.TYPE);
                }
                catch (final NoSuchMethodException e3) {
                    WrappedPacketOutPosition.constructorMode = 3;
                    try {
                        WrappedPacketOutPosition.packetConstructor = PacketTypeClasses.Play.Server.POSITION.getConstructor(Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE, Set.class, Integer.TYPE, Boolean.TYPE);
                    }
                    catch (final NoSuchMethodException e4) {
                        throw new IllegalStateException("Failed to locate a supported constructor of the PacketPlayOutPosition packet class.");
                    }
                }
            }
        }
    }
    
    public Optional<Boolean> isOnGround() {
        if (WrappedPacketOutPosition.v_1_8 && !WrappedPacketOutPosition.v_1_17) {
            return Optional.empty();
        }
        if (this.packet != null) {
            return Optional.of(this.readBoolean(0));
        }
        return Optional.of(this.onGround);
    }
    
    public void setOnGround(final boolean onGround) {
        if (WrappedPacketOutPosition.v_1_8 && !WrappedPacketOutPosition.v_1_17) {
            return;
        }
        if (this.packet != null) {
            this.writeBoolean(0, onGround);
        }
        else {
            this.onGround = onGround;
        }
    }
    
    public byte getRelativeFlagsMask() {
        byte relativeMask = 0;
        if (this.packet != null) {
            if (WrappedPacketOutPosition.version.isOlderThan(ServerVersion.v_1_8)) {
                relativeMask = this.readByte(0);
            }
            else {
                final Set<PlayerTeleportFlags> flags = this.getRelativeFlags();
                for (final PlayerTeleportFlags flag : flags) {
                    relativeMask |= flag.maskFlag;
                }
            }
        }
        else {
            final Set<PlayerTeleportFlags> flags = this.getRelativeFlags();
            for (final PlayerTeleportFlags flag : flags) {
                relativeMask |= flag.maskFlag;
            }
        }
        return relativeMask;
    }
    
    public void setRelativeFlagsMask(final byte mask) {
        if (this.packet != null) {
            if (WrappedPacketOutPosition.version.isOlderThan(ServerVersion.v_1_8)) {
                this.writeByte(0, mask);
            }
            else {
                final Set<Enum<?>> nmsRelativeFlags = new HashSet<Enum<?>>();
                for (final PlayerTeleportFlags flag : PlayerTeleportFlags.values()) {
                    if ((mask & flag.maskFlag) == flag.maskFlag) {
                        nmsRelativeFlags.add(EnumUtil.valueOf(WrappedPacketOutPosition.enumPlayerTeleportFlagsClass, flag.name()));
                    }
                }
                this.write(Set.class, 0, nmsRelativeFlags);
            }
        }
        else {
            this.relativeFlags.clear();
            for (final PlayerTeleportFlags flag2 : PlayerTeleportFlags.values()) {
                if ((mask & flag2.maskFlag) == flag2.maskFlag) {
                    this.relativeFlags.add(flag2);
                }
            }
        }
    }
    
    public Set<PlayerTeleportFlags> getRelativeFlags() {
        if (this.packet != null) {
            final Set<PlayerTeleportFlags> relativeFlags = new HashSet<PlayerTeleportFlags>();
            if (WrappedPacketOutPosition.version.isOlderThan(ServerVersion.v_1_8)) {
                final byte relativeBitMask = this.readByte(0);
                for (final PlayerTeleportFlags flag : PlayerTeleportFlags.values()) {
                    if ((relativeBitMask & flag.maskFlag) == flag.maskFlag) {
                        relativeFlags.add(flag);
                    }
                }
            }
            else {
                final Set<Enum<?>> set = this.readObject(0, (Class<? extends Set<Enum<?>>>)Set.class);
                for (final Enum<?> e : set) {
                    relativeFlags.add(PlayerTeleportFlags.valueOf(e.name()));
                }
            }
            return relativeFlags;
        }
        return this.relativeFlags;
    }
    
    public void setRelativeFlags(final Set<PlayerTeleportFlags> flags) {
        if (this.packet != null) {
            if (WrappedPacketOutPosition.version.isOlderThan(ServerVersion.v_1_8)) {
                byte relativeBitMask = 0;
                for (final PlayerTeleportFlags flag : flags) {
                    relativeBitMask |= flag.maskFlag;
                }
                this.writeByte(0, relativeBitMask);
            }
            else {
                final Set<Enum<?>> nmsRelativeFlags = new HashSet<Enum<?>>();
                for (final PlayerTeleportFlags flag : flags) {
                    nmsRelativeFlags.add(EnumUtil.valueOf(WrappedPacketOutPosition.enumPlayerTeleportFlagsClass, flag.name()));
                }
                this.write(Set.class, 0, nmsRelativeFlags);
            }
        }
        else {
            this.relativeFlags = flags;
        }
    }
    
    public Vector3d getPosition() {
        if (this.packet != null) {
            final double x = this.readDouble(0);
            final double y = this.readDouble(1);
            final double z = this.readDouble(2);
            return new Vector3d(x, y, z);
        }
        return this.position;
    }
    
    public void setPosition(final Vector3d position) {
        if (this.packet != null) {
            this.writeDouble(0, position.x);
            this.writeDouble(1, position.y);
            this.writeDouble(2, position.z);
        }
        else {
            this.position = position;
        }
    }
    
    public float getYaw() {
        if (this.packet != null) {
            return this.readFloat(0);
        }
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        if (this.packet != null) {
            this.writeFloat(0, yaw);
        }
        else {
            this.yaw = yaw;
        }
    }
    
    public float getPitch() {
        if (this.packet != null) {
            return this.readFloat(1);
        }
        return this.pitch;
    }
    
    public void setPitch(final float pitch) {
        if (this.packet != null) {
            this.writeFloat(1, pitch);
        }
        else {
            this.pitch = pitch;
        }
    }
    
    public Optional<Integer> getTeleportId() {
        if (WrappedPacketOutPosition.version.isOlderThan(ServerVersion.v_1_9)) {
            return Optional.empty();
        }
        if (this.packet != null) {
            return Optional.of(this.readInt(0));
        }
        return Optional.of(this.teleportID);
    }
    
    public void setTeleportId(final int teleportID) {
        if (WrappedPacketOutPosition.version.isOlderThan(ServerVersion.v_1_9)) {
            return;
        }
        if (this.packet != null) {
            this.writeInt(0, teleportID);
        }
        else {
            this.teleportID = teleportID;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        final Set<Object> nmsRelativeFlags = new HashSet<Object>();
        if (WrappedPacketOutPosition.constructorMode != 0) {
            for (final PlayerTeleportFlags flag : this.getRelativeFlags()) {
                nmsRelativeFlags.add(EnumUtil.valueOf(WrappedPacketOutPosition.enumPlayerTeleportFlagsClass, flag.name()));
            }
        }
        final Vector3d position = this.getPosition();
        switch (WrappedPacketOutPosition.constructorMode) {
            case 0: {
                return WrappedPacketOutPosition.packetConstructor.newInstance(position.x, position.y, position.z, this.getYaw(), this.getPitch(), this.isOnGround(), this.getRelativeFlagsMask());
            }
            case 1: {
                return WrappedPacketOutPosition.packetConstructor.newInstance(position.x, position.y, position.z, this.getYaw(), this.getPitch(), nmsRelativeFlags);
            }
            case 2: {
                return WrappedPacketOutPosition.packetConstructor.newInstance(position.x, position.y, position.z, this.getYaw(), this.getPitch(), nmsRelativeFlags, this.getTeleportId().get());
            }
            case 3: {
                return WrappedPacketOutPosition.packetConstructor.newInstance(position.x, position.y, position.z, this.getYaw(), this.getPitch(), nmsRelativeFlags, this.getTeleportId(), this.isOnGround().get());
            }
            default: {
                return null;
            }
        }
    }
    
    static {
        WrappedPacketOutPosition.constructorMode = 0;
    }
    
    public enum PlayerTeleportFlags
    {
        X(1), 
        Y(2), 
        Z(4), 
        Y_ROT(8), 
        X_ROT(16);
        
        final byte maskFlag;
        
        private PlayerTeleportFlags(final int maskFlag) {
            this.maskFlag = (byte)maskFlag;
        }
    }
}
