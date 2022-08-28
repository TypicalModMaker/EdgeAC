package xyz.edge.ac.packetevents.utils.immutableset;

import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.com.google.common.collect.ImmutableSet;

class ImmutableSet_7<T> extends ImmutableSetAbstract<T>
{
    private ImmutableSet<T> immutableSet;
    
    public ImmutableSet_7() {
        this.immutableSet = (ImmutableSet<T>)ImmutableSet.builder().build();
    }
    
    public ImmutableSet_7(final List<T> data) {
        this.immutableSet = (ImmutableSet<T>)ImmutableSet.builder().addAll(data).build();
    }
    
    @SafeVarargs
    public ImmutableSet_7(final T... data) {
        final ImmutableSet.Builder<T> builder = (ImmutableSet.Builder<T>)ImmutableSet.builder();
        for (final T value : data) {
            builder.add(value);
        }
        this.immutableSet = (ImmutableSet<T>)builder.build();
    }
    
    @Override
    public boolean contains(final T element) {
        return this.immutableSet.contains(element);
    }
    
    @Override
    public void add(final T element) {
        final List<T> elements = new ArrayList<T>(this.immutableSet);
        this.immutableSet = (ImmutableSet<T>)ImmutableSet.builder().addAll(elements).add(element).build();
    }
    
    @SafeVarargs
    @Override
    public final void addAll(final T... elements) {
        final List<T> localElements = new ArrayList<T>(this.immutableSet);
        this.immutableSet = (ImmutableSet<T>)ImmutableSet.builder().addAll(localElements).addAll(Arrays.asList(elements)).build();
    }
}
