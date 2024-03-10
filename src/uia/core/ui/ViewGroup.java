package uia.core.ui;

import java.util.Objects;

/**
 * ViewGroup ADT - ViewGroup is designed to manage a set of Views.
 * <br>
 * <br>
 * <b>Responsibilities</b>
 * <br>
 * A ViewGroup is responsible for managing its children, including their visibility, and sending
 * them the right messages.
 * <br>
 * <br>
 * <b>Children management</b>
 * <br>
 * ViewGroup isolates its children from the external graphical environment: this means that each child
 * is contained by its group and receives the information provided by it.
 * However, it is still possible to let a child communicate with the external environment by sending a message,
 * either from an external view or manually.
 */

public interface ViewGroup extends View, Iterable<View> {

    /**
     * The clip feature ensures that any graphical component outside the group boundaries won't be drawn.
     * This means that for any graphical entity, that isn't completely contained within the group boundaries,
     * only the part that is inside the group boundaries will be displayed.
     *
     * @param clipRegion true to enable clip feature
     */

    void setClip(boolean clipRegion);

    /**
     * @return true if this group has the clip feature enabled
     */

    boolean hasClip();

    /**
     * Inserts a new View to this group.
     * <br>
     * <i>Duplicate views are not allowed.</i>
     *
     * @param index the position where the view should be inserted
     * @param view  the view to insert
     * @return true if the specified View has been added to this group
     * @throws NullPointerException      if {@code view == null}
     * @throws IndexOutOfBoundsException if {@code i < 0 or i >= size()}
     * @throws IllegalArgumentException  if this group already contains the given view
     */

    boolean insert(int index, View view);

    /**
     * Removes the specified View from this group.
     *
     * @param view the view to remove
     * @return true if the specified View has been removed from this group
     */

    boolean remove(View view);

    /**
     * Removes all Views from this group.
     */

    void removeAll();

    /**
     * @return the number of views managed by this group
     */

    int size();

    /**
     * @param index the view position (index) within this group
     * @return the specified view
     * @throws IndexOutOfBoundsException if {@code i < 0 or i >= size()}
     */

    View get(int index);

    /**
     * @param viewID the View ID to look for
     * @return the specified view or null if no view is found
     */

    View get(String viewID);

    /**
     * Returns the position (index) of the specified view within this group.
     *
     * @param view a view to look for
     * @return the view position (index) within this group or -1 if no view is found
     */

    int indexOf(View view);

    /**
     * Returns the boundaries occupied by the views in this group.
     *
     * @return a new array made up of four elements:
     * <ul>
     *     <li>the top left-hand corner on the x-axis;</li>
     *     <li>the top left-hand corner on the y-axis;</li>
     *     <li>width;</li>
     *     <li>height</li>
     * </ul>
     */

    float[] boundsContent();

    /**
     * Inserts the specified view as last element in the specified group.
     *
     * @param group a not null {@link ViewGroup}
     * @param view  a not null {@link View}
     * @throws NullPointerException if {@code group == null || view == null}
     */

    static void insert(ViewGroup group, View view) {
        Objects.requireNonNull(group);
        group.insert(group.size(), view);
    }

    /**
     * Inserts the specified views inside the specified group.
     *
     * @param group a not null {@link ViewGroup}
     * @param views a not null Array of {@link View}s
     * @throws NullPointerException if {@code group == null || views == null || views[i] == null}
     */

    static void insert(ViewGroup group, View... views) {
        Objects.requireNonNull(group);
        Objects.requireNonNull(views);

        for (View view : views) {
            group.insert(group.size(), view);
        }
    }

    /**
     * Removes a View from the specified group.
     *
     * @param group a not null {@link ViewGroup}
     * @param index the position (index), inside the specified group, of the View to remove
     * @throws NullPointerException      if {@code group == null}
     * @throws IndexOutOfBoundsException if {@code index < 0 or index >= group.size()}
     */

    static void remove(ViewGroup group, int index) {
        Objects.requireNonNull(group);
        group.remove(group.get(index));
    }

    /**
     * Removes the first found View from the specified group.
     *
     * @param group  a not null {@link ViewGroup}
     * @param viewID the ID of the View to remove
     * @throws NullPointerException if {@code group == null}
     */

    static void remove(ViewGroup group, String viewID) {
        Objects.requireNonNull(group);
        group.remove(group.get(viewID));
    }
}
