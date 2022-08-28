package xyz.edge.ac.packetevents.packetwrappers.login.in.encryptionbegin;

import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import java.lang.reflect.InvocationTargetException;
import xyz.edge.ac.packetevents.utils.MojangEitherUtil;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.utils.SaltSignature;
import xyz.edge.ac.packetevents.utils.ConditionalValue;
import xyz.edge.ac.packetevents.utils.reflection.SubclassUtil;
import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketLoginInEncryptionBegin extends WrappedPacket
{
    private static Class<?> VANILLA_SALT_SIGNATURE_CLASS;
    private static Constructor<?> VANILLA_SALT_SIGNATURE_CONSTRUCTOR;
    
    public WrappedPacketLoginInEncryptionBegin(final NMSPacket packet) {
        super(packet);
    }
    
    @Override
    protected void load() {
        final Class<?> minecraftEncryptionClass = Reflection.getClassByNameWithoutException("net.minecraft.util.MinecraftEncryption");
        if (minecraftEncryptionClass != null) {
            WrappedPacketLoginInEncryptionBegin.VANILLA_SALT_SIGNATURE_CLASS = SubclassUtil.getSubClass(minecraftEncryptionClass, "b");
            try {
                WrappedPacketLoginInEncryptionBegin.VANILLA_SALT_SIGNATURE_CONSTRUCTOR = WrappedPacketLoginInEncryptionBegin.VANILLA_SALT_SIGNATURE_CLASS.getConstructor(Long.TYPE, byte[].class);
            }
            catch (final NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
    
    public byte[] getPublicKey() {
        return this.readByteArray(0);
    }
    
    public void setPublicKey(final byte[] key) {
        this.writeByteArray(0, key);
    }
    
    public ConditionalValue<byte[], SaltSignature> getVerifyTokenOrSaltSignature() {
        if (!WrappedPacketLoginInEncryptionBegin.version.isNewerThanOrEquals(ServerVersion.v_1_19)) {
            return ConditionalValue.makeLeft(this.readByteArray(1));
        }
        final ConditionalValue<byte[], Object> rawEither = this.readEither(0);
        if (rawEither.left().isPresent()) {
            return ConditionalValue.makeLeft(rawEither.left().get());
        }
        final Object rawRight = rawEither.right().get();
        final WrappedPacket rawRightWrapper = new WrappedPacket(new NMSPacket(rawRight));
        final SaltSignature ss = new SaltSignature(rawRightWrapper.readLong(0), rawRightWrapper.readByteArray(0));
        return ConditionalValue.makeRight(ss);
    }
    
    public void setVerifyTokenOrSaltSignature(final ConditionalValue<byte[], SaltSignature> value) {
        if (WrappedPacketLoginInEncryptionBegin.version.isNewerThanOrEquals(ServerVersion.v_1_19)) {
            Object either;
            if (value.left().isPresent()) {
                either = MojangEitherUtil.makeLeft(value.left().get());
            }
            else {
                final SaltSignature ss = value.right().get();
                Object rawSS = null;
                try {
                    rawSS = WrappedPacketLoginInEncryptionBegin.VANILLA_SALT_SIGNATURE_CONSTRUCTOR.newInstance(ss.getSalt(), ss.getSignature());
                }
                catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                either = MojangEitherUtil.makeRight(rawSS);
            }
            this.write(NMSUtils.mojangEitherClass, 0, either);
        }
        else {
            this.writeByteArray(1, value.left().get());
        }
    }
    
    @Override
    public boolean isSupported() {
        return PacketTypeClasses.Login.Client.ENCRYPTION_BEGIN != null;
    }
}
