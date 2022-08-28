package xyz.edge.ac.packetevents.utils.list;

import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.ListIterator;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;

public class ConcurrentList<E> extends ArrayList<E>
{
    private final Object lock;
    
    public ConcurrentList() {
        this.lock = new Object();
    }
    
    @Override
    public boolean add(final E e) {
        synchronized (this.lock) {
            return super.add(e);
        }
    }
    
    @Override
    public void add(final int index, final E element) {
        synchronized (this.lock) {
            super.add(index, element);
        }
    }
    
    @Override
    public boolean addAll(final Collection<? extends E> c) {
        synchronized (this.lock) {
            return super.addAll(c);
        }
    }
    
    @Override
    public boolean addAll(final int index, final Collection<? extends E> c) {
        synchronized (this.lock) {
            return super.addAll(index, c);
        }
    }
    
    @Override
    public void clear() {
        synchronized (this.lock) {
            super.clear();
        }
    }
    
    @Override
    public Object clone() {
        synchronized (this.lock) {
            try {
                final ConcurrentList<E> clist = (ConcurrentList<E>)super.clone();
                clist.modCount = 0;
                final Field f = ArrayList.class.getDeclaredField("elementData");
                f.setAccessible(true);
                f.set(clist, Arrays.copyOf((Object[])f.get(this), this.size()));
                return clist;
            }
            catch (final ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    @Override
    public boolean contains(final Object o) {
        synchronized (this.lock) {
            return super.contains(o);
        }
    }
    
    @Override
    public void ensureCapacity(final int minCapacity) {
        synchronized (this.lock) {
            super.ensureCapacity(minCapacity);
        }
    }
    
    @Override
    public E get(final int index) {
        synchronized (this.lock) {
            return super.get(index);
        }
    }
    
    @Override
    public int indexOf(final Object o) {
        synchronized (this.lock) {
            return super.indexOf(o);
        }
    }
    
    @Override
    public int lastIndexOf(final Object o) {
        synchronized (this.lock) {
            return super.lastIndexOf(o);
        }
    }
    
    @Override
    public E remove(final int index) {
        synchronized (this.lock) {
            return super.remove(index);
        }
    }
    
    @Override
    public boolean remove(final Object o) {
        synchronized (this.lock) {
            return super.remove(o);
        }
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        synchronized (this.lock) {
            return super.removeAll(c);
        }
    }
    
    @Override
    public boolean retainAll(final Collection<?> c) {
        synchronized (this.lock) {
            return super.retainAll(c);
        }
    }
    
    @Override
    public E set(final int index, final E element) {
        synchronized (this.lock) {
            return super.set(index, element);
        }
    }
    
    @NotNull
    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        synchronized (this.lock) {
            return super.subList(fromIndex, toIndex);
        }
    }
    
    @Override
    public Object[] toArray() {
        synchronized (this.lock) {
            return super.toArray();
        }
    }
    
    @Override
    public <T> T[] toArray(final T[] a) {
        synchronized (this.lock) {
            return super.toArray(a);
        }
    }
    
    @Override
    public void trimToSize() {
        synchronized (this.lock) {
            super.trimToSize();
        }
    }
    
    @NotNull
    @Override
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }
    
    @NotNull
    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }
    
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
}
