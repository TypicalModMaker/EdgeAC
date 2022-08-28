package xyz.edge.ac.packetevents.injector;

import org.bukkit.entity.Player;

public interface EarlyInjector extends ChannelInjector
{
    void updatePlayerObject(final Player p0, final Object p1);
}
