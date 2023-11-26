package uia.core.ui;

import java.util.Objects;

/**
 * ViewGroup ADT.
 * <br>
 * ViewGroup is designed to manage a set of Views.
 */

public interface ViewGroup extends View, Iterable<View> {

    /**
     * Clip functionality ensures that every graphical component, that lays outside the View boundaries, isn't drawn.
     * <br>
     * <b>Some notes:</b>
     * <ul>
     *     <li>clip function could be expensive in term of CPU/GPU cycles, so use it only when really needed;</li>
     *     <li>for every graphical entity, that isn't totally contained inside the View boundaries, will be
     *     displayed only the piece that lays inside the View boundaries.</li>
     * </ul>
     *
     * @param clipRegion true to enable clip functionality
     */

    void setClip(boolean clipRegion);

    /**
     * @return true if this View has the clip functionality enabled
     */

    boolean hasClip();

    /**
     * Inserts a new View to this group
     *
     * @param i    the position where to insert the View
     * @param view a new not null {@link View}
     * @return true if the specified View has been added to this group
     * @throws IndexOutOfBoundsException if {@code i < 0 or i >= size()}
     * @throws NullPointerException      if {@code view == null}
     */

    boolean insert(int i, View view);

    /**
     * Removes the specified View from this group
     *
     * @param view a not null {@link View} to remove
     * @return true if the specified View has been removed from this group
     */

    boolean remove(View view);

    /**
     * Removes all Views from this group
     */

    void removeAll();

    /**
     * @return the number of Views inside this group
     */

    int size();

    /**
     * @param i the View position inside this group
     * @return the specified View
     * @throws IndexOutOfBoundsException if {@code i < 0 or i >= size()}
     */

    View get(int i);

    /**
     * @param id the View ID to look for
     * @return the specified View; a null value is returned if no View is found
     */

    View get(String id);

    /**
     * Returns the position of the specified View inside this group
     *
     * @param view a {@link View}
     * @return the view position inside this group otherwise -1
     */

    int indexOf(View view);

    /**
     * Returns the boundaries occupied by the group Views.
     *
     * @return a new array made up of four elements:
     * <ul>
     *     <li>left top corner along x-axis;</li>
     *     <li>left top corner along y-axis;</li>
     *     <li>width;</li>
     *     <li>height</li>
     * </ul>
     */

    float[] boundsContent();

    /**
     * Inserts the specified view as last element in the specified group
     *
     * @param group a not null {@link ViewGroup}
     * @param view  a not null {@link View}
     * @throws NullPointerException if {@code group == null}
     */

    static void insert(ViewGroup group, View view) {
        Objects.requireNonNull(group);
        group.insert(group.size(), view);
    }

    /**
     * Inserts the specified views inside the specified group
     *
     * @param group a not null {@link ViewGroup}
     * @param views a not null Array of {@link View}s
     * @throws NullPointerException if {@code group == null or views == null or views[i] == null}
     */

    static void insert(ViewGroup group, View... views) {
        Objects.requireNonNull(group);
        Objects.requireNonNull(views);
        for (View view : views) {
            group.insert(group.size(), view);
        }
    }

    /**
     * Removes a View from the specified group
     *
     * @param group a not null {@link ViewGroup}
     * @param i     the position, inside the specified group, of the View to remove
     * @throws NullPointerException      if {@code group == null}
     * @throws IndexOutOfBoundsException if {@code i < 0 or i >= group.size()}
     */

    static void remove(ViewGroup group, int i) {
        Objects.requireNonNull(group);
        group.remove(group.get(i));
    }

    /**
     * Removes the first found View from the specified group
     *
     * @param group a not null {@link ViewGroup}
     * @param id    the id of the View to remove
     * @throws NullPointerException if {@code group == null}
     */

    static void remove(ViewGroup group, String id) {
        Objects.requireNonNull(group);
        group.remove(group.get(id));
    }
}
