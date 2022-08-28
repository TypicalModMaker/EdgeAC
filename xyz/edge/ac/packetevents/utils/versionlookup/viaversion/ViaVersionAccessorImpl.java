package xyz.edge.ac.packetevents.utils.versionlookup.viaversion;

import com.viaversion.viaversion.api.Via;
import org.bukkit.entity.Player;

public class ViaVersionAccessorImpl implements ViaVersionAccessor
{
    @Override
    public int getProtocolVersion(final Player player) {
        return Via.getAPI().getPlayerVersion(player);
    }
}
