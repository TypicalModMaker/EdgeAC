package xyz.edge.ac.packetevents.utils.versionlookup.viaversion;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

public class ViaVersionLookupUtils
{
    private static ViaVersionAccessor viaVersionAccessor;
    
    private ViaVersionLookupUtils() {
    }
    
    public static boolean isAvailable() {
        return Bukkit.getPluginManager().getPlugin("ViaVersion") != null;
    }
    
    public static int getProtocolVersion(final Player player) {
        if (ViaVersionLookupUtils.viaVersionAccessor == null) {
            try {
                Class.forName("com.viaversion.viaversion.api.Via");
                ViaVersionLookupUtils.viaVersionAccessor = new ViaVersionAccessorImpl();
            }
            catch (final Exception e) {
                ViaVersionLookupUtils.viaVersionAccessor = new ViaVersionAccessorImplLegacy();
            }
        }
        return ViaVersionLookupUtils.viaVersionAccessor.getProtocolVersion(player);
    }
}
