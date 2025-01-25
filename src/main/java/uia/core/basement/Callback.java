package uia.core.basement;

/**
 * Callback ADT.
 * <br>
 * Function called in response to an event.
 */

@FunctionalInterface
public interface Callback<T> {

    /**
     * Updates this callback when the event occurs.
     *
     * @param object the event payload
     */

    void update(T object);
}
