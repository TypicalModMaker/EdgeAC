package xyz.edge.ac.util.utils;

import org.bukkit.util.Vector;
import xyz.edge.ac.util.type.BoundingBox;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.entity.Entity;
import org.bukkit.Bukkit;
import java.lang.reflect.Method;
import xyz.edge.ac.util.SystemLogsUtil;
import java.util.Arrays;
import java.lang.reflect.Field;

public final class ReflectionUtil
{
    private static String versionString;
    
    public static Field getField(final Class<?> clazz, final String fieldName) {
        try {
            return clazz.getField(fieldName);
        }
        catch (final NoSuchFieldException e) {
            SystemLogsUtil.createNewLog(Arrays.toString(e.getStackTrace()), "ReflectionUtil (getField)", e.getMessage());
            return null;
        }
    }
    
    public static Method getMethod(final Class<?> clazz, final String methodName, final Class<?>... params) {
        try {
            return clazz.getMethod(methodName, params);
        }
        catch (final NoSuchMethodException e) {
            SystemLogsUtil.createNewLog(Arrays.toString(e.getStackTrace()), "ReflectionUtil (getMethod)", e.getMessage());
            return null;
        }
    }
    
    public static String getVersion() {
        if (ReflectionUtil.versionString == null) {
            final String name = Bukkit.getServer().getClass().getPackage().getName();
            ReflectionUtil.versionString = name.substring(name.lastIndexOf(46) + 1) + ".";
        }
        return ReflectionUtil.versionString;
    }
    
    public static Class<?> getNMSClass(final String nmsClassName) {
        final String clazzName = "net.minecraft.server." + getVersion() + nmsClassName;
        try {
            return Class.forName(clazzName);
        }
        catch (final Throwable t) {
            SystemLogsUtil.createNewLog(Arrays.toString(t.getStackTrace()), "ReflectionUtil (getNMSClass)", t.getMessage());
            return null;
        }
    }
    
    public static Class<?> getOBCClass(final String obcClassName) {
        final String clazzName = "org.bukkit.craftbukkit." + getVersion() + obcClassName;
        try {
            return Class.forName(clazzName);
        }
        catch (final Throwable t) {
            SystemLogsUtil.createNewLog(Arrays.toString(t.getStackTrace()), "ReflectionUtil (getOBCClass)", t.getMessage());
            return null;
        }
    }
    
    public static Object craftEntity(final Entity entity) {
        try {
            return getMethod(getOBCClass("entity.CraftEntity"), "getHandle", (Class<?>[])new Class[0]).invoke(entity, new Object[0]);
        }
        catch (final IllegalAccessException | InvocationTargetException e) {
            SystemLogsUtil.createNewLog(Arrays.toString(e.getStackTrace()), "ReflectionUtil (craftEntity)", e.getMessage());
            return null;
        }
    }
    
    public static BoundingBox getBoundingBox(final Entity entity) {
        try {
            final Object nmsBoundingBox = getMethod(getNMSClass("Entity"), "getBoundingBox", (Class<?>[])new Class[0]).invoke(craftEntity(entity), new Object[0]);
            return toBoundingBox(nmsBoundingBox);
        }
        catch (final IllegalAccessException | InvocationTargetException e) {
            SystemLogsUtil.createNewLog(Arrays.toString(e.getStackTrace()), "ReflectionUtil (getBoundingBox)", e.getMessage());
            return null;
        }
    }
    
    public static BoundingBox toBoundingBox(final Object aaBB) {
        final Vector min = getBoxMin(aaBB);
        final Vector max = getBoxMax(aaBB);
        return new BoundingBox(min, max);
    }
    
    private static Vector getBoxMin(final Object box) {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        final Class<?> boxClass = box.getClass();
        try {
            if (!ServerUtil.isHigherThan1_13_2()) {
                x = (double)getField(boxClass, "a").get(box);
                y = (double)getField(boxClass, "b").get(box);
                z = (double)getField(boxClass, "c").get(box);
            }
            else {
                x = (double)getField(boxClass, "minX").get(box);
                y = (double)getField(boxClass, "minY").get(box);
                z = (double)getField(boxClass, "minZ").get(box);
            }
        }
        catch (final IllegalAccessException e) {
            SystemLogsUtil.createNewLog(Arrays.toString(e.getStackTrace()), "ReflectionUtil (getBoxMin)", e.getMessage());
        }
        return new Vector(x, y, z);
    }
    
    private static Vector getBoxMax(final Object box) {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        final Class<?> boxClass = box.getClass();
        try {
            if (!ServerUtil.isHigherThan1_13_2()) {
                x = (double)getField(boxClass, "d").get(box);
                y = (double)getField(boxClass, "e").get(box);
                z = (double)getField(boxClass, "f").get(box);
            }
            else {
                x = (double)getField(boxClass, "maxX").get(box);
                y = (double)getField(boxClass, "maxY").get(box);
                z = (double)getField(boxClass, "maxZ").get(box);
            }
        }
        catch (final IllegalAccessException e) {
            SystemLogsUtil.createNewLog(Arrays.toString(e.getStackTrace()), "ReflectionUtil (getBoxMax)", e.getMessage());
        }
        return new Vector(x, y, z);
    }
    
    private ReflectionUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
