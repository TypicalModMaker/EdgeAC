package xyz.edge.ac.packetevents.packetwrappers.play.out.namedsoundeffect;

import xyz.edge.ac.packetevents.utils.enums.EnumUtil;
import java.util.Optional;
import java.lang.reflect.InvocationTargetException;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import org.jetbrains.annotations.Nullable;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.utils.vector.Vector3d;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutNamedSoundEffect extends WrappedPacket implements SendableWrapper
{
    private static boolean v_1_9;
    private static boolean v_1_17;
    private static Constructor<?> packetConstructor;
    private static Constructor<?> soundEffectConstructor;
    private static Class<? extends Enum<?>> enumSoundCategoryClass;
    private static boolean soundEffectVarExists;
    private static float pitchMultiplier;
    private String soundEffectName;
    private SoundCategory soundCategory;
    private Vector3d effectPosition;
    private float volume;
    private float pitch;
    
    public WrappedPacketOutNamedSoundEffect(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutNamedSoundEffect(final String soundEffectName, @Nullable final SoundCategory soundCategory, final Vector3d effectPosition, final float volume, final float pitch) {
        this.soundEffectName = soundEffectName;
        this.soundCategory = soundCategory;
        this.effectPosition = effectPosition;
        this.volume = volume;
        this.pitch = pitch;
    }
    
    public WrappedPacketOutNamedSoundEffect(final String soundEffectName, @Nullable final SoundCategory soundCategory, final double effectPositionX, final double effectPositionY, final double effectPositionZ, final float volume, final float pitch) {
        this.soundEffectName = soundEffectName;
        this.soundCategory = soundCategory;
        this.effectPosition = new Vector3d(effectPositionX, effectPositionY, effectPositionZ);
        this.volume = volume;
        this.pitch = pitch;
    }
    
    @Override
    protected void load() {
        WrappedPacketOutNamedSoundEffect.v_1_9 = WrappedPacketOutNamedSoundEffect.version.isNewerThanOrEquals(ServerVersion.v_1_9);
        WrappedPacketOutNamedSoundEffect.v_1_17 = WrappedPacketOutNamedSoundEffect.version.isNewerThanOrEquals(ServerVersion.v_1_17);
        WrappedPacketOutNamedSoundEffect.soundEffectVarExists = (NMSUtils.soundEffectClass != null);
        if (WrappedPacketOutNamedSoundEffect.soundEffectVarExists) {
            WrappedPacketOutNamedSoundEffect.enumSoundCategoryClass = NMSUtils.getNMSEnumClassWithoutException("SoundCategory");
            if (WrappedPacketOutNamedSoundEffect.enumSoundCategoryClass == null) {
                WrappedPacketOutNamedSoundEffect.enumSoundCategoryClass = NMSUtils.getNMEnumClassWithoutException("sounds.SoundCategory");
            }
            try {
                WrappedPacketOutNamedSoundEffect.soundEffectConstructor = NMSUtils.soundEffectClass.getConstructor(NMSUtils.minecraftKeyClass);
            }
            catch (final NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        WrappedPacketOutNamedSoundEffect.pitchMultiplier = (WrappedPacketOutNamedSoundEffect.version.isNewerThan(ServerVersion.v_1_9_4) ? 1.0f : (WrappedPacketOutNamedSoundEffect.version.isNewerThan(ServerVersion.v_1_8_8) ? 63.5f : 63.0f));
        try {
            if (WrappedPacketOutNamedSoundEffect.v_1_17) {
                WrappedPacketOutNamedSoundEffect.packetConstructor = PacketTypeClasses.Play.Server.NAMED_SOUND_EFFECT.getConstructor(NMSUtils.soundEffectClass, WrappedPacketOutNamedSoundEffect.enumSoundCategoryClass, Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE);
            }
            else {
                WrappedPacketOutNamedSoundEffect.packetConstructor = PacketTypeClasses.Play.Server.NAMED_SOUND_EFFECT.getConstructor((Class<?>[])new Class[0]);
            }
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public String getSoundEffectName() {
        if (this.packet == null) {
            return this.soundEffectName;
        }
        if (WrappedPacketOutNamedSoundEffect.soundEffectVarExists) {
            final Object soundEffect = this.readObject(0, NMSUtils.soundEffectClass);
            final WrappedPacket soundEffectWrapper = new WrappedPacket(new NMSPacket(soundEffect));
            return soundEffectWrapper.readMinecraftKey(0);
        }
        return this.readString(0);
    }
    
    public void setSoundEffectName(final String name) {
        if (this.packet != null) {
            if (WrappedPacketOutNamedSoundEffect.soundEffectVarExists) {
                final Object minecraftKey = NMSUtils.generateMinecraftKeyNew(name);
                Object soundEffect = null;
                try {
                    soundEffect = WrappedPacketOutNamedSoundEffect.soundEffectConstructor.newInstance(minecraftKey);
                }
                catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                this.write(NMSUtils.soundEffectClass, 0, soundEffect);
            }
            else {
                this.writeString(0, name);
            }
        }
        else {
            this.soundEffectName = name;
        }
    }
    
    public Optional<SoundCategory> getSoundCategory() {
        if (!WrappedPacketOutNamedSoundEffect.v_1_9) {
            return Optional.empty();
        }
        if (this.packet != null) {
            final Enum<?> enumConst = this.readEnumConstant(0, WrappedPacketOutNamedSoundEffect.enumSoundCategoryClass);
            return Optional.ofNullable(SoundCategory.values()[enumConst.ordinal()]);
        }
        return Optional.of(this.soundCategory);
    }
    
    public void setSoundCategory(final SoundCategory soundCategory) {
        if (!WrappedPacketOutNamedSoundEffect.v_1_9) {
            return;
        }
        if (this.packet != null) {
            final Enum<?> enumConst = EnumUtil.valueByIndex(WrappedPacketOutNamedSoundEffect.enumSoundCategoryClass, soundCategory.ordinal());
            this.writeEnumConstant(0, enumConst);
        }
        else {
            this.soundCategory = soundCategory;
        }
    }
    
    public Vector3d getEffectPosition() {
        if (this.packet != null) {
            final double x = this.readInt(0) / 8.0;
            final double y = this.readInt(1) / 8.0;
            final double z = this.readInt(2) / 8.0;
            return new Vector3d(x, y, z);
        }
        return this.effectPosition;
    }
    
    public void setEffectPosition(final Vector3d effectPosition) {
        if (this.packet != null) {
            this.writeInt(0, (int)(effectPosition.x / 8.0));
            this.writeInt(1, (int)(effectPosition.y / 8.0));
            this.writeInt(2, (int)(effectPosition.z / 8.0));
        }
        else {
            this.effectPosition = effectPosition;
        }
    }
    
    public float getVolume() {
        if (this.packet != null) {
            return this.readFloat(WrappedPacketOutNamedSoundEffect.v_1_17 ? 1 : 0);
        }
        return this.volume;
    }
    
    public void setVolume(final float volume) {
        if (this.packet != null) {
            this.writeFloat(WrappedPacketOutNamedSoundEffect.v_1_17 ? 1 : 0, volume);
        }
        else {
            this.volume = volume;
        }
    }
    
    public float getPitch() {
        if (this.packet == null) {
            return this.pitch;
        }
        if (WrappedPacketOutNamedSoundEffect.version.isOlderThan(ServerVersion.v_1_10)) {
            return this.readInt(3) / WrappedPacketOutNamedSoundEffect.pitchMultiplier;
        }
        return this.readFloat(WrappedPacketOutNamedSoundEffect.v_1_17 ? 2 : 1);
    }
    
    public void setPitch(final float pitch) {
        if (this.packet != null) {
            if (WrappedPacketOutNamedSoundEffect.version.isOlderThan(ServerVersion.v_1_10)) {
                this.writeInt(1, (int)(pitch * WrappedPacketOutNamedSoundEffect.pitchMultiplier));
            }
            else {
                this.writeFloat(WrappedPacketOutNamedSoundEffect.v_1_17 ? 2 : 1, pitch);
            }
        }
        else {
            this.pitch = pitch;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance;
        if (WrappedPacketOutNamedSoundEffect.v_1_17) {
            final Object nmsSoundEffect = WrappedPacketOutNamedSoundEffect.soundEffectConstructor.newInstance(NMSUtils.generateMinecraftKeyNew(this.getSoundEffectName()));
            final Object nmsSoundCategory = EnumUtil.valueByIndex(WrappedPacketOutNamedSoundEffect.enumSoundCategoryClass, this.getSoundCategory().get().ordinal());
            final Vector3d effectPos = this.getEffectPosition();
            packetInstance = WrappedPacketOutNamedSoundEffect.packetConstructor.newInstance(nmsSoundEffect, nmsSoundCategory, effectPos.x, effectPos.y, effectPos.z, this.getVolume(), this.getPitch());
        }
        else {
            packetInstance = WrappedPacketOutNamedSoundEffect.packetConstructor.newInstance(new Object[0]);
            final WrappedPacketOutNamedSoundEffect wrappedPacketOutNamedSoundEffect = new WrappedPacketOutNamedSoundEffect(new NMSPacket(packetInstance));
            wrappedPacketOutNamedSoundEffect.setSoundEffectName(this.getSoundEffectName());
            if (WrappedPacketOutNamedSoundEffect.soundEffectVarExists) {
                wrappedPacketOutNamedSoundEffect.setSoundCategory(this.getSoundCategory().get());
            }
            wrappedPacketOutNamedSoundEffect.setEffectPosition(this.getEffectPosition());
            wrappedPacketOutNamedSoundEffect.setPitch(this.getPitch());
            wrappedPacketOutNamedSoundEffect.setVolume(this.getVolume());
        }
        return packetInstance;
    }
    
    static {
        WrappedPacketOutNamedSoundEffect.pitchMultiplier = 63.0f;
    }
    
    public enum SoundCategory
    {
        MASTER, 
        MUSIC, 
        RECORDS, 
        WEATHER, 
        BLOCKS, 
        HOSTILE, 
        NEUTRAL, 
        PLAYERS, 
        AMBIENT, 
        VOICE;
    }
}
