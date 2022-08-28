package xyz.edge.ac.packetevents.packetwrappers.api;

public interface WrapperPacketReader
{
    boolean readBoolean(final int p0);
    
    byte readByte(final int p0);
    
    short readShort(final int p0);
    
    int readInt(final int p0);
    
    long readLong(final int p0);
    
    float readFloat(final int p0);
    
    double readDouble(final int p0);
    
    boolean[] readBooleanArray(final int p0);
    
    byte[] readByteArray(final int p0);
    
    short[] readShortArray(final int p0);
    
    int[] readIntArray(final int p0);
    
    long[] readLongArray(final int p0);
    
    float[] readFloatArray(final int p0);
    
    double[] readDoubleArray(final int p0);
    
    String[] readStringArray(final int p0);
    
    String readString(final int p0);
    
     <T> T readObject(final int p0, final Class<? extends T> p1);
    
    Enum<?> readEnumConstant(final int p0, final Class<? extends Enum<?>> p1);
    
    Object readAnyObject(final int p0);
}
