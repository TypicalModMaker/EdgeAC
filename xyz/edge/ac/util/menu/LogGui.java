package xyz.edge.ac.util.menu;

import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.SkullType;
import xyz.edge.ac.util.XMaterial;
import java.util.Map;
import org.bukkit.OfflinePlayer;
import java.util.HashMap;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.entity.HumanEntity;

public class LogGui
{
    private final HumanEntity player;
    private final Inventory inventory;
    
    public LogGui(final HumanEntity player) {
        this.player = player;
        this.inventory = Bukkit.createInventory(player, 27, "AC Logs");
    }
    
    public void openGui() {
        this.player.openInventory(this.inventory);
    }
    
    public void update() {
        final Map<String, OfflinePlayer> log = new HashMap<String, OfflinePlayer>();
    }
    
    protected void addSkullByName(final String name, final String... lore) {
        assert XMaterial.SKELETON_SKULL.parseMaterial() != null;
        final ItemStack skull = new ItemStack(XMaterial.SKELETON_SKULL.parseMaterial(), 1, (short)SkullType.PLAYER.ordinal());
        final SkullMeta meta = (SkullMeta)skull.getItemMeta();
        meta.setOwner(name);
        meta.setDisplayName(ChatColor.AQUA + name);
        meta.setLore(Arrays.asList(lore));
        skull.setItemMeta(meta);
        this.inventory.addItem(new ItemStack[] { skull });
    }
}
