package xyz.edge.ac.util.mc.reach;

public class ReachInterpolationData
{
    public final SimpleCollisionBox targetLocation;
    public SimpleCollisionBox startingLocation;
    public int interpolationStepsLowBound;
    public int interpolationStepsHighBound;
    
    public ReachInterpolationData(final SimpleCollisionBox startingLocation, final double x, final double y, final double z, final boolean isPointNine) {
        this.interpolationStepsLowBound = 0;
        this.interpolationStepsHighBound = 0;
        this.startingLocation = startingLocation;
        this.targetLocation = GetBoundingBox.getBoundingBoxFromPosAndSize(x, y, z, 0.6, 1.8);
        if (isPointNine) {
            this.interpolationStepsHighBound = 3;
        }
    }
    
    public SimpleCollisionBox getPossibleLocationCombined() {
        final double stepMinX = (this.targetLocation.minX - this.startingLocation.minX) / 3.0;
        final double stepMaxX = (this.targetLocation.maxX - this.startingLocation.maxX) / 3.0;
        final double stepMinY = (this.targetLocation.minY - this.startingLocation.minY) / 3.0;
        final double stepMaxY = (this.targetLocation.maxY - this.startingLocation.maxY) / 3.0;
        final double stepMinZ = (this.targetLocation.minZ - this.startingLocation.minZ) / 3.0;
        final double stepMaxZ = (this.targetLocation.maxZ - this.startingLocation.maxZ) / 3.0;
        SimpleCollisionBox minimumInterpLocation = new SimpleCollisionBox(this.startingLocation.minX + this.interpolationStepsLowBound * stepMinX, this.startingLocation.minY + this.interpolationStepsLowBound * stepMinY, this.startingLocation.minZ + this.interpolationStepsLowBound * stepMinZ, this.startingLocation.maxX + this.interpolationStepsLowBound * stepMaxX, this.startingLocation.maxY + this.interpolationStepsLowBound * stepMaxY, this.startingLocation.maxZ + this.interpolationStepsLowBound * stepMaxZ);
        for (int step = this.interpolationStepsLowBound + 1; step <= this.interpolationStepsHighBound; ++step) {
            minimumInterpLocation = combineCollisionBox(minimumInterpLocation, new SimpleCollisionBox(this.startingLocation.minX + step * stepMinX, this.startingLocation.minY + step * stepMinY, this.startingLocation.minZ + step * stepMinZ, this.startingLocation.maxX + step * stepMaxX, this.startingLocation.maxY + step * stepMaxY, this.startingLocation.maxZ + step * stepMaxZ));
        }
        return minimumInterpLocation;
    }
    
    public static SimpleCollisionBox combineCollisionBox(final SimpleCollisionBox one, final SimpleCollisionBox two) {
        final double minX = Math.min(one.minX, two.minX);
        final double maxX = Math.max(one.maxX, two.maxX);
        final double minY = Math.min(one.minY, two.minY);
        final double maxY = Math.max(one.maxY, two.maxY);
        final double minZ = Math.min(one.minZ, two.minZ);
        final double maxZ = Math.max(one.maxZ, two.maxZ);
        return new SimpleCollisionBox(minX, minY, minZ, maxX, maxY, maxZ);
    }
    
    public void updatePossibleStartingLocation(final SimpleCollisionBox possibleLocationCombined) {
        this.startingLocation = combineCollisionBox(this.startingLocation, possibleLocationCombined);
    }
    
    public void tickMovement(final boolean incrementLowBound) {
        if (incrementLowBound) {
            this.interpolationStepsLowBound = Math.min(this.interpolationStepsLowBound + 1, 3);
        }
        this.interpolationStepsHighBound = Math.min(this.interpolationStepsHighBound + 1, 3);
    }
    
    @Override
    public String toString() {
        return "ReachInterpolationData{targetLocation=" + this.targetLocation + ", startingLocation=" + this.startingLocation + ", interpolationStepsLowBound=" + this.interpolationStepsLowBound + ", interpolationStepsHighBound=" + this.interpolationStepsHighBound + '}';
    }
}
