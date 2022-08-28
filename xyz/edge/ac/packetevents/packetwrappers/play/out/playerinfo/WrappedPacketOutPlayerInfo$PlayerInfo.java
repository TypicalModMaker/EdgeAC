package xyz.edge.ac.packetevents.packetwrappers.play.out.playerinfo;

import org.jetbrains.annotations.Nullable;
import xyz.edge.ac.packetevents.utils.player.GameMode;
import xyz.edge.ac.packetevents.utils.gameprofile.WrappedGameProfile;

public static class PlayerInfo
{
    private String username;
    private WrappedGameProfile gameProfile;
    private GameMode gameMode;
    private int ping;
    
    public PlayerInfo(@Nullable final String username, final WrappedGameProfile gameProfile, final GameMode gameMode, final int ping) {
        this.username = username;
        this.gameProfile = gameProfile;
        this.gameMode = gameMode;
        this.ping = ping;
    }
    
    public PlayerInfo(@Nullable final String username, final WrappedGameProfile gameProfile, final org.bukkit.GameMode gameMode, final int ping) {
        this.username = username;
        this.gameProfile = gameProfile;
        this.gameMode = GameMode.valueOf(gameMode.name());
        this.ping = ping;
    }
    
    public WrappedGameProfile getGameProfile() {
        return this.gameProfile;
    }
    
    public void setGameProfile(final WrappedGameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }
    
    public GameMode getGameMode() {
        return this.gameMode;
    }
    
    public void setGameMode(final GameMode gameMode) {
        this.gameMode = gameMode;
    }
    
    public int getPing() {
        return this.ping;
    }
    
    public void setPing(final int ping) {
        this.ping = ping;
    }
    
    @Nullable
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(@Nullable final String username) {
        this.username = username;
    }
}
