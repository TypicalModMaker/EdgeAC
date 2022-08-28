package xyz.edge.ac.packetevents.exceptions;

import xyz.edge.ac.packetevents.utils.reflection.ClassUtil;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrapperUnsupportedUsageException extends RuntimeException
{
    public WrapperUnsupportedUsageException(final String message) {
        super(message);
    }
    
    public WrapperUnsupportedUsageException(final Class<? extends WrappedPacket> wrapperClass) {
        this("You are using a packet wrapper which happens to be unsupported on the local server version. Packet wrapper you attempted to use: " + ClassUtil.getClassSimpleName(wrapperClass));
    }
}
