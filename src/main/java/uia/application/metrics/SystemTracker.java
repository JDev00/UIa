package uia.application.metrics;

import uia.application.metrics.monitorable.SystemProperty;
import uia.application.metrics.monitorable.Monitorable;

import java.util.NoSuchElementException;
import java.util.HashMap;
import java.util.Map;

/**
 * The SystemTracker is a service responsible for providing information about
 * the resources used by the running application.
 */

public final class SystemTracker {
    private static final SystemTracker SYSTEM_TRACKER = new SystemTracker();
    private static int seedID = 10;

    private final Map<Integer, Monitorable> defaultProperties;
    private final Map<Integer, Monitorable> externalProperties;

    private SystemTracker() {
        defaultProperties = new HashMap<>();
        externalProperties = new HashMap<>();

        setupDefaultProperties();
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
     * Creates and registers the default properties to be observed.
     */

    private void setupDefaultProperties() {
        for (DefaultSystemProperties property : DefaultSystemProperties.values()) {
            defaultProperties.put(property.getID(), new SystemProperty<>());
        }
    }

    /**
     * @param propertyID the property ID
     * @return the property with the specified ID
     * @throws NoSuchElementException if the propertyID is not associated with a property
     */

    @SuppressWarnings("unchecked")
    private <T> Monitorable<T> getPropertyByID(int propertyID) {
        // 1. searches for the property among those registered
        Monitorable<T> result = externalProperties.get(propertyID);
        // 2. searches for the property in the default ones
        if (result == null) {
            result = defaultProperties.get(propertyID);
        }
        // 3. no property matches the ID
        if (result == null) {
            throw new NoSuchElementException("Property with ID " + propertyID + " does not exist");
        }
        return result;
    }

    /**
     * Registers a new property to watch. Once registered, its value
     * will be accessed with {@link #getPropertyValue(int)} by using
     * the generated ID.
     *
     * @param monitorable a not duplicated monitorable to be registered
     * @return the registered property ID
     * @throws IllegalArgumentException if the monitorable object is duplicated
     */

    public <T> int registerProperty(Monitorable<T> monitorable) {
        if (externalProperties.containsValue(monitorable)) {
            throw new IllegalArgumentException("The monitorable is already registered");
        }

        int propertyID = generateID();
        externalProperties.put(propertyID, monitorable);
        return propertyID;
    }

    /**
     * Unregisters the specified property.
     *
     * @param propertyID the ID of the property to be removed
     * @return this SystemTracker
     * @throws NoSuchElementException if the propertyID is not associated with a property
     */

    public SystemTracker unregisterProperty(int propertyID) {
        if (externalProperties.remove(propertyID) == null) {
            throw new NoSuchElementException("Property with ID " + propertyID + " does not exist");
        }
        return this;
    }

    /**
     * Updates the property value.
     *
     * @param propertyID the property ID
     * @param value      the new property value
     * @return this SystemTracker
     * @throws NoSuchElementException if the propertyID is not associated with a property
     */

    public <T> SystemTracker updatePropertyValue(int propertyID, T value) {
        Monitorable<T> monitorable = getPropertyByID(propertyID);
        monitorable.update(value);
        return this;
    }

    /**
     * @param propertyID the ID of the property for which the value is to be retrieved
     * @return the value of the property
     * @throws NoSuchElementException if the propertyID is not associated with a property
     */

    public <T> T getPropertyValue(int propertyID) {
        Monitorable<T> monitorable = getPropertyByID(propertyID);
        return monitorable.getValue();
    }

    /**
     * @return the unique SystemTracker instance
     */

    static SystemTracker getInstance() {
        return SYSTEM_TRACKER;
    }
}
