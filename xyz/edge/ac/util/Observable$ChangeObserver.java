package xyz.edge.ac.util;

@FunctionalInterface
public interface ChangeObserver<T>
{
    void handle(final T p0, final T p1);
}
