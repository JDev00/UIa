package uia.core.basement;

/**
 * Generic parameterized Event.
 */

public interface Event<T> {

    /**
     * Update this event
     *
     * @param t a not null generic Object
     */

    void update(T t);
}
