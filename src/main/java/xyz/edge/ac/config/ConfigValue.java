package xyz.edge.ac.config;

public final class ConfigValue
{
    private Object value;
    private final ValueType type;
    private final String name;
    
    public ConfigValue(final ValueType type, final String name) {
        this.type = type;
        this.name = name;
    }
    
    public boolean getBoolean() {
        return (boolean)this.value;
    }
    
    public double getDouble() {
        return (double)this.value;
    }
    
    public int getInt() {
        return (int)this.value;
    }
    
    public long getLong() {
        return (long)this.value;
    }
    
    public String getString() {
        return (String)this.value;
    }
    
    public void setValue(final Object value) {
        this.value = value;
    }
    
    public ValueType getType() {
        return this.type;
    }
    
    public String getName() {
        return this.name;
    }
    
    public enum ValueType
    {
        INTEGER, 
        DOUBLE, 
        BOOLEAN, 
        STRING, 
        LONG;
    }
}
