package xyz.edge.ac.util.menu;

import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import java.util.Arrays;
import xyz.edge.ac.util.color.ColorUtil;
import org.bukkit.Material;
import xyz.edge.ac.user.User;
import org.bukkit.inventory.ItemStack;
import xyz.edge.ac.util.utils.PlayerUtil;
import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.entity.Player;
import xyz.edge.ac.Edge;
import xyz.edge.ac.util.XMaterial;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.edge.ac.config.Config;
import java.util.HashMap;
import org.bukkit.inventory.Inventory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Listener;

public class PlayerMenu implements Listener
{
    private final HumanEntity player;
    private final HumanEntity target;
    private final Inventory inventory;
    public HashMap<Integer, Inventory> Checks;
    
    public PlayerMenu(final HumanEntity player, final HumanEntity target) {
        this.Checks = new HashMap<Integer, Inventory>();
        this.player = player;
        this.target = target;
        this.inventory = Bukkit.createInventory(player, 27, ChatColor.translateAlternateColorCodes('&', Config.PREFIX + "&c&lPlayer Viewer"));
        this.update();
    }
    
    public void openGui() {
        this.player.openInventory(this.inventory);
    }
    
    private void update() {
        final ItemStack i = XMaterial.BLUE_STAINED_GLASS_PANE.parseItem();
        MenuUtil.fillBorder(this.inventory, i);
        final User targetUser = Edge.getInstance().getUserManager().getPlayeruser((Player)this.target);
        if (targetUser == null) {
            this.player.sendMessage("null");
            return;
        }
        final String CHECK_MARK = "&a\u2713";
        final String x_MARK = "&c\u2718";
        final String lagging = (PacketEvents.get().getPlayerUtils().getPing((Player)this.target) > 250) ? CHECK_MARK : x_MARK;
        final String geyser = PacketEvents.get().getPlayerUtils().isGeyserPlayer((Player)this.target) ? CHECK_MARK : x_MARK;
        final String ip = ((Player)this.target).getAddress().getAddress().getHostAddress();
        final String hostname = ((Player)this.target).getAddress().getHostName();
        final String ms = String.valueOf(PlayerUtil.getPing((Player)this.target));
        final String enabledChecks = String.valueOf(targetUser.getChecks().size());
        final String totalChecks = String.valueOf(Config.ENABLEDCHECKS.size());
        final String uuid = String.valueOf(this.target.getUniqueId());
        final String clientBrand = targetUser.getClientBrand();
        final String clientVersion = String.valueOf(PacketEvents.get().getPlayerUtils().getClientVersion((Player)this.target));
        final String channels = PacketEvents.get().getPlayerUtils().channels.toString();
        final String smoothedPing = String.valueOf(PacketEvents.get().getPlayerUtils().getSmoothedPing((Player)this.target));
        this.inventory.setItem(11, this.createItem(XMaterial.BLUE_STAINED_GLASS_PANE.parseMaterial(), "", new String[0]));
        this.inventory.setItem(12, this.createItem(XMaterial.BLUE_STAINED_GLASS_PANE.parseMaterial(), "", new String[0]));
        this.inventory.setItem(14, this.createItem(XMaterial.BLUE_STAINED_GLASS_PANE.parseMaterial(), "", new String[0]));
        this.inventory.setItem(15, this.createItem(XMaterial.BLUE_STAINED_GLASS_PANE.parseMaterial(), "", new String[0]));
        this.inventory.setItem(10, this.createItem(XMaterial.REPEATER.parseMaterial(), "&7&m-----------------", "&8\u239f &c&lNetwork Profile", "", "&8\u239f  &bIP: &7" + ip, "&8\u239f  &bHostname: &7" + hostname, "&8\u239f  &bLagging: &7" + lagging, "&8\u239f  &bLatency: &7" + ms + "ms", "&8\u239f  &bSmoothed: &7" + smoothedPing + "ms", "&8\u239f  &bGeyser: &7" + geyser, "", "&7&m-----------------"));
        this.inventory.setItem(13, this.createItem(XMaterial.ENCHANTED_BOOK.parseMaterial(), "&7&m-----------------", "&8\u239f &c&lInformation", "", "&8\u239f  &bNearby Blocks: &7", "", targetUser.getMovementHandler().getBlocks().toString(), "", "&8\u239f  &bEffects: &7", targetUser.getPlayer().getActivePotionEffects().toString(), "", "&8\u239f  &bYaw: &7" + targetUser.getRotationHandler().getYaw(), "&8\u239f  &bPitch: &7" + targetUser.getRotationHandler().getPitch(), "&8\u239f  &bDeltaXZ: &7" + targetUser.getMovementHandler().getDeltaXZ(), "&8\u239f  &bDeltaY: &7" + targetUser.getMovementHandler().getDeltaY(), "", "&7&m-----------------"));
        this.inventory.setItem(16, this.createItem(XMaterial.BOOK.parseMaterial(), "&7&m-----------------", "&8\u239f &c&lPlayer Profile", "", "&8\u239f  &bUUID: &7" + uuid, "&8\u239f  &bUsername: &7" + targetUser.getPlayer().getName(), "&8\u239f  &bDisplayName: &7" + targetUser.getPlayer().getDisplayName(), "&8\u239f  &bClientBrand: &7" + clientBrand, "&8\u239f  &bVersion: &7" + clientVersion, "&8\u239f  &bCord X: &7" + targetUser.getPlayer().getLocation().getX(), "&8\u239f  &bCord Y: &7" + targetUser.getPlayer().getLocation().getY(), "&8\u239f  &bCord Z: &7" + targetUser.getPlayer().getLocation().getZ(), "", "&7&m-----------------"));
        this.inventory.setItem(26, this.createItem(XMaterial.REDSTONE.parseMaterial(), "&cExit", new String[0]));
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
