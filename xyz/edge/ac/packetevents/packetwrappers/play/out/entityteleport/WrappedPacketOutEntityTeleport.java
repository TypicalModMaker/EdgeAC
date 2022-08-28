package xyz.edge.ac.packetevents.packetwrappers.play.out.entityteleport;

import xyz.edge.ac.packetevents.PacketEvents;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;
import org.bukkit.Location;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.utils.vector.Vector3d;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

public class WrappedPacketOutEntityTeleport extends WrappedPacketEntityAbstraction implements SendableWrapper
{
    private static final float rotationMultiplier = 0.7111111f;
    private static boolean v_1_17;
    private static boolean legacyVersionMode;
    private static boolean ultraLegacyVersionMode;
    private static Constructor<?> constructor;
    private Vector3d position;
    private float yaw;
    private float pitch;
    private boolean onGround;
    
    public WrappedPacketOutEntityTeleport(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutEntityTeleport(final int entityID, final Location loc, final boolean onGround) {
        this(entityID, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), onGround);
    }
    
    public WrappedPacketOutEntityTeleport(final Entity entity, final Location loc, final boolean onGround) {
        this(entity, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), onGround);
    }
    
    public WrappedPacketOutEntityTeleport(final int entityID, final Vector3d position, final float yaw, final float pitch, final boolean onGround) {
        this.entityID = entityID;
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }
    
    public WrappedPacketOutEntityTeleport(final Entity entity, final Vector3d position, final float yaw, final float pitch, final boolean onGround) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }
    
    public WrappedPacketOutEntityTeleport(final int entityID, final double x, final double y, final double z, final float yaw, final float pitch, final boolean onGround) {
        this.entityID = entityID;
        this.position = new Vector3d(x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }
    
    public WrappedPacketOutEntityTeleport(final Entity entity, final double x, final double y, final double z, final float yaw, final float pitch, final boolean onGround) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.position = new Vector3d(x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }
    
    private static int floor(final double value) {
        final int i = (int)value;
        return (value < i) ? (i - 1) : i;
    }
    
    @Override
    protected void load() {
        WrappedPacketOutEntityTeleport.v_1_17 = WrappedPacketOutEntityTeleport.version.isNewerThanOrEquals(ServerVersion.v_1_17);
        final Class<?> packetClass = PacketTypeClasses.Play.Server.ENTITY_TELEPORT;
        try {
            WrappedPacketOutEntityTeleport.constructor = packetClass.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Byte.TYPE, Byte.TYPE, Boolean.TYPE, Boolean.TYPE);
            WrappedPacketOutEntityTeleport.ultraLegacyVersionMode = true;
            WrappedPacketOutEntityTeleport.legacyVersionMode = true;
        }
        catch (final NoSuchMethodException e) {
            try {
                WrappedPacketOutEntityTeleport.constructor = packetClass.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Byte.TYPE, Byte.TYPE, Boolean.TYPE);
                WrappedPacketOutEntityTeleport.ultraLegacyVersionMode = false;
                WrappedPacketOutEntityTeleport.legacyVersionMode = true;
            }
            catch (final NoSuchMethodException e2) {
                try {
                    if (WrappedPacketOutEntityTeleport.v_1_17) {
                        WrappedPacketOutEntityTeleport.constructor = packetClass.getConstructor(NMSUtils.packetDataSerializerClass);
                    }
                    else {
                        WrappedPacketOutEntityTeleport.constructor = packetClass.getConstructor((Class<?>[])new Class[0]);
                    }
                    WrappedPacketOutEntityTeleport.ultraLegacyVersionMode = false;
                    WrappedPacketOutEntityTeleport.legacyVersionMode = false;
                }
                catch (final NoSuchMethodException e3) {
                    e3.printStackTrace();
                }
            }
        }
    }
    
    public Vector3d getPosition() {
        if (this.packet != null) {
            double x;
            double y;
            double z;
            if (WrappedPacketOutEntityTeleport.legacyVersionMode) {
                x = this.readInt(1) / 32.0;
                y = this.readInt(2) / 32.0;
                z = this.readInt(3) / 32.0;
            }
            else {
                x = this.readDouble(0);
                y = this.readDouble(1);
                z = this.readDouble(2);
            }
            return new Vector3d(x, y, z);
        }
        return this.position;
    }
    
    public void setPosition(final Vector3d position) {
        if (this.packet != null) {
            if (WrappedPacketOutEntityTeleport.legacyVersionMode) {
                this.writeInt(1, floor(position.x * 32.0));
                this.writeInt(2, floor(position.y * 32.0));
                this.writeInt(3, floor(position.z * 32.0));
            }
            else {
                this.writeDouble(0, position.x);
                this.writeDouble(1, position.y);
                this.writeDouble(2, position.z);
            }
        }
        else {
            this.position = position;
        }
    }
    
    public float getYaw() {
        if (this.packet != null) {
            return this.readByte(0) / 0.7111111f;
        }
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        if (this.packet != null) {
            this.writeByte(0, (byte)(yaw * 0.7111111f));
        }
        else {
            this.yaw = yaw;
        }
    }
    
    public float getPitch() {
        if (this.packet != null) {
            return this.readByte(1) / 0.7111111f;
        }
        return this.pitch;
    }
    
    public void setPitch(final float pitch) {
        if (this.packet != null) {
            this.writeByte(1, (byte)(pitch * 0.7111111f));
        }
        else {
            this.pitch = pitch;
        }
    }
    
    public boolean isOnGround() {
        if (this.packet != null) {
            return this.readBoolean(0);
        }
        return this.onGround;
    }
    
    public void setOnGround(final boolean onGround) {
        if (this.packet != null) {
            this.writeBoolean(0, onGround);
        }
        else {
            this.onGround = onGround;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        final Vector3d pos = this.getPosition();
        if (WrappedPacketOutEntityTeleport.ultraLegacyVersionMode) {
            return WrappedPacketOutEntityTeleport.constructor.newInstance(this.entityID, floor(pos.x * 32.0), floor(pos.y * 32.0), floor(pos.z * 32.0), (byte)((int)this.getYaw() * 0.7111111f), (byte)(this.getPitch() * 0.7111111f), false, false);
        }
        if (WrappedPacketOutEntityTeleport.legacyVersionMode) {
            return WrappedPacketOutEntityTeleport.constructor.newInstance(this.entityID, floor(pos.x * 32.0), floor(pos.y * 32.0), floor(pos.z * 32.0), (byte)((int)this.getYaw() * 0.7111111f), (byte)(this.getPitch() * 0.7111111f), false);
        }
        Object instance;
        if (WrappedPacketOutEntityTeleport.v_1_17) {
            final Object byteBuf = PacketEvents.get().getByteBufUtil().newByteBuf(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
            final Object packetDataSerializer = NMSUtils.generatePacketDataSerializer(byteBuf);
            instance = WrappedPacketOutEntityTeleport.constructor.newInstance(packetDataSerializer);
        }
        else {
            instance = WrappedPacketOutEntityTeleport.constructor.newInstance(new Object[0]);
        }
        final WrappedPacketOutEntityTeleport instanceWrapper = new WrappedPacketOutEntityTeleport(new NMSPacket(instance));
        instanceWrapper.setEntityId(this.getEntityId());
        instanceWrapper.setPosition(pos);
        instanceWrapper.setPitch(this.getPitch());
        instanceWrapper.setYaw(this.getYaw());
        if (this.isOnGround()) {
            instanceWrapper.setOnGround(true);
        }
        return instance;
    }
}
