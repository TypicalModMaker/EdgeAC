package xyz.edge.ac.checks;

public enum CheckType
{
    COMBAT("Combat");
    
    private final String name;
    
    private CheckType(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public static CheckType fromPackageName(final String packageName) {
        for (final CheckType checkType : values()) {
            if (packageName.contains(checkType.getName().toLowerCase())) {
                return checkType;
            }
        }
        return null;
    }
}
