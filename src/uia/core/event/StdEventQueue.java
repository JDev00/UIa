package uia.core.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Standard {@link EventQueue} implementation
 */

public class StdEventQueue<T> implements EventQueue<T> {
    private final List<T> list;

    public StdEventQueue() {
        list = new ArrayList<>();
    }

    @Override
    public StdEventQueue<T> clear() {
        list.clear();
        return this;
    }

    @Override
    public StdEventQueue<T> addEvent(T event) {
        if (!list.contains(event)) list.add(event);
        return this;
    }

    @Override
    public StdEventQueue<T> setEvent(int i, T event) {
        if (i >= 0 && i < list.size()) list.set(i, event);
        return this;
    }

    @Override
    public StdEventQueue<T> removeEvent(int i) {
        list.remove(i);
        return this;
    }

    @Override
    public StdEventQueue<T> removeEvent(T event) {
        list.remove(event);
        return this;
    }

    @Override
    public StdEventQueue<T> apply(Consumer<T> consumer) {
        for (T i : list) {
            consumer.accept(i);
        }
        return this;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public int indexOf(T event) {
        return list.indexOf(event);
    }
}
