package uia.core.basement;

/**
 * Event ADT.
 */

public interface Event<T> {

    /**
     * Update this event
     *
     * @param t a not null generic Object
     */

    void update(T t);
}
