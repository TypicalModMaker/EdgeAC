package xyz.edge.ac.packetevents.packetwrappers.play.out.entity;

import java.util.Optional;
import java.util.Objects;
import java.lang.reflect.Field;
import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

public class WrappedPacketOutEntity extends WrappedPacketEntityAbstraction implements SendableWrapper
{
    private static boolean v_1_8;
    private static boolean v_1_15;
    private static boolean v_1_17;
    private static byte mode;
    private static double dXYZDivisor;
    private static final float ROTATION_FACTOR = 0.7111111f;
    private static int yawByteIndex;
    private static int pitchByteIndex;
    private static Constructor<?> entityPacketConstructor;
    private static Constructor<?> entityRelMovePacketConstructor;
    private static Constructor<?> entityLookConstructor;
    private static Constructor<?> entityRelMoveLookConstructor;
    private double deltaX;
    private double deltaY;
    private double deltaZ;
    private float pitch;
    private float yaw;
    private boolean onGround;
    private boolean rotating;
    private boolean moving;
    
    public WrappedPacketOutEntity(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutEntity(final int entityID, final double deltaX, final double deltaY, final double deltaZ, final float yaw, final float pitch, final boolean onGround, final boolean rotating, final boolean moving) {
        this.entityID = entityID;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.rotating = rotating;
        this.moving = moving;
    }
    
    public WrappedPacketOutEntity(final int entityID, final double deltaX, final double deltaY, final double deltaZ, final float yaw, final float pitch, final boolean onGround, final boolean rotating) {
        this(entityID, deltaX, deltaY, deltaZ, yaw, pitch, onGround, rotating, false);
    }
    
    public WrappedPacketOutEntity(final int entityID, final double deltaX, final double deltaY, final double deltaZ, final float yaw, final float pitch, final boolean onGround) {
        this(entityID, deltaX, deltaY, deltaZ, yaw, pitch, onGround, false, false);
    }
    
    public WrappedPacketOutEntity(final Entity entity, final double deltaX, final double deltaY, final double deltaZ, final float yaw, final float pitch, final boolean onGround, final boolean isLook) {
        this(entity.getEntityId(), deltaX, deltaY, deltaZ, yaw, pitch, onGround, isLook);
    }
    
    public WrappedPacketOutEntity(final Entity entity, final double deltaX, final double deltaY, final double deltaZ, final float yaw, final float pitch, final boolean onGround) {
        this(entity.getEntityId(), deltaX, deltaY, deltaZ, yaw, pitch, onGround, false);
    }
    
    @Override
    protected void load() {
        WrappedPacketOutEntity.v_1_8 = WrappedPacketOutEntity.version.isNewerThanOrEquals(ServerVersion.v_1_8);
        WrappedPacketOutEntity.v_1_15 = WrappedPacketOutEntity.version.isNewerThanOrEquals(ServerVersion.v_1_15);
        WrappedPacketOutEntity.v_1_17 = WrappedPacketOutEntity.version.isNewerThanOrEquals(ServerVersion.v_1_17);
        final Class<?> packetClass = PacketTypeClasses.Play.Server.ENTITY;
        final Field dxField = Reflection.getField(packetClass, WrappedPacketOutEntity.v_1_17 ? 2 : 1);
        if (Objects.requireNonNull(dxField).equals(Reflection.getField(packetClass, Byte.TYPE, 0))) {
            WrappedPacketOutEntity.mode = 0;
            WrappedPacketOutEntity.yawByteIndex = 3;
            WrappedPacketOutEntity.pitchByteIndex = 4;
        }
        else if (dxField.equals(Reflection.getField(packetClass, Integer.TYPE, 1))) {
            WrappedPacketOutEntity.mode = 1;
        }
        else if (dxField.equals(Reflection.getField(packetClass, Short.TYPE, 0)) || dxField.equals(Reflection.getField(packetClass, Short.TYPE, 1))) {
            WrappedPacketOutEntity.mode = 2;
        }
        if (WrappedPacketOutEntity.mode == 0) {
            WrappedPacketOutEntity.dXYZDivisor = 32.0;
        }
        else {
            WrappedPacketOutEntity.dXYZDivisor = 4096.0;
        }
        try {
            if (WrappedPacketOutEntity.v_1_17) {
                (WrappedPacketOutEntity.entityPacketConstructor = packetClass.getDeclaredConstructor(Integer.TYPE, Short.TYPE, Short.TYPE, Short.TYPE, Byte.TYPE, Byte.TYPE, Boolean.TYPE, Boolean.TYPE, Boolean.TYPE)).setAccessible(true);
            }
            else {
                WrappedPacketOutEntity.entityPacketConstructor = packetClass.getConstructor(Integer.TYPE);
            }
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public float getPitch() {
        if (this.packet != null) {
            return this.readByte(WrappedPacketOutEntity.pitchByteIndex) / 0.7111111f;
        }
        return this.pitch;
    }
    
    public void setPitch(final float pitch) {
        if (this.packet != null) {
            this.writeByte(WrappedPacketOutEntity.pitchByteIndex, (byte)(pitch * 0.7111111f));
        }
        else {
            this.pitch = pitch;
        }
    }
    
    public float getYaw() {
        if (this.packet != null) {
            return this.readByte(WrappedPacketOutEntity.yawByteIndex) / 0.7111111f;
        }
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        if (this.packet != null) {
            this.writeByte(WrappedPacketOutEntity.yawByteIndex, (byte)(yaw * 0.7111111f));
        }
    }
    
    public double getDeltaX() {
        if (this.packet == null) {
            return this.deltaX;
        }
        switch (WrappedPacketOutEntity.mode) {
            case 0: {
                return this.readByte(0) / WrappedPacketOutEntity.dXYZDivisor;
            }
            case 1: {
                return this.readInt(1) / WrappedPacketOutEntity.dXYZDivisor;
            }
            case 2: {
                return this.readShort(0) / WrappedPacketOutEntity.dXYZDivisor;
            }
            default: {
                return -1.0;
            }
        }
    }
    
    public void setDeltaX(final double deltaX) {
        if (this.packet != null) {
            final int dx = (int)(deltaX * WrappedPacketOutEntity.dXYZDivisor);
            switch (WrappedPacketOutEntity.mode) {
                case 0: {
                    this.writeByte(0, (byte)dx);
                    break;
                }
                case 1: {
                    this.writeInt(1, dx);
                    break;
                }
                case 2: {
                    this.writeShort(0, (short)dx);
                    break;
                }
            }
        }
        else {
            this.deltaX = deltaX;
        }
    }
    
    public double getDeltaY() {
        if (this.packet == null) {
            return this.deltaY;
        }
        switch (WrappedPacketOutEntity.mode) {
            case 0: {
                return this.readByte(1) / WrappedPacketOutEntity.dXYZDivisor;
            }
            case 1: {
                return this.readInt(2) / WrappedPacketOutEntity.dXYZDivisor;
            }
            case 2: {
                return this.readShort(1) / WrappedPacketOutEntity.dXYZDivisor;
            }
            default: {
                return -1.0;
            }
        }
    }
    
    public void setDeltaY(final double deltaY) {
        if (this.packet != null) {
            final int dy = (int)(deltaY * WrappedPacketOutEntity.dXYZDivisor);
            switch (WrappedPacketOutEntity.mode) {
                case 0: {
                    this.writeByte(1, (byte)dy);
                    break;
                }
                case 1: {
                    this.writeInt(2, dy);
                    break;
                }
                case 2: {
                    this.writeShort(1, (short)dy);
                    break;
                }
            }
        }
        else {
            this.deltaY = deltaY;
        }
    }
    
    public double getDeltaZ() {
        if (this.packet == null) {
            return this.deltaZ;
        }
        switch (WrappedPacketOutEntity.mode) {
            case 0: {
                return this.readByte(2) / WrappedPacketOutEntity.dXYZDivisor;
            }
            case 1: {
                return this.readInt(3) / WrappedPacketOutEntity.dXYZDivisor;
            }
            case 2: {
                return this.readShort(2) / WrappedPacketOutEntity.dXYZDivisor;
            }
            default: {
                return -1.0;
            }
        }
    }
    
    public void setDeltaZ(final double deltaZ) {
        if (this.packet != null) {
            final int dz = (int)(deltaZ * WrappedPacketOutEntity.dXYZDivisor);
            switch (WrappedPacketOutEntity.mode) {
                case 0: {
                    this.writeByte(2, (byte)dz);
                    break;
                }
                case 1: {
                    this.writeInt(3, dz);
                    break;
                }
                case 2: {
                    this.writeShort(2, (short)dz);
                    break;
                }
            }
        }
        else {
            this.deltaZ = deltaZ;
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
    
    public Optional<Boolean> isRotating() {
        if (!WrappedPacketOutEntity.v_1_8) {
            return Optional.empty();
        }
        if (this.packet != null) {
            return Optional.of(this.readBoolean(1));
        }
        return Optional.of(this.rotating);
    }
    
    public void setRotating(final boolean rotating) {
        if (WrappedPacketOutEntity.v_1_8) {
            if (this.packet != null) {
                this.writeBoolean(1, rotating);
            }
            else {
                this.rotating = rotating;
            }
        }
    }
    
    public Optional<Boolean> isMoving() {
        if (!WrappedPacketOutEntity.v_1_15) {
            return Optional.empty();
        }
        if (this.packet != null) {
            return Optional.of(this.readBoolean(2));
        }
        return Optional.of(this.moving);
    }
    
    public void setMoving(final boolean moving) {
        if (WrappedPacketOutEntity.v_1_15) {
            if (this.packet != null) {
                this.writeBoolean(2, moving);
            }
            else {
                this.moving = moving;
            }
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        if (WrappedPacketOutEntity.v_1_17) {
            final byte angleYaw = (byte)(this.getYaw() * 0.7111111f);
            final byte anglePitch = (byte)(this.getPitch() * 0.7111111f);
            return WrappedPacketOutEntity.entityPacketConstructor.newInstance(this.getEntityId(), (short)(this.getDeltaX() * WrappedPacketOutEntity.dXYZDivisor), (short)(this.getDeltaY() * WrappedPacketOutEntity.dXYZDivisor), (short)(this.getDeltaZ() * WrappedPacketOutEntity.dXYZDivisor), angleYaw, anglePitch, this.isOnGround(), this.isRotating().get(), this.isMoving().get());
        }
        final Object packetInstance = WrappedPacketOutEntity.entityPacketConstructor.newInstance(this.getEntityId());
        final WrappedPacketOutEntity wrapper = new WrappedPacketOutEntity(new NMSPacket(packetInstance));
        wrapper.setDeltaX(this.getDeltaX());
        wrapper.setDeltaY(this.getDeltaY());
        wrapper.setDeltaZ(this.getDeltaZ());
        wrapper.setYaw(this.getYaw());
        wrapper.setPitch(this.getPitch());
        wrapper.setOnGround(this.isOnGround());
        if (WrappedPacketOutEntity.v_1_8) {
            wrapper.setRotating(this.isRotating().get());
        }
        return packetInstance;
    }
    
    static {
        WrappedPacketOutEntity.yawByteIndex = 0;
        WrappedPacketOutEntity.pitchByteIndex = 1;
    }
    
    public static class WrappedPacketOutEntityLook extends WrappedPacketOutEntity
    {
        public WrappedPacketOutEntityLook(final NMSPacket packet) {
            super(packet);
        }
        
        public WrappedPacketOutEntityLook(final int entityID, final float yaw, final float pitch, final boolean onGround) {
            super(entityID, 0.0, 0.0, 0.0, yaw, pitch, onGround, true);
        }
        
        @Override
        protected void load() {
            super.load();
            try {
                if (WrappedPacketOutEntity.v_1_17) {
                    WrappedPacketOutEntity.entityLookConstructor = PacketTypeClasses.Play.Server.ENTITY_LOOK.getConstructor(Integer.TYPE, Byte.TYPE, Byte.TYPE, Boolean.TYPE);
                }
                else {
                    WrappedPacketOutEntity.entityLookConstructor = PacketTypeClasses.Play.Server.ENTITY_LOOK.getConstructor((Class<?>[])new Class[0]);
                }
            }
            catch (final NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public Object asNMSPacket() throws Exception {
            Object packetInstance;
            if (WrappedPacketOutEntity.v_1_17) {
                final byte angleYaw = (byte)(this.getYaw() * 0.7111111f);
                final byte anglePitch = (byte)(this.getPitch() * 0.7111111f);
                packetInstance = WrappedPacketOutEntity.entityLookConstructor.newInstance(this.getEntityId(), angleYaw, anglePitch, this.isOnGround());
            }
            else {
                packetInstance = WrappedPacketOutEntity.entityLookConstructor.newInstance(new Object[0]);
                final WrappedPacketOutEntityLook wrapper = new WrappedPacketOutEntityLook(new NMSPacket(packetInstance));
                wrapper.setEntityId(this.getEntityId());
                wrapper.setYaw(this.getYaw());
                wrapper.setPitch(this.getPitch());
                wrapper.setOnGround(this.isOnGround());
                if (WrappedPacketOutEntity.v_1_8) {
                    wrapper.setRotating(true);
                }
            }
            return packetInstance;
        }
    }
    
    public static class WrappedPacketOutRelEntityMove extends WrappedPacketOutEntity
    {
        public WrappedPacketOutRelEntityMove(final NMSPacket packet) {
            super(packet);
        }
        
        public WrappedPacketOutRelEntityMove(final int entityID, final double deltaX, final double deltaY, final double deltaZ, final boolean onGround) {
            super(entityID, deltaX, deltaY, deltaZ, 0.0f, 0.0f, onGround, false);
        }
        
        @Override
        protected void load() {
            super.load();
            try {
                if (WrappedPacketOutEntity.v_1_17) {
                    WrappedPacketOutEntity.entityRelMovePacketConstructor = PacketTypeClasses.Play.Server.REL_ENTITY_MOVE.getConstructor(Integer.TYPE, Short.TYPE, Short.TYPE, Short.TYPE, Boolean.TYPE);
                }
                else {
                    WrappedPacketOutEntity.entityRelMovePacketConstructor = PacketTypeClasses.Play.Server.REL_ENTITY_MOVE.getConstructor((Class<?>[])new Class[0]);
                }
            }
            catch (final NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public Object asNMSPacket() throws Exception {
            Object packetInstance;
            if (WrappedPacketOutEntity.v_1_17) {
                final short dx = (short)(this.getDeltaX() * WrappedPacketOutEntity.dXYZDivisor);
                final short dy = (short)(this.getDeltaY() * WrappedPacketOutEntity.dXYZDivisor);
                final short dz = (short)(this.getDeltaZ() * WrappedPacketOutEntity.dXYZDivisor);
                packetInstance = WrappedPacketOutEntity.entityRelMovePacketConstructor.newInstance(this.getEntityId(), dx, dy, dz, this.isOnGround());
            }
            else {
                packetInstance = WrappedPacketOutEntity.entityRelMovePacketConstructor.newInstance(new Object[0]);
                final WrappedPacketOutRelEntityMove wrapper = new WrappedPacketOutRelEntityMove(new NMSPacket(packetInstance));
                wrapper.setEntityId(this.getEntityId());
                wrapper.setDeltaX(this.getDeltaX());
                wrapper.setDeltaY(this.getDeltaY());
                wrapper.setDeltaZ(this.getDeltaZ());
                wrapper.setOnGround(this.isOnGround());
                if (WrappedPacketOutEntity.v_1_15) {
                    wrapper.setMoving(true);
                }
            }
            return packetInstance;
        }
    }
    
    public static class WrappedPacketOutRelEntityMoveLook extends WrappedPacketOutEntity
    {
        public WrappedPacketOutRelEntityMoveLook(final NMSPacket packet) {
            super(packet);
        }
        
        public WrappedPacketOutRelEntityMoveLook(final int entityID, final double deltaX, final double deltaY, final double deltaZ, final float yaw, final float pitch, final boolean onGround) {
            super(entityID, deltaX, deltaY, deltaZ, yaw, pitch, onGround, false);
        }
        
        @Override
        protected void load() {
            super.load();
            try {
                if (WrappedPacketOutEntity.v_1_17) {
                    WrappedPacketOutEntity.entityRelMoveLookConstructor = PacketTypeClasses.Play.Server.REL_ENTITY_MOVE_LOOK.getConstructor(Integer.TYPE, Short.TYPE, Short.TYPE, Short.TYPE, Byte.TYPE, Byte.TYPE, Boolean.TYPE);
                }
                else {
                    WrappedPacketOutEntity.entityRelMoveLookConstructor = PacketTypeClasses.Play.Server.REL_ENTITY_MOVE_LOOK.getConstructor((Class<?>[])new Class[0]);
                }
            }
            catch (final NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public Object asNMSPacket() throws Exception {
            Object packetInstance;
            if (WrappedPacketOutEntity.v_1_17) {
                final short dx = (short)(this.getDeltaX() * WrappedPacketOutEntity.dXYZDivisor);
                final short dy = (short)(this.getDeltaY() * WrappedPacketOutEntity.dXYZDivisor);
                final short dz = (short)(this.getDeltaZ() * WrappedPacketOutEntity.dXYZDivisor);
                final byte angleYaw = (byte)(this.getYaw() * 0.7111111f);
                final byte anglePitch = (byte)(this.getPitch() * 0.7111111f);
                packetInstance = WrappedPacketOutEntity.entityRelMoveLookConstructor.newInstance(this.getEntityId(), dx, dy, dz, angleYaw, anglePitch, this.isOnGround());
            }
            else {
                packetInstance = WrappedPacketOutEntity.entityRelMoveLookConstructor.newInstance(new Object[0]);
                final WrappedPacketOutRelEntityMoveLook wrapper = new WrappedPacketOutRelEntityMoveLook(new NMSPacket(packetInstance));
                wrapper.setEntityId(this.getEntityId());
                wrapper.setDeltaX(this.getDeltaX());
                wrapper.setDeltaY(this.getDeltaY());
                wrapper.setDeltaZ(this.getDeltaZ());
                wrapper.setYaw(this.getYaw());
                wrapper.setPitch(this.getPitch());
                wrapper.setOnGround(this.isOnGround());
                if (WrappedPacketOutEntity.v_1_8) {
                    wrapper.setRotating(true);
                    if (WrappedPacketOutEntity.v_1_15) {
                        wrapper.setMoving(true);
                    }
                }
            }
            return packetInstance;
        }
    }
}
