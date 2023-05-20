package com.adamsvestka.pijl.rerecodex.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.swing.SwingUtilities;

/**
 * This class represents an observable list, or "data" in the model-view
 * paradigm. It can be subclassed to represent a list that the application
 * wants to have observed. An observable list will notify its observers about
 * changes to its state.
 *
 * @param <T> The type of the observable list.
 */
public class ObservableList<T> extends ArrayList<T> {
    private List<Consumer<List<T>>> subscribers = new ArrayList<>();
    private List<Consumer<List<T>>> addSubscribers = new ArrayList<>();
    private List<Consumer<List<T>>> removeSubscribers = new ArrayList<>();

    /**
     * Subscribes a callback that will be executed when the observable updates its
     * state.
     * 
     * @param callback A function that takes the updated observable as an argument.
     *                 This callback will be called when the observable changes its
     *                 state.
     */
    public void subscribe(Consumer<List<T>> callback) {
        subscribers.add(callback);
    }

    /**
     * Subscribes a callback that will be executed when the observable adds an
     * element.
     * 
     * @param callback A function that takes the added element as an argument. This
     *                 callback will be called when the observable adds an element.
     */
    public void subscribeAdd(Consumer<List<T>> callback) {
        addSubscribers.add(callback);
    }

    /**
     * Subscribes a callback that will be executed when the observable removes an
     * element.
     * 
     * @param callback A function that takes the removed element as an argument.
     *                 This callback will be called when the observable removes an
     *                 element.
     */
    public void subscribeRemove(Consumer<List<T>> callback) {
        removeSubscribers.add(callback);
    }

    /**
     * Unsubscribes a callback from this observable, so that it is no longer
     * notified of updates.
     * 
     * @param callback The callback function to remove from the list of subscribers.
     */
    public void unsubscribe(Consumer<List<T>> callback) {
        subscribers.remove(callback);
        addSubscribers.remove(callback);
        removeSubscribers.remove(callback);
    }

    /**
     * Notifies all subscribed callbacks of an update to this observable's state by
     * executing them with an instance of the updated observable. This method should
     * be called within the observable class whenever its state is updated.
     */
    public void notifySubscribers() {
        for (var subscriber : subscribers) {
            SwingUtilities.invokeLater(() -> subscriber.accept(Collections.unmodifiableList(this)));
        }
    }

    /**
     * Notifies all subscribed callbacks of elements added to this observable's
     * state by executing them with an instance of the updated observable. This
     * method should be called within the observable class whenever its state is
     * updated.
     */
    public void notifyAddSubscribers(List<T> added) {
        for (var subscriber : addSubscribers) {
            SwingUtilities.invokeLater(() -> subscriber.accept(added));
        }
    }

    /**
     * Notifies all subscribed callbacks of elements removed from this observable's
     * state by executing them with an instance of the updated observable. This
     * method should be called within the observable class whenever its state is
     * updated.
     */
    public void notifyRemoveSubscribers(List<T> removed) {
        for (var subscriber : removeSubscribers) {
            SwingUtilities.invokeLater(() -> subscriber.accept(removed));
        }
    }

    /** @apiNote This implementation notifies subscribers of the change. */
    @Override
    public T set(int index, T element) {
        notifyRemoveSubscribers(List.of(get(index)));
        T result = super.set(index, element);
        notifyAddSubscribers(List.of(element));
        notifySubscribers();
        return result;
    }

    /** @apiNote This implementation notifies subscribers of the change. */
    @Override
    public boolean add(T element) {
        boolean result = super.add(element);
        notifyAddSubscribers(List.of(element));
        notifySubscribers();
        return result;
    }

    /** @apiNote This implementation notifies subscribers of the change. */
    @Override
    public void add(int index, T element) {
        super.add(index, element);
        notifyAddSubscribers(List.of(element));
        notifySubscribers();
    }

    /** @apiNote This implementation notifies subscribers of the change. */
    @Override
    public T remove(int index) {
        notifyRemoveSubscribers(List.of(get(index)));
        T result = super.remove(index);
        notifySubscribers();
        return result;
    }

    /** @apiNote This implementation notifies subscribers of the change. */
    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object element) {
        notifyRemoveSubscribers(List.of((T) element));
        boolean result = super.remove(element);
        notifySubscribers();
        return result;
    }

    /** @apiNote This implementation notifies subscribers of the change. */
    @Override
    public void clear() {
        notifyRemoveSubscribers(Collections.unmodifiableList(this));
        super.clear();
        notifySubscribers();
    }

    /** @apiNote This implementation notifies subscribers of the change. */
    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean result = super.addAll(c);
        notifyAddSubscribers(List.copyOf(c));
        notifySubscribers();
        return result;
    }

    /** @apiNote This implementation notifies subscribers of the change. */
    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        boolean result = super.addAll(index, c);
        notifyAddSubscribers(List.copyOf(c));
        notifySubscribers();
        return result;
    }

    /** @apiNote This implementation notifies subscribers of the change. */
    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        notifyRemoveSubscribers(subList(fromIndex, toIndex));
        super.removeRange(fromIndex, toIndex);
        notifySubscribers();
    }

    /** @apiNote This implementation notifies subscribers of the change. */
    @Override
    @SuppressWarnings("unchecked")
    public boolean removeAll(Collection<?> c) {
        notifyRemoveSubscribers((List<T>) c.stream().filter(this::contains).toList());
        boolean result = super.removeAll(c);
        notifySubscribers();
        return result;
    }

    /** @apiNote This implementation notifies subscribers of the change. */
    @Override
    public boolean retainAll(Collection<?> c) {
        notifyRemoveSubscribers(stream().filter(e -> !c.contains(e)).toList());
        boolean result = super.retainAll(c);
        notifySubscribers();
        return result;
    }

    /** @apiNote This implementation notifies subscribers of the change. */
    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        notifyRemoveSubscribers(stream().filter(filter).toList());
        boolean result = super.removeIf(filter);
        notifySubscribers();
        return result;
    }

    /** @apiNote This implementation notifies subscribers of the change. */
    @Override
    public void sort(Comparator<? super T> c) {
        super.sort(c);
        notifySubscribers();
    }

    /** @apiNote This implementation returns an unmodifiable list. */
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return Collections.unmodifiableList(super.subList(fromIndex, toIndex));
    }
}
