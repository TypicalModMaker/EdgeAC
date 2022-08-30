package xyz.edge.ac.user.impl;

import java.util.Collection;
import xyz.edge.ac.util.utils.MathUtil;
import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.util.type.EvictingList;
import xyz.edge.ac.user.User;

public final class Click
{
    private final User user;
    private long lastSwing;
    private long delay;
    private int movements;
    public double cps;
    public double rate;
    private final EvictingList<Integer> clicks;
    
    public Click(final User user) {
        this.lastSwing = -1L;
        this.clicks = new EvictingList<Integer>(10);
        this.user = user;
    }
    
    public void handleArmAnimation() {
        if (!this.user.getPacketActionHandler().isDigging() && !this.user.getPacketActionHandler().isPlacing()) {
            if (this.lastSwing > 0L) {
                this.delay = System.currentTimeMillis() - this.lastSwing;
            }
            this.lastSwing = System.currentTimeMillis();
        }
        final boolean exempt = this.user.getExemptionHandler().isExempt(Exempts.PLACING, Exempts.DIGGING);
        if (!exempt) {
            if (this.movements <= 5) {
                this.clicks.add(this.movements);
            }
        }
        if (this.clicks.size() > 5) {
            final double cps = MathUtil.getCps(this.clicks);
            final double rate = cps * this.movements;
            this.cps = cps;
            this.rate = rate;
        }
        this.movements = 0;
    }
    
    public void handleFlying() {
        ++this.movements;
    }
    
    public User getUser() {
        return this.user;
    }
    
    public long getLastSwing() {
        return this.lastSwing;
    }
    
    public long getDelay() {
        return this.delay;
    }
    
    public int getMovements() {
        return this.movements;
    }
    
    public double getCps() {
        return this.cps;
    }
    
    public double getRate() {
        return this.rate;
    }
    
    public EvictingList<Integer> getClicks() {
        return this.clicks;
    }
}
