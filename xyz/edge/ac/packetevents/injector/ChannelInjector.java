package xyz.edge.ac.packetevents.injector;

import org.bukkit.entity.Player;

public interface ChannelInjector
{
    default boolean isBound() {
        return true;
    }
    
    void inject();
    
    void eject();
    
    void injectPlayer(final Player p0);
    
    void ejectPlayer(final Player p0);
    
    boolean hasInjected(final Player p0);
    
    void writePacket(final Object p0, final Object p1);
    
    void flushPackets(final Object p0);
    
    void sendPacket(final Object p0, final Object p1);
}
