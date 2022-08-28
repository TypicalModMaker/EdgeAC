package xyz.edge.api.listener;

import org.bukkit.event.HandlerList;
import xyz.edge.api.check.APICheck;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class EdgeSendAlert extends Event implements Cancellable
{
    private boolean cancelled;
    private final Player player;
    private final TextComponent message;
    private final APICheck check;
    private final String info;
    private static final HandlerList handlers;
    
    public EdgeSendAlert(final TextComponent message, final Player player, final APICheck check, final String info) {
        super(true);
        this.player = player;
        this.message = message;
        this.check = check;
        this.info = info;
        this.cancelled = false;
    }
    
    public HandlerList getHandlers() {
        return EdgeSendAlert.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return EdgeSendAlert.handlers;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public TextComponent getMessage() {
        return this.message;
    }
    
    public APICheck getCheck() {
        return this.check;
    }
    
    public String getInfo() {
        return this.info;
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
