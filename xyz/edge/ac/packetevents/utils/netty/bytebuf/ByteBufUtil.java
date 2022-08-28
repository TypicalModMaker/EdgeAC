package xyz.edge.ac.packetevents.utils.netty.bytebuf;

public interface ByteBufUtil
{
    Object newByteBuf(final byte[] p0);
    
    void retain(final Object p0);
    
    void release(final Object p0);
    
    byte[] getBytes(final Object p0);
    
    void setBytes(final Object p0, final byte[] p1);
}
