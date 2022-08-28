package xyz.edge.ac.packetevents.utils.google;

import com.google.common.base.Optional;

public class GoogleOptionalUtils
{
    public static Object getOptionalValue(final Object opt) {
        return ((Optional)opt).get();
    }
    
    public static Object getOptionalEmpty() {
        return Optional.absent();
    }
    
    public static Object getOptional(final Object value) {
        return Optional.of(value);
    }
}
