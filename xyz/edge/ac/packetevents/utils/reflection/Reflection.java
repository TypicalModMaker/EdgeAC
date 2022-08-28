package xyz.edge.ac.packetevents.utils.reflection;

import org.jetbrains.annotations.Nullable;
import java.util.Arrays;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.util.List;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;

public final class Reflection
{
    public static Field[] getFields(final Class<?> cls) {
        final Field[] declaredFields2;
        final Field[] declaredFields = declaredFields2 = cls.getDeclaredFields();
        for (final Field f : declaredFields2) {
            f.setAccessible(true);
        }
        return declaredFields;
    }
    
    public static Field getField(final Class<?> cls, final String name) {
        for (final Field f : getFields(cls)) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        if (cls.getSuperclass() != null) {
            return getField(cls.getSuperclass(), name);
        }
        return null;
    }
    
    public static Field getField(final Class<?> cls, final Class<?> dataType, final int index) {
        if (dataType == null || cls == null) {
            return null;
        }
        int currentIndex = 0;
        for (final Field f : getFields(cls)) {
            if (dataType.isAssignableFrom(f.getType()) && currentIndex++ == index) {
                return f;
            }
        }
        if (cls.getSuperclass() != null) {
            return getField(cls.getSuperclass(), dataType, index);
        }
        return null;
    }
    
    public static Field getField(final Class<?> cls, final Class<?> dataType, final int index, final boolean ignoreStatic) {
        if (dataType == null || cls == null) {
            return null;
        }
        int currentIndex = 0;
        for (final Field f : getFields(cls)) {
            if (dataType.isAssignableFrom(f.getType()) && (!ignoreStatic || !Modifier.isStatic(f.getModifiers())) && currentIndex++ == index) {
                return f;
            }
        }
        if (cls.getSuperclass() != null) {
            return getField(cls.getSuperclass(), dataType, index);
        }
        return null;
    }
    
    public static Field getField(final Class<?> cls, final int index) {
        try {
            return getFields(cls)[index];
        }
        catch (final Exception ex) {
            if (cls.getSuperclass() != null) {
                return getFields(cls.getSuperclass())[index];
            }
            return null;
        }
    }
    
    public static List<Method> getMethods(final Class<?> cls, final String name, final Class<?>... params) {
        final List<Method> methods = new ArrayList<Method>();
        for (final Method m : cls.getDeclaredMethods()) {
            if ((params == null || Arrays.equals(m.getParameterTypes(), params)) && name.equals(m.getName())) {
                m.setAccessible(true);
                methods.add(m);
            }
        }
        return methods;
    }
    
    public static Method getMethod(final Class<?> cls, final int index, final Class<?>... params) {
        int currentIndex = 0;
        for (final Method m : cls.getDeclaredMethods()) {
            if ((params == null || Arrays.equals(m.getParameterTypes(), params)) && index == currentIndex++) {
                m.setAccessible(true);
                return m;
            }
        }
        if (cls.getSuperclass() != null) {
            return getMethod(cls.getSuperclass(), index, params);
        }
        return null;
    }
    
    public static Method getMethod(final Class<?> cls, final Class<?> returning, final int index, final Class<?>... params) {
        int currentIndex = 0;
        for (final Method m : cls.getDeclaredMethods()) {
            if (Arrays.equals(m.getParameterTypes(), params) && (returning == null || m.getReturnType().equals(returning)) && index == currentIndex++) {
                m.setAccessible(true);
                return m;
            }
        }
        if (cls.getSuperclass() != null) {
            return getMethod(cls.getSuperclass(), null, index, params);
        }
        return null;
    }
    
    public static Method getMethod(final Class<?> cls, final String name, final Class<?> returning, final Class<?>... params) {
        for (final Method m : cls.getDeclaredMethods()) {
            if (m.getName().equals(name) && Arrays.equals(m.getParameterTypes(), params) && (returning == null || m.getReturnType().equals(returning))) {
                m.setAccessible(true);
                return m;
            }
        }
        if (cls.getSuperclass() != null) {
            return getMethod(cls.getSuperclass(), name, null, params);
        }
        return null;
    }
    
    public static Method getMethod(final Class<?> cls, final String name, final int index) {
        if (cls == null) {
            return null;
        }
        int currentIndex = 0;
        for (final Method m : cls.getDeclaredMethods()) {
            if (m.getName().equals(name) && index == currentIndex++) {
                m.setAccessible(true);
                return m;
            }
        }
        if (cls.getSuperclass() != null) {
            return getMethod(cls.getSuperclass(), name, index);
        }
        return null;
    }
    
    public static Method getMethod(final Class<?> cls, final Class<?> returning, final int index) {
        if (cls == null) {
            return null;
        }
        int currentIndex = 0;
        for (final Method m : cls.getDeclaredMethods()) {
            if ((returning == null || m.getReturnType().equals(returning)) && index == currentIndex++) {
                m.setAccessible(true);
                return m;
            }
        }
        if (cls.getSuperclass() != null) {
            return getMethod(cls.getSuperclass(), returning, index);
        }
        return null;
    }
    
    public static Method getMethodCheckContainsString(final Class<?> cls, final String nameContainsThisStr, final Class<?> returning) {
        if (cls == null) {
            return null;
        }
        for (final Method m : cls.getDeclaredMethods()) {
            if (m.getName().contains(nameContainsThisStr) && (returning == null || m.getReturnType().equals(returning))) {
                m.setAccessible(true);
                return m;
            }
        }
        if (cls.getSuperclass() != null) {
            return getMethodCheckContainsString(cls.getSuperclass(), nameContainsThisStr, returning);
        }
        return null;
    }
    
    public static Method getMethod(final Class<?> cls, final String name, final Class<?> returning) {
        if (cls == null) {
            return null;
        }
        for (final Method m : cls.getDeclaredMethods()) {
            if (m.getName().equals(name) && (returning == null || m.getReturnType().equals(returning))) {
                m.setAccessible(true);
                return m;
            }
        }
        if (cls.getSuperclass() != null) {
            return getMethod(cls.getSuperclass(), name, returning);
        }
        return null;
    }
    
    @Nullable
    public static Class<?> getClassByNameWithoutException(final String name) {
        try {
            return Class.forName(name);
        }
        catch (final ClassNotFoundException e) {
            return null;
        }
    }
}
