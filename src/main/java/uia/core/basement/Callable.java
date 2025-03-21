package uia.core.basement;

/**
 * Callable ADT.
 * <br>
 * Callable is responsible for storing and notifying callback subscribers.
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
     * Notifies clients who have subscribed to a callback of the specified type.
     *
     * @param type the {@link Callback} type
     * @param data the data to be sent to the clients via the callbacks
     * @throws NullPointerException if {@code callback == null || data == null}
     */

    void notifyCallbacks(Class<? extends Callback> type, Object data);

    /**
     * @return the number of registered callbacks
     */

    int numberOfCallbacks();
}
