package xyz.edge.ac.packetevents.utils.netty.bytebuf;

import net.minecraft.util.io.netty.util.internal.EmptyArrays;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public final class ByteBufUtil_7 implements ByteBufUtil
{
    @Override
    public Object newByteBuf(final byte[] data) {
        return Unpooled.wrappedBuffer(data);
    }
    
    @Override
    public void retain(final Object byteBuf) {
        ((ByteBuf)byteBuf).retain();
    }
    
    @Override
    public void release(final Object byteBuf) {
        ((ByteBuf)byteBuf).release();
    }
    
    @Override
    public byte[] getBytes(final Object byteBuf) {
        final ByteBuf bb = (ByteBuf)byteBuf;
        if (bb.refCnt() < 1) {
            return EmptyArrays.EMPTY_BYTES;
        }
        byte[] bytes;
        if (bb.hasArray()) {
            bytes = bb.array();
        }
        else {
            bytes = new byte[bb.readableBytes()];
            bb.getBytes(bb.readerIndex(), bytes);
        }
        return bytes;
    }
    
    @Override
    public void setBytes(final Object byteBuf, final byte[] bytes) {
        final ByteBuf bb = (ByteBuf)byteBuf;
        if (bb.refCnt() < 1) {
            return;
        }
        final int bytesLength = bytes.length;
        if (bb.capacity() < bytesLength) {
            bb.capacity(bytesLength);
        }
        bb.setBytes(0, bytes);
    }
}
