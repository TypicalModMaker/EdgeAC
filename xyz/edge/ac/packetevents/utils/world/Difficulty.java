package xyz.edge.ac.packetevents.utils.world;

public enum Difficulty
{
    PEACEFUL, 
    EASY, 
    NORMAL, 
    HARD;
    
    public String getName() {
        return this.name().toLowerCase();
    }
}
