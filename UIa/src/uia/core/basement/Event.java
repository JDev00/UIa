package uia.core.basement;

/**
 * Generic parameterized Event.
 */

public interface Event<T, K> {

    /**
     * Update this event
     *
     * @param t a not null generic Object
     * @param k a not null generic Object
     */

    void update(T t, K k);
}
