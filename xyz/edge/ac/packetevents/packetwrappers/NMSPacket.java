package xyz.edge.ac.packetevents.packetwrappers;

import xyz.edge.ac.packetevents.utils.reflection.ClassUtil;

public class NMSPacket
{
    private final Object rawNMSPacket;
    
    public NMSPacket(final Object rawNMSPacket) {
        this.rawNMSPacket = rawNMSPacket;
    }
    
    public Object getRawNMSPacket() {
        return this.rawNMSPacket;
    }
    
    public String getName() {
        return ClassUtil.getClassSimpleName(this.rawNMSPacket.getClass());
    }
}
