package uia.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Global utility storage
 */

public final class Storage {
    private static final Storage STORAGE = new Storage();

    private final List<Object> data = new ArrayList<>();

    private Storage() {
    }

    /**
     * Add the given object
     *
     * @param obj a non-null object
     */

    public static void push(Object obj) {
        if (obj != null)
            STORAGE.data.add(obj);
    }

    /**
     * Remove the given object
     *
     * @param o the object to remove
     * @return the removed object
     */

    public static Object pop(Object o) {
        return o == null ? null : STORAGE.data.remove(o);
    }

    /**
     * Remove the given object
     *
     * @param i the position of the object to remove
     * @return the removed object
     */

    public static Object pop(int i) {
        return i >= 0 && i < STORAGE.data.size() ? STORAGE.data.remove(i) : null;
    }

    /**
     * @param i the position of the object inside this storage
     * @return the specified object
     */

    public static Object get(int i) {
        return STORAGE.data.get(i);
    }

    /**
     * @param type     the class to search
     * @param position the position of the class occurrence
     * @return the object whose class matches the given class
     */

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> type, int position) {
        int counter = 0;

        for (Object i : STORAGE.data) {

            if (i.getClass().equals(type)) {

                if (counter == position)
                    return (T) i;

                counter++;
            }
        }

        return null;
    }

    /**
     * @return the last added object
     */

    public static Object peek() {
        return STORAGE.data.get(STORAGE.data.size() - 1);
    }

    /**
     * @return the number of stored elements
     */

    public static int size() {
        return STORAGE.data.size();
    }

    /**
     * @return the first position of the given object
     */

    public static int indexOf(Object object) {
        return STORAGE.data.indexOf(object);
    }

    /**
     * @return the last position of the given object
     */

    public static int lastIndexOf(Object object) {
        return STORAGE.data.lastIndexOf(object);
    }
}
