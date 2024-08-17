package uia.application.events;

import uia.core.basement.Callable;
import uia.core.basement.Callback;

import java.util.Objects;
import java.util.HashMap;
import java.util.Map;

/**
 * UIa standard {@link Callable} implementation.
 */

public class CallbackStore implements Callable {
    private final Map<Long, Callback> callbacks;

    public CallbackStore(int size) {
        callbacks = new HashMap<>(size);
    }

    @Override
    public long registerCallback(Callback<?> callback) {
        Objects.requireNonNull(callback, "'null' callbacks are forbidden");

        long resultID = -1;
        if (!callbacks.containsValue(callback)) {
            resultID = System.currentTimeMillis();
            callbacks.put(resultID, callback);
        }
        return resultID;
    }

    @Override
    public void unregisterCallback(long callbackID) {
        callbacks.remove(callbackID);
    }

    @Override
    public void notifyCallbacks(Class<? extends Callback> type, Object data) {
        try {
            String typeName = type.getName();

            for (Map.Entry<Long, Callback> entry : callbacks.entrySet()) {
                Callback callback = entry.getValue();
                Class<?> callbackClass = callback.getClass();

                while (callbackClass != null) {
                    Class<?>[] interfaces = callbackClass.getInterfaces();
                    for (Class<?> superType : interfaces) {
                        String superTypeName = superType.getName();
                        if (typeName.equals(superTypeName)) {
                            callback.update(data);
                        }
                    }
                    callbackClass = callbackClass.getSuperclass();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public int numberOfCallbacks() {
        return callbacks.size();
    }
}
