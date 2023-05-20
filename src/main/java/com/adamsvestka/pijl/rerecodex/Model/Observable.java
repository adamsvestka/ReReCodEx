package com.adamsvestka.pijl.rerecodex.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

/**
 * This class represents an observable object, or "data" in the model-view
 * paradigm. It can be subclassed to represent an object that the application
 * wants to have observed. An observable class will notify its observers about
 * changes to its state.
 *
 * @param <T> The type of the observable object.
 */
public class Observable<T extends Observable<T>> {
    private List<Consumer<T>> subscribers = new ArrayList<>();

    /**
     * Subscribes a callback that will be executed when the observable updates its
     * state.
     * 
     * @param callback A function that takes the updated observable as an argument.
     *                 This callback will be called when the observable changes its
     *                 state.
     */
    public void subscribe(Consumer<T> callback) {
        subscribers.add(callback);
    }

    /**
     * Unsubscribes a callback from this observable, so that it is no longer
     * notified of updates.
     * 
     * @param callback The callback function to remove from the list of subscribers.
     */
    public void unsubscribe(Consumer<T> callback) {
        subscribers.remove(callback);
    }

    /**
     * Notifies all subscribed callbacks of an update to this observable's state by
     * executing them with an instance of the updated observable. This method should
     * be called within the observable class whenever its state is updated.
     */
    @SuppressWarnings("unchecked")
    public void notifySubscribers() {
        for (var subscriber : subscribers) {
            SwingUtilities.invokeLater(() -> subscriber.accept((T) this));
        }
    }
}