package xyz.edge.ac.packetevents.settings;

import xyz.edge.ac.packetevents.utils.server.ServerVersion;

public class PacketEventsSettings
{
    private boolean locked;
    private ServerVersion fallbackServerVersion;
    private boolean checkForUpdates;
    private boolean compatInjector;
    private boolean bStatsEnabled;
    
    public PacketEventsSettings() {
        this.fallbackServerVersion = ServerVersion.v_1_7_10;
        this.checkForUpdates = true;
        this.compatInjector = false;
        this.bStatsEnabled = true;
    }
    
    public PacketEventsSettings lock() {
        this.locked = true;
        return this;
    }
    
    @Deprecated
    public PacketEventsSettings backupServerVersion(final ServerVersion serverVersion) {
        if (!this.locked) {
            this.fallbackServerVersion = serverVersion;
        }
        return this;
    }
    
    public PacketEventsSettings fallbackServerVersion(final ServerVersion version) {
        if (!this.locked) {
            this.fallbackServerVersion = version;
        }
        return this;
    }
    
    public PacketEventsSettings checkForUpdates(final boolean checkForUpdates) {
        if (!this.locked) {
            this.checkForUpdates = checkForUpdates;
        }
        return this;
    }
    
    public PacketEventsSettings bStats(final boolean bStatsEnabled) {
        if (!this.locked) {
            this.bStatsEnabled = bStatsEnabled;
        }
        return this;
    }
    
    @Deprecated
    public PacketEventsSettings compatInjector(final boolean compatInjector) {
        if (!this.locked) {
            this.compatInjector = compatInjector;
        }
        return this;
    }
    
    public boolean isLocked() {
        return this.locked;
    }
    
    @Deprecated
    public ServerVersion getBackupServerVersion() {
        return this.fallbackServerVersion;
    }
    
    public ServerVersion getFallbackServerVersion() {
        return this.fallbackServerVersion;
    }
    
    public boolean shouldCheckForUpdates() {
        return this.checkForUpdates;
    }
    
    @Deprecated
    public boolean shouldUseCompatibilityInjector() {
        return this.compatInjector;
    }
    
    public boolean isbStatsEnabled() {
        return this.bStatsEnabled;
    }
}
