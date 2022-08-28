package xyz.edge.ac.packetevents.event.manager;

import xyz.edge.ac.packetevents.event.PacketListenerAbstract;
import xyz.edge.ac.packetevents.event.PacketListenerDynamic;
import xyz.edge.ac.packetevents.event.PacketListener;
import xyz.edge.ac.packetevents.event.PacketEvent;

public interface EventManager
{
    default EventManager callEvent(final PacketEvent event) {
        PEEventManager.EVENT_MANAGER_MODERN.callEvent(event);
        return this;
    }
    
    @Deprecated
    default EventManager registerListener(final PacketListener listener) {
        if (listener != null) {
            PEEventManager.EVENT_MANAGER_LEGACY.registerListener(listener);
        }
        return this;
    }
    
    @Deprecated
    default EventManager registerListeners(final PacketListener... listeners) {
        PEEventManager.EVENT_MANAGER_LEGACY.registerListeners(listeners);
        return this;
    }
    
    @Deprecated
    default EventManager unregisterListener(final PacketListener listener) {
        PEEventManager.EVENT_MANAGER_LEGACY.unregisterListener(listener);
        return this;
    }
    
    @Deprecated
    default EventManager unregisterListeners(final PacketListener... listeners) {
        PEEventManager.EVENT_MANAGER_LEGACY.unregisterListeners(listeners);
        return this;
    }
    
    @Deprecated
    default EventManager registerListener(final PacketListenerDynamic listener) {
        PEEventManager.EVENT_MANAGER_MODERN.registerListener(listener);
        return this;
    }
    
    @Deprecated
    default EventManager registerListeners(final PacketListenerDynamic... listeners) {
        PEEventManager.EVENT_MANAGER_MODERN.registerListeners((PacketListenerAbstract[])listeners);
        return this;
    }
    
    default EventManager registerListener(final PacketListenerAbstract listener) {
        if (listener != null) {
            PEEventManager.EVENT_MANAGER_MODERN.registerListener(listener);
        }
        return this;
    }
    
    default EventManager registerListeners(final PacketListenerAbstract... listeners) {
        PEEventManager.EVENT_MANAGER_MODERN.registerListeners(listeners);
        return this;
    }
    
    default EventManager unregisterListener(final PacketListenerAbstract listener) {
        if (listener != null) {
            PEEventManager.EVENT_MANAGER_MODERN.unregisterListener(listener);
        }
        return this;
    }
    
    default EventManager unregisterListeners(final PacketListenerAbstract... listeners) {
        PEEventManager.EVENT_MANAGER_MODERN.unregisterListeners(listeners);
        return this;
    }
    
    default EventManager unregisterAllListeners() {
        PEEventManager.EVENT_MANAGER_MODERN.unregisterAllListeners();
        PEEventManager.EVENT_MANAGER_LEGACY.unregisterAllListeners();
        return this;
    }
}
