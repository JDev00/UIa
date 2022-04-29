package uia.core.event;

/**
 * Generic event description
 */

public interface Event<T> {

    /**
     * @param t     a non-null object
     * @param state the event state
     */

    void onEvent(T t, int state);
}
