package xyz.edge.ac.packetevents.exceptions;

public class PacketEventsLoadFailureException extends RuntimeException
{
    public PacketEventsLoadFailureException(final String message) {
        super(message);
    }
    
    public PacketEventsLoadFailureException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public PacketEventsLoadFailureException() {
        this("PacketEvents failed to load...");
    }
    
    public PacketEventsLoadFailureException(final Throwable cause) {
        this("PacketEvents failed to load...", cause);
    }
}
