package xyz.edge.ac.user.impl;

import xyz.edge.ac.util.utils.MathUtil;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.util.Vector;
import java.util.Map;
import xyz.edge.ac.user.User;

public final class Velocity
{
    private final User data;
    private double velocityX;
    private double velocityY;
    private double velocityZ;
    private double velocityXZ;
    private double lastVelocityX;
    private double lastVelocityY;
    private double lastVelocityZ;
    private double lastVelocityXZ;
    private int maxVelocityTicks;
    private int velocityTicks;
    private int ticksSinceVelocity;
    private int takingVelocityTicks;
    private boolean waitingForTransaction;
    private int velocityID;
    private final Map<Short, Vector> pendingVelocities;
    private Vector pendingVelocity;
    private int flyingTicks;
    
    public Velocity(final User data) {
        this.pendingVelocities = new ConcurrentHashMap<Short, Vector>();
        this.pendingVelocity = new Vector(this.velocityX, this.velocityY, this.velocityZ);
        this.data = data;
    }
    
    public void handle(final double velocityX, final double velocityY, final double velocityZ) {
        this.lastVelocityX = this.velocityX;
        this.lastVelocityY = this.velocityY;
        this.lastVelocityZ = this.velocityZ;
        this.lastVelocityXZ = this.velocityXZ;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.waitingForTransaction = true;
        if (Math.abs(this.velocityX) < 0.005) {
            this.velocityX = 0.0;
        }
        if (Math.abs(this.velocityZ) < 0.005) {
            this.velocityZ = 0.0;
        }
        this.velocityXZ = MathUtil.hypot(velocityX, velocityZ);
        this.velocityID = this.data.getTransactionsHandler().sendTransaction();
        this.pendingVelocities.put((short)this.velocityID, new Vector(velocityX, velocityY, velocityZ));
    }
    
    public void handleTransaction(final short transaction) {
        this.pendingVelocities.computeIfPresent(transaction, (id, vector) -> {
            this.ticksSinceVelocity = 0;
            this.velocityTicks = this.flyingTicks;
            this.waitingForTransaction = false;
            this.maxVelocityTicks = (int)(((vector.getX() + vector.getZ()) / 2.0 + 2.0) * 15.0) + 20;
            this.pendingVelocities.remove(transaction);
            return vector;
        });
    }
    
    public void handleFlying() {
        ++this.ticksSinceVelocity;
        ++this.flyingTicks;
        if (this.isTakingVelocity()) {
            ++this.takingVelocityTicks;
        }
        else {
            this.takingVelocityTicks = 0;
        }
        if (this.isTakingHorizontal()) {
            this.velocityX *= 0.91;
            this.velocityZ *= 0.91;
            this.velocityXZ *= 0.91;
        }
    }
    
    public boolean isTakingHorizontal() {
        return this.velocityXZ < 0.005;
    }
    
    public boolean isTakingVelocity() {
        return Math.abs(this.flyingTicks - this.velocityTicks) < this.maxVelocityTicks;
    }
    
    public User getData() {
        return this.data;
    }
    
    public double getVelocityX() {
        return this.velocityX;
    }
    
    public double getVelocityY() {
        return this.velocityY;
    }
    
    public double getVelocityZ() {
        return this.velocityZ;
    }
    
    public double getVelocityXZ() {
        return this.velocityXZ;
    }
    
    public double getLastVelocityX() {
        return this.lastVelocityX;
    }
    
    public double getLastVelocityY() {
        return this.lastVelocityY;
    }
    
    public double getLastVelocityZ() {
        return this.lastVelocityZ;
    }
    
    public double getLastVelocityXZ() {
        return this.lastVelocityXZ;
    }
    
    public int getMaxVelocityTicks() {
        return this.maxVelocityTicks;
    }
    
    public int getVelocityTicks() {
        return this.velocityTicks;
    }
    
    public int getTicksSinceVelocity() {
        return this.ticksSinceVelocity;
    }
    
    public int getTakingVelocityTicks() {
        return this.takingVelocityTicks;
    }
    
    public boolean isWaitingForTransaction() {
        return this.waitingForTransaction;
    }
    
    public int getVelocityID() {
        return this.velocityID;
    }
    
    public Map<Short, Vector> getPendingVelocities() {
        return this.pendingVelocities;
    }
    
    public Vector getPendingVelocity() {
        return this.pendingVelocity;
    }
    
    public int getFlyingTicks() {
        return this.flyingTicks;
    }
}
