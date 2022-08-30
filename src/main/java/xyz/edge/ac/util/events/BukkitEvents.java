package xyz.edge.ac.util.events;

import xyz.edge.ac.util.menu.EdgeMenu;
import xyz.edge.ac.util.color.ColorUtil;
import xyz.edge.ac.util.menu.ToggleGui;
import org.bukkit.ChatColor;
import xyz.edge.ac.config.Config;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import java.util.Iterator;
import xyz.edge.ac.packetutils.out.PushOutPacketToPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;
import xyz.edge.ac.Edge;
import xyz.edge.ac.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public final class BukkitEvents implements Listener
{
    public User getUser(final Player p) {
        final User user = Edge.getInstance().getUserManager().getPlayeruser(p);
        return user;
    }
    
    @EventHandler
    public void onPing(final ServerListPingEvent event) {
    }
    
    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        final User user = this.getUser(event.getPlayer());
        if (user != null) {
            user.getPacketActionHandler().handleBukkitBlockBreak();
        }
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final User user = this.getUser(event.getPlayer());
        if (user != null) {
            user.getPacketActionHandler().handleInteract(event);
        }
    }
    
    @EventHandler
    public void onChat(final AsyncPlayerChatEvent event) {
        if (event.getMessage().equalsIgnoreCase("massdemo") && event.getPlayer().hasPermission("edge.massdemo")) {
            event.setCancelled(true);
            for (final Player player : Bukkit.getOnlinePlayers()) {
                PushOutPacketToPlayer.sendOutPositionDemoScreen(player);
            }
        }
    }
    
    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        final User user = this.getUser(event.getPlayer());
        if (user != null) {
            user.getPacketActionHandler().handleBukkitPlace();
        }
    }
    
    @EventHandler
    public void onInventory(final InventoryClickEvent event) {
        final Player player = (Player)event.getWhoClicked();
        if (event.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Config.PREFIX + "&c&lPlayer Viewer"))) {
            event.setCancelled(true);
            if (event.getSlot() == 26) {
                player.closeInventory();
            }
        }
        if (event.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Config.PREFIX + "&c&lCheck Toggle"))) {
            final User data = Edge.getInstance().getUserManager().getPlayeruser((Player)event.getWhoClicked());
            event.setCancelled(true);
            if (event.getSlot() == 30) {
                if (data.getPageIndex() < 1) {
                    event.getWhoClicked().closeInventory();
                    final ToggleGui toggleGui = new ToggleGui(event.getWhoClicked());
                    toggleGui.openGui();
                }
                else {
                    event.getWhoClicked().closeInventory();
                    data.setPageIndex(data.getPageIndex() - 1);
                    final ToggleGui toggleGui = new ToggleGui(event.getWhoClicked());
                    toggleGui.openGui();
                }
                return;
            }
            if (event.getCurrentItem() != null && event.getSlot() == 32) {
                event.getWhoClicked().closeInventory();
                data.getPlayer().sendMessage(ColorUtil.translate(Config.PREFIX + "Toggled page!"));
                data.setPageIndex(data.getPageIndex() + 1);
                final ToggleGui toggleGui = new ToggleGui(event.getWhoClicked());
                toggleGui.openGui();
                return;
            }
            if (event.getCurrentItem() != null) {
                if (event.isLeftClick() && !event.isShiftClick()) {
                    data.getPlayer().closeInventory();
                    final ToggleGui toggleGui = new ToggleGui(event.getWhoClicked());
                    toggleGui.toggleCheck(event.getSlot());
                }
                else if (event.isRightClick() && !event.isShiftClick()) {
                    data.getPlayer().closeInventory();
                    final ToggleGui toggleGui = new ToggleGui(event.getWhoClicked());
                    toggleGui.toggleBan(event.getSlot());
                }
                else if (event.isShiftClick()) {
                    data.getPlayer().closeInventory();
                    final ToggleGui toggleGui = new ToggleGui(event.getWhoClicked());
                    toggleGui.toggleSetback(event.getSlot());
                }
            }
            if (event.getSlot() == 26) {
                event.getWhoClicked().closeInventory();
                final EdgeMenu edgeMenu = new EdgeMenu(event.getWhoClicked());
                edgeMenu.openGui();
            }
        }
        if (event.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Config.PREFIX + "&c&lMain Menu"))) {
            event.setCancelled(true);
            if (event.getSlot() == 10) {
                event.getWhoClicked().closeInventory();
                final ToggleGui toggleGui2 = new ToggleGui(event.getWhoClicked());
                toggleGui2.openGui();
            }
            else if (event.getSlot() == 16) {
                player.performCommand("edge reboot");
            }
            else if (event.getSlot() == 26) {
                event.getWhoClicked().closeInventory();
            }
        }
    }
}
