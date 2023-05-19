package com.adamsvestka.pijl.rerecodex.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.swing.SwingUtilities;

public class ObservableList<T> extends ArrayList<T> {
    private List<Consumer<List<T>>> subscribers = new ArrayList<>();
    private List<Consumer<List<T>>> addSubscribers = new ArrayList<>();
    private List<Consumer<List<T>>> removeSubscribers = new ArrayList<>();

    public void subscribe(Consumer<List<T>> callback) {
        subscribers.add(callback);
    }

    public void subscribeAdd(Consumer<List<T>> callback) {
        addSubscribers.add(callback);
    }

    public void subscribeRemove(Consumer<List<T>> callback) {
        removeSubscribers.add(callback);
    }

    public void unsubscribe(Consumer<List<T>> callback) {
        subscribers.remove(callback);
        addSubscribers.remove(callback);
        removeSubscribers.remove(callback);
    }

    public void notifySubscribers() {
        for (var subscriber : subscribers) {
            SwingUtilities.invokeLater(() -> subscriber.accept(Collections.unmodifiableList(this)));
        }
    }

    public void notifyAddSubscribers(List<T> added) {
        for (var subscriber : addSubscribers) {
            SwingUtilities.invokeLater(() -> subscriber.accept(added));
        }
    }

    public void notifyRemoveSubscribers(List<T> removed) {
        for (var subscriber : removeSubscribers) {
            SwingUtilities.invokeLater(() -> subscriber.accept(removed));
        }
    }

    @Override
    public T set(int index, T element) {
        notifyRemoveSubscribers(List.of(get(index)));
        T result = super.set(index, element);
        notifyAddSubscribers(List.of(element));
        notifySubscribers();
        return result;
    }

    @Override
    public boolean add(T element) {
        boolean result = super.add(element);
        notifyAddSubscribers(List.of(element));
        notifySubscribers();
        return result;
    }

    @Override
    public void add(int index, T element) {
        super.add(index, element);
        notifyAddSubscribers(List.of(element));
        notifySubscribers();
    }

    @Override
    public T remove(int index) {
        notifyRemoveSubscribers(List.of(get(index)));
        T result = super.remove(index);
        notifySubscribers();
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object element) {
        notifyRemoveSubscribers(List.of((T) element));
        boolean result = super.remove(element);
        notifySubscribers();
        return result;
    }

    @Override
    public void clear() {
        notifyRemoveSubscribers(Collections.unmodifiableList(this));
        super.clear();
        notifySubscribers();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean result = super.addAll(c);
        notifyAddSubscribers(List.copyOf(c));
        notifySubscribers();
        return result;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        boolean result = super.addAll(index, c);
        notifyAddSubscribers(List.copyOf(c));
        notifySubscribers();
        return result;
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        notifyRemoveSubscribers(subList(fromIndex, toIndex));
        super.removeRange(fromIndex, toIndex);
        notifySubscribers();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean removeAll(Collection<?> c) {
        notifyRemoveSubscribers((List<T>) c.stream().filter(this::contains).toList());
        boolean result = super.removeAll(c);
        notifySubscribers();
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        notifyRemoveSubscribers(stream().filter(e -> !c.contains(e)).toList());
        boolean result = super.retainAll(c);
        notifySubscribers();
        return result;
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        notifyRemoveSubscribers(stream().filter(filter).toList());
        boolean result = super.removeIf(filter);
        notifySubscribers();
        return result;
    }

    @Override
    public void sort(Comparator<? super T> c) {
        super.sort(c);
        notifySubscribers();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return Collections.unmodifiableList(super.subList(fromIndex, toIndex));
    }
}
