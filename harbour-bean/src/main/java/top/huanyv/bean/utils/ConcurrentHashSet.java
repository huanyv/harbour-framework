package top.huanyv.bean.utils;

import javafx.beans.binding.ObjectExpression;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author huanyv
 * @date 2022/10/14 21:27
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> implements Set<E> {

    public static final Object OBJECT = new Object();

    private final ConcurrentHashMap<E, Object> map;

    public ConcurrentHashSet() {
        map = new ConcurrentHashMap<>();
    }

    public ConcurrentHashSet(Collection<? extends E> c) {
        map = new ConcurrentHashMap<>(Math.max((int) (c.size()/.75f) + 1, 16));
        addAll(c);
    }

    public ConcurrentHashSet(int initialCapacity, float loadFactor) {
        map = new ConcurrentHashMap<>(initialCapacity, loadFactor);
    }

    public ConcurrentHashSet(int initialCapacity) {
        map = new ConcurrentHashMap<>(initialCapacity);
    }


    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return map.keySet().toArray(a);
    }

    @Override
    public Object[] toArray() {
        return map.keySet().toArray();
    }

    @Override
    public boolean add(E t) {
        return map.put(t, OBJECT) == null;
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o) == OBJECT;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return map.keySet().retainAll(c);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public String toString() {
        return map.keySet().toString();
    }

    @Override
    public Spliterator<E> spliterator() {
        return map.keySet().spliterator();
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return map.keySet().removeIf(filter);
    }

    @Override
    public Stream<E> stream() {
        return map.keySet().stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return map.keySet().parallelStream();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        map.keySet().forEach(action);
    }

}
