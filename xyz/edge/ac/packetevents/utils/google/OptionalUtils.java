package xyz.edge.ac.packetevents.utils.google;

import java.util.Optional;

public class OptionalUtils
{
    public static Optional<?> convertToJavaOptional(final Object googleOptional) {
        return Optional.of(GoogleOptionalUtils.getOptionalValue(googleOptional));
    }
}
