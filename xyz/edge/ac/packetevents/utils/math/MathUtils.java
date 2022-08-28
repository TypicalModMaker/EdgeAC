package xyz.edge.ac.packetevents.utils.math;

public class MathUtils
{
    public static int floor(final double value) {
        final int temp = (int)value;
        return (value < temp) ? (temp - 1) : temp;
    }
    
    public static int floor(final float value) {
        final int temp = (int)value;
        return (value < temp) ? (temp - 1) : temp;
    }
}
