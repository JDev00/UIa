package uia.core.basement;

/**
 * Callable stores a group of {@link Callback}s.
 */

public interface Callable {

    /**
     * Registers a new Callback
     *
     * @param callback a new (not duplicated) {@link Callback}
     */

    void registerCallback(Callback<?> callback);

    /**
     * Unregisters the specified Callback
     *
     * @param callback the {@link Callback} to unregister
     */

    void unregisterCallback(Callback<?> callback);

    /**
     * Notifies the specified Callback type.
     * <br>
     * This operation notifies all callbacks whose type is the specified one.
     *
     * @param type the {@link Callback} type
     * @param data a not null Object to dispatch to the callbacks
     */

    void notifyCallbacks(Class<? extends Callback> type, Object data);
}
