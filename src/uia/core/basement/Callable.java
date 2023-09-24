package uia.core.basement;

/**
 * Callable stores a group of {@link Callback}s.
 */

public interface Callable {

    /**
     * Add a new Callback to this View.
     *
     * @param callback a new (not duplicated) {@link Callback}
     */

    void addCallback(Callback<?> callback);

    /**
     * Remove the given Callback from this View.
     *
     * @param callback the {@link Callback} to remove
     */

    void removeCallback(Callback<?> callback);

    /**
     * Notify the specified Callback types.
     *
     * @param type the {@link Callback} type
     * @param data a not null Object to pass to the callback
     */

    void notifyCallbacks(Class<? extends Callback> type, Object data);
}
