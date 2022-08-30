package xyz.edge.ac.util.type;

public final class Pair<X, Y>
{
    private X x;
    private Y y;
    
    public X getX() {
        return this.x;
    }
    
    public Y getY() {
        return this.y;
    }
    
    public Pair(final X x, final Y y) {
        this.x = x;
        this.y = y;
    }
}
