package xyz.edge.ac.cmd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface CommandInfo {
    String name();
    
    String purpose();
    
    String syntax() default "";
}
