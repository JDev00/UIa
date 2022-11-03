package uia.core.event;

import java.util.function.Consumer;

/**
 * EventQueue ADT.
 * <br>
 * An EventQueue is a structure used to store generic events
 */

public interface EventQueue<T> {

    /**
     * Remove all the attached events
     */

    EventQueue<T> clear();

    /**
     * Add a new event
     *
     * @param event a new not null event
     */

    EventQueue<T> addEvent(T event);

    /**
     * Replace the specified event
     *
     * @param i     the position of the event to replace
     * @param event a new not null event
     */

    EventQueue<T> setEvent(int i, T event);

    /**
     * Remove the specified event
     *
     * @param i the position of the event to remove
     */

    EventQueue<T> removeEvent(int i);

    /**
     * Remove the given event
     *
     * @param event a not null event to remove
     */

    EventQueue<T> removeEvent(T event);

    /**
     * Apply a function
     *
     * @param consumer a not null {@link Consumer}
     */

    EventQueue<T> apply(Consumer<T> consumer);

    /**
     * @return the number of events
     */

    int size();

    /**
     * @param event a not null event
     * @return the position of the specified event
     */

    int indexOf(T event);
}
