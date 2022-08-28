package xyz.edge.ac.packetevents.utils;

import java.util.Optional;

public class ConditionalValue<T, K>
{
    private final Optional<T> left;
    private final Optional<K> right;
    
    private ConditionalValue(final Optional<T> left, final Optional<K> right) {
        this.left = left;
        this.right = right;
    }
    
    public Optional<T> left() {
        return this.left;
    }
    
    public Optional<K> right() {
        return this.right;
    }
    
    @Override
    public String toString() {
        return this.left.map(t -> "ConditionalValue {left: " + t.toString() + "}").orElseGet(() -> "ConditionalValue {right: " + this.right.get().toString() + "}");
    }
    
    public static <L, R> ConditionalValue<L, R> makeLeft(final L left) {
        return new ConditionalValue<L, R>(Optional.of(left), Optional.empty());
    }
    
    public static <L, R> ConditionalValue<L, R> makeRight(final R right) {
        return new ConditionalValue<L, R>(Optional.empty(), Optional.of(right));
    }
}
