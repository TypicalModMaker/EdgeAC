package xyz.edge.ac.packetevents.utils.list;

import java.util.NoSuchElementException;
import java.util.ListIterator;

public class ListItr extends Itr implements ListIterator<E>
{
    ListItr(final int index) {
        this.cursor = index;
    }
    
    @Override
    public boolean hasPrevious() {
        return this.cursor > 0;
    }
    
    @Override
    public int nextIndex() {
        return this.cursor;
    }
    
    @Override
    public int previousIndex() {
        return this.cursor - 1;
    }
    
    @Override
    public E previous() {
        final int i = this.cursor - 1;
        if (i < 0) {
            throw new NoSuchElementException();
        }
        this.cursor = i;
        final ConcurrentList l = this.l;
        final int n = i;
        this.lastRet = n;
        return l.get(n);
    }
    
    @Override
    public void set(final E e) {
        if (this.lastRet < 0) {
            throw new IllegalStateException();
        }
        this.l.set(this.lastRet, e);
        ConcurrentList.this.set(this.lastRet, e);
    }
    
    @Override
    public void add(final E e) {
        final int i = this.cursor;
        this.l.add(i, e);
        ConcurrentList.this.add(i, e);
        this.cursor = i + 1;
        this.lastRet = -1;
    }
}
