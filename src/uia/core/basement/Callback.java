package uia.core.basement;

/**
 * Callback ADT.
 * <br>
 * A callback is a function invoked in response to an appropriate event.
 */

public interface Callback<T> {

    /**
     * Update this callback when the event happens
     *
     * @param t a not null Object
     */

    void onEvent(T t);
}
