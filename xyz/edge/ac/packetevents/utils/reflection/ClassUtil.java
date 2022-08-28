package xyz.edge.ac.packetevents.utils.reflection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class ClassUtil
{
    private static final Map<Class<?>, String> CLASS_SIMPLE_NAME_CACHE;
    
    public static String getClassSimpleName(final Class<?> cls) {
        return ClassUtil.CLASS_SIMPLE_NAME_CACHE.computeIfAbsent(cls, k -> cls.getSimpleName());
    }
    
    static {
        CLASS_SIMPLE_NAME_CACHE = new ConcurrentHashMap<Class<?>, String>();
    }
}
