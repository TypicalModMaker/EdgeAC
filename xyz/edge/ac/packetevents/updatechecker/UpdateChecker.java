package xyz.edge.ac.packetevents.updatechecker;

import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import xyz.edge.ac.packetevents.utils.version.PEVersion;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.PacketEvents;

public class UpdateChecker
{
    private final LowLevelUpdateChecker lowLevelUpdateChecker;
    
    public UpdateChecker() {
        if (PacketEvents.get().getServerUtils().getVersion().isOlderThan(ServerVersion.v_1_8)) {
            this.lowLevelUpdateChecker = new LowLevelUpdateCheckerLegacy();
        }
        else {
            this.lowLevelUpdateChecker = new LowLevelUpdateCheckerModern();
        }
    }
    
    public String checkLatestReleasedVersion() {
        return this.lowLevelUpdateChecker.getLatestRelease();
    }
    
    public UpdateCheckerStatus checkForUpdate() {
        final PEVersion localVersion = PacketEvents.get().getVersion();
        PEVersion newVersion;
        try {
            newVersion = new PEVersion(this.checkLatestReleasedVersion());
        }
        catch (final Exception ex) {
            newVersion = null;
        }
        if (newVersion != null && localVersion.isOlderThan(newVersion)) {
            this.inform("There is an update available for the PacketEvents API! Your build: (" + localVersion.toString() + ") | Latest released build: (" + newVersion.toString() + ")");
            return UpdateCheckerStatus.OUTDATED;
        }
        if (newVersion != null && localVersion.isNewerThan(newVersion)) {
            this.inform("You are on a dev or pre released build of PacketEvents. Your build: (" + localVersion.toString() + ") | Latest released build: (" + newVersion.toString() + ")");
            return UpdateCheckerStatus.PRE_RELEASE;
        }
        if (localVersion.equals(newVersion)) {
            this.inform("You are on the latest released version of PacketEvents. (" + newVersion.toString() + ")");
            return UpdateCheckerStatus.UP_TO_DATE;
        }
        this.report("Something went wrong while checking for an update. Your build: (" + localVersion.toString() + ") | Latest released build: (" + newVersion.toString() + ")");
        return UpdateCheckerStatus.FAILED;
    }
    
    private void inform(final String message) {
        Bukkit.getLogger().info("[packetevents] " + message);
    }
    
    private void report(final String message) {
        Bukkit.getLogger().warning(ChatColor.DARK_RED + "[packetevents] " + message);
    }
    
    public enum UpdateCheckerStatus
    {
        OUTDATED, 
        PRE_RELEASE, 
        UP_TO_DATE, 
        FAILED;
    }
}
