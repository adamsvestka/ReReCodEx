package com.adamsvestka.pijl.rerecodex.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.swing.SwingUtilities;

public class ObservableList<T> extends ArrayList<T> {
    private List<Consumer<ObservableList<T>>> subscribers = new ArrayList<>();

    public void subscribe(Consumer<ObservableList<T>> callback) {
        subscribers.add(callback);
    }

    public void unsubscribe(Consumer<ObservableList<T>> callback) {
        subscribers.remove(callback);
    }

    public void notifySubscribers() {
        for (var subscriber : subscribers) {
            SwingUtilities.invokeLater(() -> subscriber.accept(this));
        }
    }

    @Override
    public T set(int index, T element) {
        T result = super.set(index, element);
        notifySubscribers();
        return result;
    }

    @Override
    public boolean add(T element) {
        boolean result = super.add(element);
        notifySubscribers();
        return result;
    }

    @Override
    public void add(int index, T element) {
        super.add(index, element);
        notifySubscribers();
    }

    @Override
    public T remove(int index) {
        T result = super.remove(index);
        notifySubscribers();
        return result;
    }

    @Override
    public boolean remove(Object element) {
        boolean result = super.remove(element);
        notifySubscribers();
        return result;
    }

    @Override
    public void clear() {
        super.clear();
        notifySubscribers();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean result = super.addAll(c);
        notifySubscribers();
        return result;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        boolean result = super.addAll(index, c);
        notifySubscribers();
        return result;
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
        notifySubscribers();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = super.removeAll(c);
        notifySubscribers();
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean result = super.retainAll(c);
        notifySubscribers();
        return result;
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        boolean result = super.removeIf(filter);
        notifySubscribers();
        return result;
    }

    @Override
    public void sort(Comparator<? super T> c) {
        super.sort(c);
        notifySubscribers();
    }
}
