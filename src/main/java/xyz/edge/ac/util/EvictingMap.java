package xyz.edge.ac.util;

import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.Map;
import java.util.Deque;
import java.util.HashMap;

public final class EvictingMap<K, V> extends HashMap<K, V>
{
    private final int size;
    private final Deque<K> storedKeys;
    
    @Override
    public boolean remove(final Object key, final Object value) {
        this.storedKeys.remove(key);
        return super.remove(key, value);
    }
    
    @Override
    public V putIfAbsent(final K key, final V value) {
        if (!this.storedKeys.contains(key) || !this.get(key).equals(value)) {
            this.checkAndRemove();
        }
        return super.putIfAbsent(key, value);
    }
    
    @Override
    public V put(final K key, final V value) {
        this.checkAndRemove();
        this.storedKeys.addLast(key);
        return super.put(key, value);
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        m.forEach(this::put);
    }
    
    @Override
    public void clear() {
        this.storedKeys.clear();
        super.clear();
    }
    
    @Override
    public V remove(final Object key) {
        this.storedKeys.remove(key);
        return super.remove(key);
    }
    
    private boolean checkAndRemove() {
        if (this.storedKeys.size() >= this.size) {
            final K key = this.storedKeys.removeFirst();
            this.remove(key);
            return true;
        }
        return false;
    }
    
    public EvictingMap(final int size) {
        this.storedKeys = new LinkedList<K>();
        this.size = size;
    }
    
    public int getSize() {
        return this.size;
    }
}
