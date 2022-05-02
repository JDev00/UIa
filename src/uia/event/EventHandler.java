package uia.core.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Standard event handler
 */

public class EventHandler<T> {
    private final List<List<Event<T>>> list;
    private final List<Class<?>> eventSet;

    public EventHandler() {
        list = new ArrayList<>();
        eventSet = new ArrayList<>();
    }

    /**
     * @return the event key
     */

    private int getKey(Class<?> event) {
        int index = 0;

        for (List<Event<T>> i : list) {

            if (i.size() > 0) {
                Class<?>[] object = i.get(0).getClass().getInterfaces();

                if (object.length > 0 && object[0].equals(event)) {
                    return index;
                }
            }

            index++;
        }

        return -1;
    }

    /**
     * Remove all events
     */

    public void clear() {
        list.clear();
        eventSet.clear();
    }

    /**
     * Add a new event to this handler
     *
     * @param event a non-null event
     */

    public void add(Event<T> event) {
        if (event != null) {

            Class<?> object = event.getClass().getInterfaces()[0];
            int key = getKey(object);

            if (key == -1) {
                ArrayList<Event<T>> arrayList = new ArrayList<>();
                arrayList.add(event);
                list.add(arrayList);

                eventSet.add(object);
            } else {
                list.get(key).add(event);
            }
        }
    }

    /**
     * Replace a specified event
     *
     * @param eventType the event type
     * @param i         the position of the event to replace
     * @param newEvent  a non-null event used to replace the old one
     */

    public boolean set(Class<?> eventType, int i, Event<T> newEvent) {
        int key = getKey(eventType);

        if (key != -1 && newEvent != null) {
            int size = size(eventType);

            if (i >= 0 && i < size) {
                list.get(key).set(i, newEvent);
                return true;
            }
        }

        return false;
    }

    /**
     * Remove the specified event
     *
     * @param eventType the event type
     * @param i         the index of the event to remove
     */

    public void remove(Class<?> eventType, int i) {
        int key = getKey(eventType);

        if (key != -1) {
            int size = size(eventType);

            if (i >= 0 && i < size) {
                list.get(key).remove(i);

                if (list.get(key).size() == 0) {
                    eventSet.remove(key);
                }
            }
        }
    }

    /**
     * Remove the specified set of events
     *
     * @param eventType the event type to remove
     */

    public void remove(Class<?> eventType) {
        int key = getKey(eventType);

        if (key != -1) {
            list.remove(key);
            eventSet.remove(key);
        }
    }

    /**
     * Update the specified set of events
     *
     * @param event the event type
     * @param t     an object
     * @param state the event state
     */

    public void update(Class<?> event, T t, int state) {
        int key = getKey(event);

        if (key != -1) {
            List<Event<T>> events = list.get(key);

            for (Event<T> i : events) {
                i.onEvent(t, state);
            }
        }
    }

    /**
     * @return the number of different events
     */

    public final int events() {
        return list.size();
    }

    /**
     * @param event the event type
     * @return the number of events of the specified type
     */

    public final int size(Class<?> event) {
        int key = getKey(event);
        return (key == -1) ? 0 : list.get(key).size();
    }

    /**
     * @return the set of events
     */

    public final List<Class<?>> getEventSet() {
        return eventSet;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(1000);

        for (Class<?> i : eventSet) {
            builder.append("\n").append(i).append(", size=").append(size(i));
        }

        return "EventHandler{" +
                "number of event of different type=" + events() +
                "\nevent type=" + builder +
                '}';
    }
}
