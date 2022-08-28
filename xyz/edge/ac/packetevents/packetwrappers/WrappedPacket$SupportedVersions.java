package xyz.edge.ac.packetevents.packetwrappers;

import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR })
@Retention(RetentionPolicy.RUNTIME)
public @interface SupportedVersions {
    ServerVersion[] ranges() default {};
}
