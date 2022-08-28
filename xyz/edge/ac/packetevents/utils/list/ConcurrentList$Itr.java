package xyz.edge.ac.packetevents.utils.list;

import java.util.NoSuchElementException;
import java.util.Iterator;

private class Itr implements Iterator<E>
{
    final ConcurrentList l;
    protected int cursor;
    protected int lastRet;
    
    public Itr() {
        this.cursor = 0;
        this.lastRet = -1;
        this.l = (ConcurrentList)ConcurrentList.this.clone();
    }
    
    @Override
    public boolean hasNext() {
        return this.cursor < this.l.size();
    }
    
    @Override
    public E next() {
        final int i = this.cursor;
        if (i >= this.l.size()) {
            throw new NoSuchElementException();
        }
        this.cursor = i + 1;
        final ConcurrentList l = this.l;
        final int n = i;
        this.lastRet = n;
        return l.get(n);
    }
    
    @Override
    public void remove() {
        if (this.lastRet < 0) {
            throw new IllegalStateException();
        }
        this.l.remove(this.lastRet);
        ConcurrentList.this.remove(this.lastRet);
        this.cursor = this.lastRet;
        this.lastRet = -1;
    }
}
