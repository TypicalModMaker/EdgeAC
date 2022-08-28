package xyz.edge.ac.packetevents.event;

public enum PacketListenerPriority
{
    LOWEST, 
    LOW, 
    NORMAL, 
    HIGH, 
    HIGHEST, 
    MONITOR;
    
    public static PacketListenerPriority getById(final byte id) {
        return values()[id];
    }
    
    public byte getId() {
        return (byte)this.ordinal();
    }
}
