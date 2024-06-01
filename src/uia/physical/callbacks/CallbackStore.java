package uia.physical.callbacks;

import uia.core.basement.Callable;
import uia.core.basement.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * UIa standard {@link Callable} implementation.
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
            String typeName = type.getName();

            callbacks.forEach(callback -> {
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
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
