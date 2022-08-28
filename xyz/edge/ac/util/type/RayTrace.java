package xyz.edge.ac.util.type;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public final class RayTrace
{
    private final Vector origin;
    private final Vector direction;
    
    public RayTrace(final Vector origin, final Vector direction) {
        this.origin = origin;
        this.direction = direction;
    }
    
    public RayTrace(final Player player) {
        this.origin = player.getEyeLocation().toVector();
        this.direction = player.getEyeLocation().getDirection();
    }
    
    public double origin(final int i) {
        switch (i) {
            case 0: {
                return this.origin.getX();
            }
            case 1: {
                return this.origin.getY();
            }
            case 2: {
                return this.origin.getZ();
            }
            default: {
                return 0.0;
            }
        }
    }
    
    public double direction(final int i) {
        switch (i) {
            case 0: {
                return this.direction.getX();
            }
            case 1: {
                return this.direction.getY();
            }
            case 2: {
                return this.direction.getZ();
            }
            default: {
                return 0.0;
            }
        }
    }
}
