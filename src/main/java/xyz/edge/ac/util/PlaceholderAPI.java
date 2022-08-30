package xyz.edge.ac.util;

import xyz.edge.ac.user.User;
import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;
import xyz.edge.ac.Edge;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlaceholderAPI extends PlaceholderExpansion
{
    private final Edge plugin;
    
    public PlaceholderAPI(final Edge plugin) {
        this.plugin = plugin;
    }
    
    public String getAuthor() {
        return "RealTrippy";
    }
    
    public String getIdentifier() {
        return "edge";
    }
    
    public String getVersion() {
        return "1.0";
    }
    
    public boolean persist() {
        return true;
    }
    
    public String onRequest(final OfflinePlayer p, final String params) {
        final Player player = (Player)p;
        if (p == null) {
            return "";
        }
        final User user = Edge.getInstance().getUserManager().getPlayeruser(player);
        if (params.equalsIgnoreCase("tps")) {
            return TPSUtil.getFormattedTPS();
        }
        if (params.equalsIgnoreCase("totalviolations")) {
            return String.valueOf(user.getTotalViolations());
        }
        if (params.equalsIgnoreCase("clientbrand")) {
            return String.valueOf(user.getClientBrand());
        }
        if (params.equalsIgnoreCase("version")) {
            return String.valueOf(user.getVersion().getProtocolVersion());
        }
        return null;
    }
}
