package xyz.edge.ac.packetevents.event.manager;

public class PEEventManager implements EventManager
{
    public static final EventManagerLegacy EVENT_MANAGER_LEGACY;
    public static final EventManagerModern EVENT_MANAGER_MODERN;
    
    static {
        EVENT_MANAGER_LEGACY = new EventManagerLegacy();
        EVENT_MANAGER_MODERN = new EventManagerModern();
    }
}
