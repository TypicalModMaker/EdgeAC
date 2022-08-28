package xyz.edge.ac.wrappers;

import java.util.ListIterator;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class HookedListWrapper<T> extends ListWrapper<T>
{
    public HookedListWrapper(final List<T> base) {
        super(base);
    }
    
    public abstract void onIterator();
    
    @Override
    public int size() {
        return this.base.size();
    }
    
    @Override
    public boolean isEmpty() {
        return this.base.isEmpty();
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.base.contains(o);
    }
    
    @Override
    public Iterator<T> iterator() {
        this.onIterator();
        return this.listIterator();
    }
    
    @Override
    public Object[] toArray() {
        return this.base.toArray();
    }
    
    @Override
    public boolean add(final T o) {
        return this.base.add(o);
    }
    
    @Override
    public boolean remove(final Object o) {
        return this.base.remove(o);
    }
    
    @Override
    public boolean addAll(final Collection c) {
        return this.base.addAll(c);
    }
    
    @Override
    public boolean addAll(final int index, final Collection c) {
        return this.base.addAll(index, c);
    }
    
    @Override
    public void clear() {
        this.base.clear();
    }
    
    @Override
    public T get(final int index) {
        return this.base.get(index);
    }
    
    @Override
    public T set(final int index, final T element) {
        return this.base.set(index, element);
    }
    
    @Override
    public void add(final int index, final T element) {
        this.base.add(index, element);
    }
    
    @Override
    public T remove(final int index) {
        return this.base.remove(index);
    }
    
    @Override
    public int indexOf(final Object o) {
        return this.base.indexOf(o);
    }
    
    @Override
    public int lastIndexOf(final Object o) {
        return this.base.lastIndexOf(o);
    }
    
    @Override
    public ListIterator<T> listIterator() {
        return this.base.listIterator();
    }
    
    @Override
    public ListIterator<T> listIterator(final int index) {
        return this.base.listIterator(index);
    }
    
    @Override
    public List<T> subList(final int fromIndex, final int toIndex) {
        return this.base.subList(fromIndex, toIndex);
    }
    
    @Override
    public boolean retainAll(final Collection c) {
        return this.base.retainAll(c);
    }
    
    @Override
    public boolean removeAll(final Collection c) {
        return this.base.removeAll(c);
    }
    
    @Override
    public boolean containsAll(final Collection c) {
        return this.base.containsAll(c);
    }
    
    @Override
    public Object[] toArray(final Object[] a) {
        return this.base.toArray(a);
    }
}
