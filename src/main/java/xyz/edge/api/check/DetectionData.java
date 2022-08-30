package xyz.edge.api.check;

import xyz.edge.api.license.LicenseType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.RUNTIME)
public @interface DetectionData {
    String name();
    
    String type();
    
    boolean exemptBedrock() default false;
    
    boolean punish() default true;
    
    boolean experimental() default false;
    
    LicenseType licenseType() default LicenseType.PREMIUM;
}
