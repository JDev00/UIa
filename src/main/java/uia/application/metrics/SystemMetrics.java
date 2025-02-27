package uia.application.metrics;

import uia.application.metrics.monitorable.Monitorable;

import java.util.NoSuchElementException;
import java.util.HashMap;
import java.util.Map;

/**
 * SystemMetrics is a service responsible for providing information about
 * the resources used by the running application.
 */

public final class SystemMetrics {
    private static final SystemMetrics SYSTEM_METRICS = new SystemMetrics();
    private static int seedID = 10;

    private final Map<Integer, Monitorable> defaultMetrics;
    private final Map<Integer, Monitorable> externalMetrics;

    private SystemMetrics() {
        defaultMetrics = new HashMap<>();
        externalMetrics = new HashMap<>();

        setupDefaultMetrics();
    }

    /**
     * Generates a new ID. Each call to this function guarantees
     * the generation of a new ID.
     *
     * @return the generated ID
     */

    private static int generateID() {
        seedID++;
        return seedID;
    }

    /**
     * Creates and registers the default metrics.
     */

    private void setupDefaultMetrics() {
        for (DefaultSystemMetrics metric : DefaultSystemMetrics.values()) {
            defaultMetrics.put(metric.getID(), null);
        }
    }

    /**
     * @param metricID the metric ID
     * @return the metric with the specified ID
     * @throws NoSuchElementException if the metricID is not associated with a metric
     */

    @SuppressWarnings("unchecked")
    private <T> Monitorable<T> getMetricByID(int metricID) {
        // 1. searches for the metric among those registered
        Monitorable<T> result = externalMetrics.get(metricID);
        // 2. searches for the metric in the default ones
        if (result == null) {
            result = defaultMetrics.get(metricID);
        }
        // 3. no metric matches the ID
        if (result == null) {
            throw new NoSuchElementException("Metric with ID " + metricID + " does not exist");
        }
        return result;
    }

    /**
     * Registers a new monitorable object. Once registered, its value
     * will be accessed with {@link #getMetricValue(int)} by using
     * the generated ID.
     *
     * @param monitorable a not duplicated monitorable to be registered
     * @return the registered metric ID
     * @throws IllegalArgumentException if the monitorable object is duplicated
     */

    public <T> int registerMetric(Monitorable<T> monitorable) {
        if (externalMetrics.containsValue(monitorable)) {
            throw new IllegalArgumentException("The monitorable is already registered");
        }

        int metricID = generateID();
        externalMetrics.put(metricID, monitorable);
        return metricID;
    }

    /**
     * Unregisters the specified metric.
     *
     * @param metricID the ID of the metric to be removed
     * @return this SystemMetrics
     * @throws NoSuchElementException if the metricID is not associated with a metric
     */

    public SystemMetrics unregisterMetric(int metricID) {
        if (externalMetrics.remove(metricID) == null) {
            throw new NoSuchElementException("Metric with ID " + metricID + " does not exist");
        }
        return this;
    }

    /**
     * Updates the metric with the given value.
     *
     * @param metricID the metric ID
     * @param value    the new metric value
     * @return this SystemMetrics
     * @throws NoSuchElementException if the metricID is not associated with a metric
     */

    public <T> SystemMetrics updateMetric(int metricID, T value) {
        Monitorable<T> monitorable = getMetricByID(metricID);
        monitorable.update(value);
        return this;
    }

    /**
     * @param metricID the ID of the metric for which the value is to be retrieved
     * @return the value of the requested metric
     * @throws NoSuchElementException if the metricID is not associated with a metric
     */

    public <T> T getMetricValue(int metricID) {
        Monitorable<T> monitorable = getMetricByID(metricID);
        return monitorable.getValue();
    }

    /**
     * @return the unique SystemMetrics instance
     */

    static SystemMetrics getInstance() {
        return SYSTEM_METRICS;
    }
}
