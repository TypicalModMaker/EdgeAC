package xyz.edge.ac.packetevents.packetwrappers.play.in.chat;

import java.lang.reflect.InvocationTargetException;
import xyz.edge.ac.packetevents.utils.SaltSignature;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import java.time.Instant;
import java.util.Optional;
import xyz.edge.ac.packetevents.utils.reflection.SubclassUtil;
import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public final class WrappedPacketInChat extends WrappedPacket
{
    private static Class<?> VANILLA_SALT_SIGNATURE_CLASS;
    private static Constructor<?> VANILLA_SALT_SIGNATURE_CONSTRUCTOR;
    
    public WrappedPacketInChat(final NMSPacket packet) {
        super(packet);
    }
    
    @Override
    protected void load() {
        final Class<?> minecraftEncryptionClass = Reflection.getClassByNameWithoutException("net.minecraft.util.MinecraftEncryption");
        if (minecraftEncryptionClass != null) {
            WrappedPacketInChat.VANILLA_SALT_SIGNATURE_CLASS = SubclassUtil.getSubClass(minecraftEncryptionClass, "b");
            try {
                WrappedPacketInChat.VANILLA_SALT_SIGNATURE_CONSTRUCTOR = WrappedPacketInChat.VANILLA_SALT_SIGNATURE_CLASS.getConstructor(Long.TYPE, byte[].class);
            }
            catch (final NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
    
    public String getMessage() {
        return this.readString(0);
    }
    
    public void setMessage(final String message) {
        this.writeString(0, message);
    }
    
    public Optional<Instant> getInstant() {
        return WrappedPacketInChat.version.isNewerThanOrEquals(ServerVersion.v_1_19) ? Optional.of(this.readObject(0, (Class<? extends Instant>)Instant.class)) : Optional.empty();
    }
    
    public void setInstant(final Instant instant) {
        if (WrappedPacketInChat.version.isNewerThanOrEquals(ServerVersion.v_1_19)) {
            this.writeObject(0, instant);
        }
    }
    
    public Optional<SaltSignature> getSaltSignature() {
        if (WrappedPacketInChat.version.isNewerThanOrEquals(ServerVersion.v_1_19)) {
            final Object rawSS = this.readObject(0, WrappedPacketInChat.VANILLA_SALT_SIGNATURE_CLASS);
            final WrappedPacket rawSSWrapper = new WrappedPacket(new NMSPacket(rawSS));
            final SaltSignature ss = new SaltSignature(rawSSWrapper.readLong(0), rawSSWrapper.readByteArray(0));
            return Optional.of(ss);
        }
        return Optional.empty();
    }
    
    public void setSaltSignature(final SaltSignature saltSignature) {
        if (WrappedPacketInChat.version.isNewerThanOrEquals(ServerVersion.v_1_19)) {
            Object rawSS = null;
            try {
                rawSS = WrappedPacketInChat.VANILLA_SALT_SIGNATURE_CONSTRUCTOR.newInstance(saltSignature.getSalt(), saltSignature.getSignature());
            }
            catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            this.write(WrappedPacketInChat.VANILLA_SALT_SIGNATURE_CLASS, 0, rawSS);
        }
    }
    
    public boolean isSignedPreview() {
        return WrappedPacketInChat.version.isNewerThanOrEquals(ServerVersion.v_1_19) && this.readBoolean(0);
    }
    
    public void setSignedPreview(final boolean signedPreview) {
        if (WrappedPacketInChat.version.isNewerThanOrEquals(ServerVersion.v_1_19)) {
            this.writeBoolean(0, signedPreview);
        }
    }
}
