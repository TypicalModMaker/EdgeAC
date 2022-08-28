package xyz.edge.ac.packetevents.utils.world;

public enum Dimension
{
    NETHER(-1), 
    OVERWORLD(0), 
    END(1);
    
    private final int id;
    
    private Dimension(final int id) {
        this.id = id;
    }
    
    public static Dimension getById(final int id) {
        return values()[id + 1];
    }
    
    public int getId() {
        return this.id;
    }
}
