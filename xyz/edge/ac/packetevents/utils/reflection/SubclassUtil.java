package xyz.edge.ac.packetevents.utils.reflection;

import java.lang.annotation.Annotation;

public class SubclassUtil
{
    public static Class<? extends Enum<?>> getEnumSubClass(final Class<?> cls, final String name) {
        return (Class<? extends Enum<?>>)getSubClass(cls, name);
    }
    
    public static Class<? extends Enum<?>> getEnumSubClass(final Class<?> cls, final int index) {
        return (Class<? extends Enum<?>>)getSubClass(cls, index);
    }
    
    public static Class<?> getSubClass(final Class<?> cls, final String name) {
        if (cls == null) {
            return null;
        }
        for (final Class<?> subClass : cls.getDeclaredClasses()) {
            if (subClass.getSimpleName().equals(name)) {
                return subClass;
            }
        }
        return null;
    }
    
    public static Class<?> getSubClass(final Class<?> cls, final int index) {
        if (cls == null) {
            return null;
        }
        int currentIndex = 0;
        for (final Class<?> subClass : cls.getDeclaredClasses()) {
            if (index == currentIndex++) {
                return subClass;
            }
        }
        return null;
    }
    
    public static Class<?> getSubClass(final Class<?> cls, final Annotation annotation, final int index) {
        int currentIndex = 0;
        for (final Class<?> subClass : cls.getDeclaredClasses()) {
            if (subClass.isAnnotationPresent(annotation.getClass()) && index == currentIndex++) {
                return subClass;
            }
        }
        return null;
    }
}
