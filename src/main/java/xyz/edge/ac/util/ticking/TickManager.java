package xyz.edge.ac.util.ticking;

import xyz.edge.ac.user.User;
import org.bukkit.entity.Entity;
import org.bukkit.Location;
import xyz.edge.ac.util.type.Pair;
import org.bukkit.plugin.Plugin;
import xyz.edge.ac.Edge;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public final class TickManager implements Runnable
{
    private int ticks;
    private static BukkitTask task;
    
    public void start() {
        assert TickManager.task == null : "TickProcessor has already been started!";
        TickManager.task = Bukkit.getScheduler().runTaskTimer(Edge.getInstance(), this, 0L, 1L);
    }
    
    public void stop() {
        if (TickManager.task == null) {
            return;
        }
        TickManager.task.cancel();
        TickManager.task = null;
    }
    
    @Override
    public void run() {
        ++this.ticks;
        Edge.getInstance().getUserManager().getAlluser().parallelStream().forEach(user -> {
            final Entity target = user.getFightHandler().getTarget();
            final Entity lastTarget = user.getFightHandler().getLastTarget();
            if (target != null && lastTarget != null) {
                if (target != lastTarget) {
                    user.getTargetLocations().clear();
                }
                final Location location = target.getLocation();
                user.getTargetLocations().add(new Pair<Location, Integer>(location, this.ticks));
            }
        });
    }
    
    public int getTicks() {
        return this.ticks;
    }
}
