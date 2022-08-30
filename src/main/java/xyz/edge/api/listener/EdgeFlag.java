package xyz.edge.api.listener;

import org.bukkit.event.HandlerList;
import xyz.edge.api.check.APICheck;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public final class EdgeFlag extends Event implements Cancellable
{
    private boolean cancelled;
    private final Player player;
    private final APICheck check;
    private static final HandlerList handlers;
    
    public EdgeFlag(final Player player, final APICheck check) {
        super(true);
        this.player = player;
        this.check = check;
        this.cancelled = false;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public APICheck getCheck() {
        return this.check;
    }
    
    public HandlerList getHandlers() {
        return EdgeFlag.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return EdgeFlag.handlers;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean b) {
        this.cancelled = b;
    }
    
    static {
        handlers = new HandlerList();
    }
}
