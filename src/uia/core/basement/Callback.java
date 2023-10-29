package uia.core.basement;

/**
 * Callback ADT.
 * <br>
 * A callback is a function that is called in response to an event.
 */

public interface Callback<T> {

    /**
     * Update this callback when the event happens
     *
     * @param object a not null Object
     */

    void update(T object);
}
