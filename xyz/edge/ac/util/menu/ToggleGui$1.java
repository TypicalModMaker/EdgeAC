package xyz.edge.ac.util.menu;

import org.bukkit.scheduler.BukkitRunnable;

class ToggleGui$1 extends BukkitRunnable {
    public void run() {
        final ToggleGui toggleGui = new ToggleGui(ToggleGui.access$000(ToggleGui.this));
        toggleGui.openGui();
    }
}