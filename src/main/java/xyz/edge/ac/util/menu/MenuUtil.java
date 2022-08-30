package xyz.edge.ac.util.menu;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;

public final class MenuUtil
{
    public static void fillBorder(final Inventory inv, final ItemStack item) {
        final int size = inv.getSize();
        final int rows = (size + 1) / 9;
        for (int i = 0; i < 9; ++i) {
            inv.setItem(i, item.clone());
        }
        for (int i = size - 9; i < size; ++i) {
            inv.setItem(i, item.clone());
        }
        for (int i = 2; i <= rows - 1; ++i) {
            final int[] slots = { i * 9 - 1, (i - 1) * 9 };
            inv.setItem(slots[0], item.clone());
            inv.setItem(slots[1], item.clone());
        }
    }
    
    private MenuUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
