package com.adamsvestka.pijl.rerecodex.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

public class Observable<T extends Observable<T>> {
    private List<Consumer<T>> subscribers = new ArrayList<>();

    public void subscribe(Consumer<T> callback) {
        subscribers.add(callback);
    }

    public void unsubscribe(Consumer<T> callback) {
        subscribers.remove(callback);
    }

    @SuppressWarnings("unchecked")
    public void notifySubscribers() {
        for (var subscriber : subscribers) {
            SwingUtilities.invokeLater(() -> subscriber.accept((T) this));
        }
    }
}
