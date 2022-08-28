package xyz.edge.ac.util.menu;

import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import java.util.Arrays;
import xyz.edge.ac.util.color.ColorUtil;
import org.bukkit.Material;
import xyz.edge.ac.user.User;
import org.bukkit.inventory.ItemStack;
import xyz.edge.ac.util.TimeUtil;
import java.util.Date;
import xyz.edge.ac.Edge;
import xyz.edge.ac.packetevents.PacketEvents;
import org.bukkit.entity.Player;
import xyz.edge.ac.user.UserManager;
import xyz.edge.ac.util.XMaterial;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.edge.ac.config.Config;
import org.bukkit.inventory.Inventory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Listener;

public class DevMenu implements Listener
{
    private final HumanEntity player;
    private final Inventory inventory;
    
    public DevMenu(final HumanEntity player) {
        this.player = player;
        this.inventory = Bukkit.createInventory(player, 27, ChatColor.translateAlternateColorCodes('&', Config.PREFIX + "&c&lMain Menu"));
        this.update();
    }
    
    public void openGui() {
        this.player.openInventory(this.inventory);
    }
    
    private void update() {
        final ItemStack i = XMaterial.BLUE_STAINED_GLASS_PANE.parseItem();
        MenuUtil.fillBorder(this.inventory, i);
        final User user = UserManager.getInstance().getPlayeruser((Player)this.player);
        final String CHECK_MARK = "&a\u2713";
        final String x_MARK = "&c\u2718";
        final String bungee = PacketEvents.get().getServerUtils().isBungeeCordEnabled() ? CHECK_MARK : x_MARK;
        final String geyser = PacketEvents.get().getServerUtils().isGeyserAvailable() ? CHECK_MARK : x_MARK;
        final String lagging = (PacketEvents.get().getServerUtils().getTPS() < 19.3) ? CHECK_MARK : x_MARK;
        final String licenceType = Edge.getInstance().getLicenseType().name();
        final String expireDate = (Edge.getInstance().getExpireDate() == -1L) ? "&cNever" : (TimeUtil.dateToString(new Date(Edge.getInstance().getExpireDate() * 1000L)) + " &7(" + TimeUtil.formatDurationShort(Edge.getInstance().getExpireDate() + System.currentTimeMillis()) + ")");
        this.inventory.setItem(11, this.createItem(XMaterial.BLUE_STAINED_GLASS_PANE.parseMaterial(), "", new String[0]));
        this.inventory.setItem(12, this.createItem(XMaterial.BLUE_STAINED_GLASS_PANE.parseMaterial(), "", new String[0]));
        this.inventory.setItem(14, this.createItem(XMaterial.BLUE_STAINED_GLASS_PANE.parseMaterial(), "", new String[0]));
        this.inventory.setItem(15, this.createItem(XMaterial.BLUE_STAINED_GLASS_PANE.parseMaterial(), "", new String[0]));
        this.inventory.setItem(10, this.createItem(XMaterial.ENCHANTED_BOOK.parseMaterial(), "&b&lDetections Menu", "", "&cView, check & edit all checks on", "&c" + Edge.getInstance().getDescription().getName() + " anticheat to your liking", "", "&b&lRight Click To Open"));
        this.inventory.setItem(13, this.createItem(XMaterial.ENDER_PEARL.parseMaterial(), Config.PREFIX + "&7(&c" + Edge.getInstance().getDescription().getVersion() + "&7)", "", "&7» &bLicence Owner&7: " + Edge.getInstance().getLicenceUsername(), "&7» &bLicence Type&7: " + licenceType.toLowerCase(), "&7» &bExpires&7: " + expireDate, "&7» &bLicence Email&7: " + Edge.getInstance().getLicenceUsername(), "", "&7» &bDisguised&7: " + !Edge.getInstance().getDescription().getName().equals("Edge"), "&7» &bName: &7" + Edge.getInstance().getDescription().getName(), "&7» &BVersion: &7" + Edge.getInstance().getDescription().getVersion(), "&7» &bDescription: &7" + Edge.getInstance().getDescription().getDescription(), "&7» &bWebsite: &7" + Edge.getInstance().getDescription().getWebsite(), ""));
        this.inventory.setItem(16, this.createItem(XMaterial.REPEATER.parseMaterial(), "&b&lReboot " + Edge.getInstance().getDescription().getName(), "", "&cReboot all of " + Edge.getInstance().getDescription().getName() + "'s", "&cchecks, data, values & config files", "", "&b&lRight Click To Reboot"));
    }
    
    protected ItemStack createItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ColorUtil.translate(name));
        meta.setLore(ColorUtil.translate(Arrays.asList(lore)));
        item.setItemMeta(meta);
        return item;
    }
}
