package xyz.edge.ac.packetevents.utils.versionlookup.protocolsupport;

import protocolsupport.api.ProtocolSupportAPI;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

public class ProtocolSupportVersionLookupUtils
{
    public static boolean isAvailable() {
        return Bukkit.getPluginManager().getPlugin("ProtocolSupport") != null;
    }
    
    public static int getProtocolVersion(final Player player) {
        return ProtocolSupportAPI.getProtocolVersion(player).getId();
    }
}
