package uia.core.basement;

/**
 * Callable is responsible for storing and notifying a list of {@link Callback}s.
 */

public interface Callable {

    /**
     * Registers a new callback.
     *
     * @param callback a new (not duplicated) {@link Callback}
     * @return a unique ID for the registered callback or -1 if the callback
     * is not registered because it is duplicated
     * @throws NullPointerException if {@code callback == null}
     */

    long registerCallback(Callback<?> callback);

    /**
     * Unregisters the specified callback.
     *
     * @param callbackID the ID of the callback to unregister; if an
     *                   unregistered callback ID is passed, nothing happens
     */

    void unregisterCallback(long callbackID);

    /**
     * Notifies all callbacks of the specified type.
     *
     * @param type the {@link Callback} type
     * @param data an Object to dispatch to the callbacks
     */

    void notifyCallbacks(Class<? extends Callback> type, Object data);

    /**
     * @return the number of registered callbacks
     */

    int numberOfCallbacks();
}
