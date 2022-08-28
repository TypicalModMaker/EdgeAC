package xyz.edge.ac.packetevents.utils.immutableset;

abstract class ImmutableSetAbstract<T>
{
    public abstract boolean contains(final T p0);
    
    public abstract void add(final T p0);
    
    public abstract void addAll(final T... p0);
}
