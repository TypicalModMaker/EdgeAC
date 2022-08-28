package xyz.edge.ac.util.type;

public final class Velocity
{
    private int index;
    private double velocityX;
    private double velocityY;
    private double velocityZ;
    
    public int getIndex() {
        return this.index;
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
    
    public void setIndex(final int index) {
        this.index = index;
    }
    
    public void setVelocityX(final double velocityX) {
        this.velocityX = velocityX;
    }
    
    public void setVelocityY(final double velocityY) {
        this.velocityY = velocityY;
    }
    
    public void setVelocityZ(final double velocityZ) {
        this.velocityZ = velocityZ;
    }
    
    public Velocity(final int index, final double velocityX, final double velocityY, final double velocityZ) {
        this.index = index;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
    }
}
