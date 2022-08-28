package xyz.edge.ac.packetevents.event.manager;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.lang.reflect.InvocationTargetException;
import xyz.edge.ac.packetevents.event.annotation.PacketHandler;
import xyz.edge.ac.packetevents.event.eventtypes.CancellableEvent;
import xyz.edge.ac.packetevents.event.PacketEvent;
import java.util.HashMap;
import java.lang.reflect.Method;
import java.util.HashSet;
import xyz.edge.ac.packetevents.event.PacketListener;
import java.util.Map;

@Deprecated
class EventManagerLegacy
{
    private final Map<PacketListener, HashSet<Method>> staticRegisteredMethods;
    
    EventManagerLegacy() {
        this.staticRegisteredMethods = new HashMap<PacketListener, HashSet<Method>>();
    }
    
    @Deprecated
    public void callEvent(final PacketEvent event, byte eventPriority) {
        boolean isCancelled = false;
        if (event instanceof CancellableEvent) {
            isCancelled = ((CancellableEvent)event).isCancelled();
        }
        for (final PacketListener listener : this.staticRegisteredMethods.keySet()) {
            final HashSet<Method> methods = this.staticRegisteredMethods.get(listener);
            for (final Method method : methods) {
                final Class<?> parameterType = method.getParameterTypes()[0];
                if (parameterType.equals(PacketEvent.class) || parameterType.isInstance(event)) {
                    final PacketHandler annotation = method.getAnnotation(PacketHandler.class);
                    try {
                        method.invoke(listener, event);
                    }
                    catch (final IllegalAccessException | InvocationTargetException ex) {
                        ex.printStackTrace();
                    }
                    if (!(event instanceof CancellableEvent)) {
                        continue;
                    }
                    final CancellableEvent ce = (CancellableEvent)event;
                    if (annotation.priority() < eventPriority) {
                        continue;
                    }
                    eventPriority = annotation.priority();
                    isCancelled = ce.isCancelled();
                }
            }
        }
        if (event instanceof CancellableEvent) {
            final CancellableEvent ce2 = (CancellableEvent)event;
            ce2.setCancelled(isCancelled);
        }
    }
    
    @Deprecated
    public void registerListener(final PacketListener listener) {
        final HashSet<Method> methods = new HashSet<Method>();
        for (final Method m : listener.getClass().getDeclaredMethods()) {
            if (!m.isAccessible()) {
                m.setAccessible(true);
            }
            if (m.isAnnotationPresent(PacketHandler.class) && m.getParameterTypes().length == 1) {
                methods.add(m);
            }
        }
        if (!methods.isEmpty()) {
            this.staticRegisteredMethods.put(listener, methods);
        }
    }
    
    @Deprecated
    public void registerListeners(final PacketListener... listeners) {
        for (final PacketListener listener : listeners) {
            this.registerListener(listener);
        }
    }
    
    @Deprecated
    public void unregisterListener(final PacketListener listener) {
        this.staticRegisteredMethods.remove(listener);
    }
    
    @Deprecated
    public void unregisterListeners(final PacketListener... listeners) {
        for (final PacketListener listener : listeners) {
            this.unregisterListener(listener);
        }
    }
    
    @Deprecated
    public void unregisterAllListeners() {
        this.staticRegisteredMethods.clear();
    }
}
