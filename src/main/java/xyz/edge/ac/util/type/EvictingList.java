package xyz.edge.ac.util.type;

import java.util.Collection;
import java.util.LinkedList;

public final class EvictingList<T> extends LinkedList<T>
{
    private final int maxSize;
    
    public EvictingList(final int maxSize) {
        this.maxSize = maxSize;
    }
    
    public EvictingList(final Collection<? extends T> c, final int maxSize) {
        super(c);
        this.maxSize = maxSize;
    }
    
    @Override
    public boolean add(final T t) {
        if (this.size() >= this.getMaxSize()) {
            this.removeFirst();
        }
        return super.add(t);
    }
    
    public boolean isFull() {
        return this.size() >= this.getMaxSize();
    }
    
    public int getMaxSize() {
        return this.maxSize;
    }
}
