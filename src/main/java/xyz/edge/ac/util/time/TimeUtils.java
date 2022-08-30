package xyz.edge.ac.util.time;

public class TimeUtils
{
    public static long elapsed(final long time) {
        return Math.abs(System.currentTimeMillis() - time);
    }
}
