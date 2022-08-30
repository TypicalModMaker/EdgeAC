package xyz.edge.ac.util.mc.reach;

import io.github.retrooper.packetevents.utils.vector.Vector3d;
import xyz.edge.ac.util.utils.MathUtil;
import org.bukkit.util.Vector;

public class VectorUtils
{
    public static Vector cutBoxToVector(final Vector vectorToCutTo, final Vector min, final Vector max) {
        final SimpleCollisionBox box = new SimpleCollisionBox(min, max).sort();
        return cutBoxToVector(vectorToCutTo, box);
    }
    
    public static Vector cutBoxToVector(final Vector vectorCutTo, final SimpleCollisionBox box) {
        return new Vector(MathUtil.clamp(vectorCutTo.getX(), box.minX, box.maxX), MathUtil.clamp(vectorCutTo.getY(), box.minY, box.maxY), MathUtil.clamp(vectorCutTo.getZ(), box.minZ, box.maxZ));
    }
    
    public static Vector fromVec3d(final Vector3d vector3d) {
        return new Vector(vector3d.getX(), vector3d.getY(), vector3d.getZ());
    }
    
    public static Vector3d clampVector(final Vector3d toClamp) {
        final double x = MathUtil.clamp(toClamp.getX(), -3.0E7, 3.0E7);
        final double y = MathUtil.clamp(toClamp.getY(), -2.0E7, 2.0E7);
        final double z = MathUtil.clamp(toClamp.getZ(), -3.0E7, 3.0E7);
        return new Vector3d(x, y, z);
    }
}
