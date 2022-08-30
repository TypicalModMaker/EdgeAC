package xyz.edge.ac.user.impl;

import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import xyz.edge.ac.Edge;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import xyz.edge.ac.util.Observable;
import org.bukkit.entity.Entity;
import xyz.edge.ac.user.User;

public final class Combat
{
    private final User user;
    private int hitTicks;
    private int swings;
    private int hits;
    private int currentTargets;
    private double hitMissRatio;
    private double distance;
    private int lastAttackTick;
    private long lastAttack;
    private long lastCombat;
    private long lastHitSlow;
    private int entityID;
    private Entity target;
    private Entity lastTarget;
    private final Observable<Entity> entity;
    
    public Combat(final User user) {
        this.entity = new Observable<Entity>(null);
        this.user = user;
    }
    
    public void handleUseEntity(final WrappedPacketInUseEntity wrapper) {
        if (wrapper.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK || wrapper.getEntity() == null) {
            return;
        }
        this.lastTarget = ((this.target == null) ? wrapper.getEntity() : this.target);
        this.target = wrapper.getEntity();
        this.distance = this.user.getPlayer().getLocation().toVector().setY(0).distance(this.target.getLocation().toVector().setY(0)) - 0.42;
        ++this.hits;
        this.hitTicks = 0;
        if (this.target != this.lastTarget) {
            ++this.currentTargets;
        }
    }
    
    public void handleArmAnimation() {
        ++this.swings;
    }
    
    public void onHitTarget(final WrappedPacketInUseEntity packet) {
        if (packet.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
            this.lastAttack = System.currentTimeMillis();
            this.lastAttackTick = Edge.getInstance().getTickManager().getTicks();
            this.entityID = packet.getEntityId();
            this.entity.set(packet.getEntity());
            ++this.hits;
            if (this.entity.get() != null) {
                if (this.entity.get() instanceof LivingEntity) {
                    final LivingEntity livingEntity = (LivingEntity)this.entity.get();
                    if (livingEntity.getNoDamageTicks() != 0) {
                        this.lastCombat = System.currentTimeMillis();
                    }
                    this.distance = this.user.getPlayer().getLocation().toVector().setY(0).distance(this.entity.get().getLocation().toVector().setY(0)) - 0.42;
                }
                if (this.entity.get() instanceof Player && this.user.getPacketActionHandler().isSprinting()) {
                    this.lastHitSlow = System.currentTimeMillis();
                }
            }
        }
    }
    
    public void handleFlying() {
        ++this.hitTicks;
        this.currentTargets = 0;
        if (this.swings > 1) {
            this.hitMissRatio = this.hits / (double)this.swings * 100.0;
        }
        if (this.hits > 100 || this.swings > 100) {
            final int n = 0;
            this.swings = n;
            this.hits = n;
        }
    }
    
    public User getUser() {
        return this.user;
    }
    
    public int getHitTicks() {
        return this.hitTicks;
    }
    
    public int getSwings() {
        return this.swings;
    }
    
    public int getHits() {
        return this.hits;
    }
    
    public int getCurrentTargets() {
        return this.currentTargets;
    }
    
    public double getHitMissRatio() {
        return this.hitMissRatio;
    }
    
    public double getDistance() {
        return this.distance;
    }
    
    public int getLastAttackTick() {
        return this.lastAttackTick;
    }
    
    public long getLastAttack() {
        return this.lastAttack;
    }
    
    public long getLastCombat() {
        return this.lastCombat;
    }
    
    public long getLastHitSlow() {
        return this.lastHitSlow;
    }
    
    public int getEntityID() {
        return this.entityID;
    }
    
    public Entity getTarget() {
        return this.target;
    }
    
    public Entity getLastTarget() {
        return this.lastTarget;
    }
    
    public Observable<Entity> getEntity() {
        return this.entity;
    }
}
