package xyz.edge.ac.packetevents.utils;

public class SaltSignature
{
    private final long salt;
    private final byte[] signature;
    
    public SaltSignature(final long salt, final byte[] signature) {
        this.salt = salt;
        this.signature = signature;
    }
    
    public long getSalt() {
        return this.salt;
    }
    
    public byte[] getSignature() {
        return this.signature;
    }
}
