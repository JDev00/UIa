package uia.application.metrics;

import uia.application.metrics.monitorable.Monitorable;

/**
 * Default implementation of {@link Monitorable}.
 */

public class SystemProperty<T> implements Monitorable<T> {
    private T value = null;

    @Override
    public void update(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return this.value;
    }
}
