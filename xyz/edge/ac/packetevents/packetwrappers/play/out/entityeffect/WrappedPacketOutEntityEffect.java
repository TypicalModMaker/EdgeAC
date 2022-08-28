package xyz.edge.ac.packetevents.packetwrappers.play.out.entityeffect;

import java.util.function.Consumer;
import xyz.edge.ac.packetevents.PacketEvents;
import java.util.Optional;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import org.bukkit.Effect;
import org.bukkit.potion.PotionEffectType;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

public class WrappedPacketOutEntityEffect extends WrappedPacketEntityAbstraction implements SendableWrapper
{
    private static boolean v_1_7_10;
    private static boolean v_1_17;
    private static boolean v_1_18_2;
    private static boolean v_1_19;
    private static Constructor<?> packetConstructor;
    private int effectID;
    private int amplifier;
    private int duration;
    private byte byteMask;
    private boolean byteMaskInitialized;
    
    public WrappedPacketOutEntityEffect(final NMSPacket packet) {
        super(packet, WrappedPacketOutEntityEffect.v_1_17 ? 3 : 0);
        this.byteMaskInitialized = false;
    }
    
    public WrappedPacketOutEntityEffect(final Entity entity, final int effectID, final int amplifier, final int duration) {
        super(WrappedPacketOutEntityEffect.v_1_17 ? 3 : 0);
        this.byteMaskInitialized = false;
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.effectID = effectID;
        this.amplifier = amplifier;
        this.duration = duration;
        this.byteMaskInitialized = true;
    }
    
    public WrappedPacketOutEntityEffect(final int entityID, final int effectID, final int amplifier, final int duration, final boolean hideParticles, final boolean ambient, final boolean showIcon) {
        super(WrappedPacketOutEntityEffect.v_1_17 ? 3 : 0);
        this.byteMaskInitialized = false;
        this.entityID = entityID;
        this.effectID = effectID;
        this.amplifier = amplifier;
        this.duration = duration;
        this.byteMaskInitialized = true;
        this.byteMask = 0;
        if (WrappedPacketOutEntityEffect.version.isOlderThan(ServerVersion.v_1_9)) {
            this.byteMask = (byte)(hideParticles ? 1 : 0);
        }
        else {
            if (hideParticles) {
                this.byteMask |= 0x2;
            }
            if (WrappedPacketOutEntityEffect.version.isNewerThan(ServerVersion.v_1_8_8)) {
                if (ambient) {
                    this.byteMask |= 0x1;
                }
                if (WrappedPacketOutEntityEffect.version.isNewerThan(ServerVersion.v_1_12_2) && showIcon) {
                    this.byteMask |= 0x4;
                }
            }
        }
    }
    
    public WrappedPacketOutEntityEffect(final Entity entity, final PotionEffectType effectType, final int amplifier, final int duration) {
        this(entity, effectType.getId(), amplifier, duration);
    }
    
    public WrappedPacketOutEntityEffect(final int entityID, final PotionEffectType effectType, final int amplifier, final int duration, final boolean hideParticles, final boolean ambient, final boolean showIcon) {
        this(entityID, effectType.getId(), amplifier, duration, hideParticles, ambient, showIcon);
    }
    
    public WrappedPacketOutEntityEffect(final Entity entity, final Effect effect, final int amplifier, final int duration) {
        this(entity, effect.getId(), amplifier, duration);
    }
    
    public WrappedPacketOutEntityEffect(final int entityID, final Effect effect, final int amplifier, final int duration, final boolean hideParticles, final boolean ambient, final boolean showIcon) {
        this(entityID, effect.getId(), amplifier, duration, hideParticles, ambient, showIcon);
    }
    
    @Override
    protected void load() {
        WrappedPacketOutEntityEffect.v_1_7_10 = WrappedPacketOutEntityEffect.version.isOlderThan(ServerVersion.v_1_8);
        WrappedPacketOutEntityEffect.v_1_17 = WrappedPacketOutEntityEffect.version.isNewerThanOrEquals(ServerVersion.v_1_17);
        WrappedPacketOutEntityEffect.v_1_18_2 = WrappedPacketOutEntityEffect.version.isNewerThanOrEquals(ServerVersion.v_1_18_2);
        WrappedPacketOutEntityEffect.v_1_19 = WrappedPacketOutEntityEffect.version.isNewerThanOrEquals(ServerVersion.v_1_19);
        try {
            if (WrappedPacketOutEntityEffect.v_1_17) {
                WrappedPacketOutEntityEffect.packetConstructor = PacketTypeClasses.Play.Server.ENTITY_EFFECT.getConstructor(NMSUtils.packetDataSerializerClass);
            }
            else {
                WrappedPacketOutEntityEffect.packetConstructor = PacketTypeClasses.Play.Server.ENTITY_EFFECT.getConstructor((Class<?>[])new Class[0]);
            }
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public int getEffectId() {
        if (this.packet == null) {
            return this.effectID;
        }
        if (WrappedPacketOutEntityEffect.v_1_19) {
            final Object mobEffectList = this.readObject(0, NMSUtils.mobEffectListClass);
            return NMSUtils.getEffectId(mobEffectList);
        }
        if (WrappedPacketOutEntityEffect.v_1_18_2) {
            return this.readInt(4);
        }
        return this.readByte(0);
    }
    
    public void setEffectId(final int effectID) {
        if (this.packet != null) {
            if (WrappedPacketOutEntityEffect.v_1_19) {
                final Object mobEffectList = NMSUtils.getMobEffectListById(effectID);
                this.write(NMSUtils.mobEffectListClass, 0, mobEffectList);
            }
            else if (WrappedPacketOutEntityEffect.v_1_18_2) {
                this.writeInt(4, effectID);
            }
            else {
                this.writeByte(0, (byte)effectID);
            }
        }
        else {
            this.effectID = effectID;
        }
    }
    
    public int getAmplifier() {
        if (this.packet != null) {
            return this.readByte(WrappedPacketOutEntityEffect.v_1_18_2 ? 0 : 1);
        }
        return this.amplifier;
    }
    
    public void setAmplifier(final int amplifier) {
        if (this.packet != null) {
            this.writeByte(WrappedPacketOutEntityEffect.v_1_18_2 ? 0 : 1, (byte)amplifier);
        }
        else {
            this.amplifier = amplifier;
        }
    }
    
    public int getDuration() {
        if (this.packet == null) {
            return this.duration;
        }
        if (WrappedPacketOutEntityEffect.v_1_7_10) {
            return this.readShort(1);
        }
        if (WrappedPacketOutEntityEffect.v_1_19) {
            return this.readInt(4);
        }
        if (WrappedPacketOutEntityEffect.v_1_18_2) {
            return this.readInt(5);
        }
        return this.readInt(WrappedPacketOutEntityEffect.v_1_17 ? 4 : 1);
    }
    
    public void setDuration(int duration) {
        if (this.packet != null) {
            this.duration = duration;
            if (WrappedPacketOutEntityEffect.v_1_7_10) {
                if (duration > 32767) {
                    duration = 32767;
                }
                else if (duration < -32768) {
                    duration = -32768;
                }
                this.writeShort(0, (short)duration);
            }
            else if (WrappedPacketOutEntityEffect.v_1_19) {
                this.writeInt(4, duration);
            }
            else if (WrappedPacketOutEntityEffect.v_1_18_2) {
                this.writeInt(5, duration);
            }
            else {
                this.writeInt(WrappedPacketOutEntityEffect.v_1_17 ? 4 : 1, duration);
            }
        }
        else {
            this.duration = duration;
        }
    }
    
    private Optional<Byte> getByteMask() {
        if (WrappedPacketOutEntityEffect.v_1_7_10) {
            return Optional.empty();
        }
        if (this.packet != null && !this.byteMaskInitialized) {
            final byte byte1 = this.readByte(WrappedPacketOutEntityEffect.v_1_18_2 ? 1 : 2);
            this.byteMask = byte1;
            return Optional.of(byte1);
        }
        return Optional.of(this.byteMask);
    }
    
    private void setByteMask(final byte byteMask) {
        if (!WrappedPacketOutEntityEffect.v_1_7_10) {
            this.byteMask = byteMask;
            if (this.packet != null) {
                this.writeByte(WrappedPacketOutEntityEffect.v_1_18_2 ? 1 : 2, byteMask);
            }
        }
    }
    
    public Optional<Boolean> shouldHideParticles() {
        final Optional<Byte> byteMaskOptional = this.getByteMask();
        if (!byteMaskOptional.isPresent()) {
            return Optional.empty();
        }
        final byte byteMask = byteMaskOptional.get();
        if (WrappedPacketOutEntityEffect.version.isOlderThan(ServerVersion.v_1_9)) {
            return Optional.of(byteMask == 1);
        }
        return Optional.of((byteMask & 0x2) == 0x2);
    }
    
    public void setShouldHideParticles(final boolean hideParticles) {
        if (WrappedPacketOutEntityEffect.version.isNewerThan(ServerVersion.v_1_7_10)) {
            final Optional<Byte> byteMaskOptional = this.getByteMask();
            if (byteMaskOptional.isPresent()) {
                byte byteMask = byteMaskOptional.get();
                final boolean currentHideParticles = this.shouldHideParticles().get();
                if (hideParticles) {
                    byteMask |= 0x2;
                }
                else if (currentHideParticles) {
                    byteMask -= 2;
                }
                this.setByteMask(byteMask);
            }
        }
    }
    
    public Optional<Boolean> isAmbient() {
        if (WrappedPacketOutEntityEffect.version.isOlderThan(ServerVersion.v_1_9)) {
            return Optional.empty();
        }
        final Optional<Byte> byteMaskOptional = this.getByteMask();
        if (!byteMaskOptional.isPresent()) {
            return Optional.empty();
        }
        final byte byteMask = byteMaskOptional.get();
        return Optional.of((byteMask & 0x1) == 0x1);
    }
    
    public void setIsAmbient(final boolean ambient) {
        if (WrappedPacketOutEntityEffect.version.isNewerThan(ServerVersion.v_1_8_8)) {
            final Optional<Byte> byteMaskOptional = this.getByteMask();
            if (byteMaskOptional.isPresent()) {
                byte byteMask = byteMaskOptional.get();
                final boolean currentAmbient = this.isAmbient().get();
                if (ambient) {
                    byteMask |= 0x1;
                }
                else if (currentAmbient) {
                    --byteMask;
                }
                this.setByteMask(byteMask);
            }
        }
    }
    
    public Optional<Boolean> shouldShowIcon() {
        if (WrappedPacketOutEntityEffect.version.isOlderThan(ServerVersion.v_1_13)) {
            return Optional.empty();
        }
        final Optional<Byte> byteMaskOptional = this.getByteMask();
        if (!byteMaskOptional.isPresent()) {
            return Optional.empty();
        }
        final byte byteMask = byteMaskOptional.get();
        return Optional.of((byteMask & 0x4) == 0x4);
    }
    
    public void setShowIcon(final boolean showIcon) {
        if (WrappedPacketOutEntityEffect.version.isNewerThan(ServerVersion.v_1_12_2)) {
            final Optional<Byte> byteMaskOptional = this.getByteMask();
            if (byteMaskOptional.isPresent()) {
                byte byteMask = byteMaskOptional.get();
                final boolean currentShowIcon = this.shouldShowIcon().get();
                if (showIcon) {
                    byteMask |= 0x4;
                }
                else if (currentShowIcon) {
                    byteMask -= 4;
                }
                this.setByteMask(byteMask);
            }
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance;
        if (WrappedPacketOutEntityEffect.v_1_17) {
            final byte[] buffer = new byte[30];
            final Object packetDataSerializer = NMSUtils.generatePacketDataSerializer(PacketEvents.get().getByteBufUtil().newByteBuf(buffer));
            packetInstance = WrappedPacketOutEntityEffect.packetConstructor.newInstance(packetDataSerializer);
        }
        else {
            packetInstance = WrappedPacketOutEntityEffect.packetConstructor.newInstance(new Object[0]);
        }
        final WrappedPacketOutEntityEffect wrappedPacketOutEntityEffect = new WrappedPacketOutEntityEffect(new NMSPacket(packetInstance));
        wrappedPacketOutEntityEffect.setEntityId(this.getEntityId());
        wrappedPacketOutEntityEffect.setEffectId(this.getEffectId());
        wrappedPacketOutEntityEffect.setAmplifier(this.getAmplifier());
        wrappedPacketOutEntityEffect.setDuration(this.getDuration());
        final Optional<Byte> optionalByteMask = this.getByteMask();
        optionalByteMask.ifPresent(wrappedPacketOutEntityEffect::setByteMask);
        return packetInstance;
    }
}
