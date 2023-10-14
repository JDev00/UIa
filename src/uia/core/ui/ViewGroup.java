package uia.core.ui;

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
     * Adds the specified Views to this group.
     * <br>
     * A duplicated View won't be added.
     *
     * @param views a not null array of {@link View}s
     * @throws NullPointerException if {@code views == null}
     */

    void add(View... views);

    /**
     * Adds a new View to this group
     *
     * @param i    the position where to insert the View
     * @param view a new not null {@link View}
     * @throws IndexOutOfBoundsException if {@code i < 0 or i >= size()}
     */

    void add(int i, View view);

    /**
     * Remove the specified View from this group
     *
     * @param view a not null {@link View} to remove
     */

    void remove(View view);

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
     * @param id the View's ID to look for
     * @return the specified View; a null value will be returned if any View can be located
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
     * Returns the boundaries occupied by the group's Views.
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
}
