package xyz.edge.ac.util.menu;

import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import java.util.Arrays;
import xyz.edge.ac.util.color.ColorUtil;
import org.bukkit.Material;
import xyz.edge.ac.user.User;
import org.bukkit.inventory.ItemStack;
import xyz.edge.ac.util.TPSUtil;
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

public class EdgeMenu implements Listener
{
    private final HumanEntity player;
    private final Inventory inventory;
    
    public EdgeMenu(final HumanEntity player) {
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
        final String lagging = (PacketEvents.get().getServerUtils().getTPS() < 19.5) ? CHECK_MARK : x_MARK;
        final String licenceType = Edge.getInstance().getLicenseType().name();
        this.inventory.setItem(11, this.createItem(XMaterial.BLUE_STAINED_GLASS_PANE.parseMaterial(), "", new String[0]));
        this.inventory.setItem(12, this.createItem(XMaterial.BLUE_STAINED_GLASS_PANE.parseMaterial(), "", new String[0]));
        this.inventory.setItem(14, this.createItem(XMaterial.BLUE_STAINED_GLASS_PANE.parseMaterial(), "", new String[0]));
        this.inventory.setItem(15, this.createItem(XMaterial.BLUE_STAINED_GLASS_PANE.parseMaterial(), "", new String[0]));
        this.inventory.setItem(10, this.createItem(XMaterial.ENCHANTED_BOOK.parseMaterial(), "&7&m-----------------", "&8\u239f &c&lChecks Menu", "", "&8\u239f &c&lChecks", "&8\u239f  &bEnabled: &7" + Config.ENABLEDCHECKS.size(), "&8\u239f  &bSetBack: &7" + Config.SETBACKCHECKS.size(), "", "&8\u239f &7Open menu for Edge's checks", "", "&8\u239f &cLeft Click", "&7&m-----------------"));
        this.inventory.setItem(13, this.createItem(XMaterial.ENDER_PEARL.parseMaterial(), "&7&m-----------------", "&8\u239f &b&lEdge AntiCheat &7(&cv" + Edge.getInstance().getDescription().getVersion() + "&7)", "", "&8\u239f &c&lAntiCheat", "&8\u239f  &bLicence Owner: &7" + Edge.getInstance().getLicenceUsername(), "&8\u239f  &bLicence Email: &7" + Edge.getInstance().getLicenceEmail(), "&8\u239f  &bLicense Type: &7" + licenceType.substring(0, 1).toUpperCase() + licenceType.substring(1).toLowerCase(), "&8\u239f  &bLoader Ver: &71.3", "&8\u239f  &bPacket API: &7PACKETEVENTS (Retrooper), NetMinecraftService", "&8\u239f  &bAPI Version: &7" + PacketEvents.get().getVersion(), "&8\u239f  &bIs Newer: &7" + PacketEvents.get().getVersion().isNewerThan(PacketEvents.get().getVersion()), "", "&8\u239f  &bPunish: &7" + Config.BANNINGCHECKS.size(), "", "&8\u239f &c&lServer", "&8\u239f  &bLagging: " + lagging, "", "&8\u239f  &bTPS: " + TPSUtil.getTpsBar() + " &7(&c" + TPSUtil.getFormattedTPS() + "&7)", "&8\u239f  &bThreads: &7" + Runtime.getRuntime().availableProcessors(), "&8\u239f  &bMemory: &7" + Runtime.getRuntime().freeMemory() / 1048576L + " &8/ &7" + Runtime.getRuntime().maxMemory() / 1048576L + " &bMB", "&7&m-----------------"));
        this.inventory.setItem(16, this.createItem(XMaterial.REPEATER.parseMaterial(), "&7&m-----------------", "&8\u239f &c&lReboot Edge", "", "&8\u239f &7Reboot the whole of Edge", "", "&8\u239f &cLeft Click", "&7&m-----------------"));
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
