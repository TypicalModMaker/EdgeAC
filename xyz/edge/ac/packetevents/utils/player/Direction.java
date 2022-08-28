package xyz.edge.ac.packetevents.utils.player;

public enum Direction
{
    DOWN, 
    UP, 
    NORTH, 
    SOUTH, 
    WEST, 
    EAST, 
    OTHER((short)255), 
    INVALID;
    
    final short face;
    
    private Direction(final short face) {
        this.face = face;
    }
    
    private Direction() {
        this.face = (short)this.ordinal();
    }
    
    public static Direction getDirection(final int face) {
        if (face == 255) {
            return Direction.OTHER;
        }
        if (face < 0 || face > 5) {
            return Direction.INVALID;
        }
        return values()[face];
    }
    
    public short getFaceValue() {
        return this.face;
    }
}
