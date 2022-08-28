package xyz.edge.ac.packetevents.packetwrappers.api;

public interface WrapperPacketWriter
{
    void writeBoolean(final int p0, final boolean p1);
    
    void writeByte(final int p0, final byte p1);
    
    void writeShort(final int p0, final short p1);
    
    void writeInt(final int p0, final int p1);
    
    void writeLong(final int p0, final long p1);
    
    void writeFloat(final int p0, final float p1);
    
    void writeDouble(final int p0, final double p1);
    
    void writeString(final int p0, final String p1);
    
    void writeBooleanArray(final int p0, final boolean[] p1);
    
    void writeByteArray(final int p0, final byte[] p1);
    
    void writeShortArray(final int p0, final short[] p1);
    
    void writeIntArray(final int p0, final int[] p1);
    
    void writeLongArray(final int p0, final long[] p1);
    
    void writeFloatArray(final int p0, final float[] p1);
    
    void writeDoubleArray(final int p0, final double[] p1);
    
    void writeStringArray(final int p0, final String[] p1);
    
    void writeObject(final int p0, final Object p1);
    
    void writeAnyObject(final int p0, final Object p1);
    
    void writeEnumConstant(final int p0, final Enum<?> p1);
}
