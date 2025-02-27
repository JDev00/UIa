package uia.application.metrics.monitorable;

/**
 * Monitorable ADT.
 * <br>
 * Monitorable is responsible for monitoring a single value. It is intended to be
 * the basement for system metrics.
 */

public interface Monitorable<T> {

    /**
     * Updates the monitored value.
     *
     * @param value the new monitored value
     */

    void update(T value);

    /**
     * @return the monitored value
     */

    T getValue();
}
