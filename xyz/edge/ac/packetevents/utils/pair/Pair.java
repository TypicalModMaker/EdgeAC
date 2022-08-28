package xyz.edge.ac.packetevents.utils.pair;

import java.util.Objects;

public class Pair<A, B>
{
    private final A first;
    private final B second;
    
    public Pair(final A first, final B second) {
        this.first = first;
        this.second = second;
    }
    
    public static <T, K> Pair<T, K> of(final T a, final K b) {
        return new Pair<T, K>(a, b);
    }
    
    public A getFirst() {
        return this.first;
    }
    
    public B getSecond() {
        return this.second;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }
        final Pair b = (Pair)o;
        return Objects.equals(this.first, b.first) && Objects.equals(this.second, b.second);
    }
}
