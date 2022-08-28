package xyz.edge.ac.packetevents.utils.list;

import java.util.ListIterator;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import java.util.Iterator;
import java.util.List;

public abstract class ListWrapper implements List
{
    private final List list;
    
    public ListWrapper(final List inputList) {
        this.list = inputList;
    }
    
    public abstract void processAdd(final Object p0);
    
    public List getOriginalList() {
        return this.list;
    }
    
    @Override
    public int size() {
        synchronized (this) {
            return this.list.size();
        }
    }
    
    @Override
    public boolean isEmpty() {
        synchronized (this) {
            return this.list.isEmpty();
        }
    }
    
    @Override
    public boolean contains(final Object o) {
        synchronized (this) {
            return this.list.contains(o);
        }
    }
    
    @NotNull
    @Override
    public Iterator iterator() {
        synchronized (this) {
            return this.listIterator();
        }
    }
    
    @Override
    public Object[] toArray() {
        synchronized (this) {
            return this.list.toArray();
        }
    }
    
    @Override
    public boolean add(final Object o) {
        this.processAdd(o);
        synchronized (this) {
            return this.list.add(o);
        }
    }
    
    @Override
    public boolean remove(final Object o) {
        synchronized (this) {
            return this.list.remove(o);
        }
    }
    
    @Override
    public boolean addAll(final Collection c) {
        for (final Object o : c) {
            this.processAdd(o);
        }
        synchronized (this) {
            return this.list.addAll(c);
        }
    }
    
    @Override
    public boolean addAll(final int index, final Collection c) {
        for (final Object o : c) {
            this.processAdd(o);
        }
        synchronized (this) {
            return this.list.addAll(index, c);
        }
    }
    
    @Override
    public void clear() {
        synchronized (this) {
            this.list.clear();
        }
    }
    
    @Override
    public Object get(final int index) {
        synchronized (this) {
            return this.list.get(index);
        }
    }
    
    @Override
    public Object set(final int index, final Object element) {
        synchronized (this) {
            return this.list.set(index, element);
        }
    }
    
    @Override
    public void add(final int index, final Object element) {
        synchronized (this) {
            this.list.add(index, element);
        }
    }
    
    @Override
    public Object remove(final int index) {
        synchronized (this) {
            return this.list.remove(index);
        }
    }
    
    @Override
    public int indexOf(final Object o) {
        synchronized (this) {
            return this.list.indexOf(o);
        }
    }
    
    @Override
    public int lastIndexOf(final Object o) {
        synchronized (this) {
            return this.list.lastIndexOf(o);
        }
    }
    
    @NotNull
    @Override
    public ListIterator listIterator() {
        synchronized (this) {
            return this.list.listIterator();
        }
    }
    
    @NotNull
    @Override
    public ListIterator listIterator(final int index) {
        synchronized (this) {
            return this.list.listIterator(index);
        }
    }
    
    @NotNull
    @Override
    public List subList(final int fromIndex, final int toIndex) {
        synchronized (this) {
            return this.list.subList(fromIndex, toIndex);
        }
    }
    
    @Override
    public boolean retainAll(@NotNull final Collection c) {
        synchronized (this) {
            return this.list.retainAll(c);
        }
    }
    
    @Override
    public boolean removeAll(@NotNull final Collection c) {
        synchronized (this) {
            return this.list.removeAll(c);
        }
    }
    
    @Override
    public boolean containsAll(@NotNull final Collection c) {
        synchronized (this) {
            return this.list.containsAll(c);
        }
    }
    
    @Override
    public Object[] toArray(final Object[] a) {
        synchronized (this) {
            return this.list.toArray(a);
        }
    }
}
