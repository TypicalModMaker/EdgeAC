package xyz.edge.ac.util.connections;

public static class JsonObject
{
    private final String value;
    
    private JsonObject(final String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return this.value;
    }
}
