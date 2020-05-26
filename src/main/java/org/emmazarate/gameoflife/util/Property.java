package org.emmazarate.gameoflife.util;

import org.emmazarate.gameoflife.viewmodel.ApplicationState;
import org.emmazarate.gameoflife.viewmodel.SimpleChangeListener;

import java.util.LinkedList;
import java.util.List;

public class Property<T> {

    private T value;
    private List<SimpleChangeListener<T>> listeners = new LinkedList<>();

    public Property(T value) {
        this.value = value;
    }

    public  Property() {
        this(null);
    }

    public void listen(SimpleChangeListener<T> listener) {
        this.listeners.add(listener);
    }

    public void set(T newValue) {
        this.value = newValue;
        notifyListeners();
    }

    public T get() {
        return this.value;
    }

    private void notifyListeners() {
        for (SimpleChangeListener<T> listener : listeners) {
            listener.valueChanged(this.value);
        }
    }
}
