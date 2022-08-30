package xyz.edge.api.license;

public enum LicenseType
{
    TEST(1), 
    PREMIUM(1), 
    ENTERPRISE(2), 
    DEVELOPMENT(10000);
    
    private final int priority;
    
    public int getPriority() {
        return this.priority;
    }
    
    private LicenseType(final int priority) {
        this.priority = priority;
    }
}
