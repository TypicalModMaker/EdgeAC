package xyz.edge.ac.packetevents.exceptions;

import xyz.edge.ac.packetevents.utils.reflection.ClassUtil;

public class WrapperFieldNotFoundException extends RuntimeException
{
    public WrapperFieldNotFoundException(final String message) {
        super(message);
    }
    
    public WrapperFieldNotFoundException(final Class<?> packetClass, final Class<?> type, final int index) {
        this("PacketEvents failed to find a " + ClassUtil.getClassSimpleName(type) + " indexed " + index + " by its type in the " + packetClass.getName() + " class!");
    }
}
