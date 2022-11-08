package uia.core.event;

import java.util.function.Consumer;

/**
 * EventQueue ADT.
 * <br>
 * An EventQueue is a structure used to store generic events.
 */

public interface EventQueue<T> {

    /**
     * Remove all the attached events
     *
     * @return this EventQueue
     */

    EventQueue<T> clear();

    /**
     * Add a new event
     *
     * @param event a new not null event
     * @return this EventQueue
     */

    EventQueue<T> addEvent(T event);

    /**
     * Replace the specified event
     *
     * @param i     the position of the event to replace
     * @param event a new not null event
     * @return this EventQueue
     */

    EventQueue<T> setEvent(int i, T event);

    /**
     * Remove the specified event
     *
     * @param i the position of the event to remove
     * @return this EventQueue
     */

    EventQueue<T> removeEvent(int i);

    /**
     * Remove the given event
     *
     * @param event a not null event to remove
     * @return this EventQueue
     */

    EventQueue<T> removeEvent(T event);

    /**
     * Visit every queue's event and access to it with a given Consumer
     *
     * @param consumer a not null {@link Consumer} used to access the queue's event
     * @return this EventQueue
     */

    EventQueue<T> forEach(Consumer<T> consumer);

    /**
     * @return the number of events
     */

    int size();

    /**
     * @param event a not null event
     * @return the position of the specified event inside this queue
     */

    int indexOf(T event);

    /**
     * Try to access the given event's type
     *
     * @param queue      a not null {@link EventQueue}
     * @param event      the event's type
     * @param catchEvent a not null {@link Consumer} used to perform an action on the selected event
     * @return true if operation succeed
     */

    static <T> boolean accessEvent(EventQueue<T> queue, Class<?> event, Consumer<T> catchEvent) {
        try {
            String name = event.getName();

            queue.forEach(t -> {
                Class<?> current = t.getClass();

                while (current != null) {
                    Class<?>[] interfaces = current.getInterfaces();

                    for (Class<?> i : interfaces) {

                        if (name.equals(i.getName())) {
                            catchEvent.accept(t);
                            return;
                        }
                    }

                    current = current.getSuperclass();
                }
            });
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }
}
