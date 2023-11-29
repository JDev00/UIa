package uia.physical.callbacks;

import uia.core.basement.Callable;
import uia.core.basement.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * UIa standard {@link Callable} implementation
 */

public class CallbackStore implements Callable {
    private final List<Callback> callbacks;

    public CallbackStore(int size) {
        callbacks = new ArrayList<>(size);
    }

    @Override
    public void registerCallback(Callback<?> callback) {
        if (!callbacks.contains(callback)) {
            callbacks.add(callback);
        }
    }

    @Override
    public void unregisterCallback(Callback<?> callback) {
        callbacks.remove(callback);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void notifyCallbacks(Class<? extends Callback> type, Object data) {
        try {
            String name = type.getName();

            callbacks.forEach(callback -> {
                Class<?> current = callback.getClass();
                while (current != null) {
                    Class<?>[] interfaces = current.getInterfaces();
                    for (Class<?> i : interfaces) {
                        if (name.equals(i.getName())) {
                            callback.update(data);
                        }
                    }
                    current = current.getSuperclass();
                }
            });
        } catch (Exception ignored) {
        }
    }
}
