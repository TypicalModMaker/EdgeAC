package xyz.edge.ac.util.menu;

import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.edge.ac.util.type.Pair;
import java.io.IOException;
import java.io.File;
import java.util.Iterator;
import xyz.edge.ac.user.User;
import xyz.edge.ac.util.XMaterial;
import xyz.edge.ac.util.color.ColorUtil;
import xyz.edge.ac.checks.EdgeCheck;
import java.util.List;
import com.google.common.collect.Lists;
import org.bukkit.entity.Player;
import xyz.edge.ac.Edge;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.edge.ac.config.Config;
import org.bukkit.inventory.Inventory;
import org.bukkit.entity.HumanEntity;

public class ToggleGui
{
    private final HumanEntity player;
    private final Inventory inventory;
    
    public ToggleGui(final HumanEntity player) {
        this.player = player;
        this.inventory = Bukkit.createInventory(player, 36, ChatColor.translateAlternateColorCodes('&', Config.PREFIX + "&c&lCheck Toggle"));
        this.update();
    }
    
    public void update() {
        final User user = Edge.getInstance().getUserManager().getPlayeruser((Player)this.player);
        final List<List<EdgeCheck>> evenList = Lists.partition(user.getChecks(), 27);
        int i = 0;
        for (final EdgeCheck check : evenList.get(user.getPageIndex())) {
            final String CHECK_MARK = "&a\u2713";
            final String x_MARK = "&c\u2718";
            final String unicode = check.isEnabled() ? CHECK_MARK : x_MARK;
            final String punish = check.isBan() ? CHECK_MARK : x_MARK;
            final String drag = check.isSetback() ? CHECK_MARK : x_MARK;
            final String dev = check.getCheckInfo().experimental() ? ColorUtil.translate(" " + Config.DEVTHING) : "";
            this.inventory.setItem(i, this.createItem(XMaterial.ENCHANTED_BOOK.parseMaterial(), "&7&m-----------------", "&8 &c&l" + check.getJustTheName() + "&7(&c" + check.getType() + "&7)" + dev, "", "&8\u239f &cEnabled: &7" + unicode, "&8\u239f &cPunish: &7" + punish, "&8\u239f &cDragDown: &7" + drag, "", "&8\u239f &cLeft click to toggle state", "&8\u239f &cRight click to toggle punish", "&8\u239f &cShift click to toggle dragdown", "&7&m-----------------"));
            ++i;
        }
        this.inventory.setItem(30, this.createItem(XMaterial.REDSTONE.parseMaterial(), "&bBack", new String[0]));
        if (user.getPageIndex() <= evenList.size() - 2) {
            this.inventory.setItem(32, this.createItem(XMaterial.GLOWSTONE_DUST.parseMaterial(), "&bNext", new String[0]));
        }
    }
    
    public void toggleCheck(final int slot) {
        final User user = Edge.getInstance().getUserManager().getPlayeruser((Player)this.player);
        final List<List<EdgeCheck>> evenList = Lists.partition(user.getChecks(), 27);
        final List<EdgeCheck> checks = evenList.get(user.getPageIndex());
        final EdgeCheck check = checks.get(slot);
        if (check != null) {
            new Thread(() -> {
                final File file = new File(Edge.getInstance().getDataFolder(), "checks.yml");
                Config.checkConfig.set("check." + check.getCheckInfo().name().split("\\(")[0].replace(" ", "") + check.getCheckInfo().type() + ".state", !check.isEnabled());
                try {
                    Config.checkConfig.save(file);
                }
                catch (final IOException ex) {}
                Config.PUNISH_COMMANDS.clear();
                Config.ENABLEDCHECKS.clear();
                Config.BANNINGCHECKS.clear();
                Config.SETBACKCHECKS.clear();
                Edge.getInstance().reloadConfig();
                Config.updateConfig();
                for(User allUser : Edge.getInstance().getUserManager().getPlayeruserMap().values()) {
                    for(EdgeCheck checkAll : allUser.getChecks()){
                        checkAll.hardSetEnabled(Config.ENABLEDCHECKS.contains(this.getClass().getSimpleName()));
                        checkAll.setSetback(Config.SETBACKCHECKS.contains(this.getClass().getSimpleName()));
                        checkAll.setCommandVlMap(Config.PUNISH_COMMANDS.get(this.getClass().getSimpleName()));
                        checkAll.setBan(Config.BANNINGCHECKS.contains(this.getClass().getSimpleName()));
                    }
                }
                for(User playerData : Edge.getInstance().getUserManager().getPlayeruserMap().values()) {
                    for(EdgeCheck checkOf : playerData.getChecks()){
                        if (checkOf.getClass().getSimpleName().equalsIgnoreCase(check.getJustTheName() + check.getType())) {
                            checkOf.setEnabled(!check.isEnabled());
                        }
                    }
                }
                new BukkitRunnable() {
                    public void run() {
                        final ToggleGui toggleGui = new ToggleGui(ToggleGui.this.player);
                        toggleGui.openGui();
                    }
                }.runTask(Edge.getInstance());
            }).start();
        }
    }
    
    public void toggleBan(final int slot) {
        final User user = Edge.getInstance().getUserManager().getPlayeruser((Player)this.player);
        final List<List<EdgeCheck>> evenList = Lists.partition(user.getChecks(), 27);
        final List<EdgeCheck> checks = evenList.get(user.getPageIndex());
        final EdgeCheck check = checks.get(slot);
        if (check != null) {
            new Thread(() -> {
                final File file = new File(Edge.getInstance().getDataFolder(), "checks.yml");
                Config.checkConfig.set("check." + check.getCheckInfo().name().split("\\(")[0].replace(" ", "") + check.getCheckInfo().type() + ".violations.enabled", !check.isBan());
                try {
                    Config.checkConfig.save(file);
                }
                catch (final IOException ex) {}
                Edge.getInstance().getUserManager().getPlayeruserMap().clear();
                Config.PUNISH_COMMANDS.clear();
                Config.ENABLEDCHECKS.clear();
                Config.BANNINGCHECKS.clear();
                Config.SETBACKCHECKS.clear();
                Edge.getInstance().reloadConfig();
                Config.updateConfig();
                for(User allUser : Edge.getInstance().getUserManager().getPlayeruserMap().values()) {
                    for(EdgeCheck checkAll : allUser.getChecks()){
                        checkAll.hardSetEnabled(Config.ENABLEDCHECKS.contains(this.getClass().getSimpleName()));
                        checkAll.setSetback(Config.SETBACKCHECKS.contains(this.getClass().getSimpleName()));
                        checkAll.setCommandVlMap(Config.PUNISH_COMMANDS.get(this.getClass().getSimpleName()));
                        checkAll.setBan(Config.BANNINGCHECKS.contains(this.getClass().getSimpleName()));
                    }
                }
                Bukkit.getOnlinePlayers().forEach(player -> Edge.getInstance().getUserManager().add(player));
                for(User playerData : Edge.getInstance().getUserManager().getPlayeruserMap().values()) {
                    for(EdgeCheck checkOf : playerData.getChecks()){
                        if (checkOf.getClass().getSimpleName().equalsIgnoreCase(check.getJustTheName() + check.getType())) {
                            checkOf.setEnabled(!check.isEnabled());
                        }
                    }
                }
                new BukkitRunnable() {
                    public void run() {
                        final ToggleGui toggleGui = new ToggleGui(ToggleGui.this.player);
                        toggleGui.openGui();
                    }
                }.runTask(Edge.getInstance());
            }).start();
        }
    }
    
    public void toggleSetback(final int slot) {
        final User user = Edge.getInstance().getUserManager().getPlayeruser((Player)this.player);
        final List<List<EdgeCheck>> evenList = Lists.partition(user.getChecks(), 27);
        final List<EdgeCheck> checks = evenList.get(user.getPageIndex());
        final EdgeCheck check = checks.get(slot);
        if (check != null) {
            new Thread(() -> {
                final File file = new File(Edge.getInstance().getDataFolder(), "checks.yml");
                Config.checkConfig.set("check." + check.getCheckInfo().name().split("\\(")[0].replace(" ", "") + check.getCheckInfo().type() + ".dragdown", !check.isSetback());
                try {
                    Config.checkConfig.save(file);
                }
                catch (final IOException ex) {}
                Edge.getInstance().getUserManager().getPlayeruserMap().clear();
                Config.PUNISH_COMMANDS.clear();
                Config.ENABLEDCHECKS.clear();
                Config.BANNINGCHECKS.clear();
                Config.SETBACKCHECKS.clear();
                Edge.getInstance().reloadConfig();
                Config.updateConfig();
                for(User allUser : Edge.getInstance().getUserManager().getPlayeruserMap().values()) {
                    for(EdgeCheck checkAll : allUser.getChecks()){
                        checkAll.hardSetEnabled(Config.ENABLEDCHECKS.contains(this.getClass().getSimpleName()));
                        checkAll.setSetback(Config.SETBACKCHECKS.contains(this.getClass().getSimpleName()));
                        checkAll.setCommandVlMap(Config.PUNISH_COMMANDS.get(this.getClass().getSimpleName()));
                        checkAll.setBan(Config.BANNINGCHECKS.contains(this.getClass().getSimpleName()));
                    }
                }
                Bukkit.getOnlinePlayers().forEach(player -> Edge.getInstance().getUserManager().add(player));
                for(User playerData : Edge.getInstance().getUserManager().getPlayeruserMap().values()) {
                    for(EdgeCheck checkOf : playerData.getChecks()){
                        if (checkOf.getClass().getSimpleName().equalsIgnoreCase(check.getJustTheName() + check.getType())) {
                            checkOf.setEnabled(!check.isEnabled());
                        }
                    }
                }
                new BukkitRunnable() {
                    public void run() {
                        final ToggleGui toggleGui = new ToggleGui(ToggleGui.this.player);
                        toggleGui.openGui();
                    }
                }.runTask(Edge.getInstance());
            }).start();
        }
    }
    
    public void openGui() {
        this.player.openInventory(this.inventory);
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
