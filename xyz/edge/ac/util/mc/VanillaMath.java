package xyz.edge.ac.util.mc;

public class VanillaMath
{
    private static final float[] SIN;
    
    public static float sin(final float f) {
        return VanillaMath.SIN[(int)(f * 10430.378f) & 0xFFFF];
    }
    
    public static float cos(final float f) {
        return VanillaMath.SIN[(int)(f * 10430.378f + 16384.0f) & 0xFFFF];
    }
    
    static {
        SIN = new float[65536];
        for (int i = 0; i < VanillaMath.SIN.length; ++i) {
            VanillaMath.SIN[i] = (float)StrictMath.sin(i * 3.141592653589793 * 2.0 / 65536.0);
        }
    }
}
