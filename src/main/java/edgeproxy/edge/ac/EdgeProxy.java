package edgeproxy.edge.ac;

import edgeproxy.edge.ac.utils.ProxyColor;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.ProxyServer;
import edgeproxy.edge.ac.listener.ConnectionListener;
import net.md_5.bungee.api.plugin.Listener;
import edgeproxy.edge.ac.listener.PluginMessageListener;
import edgeac.shared.ac.StaffManager;
import net.md_5.bungee.api.plugin.Plugin;

public class EdgeProxy extends Plugin
{
    public void onEnable() {
        this.sendLoad();
        final ProxyServer server = this.getProxy();
        final PluginManager pluginManager = server.getPluginManager();
        final StaffManager staffManager = new StaffManager();
        server.registerChannel("edgeac:channel");
        pluginManager.registerListener(this, new PluginMessageListener(staffManager));
        pluginManager.registerListener(this, new ConnectionListener(staffManager));
    }
    
    public void sendLoad() {
        final ProxyServer proxyServer = this.getProxy();
        ProxyColor.NONE.out("");
        ProxyColor.NONE.out("&7&m-------------------------------------------------------------");
        ProxyColor.NONE.out("&b  ______    _              _____                     ");
        ProxyColor.NONE.out("&b |  ____|  | |            |  __ \\                    ");
        ProxyColor.NONE.out("&b | |__   __| | __ _  ___  | |__) | __ _____  ___   _");
        ProxyColor.NONE.out("&b |  __| / _` |/ _` |/ _ \\ |  ___/ '__/ _ \\ \\/ / | | |");
        ProxyColor.NONE.out("&b | |___| (_| | (_| |  __/ | |   | | | (_) >  <| |_| |");
        ProxyColor.NONE.out("&b |______\\__,_|\\__, |\\___| |_|   |_|  \\___/_/\\_\\\\__, |");
        ProxyColor.NONE.out("&b               __/ |                            __/ |");
        ProxyColor.NONE.out("&b              |___/                            |___/ ");
        ProxyColor.NONE.out("");
        ProxyColor.NONE.out("&7| &cYou are currently running latest version of &bEdgeProxy");
        ProxyColor.NONE.out("&7| &bIf you need any help don't hesitate to contact us at &7discord.edgeac.xyz");
        ProxyColor.NONE.out("&7&m-------------------------------------------------------------");
        ProxyColor.NONE.out("");
    }
}
