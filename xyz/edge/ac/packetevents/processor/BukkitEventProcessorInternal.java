package xyz.edge.ac.packetevents.processor;

import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntitySpawnEvent;
import java.util.UUID;
import org.bukkit.event.player.PlayerQuitEvent;
import java.net.InetSocketAddress;
import xyz.edge.ac.packetevents.event.PacketEvent;
import xyz.edge.ac.packetevents.event.impl.PostPlayerInjectEvent;
import xyz.edge.ac.packetevents.utils.player.ClientVersion;
import org.bukkit.Bukkit;
import xyz.edge.ac.packetevents.utils.versionlookup.VersionLookupUtils;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import xyz.edge.ac.packetevents.PacketEvents;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.Listener;

public class BukkitEventProcessorInternal implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(final PlayerLoginEvent e) {
        final Player player = e.getPlayer();
        if (!PacketEvents.get().getSettings().shouldUseCompatibilityInjector()) {
            PacketEvents.get().getInjector().injectPlayer(player);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(final PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        final InetSocketAddress address = player.getAddress();
        final boolean shouldInject = PacketEvents.get().getSettings().shouldUseCompatibilityInjector() || !PacketEvents.get().getInjector().hasInjected(e.getPlayer());
        if (shouldInject) {
            PacketEvents.get().getInjector().injectPlayer(player);
        }
        final boolean dependencyAvailable = VersionLookupUtils.isDependencyAvailable();
        PacketEvents.get().getPlayerUtils().loginTime.put(player.getUniqueId(), System.currentTimeMillis());
        if (dependencyAvailable) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(PacketEvents.get().getPlugin(), () -> {
                try {
                    final int protocolVersion = VersionLookupUtils.getProtocolVersion(player);
                    final ClientVersion version = ClientVersion.getClientVersion(protocolVersion);
                    PacketEvents.get().getPlayerUtils().clientVersionsMap.put(address, version);
                }
                catch (final Exception ex) {}
                PacketEvents.get().getEventManager().callEvent(new PostPlayerInjectEvent(player, true));
                return;
            }, 1L);
        }
        else {
            PacketEvents.get().getEventManager().callEvent(new PostPlayerInjectEvent(e.getPlayer(), false));
        }
        PacketEvents.get().getServerUtils().entityCache.putIfAbsent(e.getPlayer().getEntityId(), e.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(final PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        final UUID uuid = player.getUniqueId();
        final InetSocketAddress address = player.getAddress();
        PacketEvents.get().getPlayerUtils().loginTime.remove(uuid);
        PacketEvents.get().getPlayerUtils().playerPingMap.remove(uuid);
        PacketEvents.get().getPlayerUtils().playerSmoothedPingMap.remove(uuid);
        PacketEvents.get().getPlayerUtils().clientVersionsMap.remove(address);
        PacketEvents.get().getPlayerUtils().tempClientVersionMap.remove(address);
        PacketEvents.get().getPlayerUtils().keepAliveMap.remove(uuid);
        PacketEvents.get().getPlayerUtils().channels.remove(player.getName());
        PacketEvents.get().getServerUtils().entityCache.remove(e.getPlayer().getEntityId());
    }
    
    @EventHandler
    public void onEntitySpawn(final EntitySpawnEvent event) {
        final Entity entity = event.getEntity();
        PacketEvents.get().getServerUtils().entityCache.putIfAbsent(entity.getEntityId(), entity);
    }
    
    @EventHandler
    public void onEntityDeath(final EntityDeathEvent event) {
        PacketEvents.get().getServerUtils().entityCache.remove(event.getEntity().getEntityId());
    }
}
