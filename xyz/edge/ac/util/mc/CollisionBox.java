package xyz.edge.ac.util.mc;

import java.util.List;
import xyz.edge.ac.util.mc.reach.SimpleCollisionBox;

public interface CollisionBox
{
    boolean isCollided(final SimpleCollisionBox p0);
    
    boolean isIntersected(final SimpleCollisionBox p0);
    
    CollisionBox copy();
    
    CollisionBox offset(final double p0, final double p1, final double p2);
    
    void downCast(final List<SimpleCollisionBox> p0);
    
    boolean isNull();
    
    boolean isFullBlock();
}
