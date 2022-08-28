package xyz.edge.ac.packetevents.event.manager;

import java.util.Iterator;
import xyz.edge.ac.packetevents.event.eventtypes.CancellableEvent;
import java.util.logging.Level;
import xyz.edge.ac.packetevents.PacketEvents;
import xyz.edge.ac.packetevents.event.PacketListenerPriority;
import xyz.edge.ac.packetevents.event.PacketEvent;
import java.util.concurrent.ConcurrentHashMap;
import xyz.edge.ac.packetevents.event.PacketListenerAbstract;
import java.util.HashSet;
import java.util.Map;

class EventManagerModern
{
    private final Map<Byte, HashSet<PacketListenerAbstract>> listenersMap;
    
    EventManagerModern() {
        this.listenersMap = new ConcurrentHashMap<Byte, HashSet<PacketListenerAbstract>>();
    }
    
    public void callEvent(final PacketEvent event) {
        byte highestReachedPriority = (byte)(PacketListenerPriority.LOWEST.getId() - 1);
        for (byte priority = PacketListenerPriority.LOWEST.getId(); priority <= PacketListenerPriority.MONITOR.getId(); ++priority) {
            final HashSet<PacketListenerAbstract> listeners = this.listenersMap.get(priority);
            if (listeners != null) {
                for (final PacketListenerAbstract listener : listeners) {
                    try {
                        event.call(listener);
                    }
                    catch (final Exception ex) {
                        PacketEvents.get().getPlugin().getLogger().log(Level.SEVERE, "PacketEvents found an exception while calling a packet listener.", ex);
                    }
                    if (event instanceof CancellableEvent && priority > highestReachedPriority) {
                        highestReachedPriority = priority;
                    }
                }
            }
        }
        PEEventManager.EVENT_MANAGER_LEGACY.callEvent(event, highestReachedPriority);
    }
    
    public synchronized void registerListener(final PacketListenerAbstract listener) {
        final byte priority = listener.getPriority().getId();
        HashSet<PacketListenerAbstract> listenerSet = this.listenersMap.get(priority);
        if (listenerSet == null) {
            listenerSet = new HashSet<PacketListenerAbstract>();
        }
        listenerSet.add(listener);
        this.listenersMap.put(priority, listenerSet);
    }
    
    public synchronized void registerListeners(final PacketListenerAbstract... listeners) {
        for (final PacketListenerAbstract listener : listeners) {
            this.registerListener(listener);
        }
    }
    
    public synchronized void unregisterListener(final PacketListenerAbstract listener) {
        final byte priority = listener.getPriority().getId();
        final HashSet<PacketListenerAbstract> listenerSet = this.listenersMap.get(priority);
        if (listenerSet != null) {
            listenerSet.remove(listener);
        }
    }
    
    public synchronized void unregisterListeners(final PacketListenerAbstract... listeners) {
        for (final PacketListenerAbstract listener : listeners) {
            this.unregisterListener(listener);
        }
    }
    
    public synchronized void unregisterAllListeners() {
        this.listenersMap.clear();
    }
}
