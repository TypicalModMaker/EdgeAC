package edgeproxy.edge.ac.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ProxyServer;

public enum ProxyColor
{
    NONE('r'), 
    ERROR('c'), 
    INFO('b');
    
    char color;
    
    private ProxyColor(final char color) {
        this.color = color;
    }
    
    public void out(String message) {
        message = Color.translate(String.format("&%c%s", this.color, message));
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent(message));
    }
}
