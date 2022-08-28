package xyz.edge.ac.util.utils;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import xyz.edge.ac.util.type.Pair;
import java.util.Random;
import org.bukkit.util.Vector;
import java.util.Iterator;
import java.util.Collection;

public final class MathUtil
{
    public static final double EXPANDER;
    
    public static double getVariance(final Collection<? extends Number> user) {
        int count = 0;
        double sum = 0.0;
        double variance = 0.0;
        for (final Number number : user) {
            sum += number.doubleValue();
            ++count;
        }
        final double average = sum / count;
        for (final Number number : user) {
            variance += Math.pow(number.doubleValue() - average, 2.0);
        }
        return variance;
    }
    
    public static boolean isOpposite(final double num1, final double num2) {
        return (num1 > 0.0 && num2 < 0.0) || (num1 < 0.0 && num2 > 0.0);
    }
    
    public static boolean isRoughlyEqual(final double d1, final double d2, final double seperator) {
        return Math.abs(d1 - d2) < seperator;
    }
    
    public static Vector getDirection(final float yaw, final float pitch) {
        final Vector vector = new Vector();
        final float rotX = (float)Math.toRadians(yaw);
        final float rotY = (float)Math.toRadians(pitch);
        vector.setY(-Math.sin(rotY));
        final double xz = Math.cos(rotY);
        vector.setX(-xz * Math.sin(rotX));
        vector.setZ(xz * Math.cos(rotX));
        return vector;
    }
    
    public static float wrapAngleTo180_float(float value) {
        if ((value %= 360.0f) >= 180.0f) {
            value -= 360.0f;
        }
        if (value < -180.0f) {
            value += 360.0f;
        }
        return value;
    }
    
    public static double clamp(final double d, final double d2, final double d3) {
        if (d < d2) {
            return d2;
        }
        return Math.min(d, d3);
    }
    
    public static double round(double number, final int decimals) {
        number *= Math.pow(10.0, decimals);
        number = (double)Math.round(number);
        return number / Math.pow(10.0, decimals);
    }
    
    public static double hypot(final double a, final double b) {
        return Math.sqrt(a * a + b * b);
    }
    
    public static double preciseRound(final double value, final int precision) {
        final int scale = (int)Math.pow(10.0, precision);
        return Math.round(value * scale) / (double)scale;
    }
    
    public static double magnitude(final double... points) {
        double sum = 0.0;
        for (final double point : points) {
            sum += point * point;
        }
        return Math.sqrt(sum);
    }
    
    public static boolean isExponentiallySmall(final Number number) {
        return number.doubleValue() < 1.0 && (Double.toString(number.doubleValue()).contains("E") || number.doubleValue() == 0.0);
    }
    
    public static double getRandomDouble(final double number1, final double number2) {
        return number1 + (number2 - number1) * new Random().nextDouble();
    }
    
    public static double getStandardDeviation(final Collection<? extends Number> user) {
        final double variance = getVariance(user);
        return Math.sqrt(variance);
    }
    
    public static boolean isScientificNotation(final Number num) {
        return num.toString().contains("E");
    }
    
    public static boolean mathOnGround(final double posY) {
        return posY % 0.015625 == 0.0;
    }
    
    public static Pair<List<Double>, List<Double>> getOutliers(final Collection<? extends Number> collection) {
        final List<Double> values = new ArrayList<Double>();
        for (final Number number : collection) {
            values.add(number.doubleValue());
        }
        final double q1 = getMedian(values.subList(0, values.size() / 2));
        final double q2 = getMedian(values.subList(values.size() / 2, values.size()));
        final double iqr = Math.abs(q1 - q2);
        final double lowThreshold = q1 - 1.5 * iqr;
        final double highThreshold = q2 + 1.5 * iqr;
        final Pair<List<Double>, List<Double>> tuple = new Pair<List<Double>, List<Double>>(new ArrayList<Double>(), new ArrayList<Double>());
        for (final Double value : values) {
            if (value < lowThreshold) {
                tuple.getX().add(value);
            }
            else {
                if (value <= highThreshold) {
                    continue;
                }
                tuple.getY().add(value);
            }
        }
        return tuple;
    }
    
    public static double getSkewness(final Collection<? extends Number> user) {
        double sum = 0.0;
        int count = 0;
        final List<Double> numbers = (List<Double>)Lists.newArrayList();
        for (final Number number : user) {
            sum += number.doubleValue();
            ++count;
            numbers.add(number.doubleValue());
        }
        Collections.sort(numbers);
        final double mean = sum / count;
        final double median = (count % 2 != 0) ? numbers.get(count / 2) : ((numbers.get((count - 1) / 2) + numbers.get(count / 2)) / 2.0);
        final double variance = getVariance(user);
        return 3.0 * (mean - median) / variance;
    }
    
    public static double getAverage(final Collection<? extends Number> user) {
        return user.stream().mapToDouble(Number::doubleValue).average().orElse(0.0);
    }
    
    public static double getKurtosis(final Collection<? extends Number> user) {
        double sum = 0.0;
        int count = 0;
        for (final Number number : user) {
            sum += number.doubleValue();
            ++count;
        }
        if (count < 3.0) {
            return 0.0;
        }
        final double efficiencyFirst = count * (count + 1.0) / ((count - 1.0) * (count - 2.0) * (count - 3.0));
        final double efficiencySecond = 3.0 * Math.pow(count - 1.0, 2.0) / ((count - 2.0) * (count - 3.0));
        final double average = sum / count;
        double variance = 0.0;
        double varianceSquared = 0.0;
        for (final Number number2 : user) {
            variance += Math.pow(average - number2.doubleValue(), 2.0);
            varianceSquared += Math.pow(average - number2.doubleValue(), 4.0);
        }
        return efficiencyFirst * (varianceSquared / Math.pow(variance / sum, 2.0)) - efficiencySecond;
    }
    
    public static int getMode(final Collection<? extends Number> array) {
        int mode = (int)array.toArray()[0];
        int maxCount = 0;
        for (final Number value : array) {
            int count = 1;
            for (final Number i : array) {
                if (i.equals(value)) {
                    ++count;
                }
                if (count > maxCount) {
                    mode = (int)value;
                    maxCount = count;
                }
            }
        }
        return mode;
    }
    
    public static double angle(final Vector a, final Vector b) {
        final double dot = Math.min(Math.max(a.dot(b) / (a.length() * b.length()), -1.0), 1.0);
        return Math.acos(dot);
    }
    
    public static Number getModeUsingMaps(final Collection<? extends Number> samples) {
        final Map<Number, Integer> occurenceMap = new HashMap<Number, Integer>();
        for (final Number entry : samples) {
            if (!occurenceMap.containsKey(entry)) {
                occurenceMap.put(entry, 1);
            }
            else {
                occurenceMap.put(entry, occurenceMap.get(entry) + 1);
            }
        }
        Number mode = null;
        int occurences = 0;
        for (final Map.Entry<Number, Integer> entry2 : occurenceMap.entrySet()) {
            if (entry2.getValue() > occurences) {
                occurences = entry2.getValue();
                mode = entry2.getKey();
            }
        }
        return mode;
    }
    
    private static double getMedian(final List<Double> user) {
        if (user.size() % 2 == 0) {
            return (user.get(user.size() / 2) + user.get(user.size() / 2 - 1)) / 2.0;
        }
        return user.get(user.size() / 2);
    }
    
    public static long getGcd(final long current, final long previous) {
        return (previous <= 16384L) ? current : getGcd(previous, current % previous);
    }
    
    public static double getGcd(final double a, final double b) {
        if (a < b) {
            return getGcd(b, a);
        }
        if (Math.abs(b) < 0.001) {
            return a;
        }
        return getGcd(b, a - Math.floor(a / b) * b);
    }
    
    public static double deviationSquared(final Iterable<? extends Number> numbers) {
        double total = 0.0;
        int i = 0;
        for (final Number number : numbers) {
            total += number.doubleValue();
            ++i;
        }
        final double average = total / i;
        double deviation = 0.0;
        for (final Number number2 : numbers) {
            deviation += Math.pow(number2.doubleValue() - average, 2.0);
        }
        return deviation / (i - 1);
    }
    
    public static int getDistinct(final Collection<? extends Number> user) {
        return (int)user.stream().distinct().count();
    }
    
    public static double getCps(final Collection<? extends Number> user) {
        return 20.0 / getAverage(user) * 50.0;
    }
    
    public static double deviation(final Iterable<? extends Number> numbers) {
        return Math.sqrt(deviationSquared(numbers));
    }
    
    private MathUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    static {
        EXPANDER = Math.pow(2.0, 24.0);
    }
}
