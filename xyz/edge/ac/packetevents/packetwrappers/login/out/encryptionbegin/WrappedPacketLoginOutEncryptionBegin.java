package xyz.edge.ac.packetevents.packetwrappers.login.out.encryptionbegin;

import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import java.security.spec.EncodedKeySpec;
import java.security.spec.KeySpec;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.security.PublicKey;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketLoginOutEncryptionBegin extends WrappedPacket
{
    private static boolean v_1_17;
    
    public WrappedPacketLoginOutEncryptionBegin(final NMSPacket packet) {
        super(packet);
    }
    
    @Override
    protected void load() {
        WrappedPacketLoginOutEncryptionBegin.v_1_17 = WrappedPacketLoginOutEncryptionBegin.version.isNewerThanOrEquals(ServerVersion.v_1_17);
    }
    
    public String getEncodedString() {
        return this.readString(0);
    }
    
    public void setEncodedString(final String encodedString) {
        this.writeString(0, encodedString);
    }
    
    public PublicKey getPublicKey() {
        return WrappedPacketLoginOutEncryptionBegin.v_1_17 ? this.encrypt(this.readByteArray(0)) : this.readObject(0, (Class<? extends PublicKey>)PublicKey.class);
    }
    
    public void setPublicKey(final PublicKey key) {
        if (WrappedPacketLoginOutEncryptionBegin.v_1_17) {
            this.writeByteArray(0, key.getEncoded());
        }
        else {
            this.writeObject(0, key);
        }
    }
    
    public byte[] getVerifyToken() {
        return this.readByteArray(WrappedPacketLoginOutEncryptionBegin.v_1_17 ? 1 : 0);
    }
    
    public void setVerifyToken(final byte[] verifyToken) {
        this.writeByteArray(WrappedPacketLoginOutEncryptionBegin.v_1_17 ? 1 : 0, verifyToken);
    }
    
    private PublicKey encrypt(final byte[] bytes) {
        try {
            final EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(bytes);
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(encodedKeySpec);
        }
        catch (final Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean isSupported() {
        return PacketTypeClasses.Login.Server.ENCRYPTION_BEGIN != null;
    }
}
