package xyz.edge.ac.packetevents.bstats;

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
