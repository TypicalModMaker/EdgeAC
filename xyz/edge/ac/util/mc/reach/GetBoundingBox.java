package xyz.edge.ac.util.mc.reach;

public class GetBoundingBox
{
    public static SimpleCollisionBox getBoundingBoxFromPosAndSize(final double centerX, final double minY, final double centerZ, final double width, final double height) {
        final double minX = centerX - width / 2.0;
        final double maxX = centerX + width / 2.0;
        final double maxY = minY + height;
        final double minZ = centerZ - width / 2.0;
        final double maxZ = centerZ + width / 2.0;
        return new SimpleCollisionBox(minX, minY, minZ, maxX, maxY, maxZ, false);
    }
}
