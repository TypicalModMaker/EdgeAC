package xyz.edge.ac.packetevents;

import xyz.edge.ac.packetevents.settings.PacketEventsSettings;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketEventsPlugin extends JavaPlugin
{
    public void onLoad() {
        final PacketEventsSettings settings = PacketEvents.create(this).getSettings();
        settings.fallbackServerVersion(ServerVersion.getLatest()).checkForUpdates(true).bStats(true);
        PacketEvents.get().loadAsyncNewThread();
    }
    
    public void onEnable() {
        PacketEvents.get().init();
    }
    
    public void onDisable() {
        PacketEvents.get().terminate();
    }
}
