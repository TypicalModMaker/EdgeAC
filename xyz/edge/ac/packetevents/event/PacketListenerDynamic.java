package xyz.edge.ac.packetevents.event;

import xyz.edge.ac.packetevents.event.priority.PacketEventPriority;

@Deprecated
public abstract class PacketListenerDynamic extends PacketListenerAbstract
{
    @Deprecated
    public PacketListenerDynamic(final PacketEventPriority priority) {
        super(priority);
    }
    
    public PacketListenerDynamic(final PacketListenerPriority priority) {
        super(priority);
    }
    
    public PacketListenerDynamic() {
        super(PacketListenerPriority.NORMAL);
    }
}
