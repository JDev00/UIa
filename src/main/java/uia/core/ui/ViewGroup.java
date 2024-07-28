package uia.core.ui;

import uia.core.basement.message.Message;

import java.util.Objects;

/**
 * ViewGroup ADT - ViewGroup is designed to manage a list of Views.
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
     * Requests or removes the focus of the group.
     * <br>
     * Note: when a group loses focus, its children also lose focus, but when it regains focus,
     * its children are not automatically set to focus.
     * <br>
     * A group can only interact directly with the user when it has focus.
     *
     * @param request true to request to this group to put on focus; false to remove focus
     * @implSpec <ul>
     * <li>focus can't be requested if a group is not visible;</li>
     * <li>when the group focus is removed, the children focus must also be removed.</li>
     * </ul>
     */

    @Override
    void requestFocus(boolean request);

    /**
     * Makes this group visible or not visible.
     * <br>
     * Note: <i>a group that becomes invisible loses its focus and all its children lose their focus.</i>
     *
     * @param visible true to make this group visible; false otherwise
     * @implSpec when set to invisible, a group must lose focus and all its children must lose their focus.
     */
    @Override
    void setVisible(boolean visible);

    /**
     * Reads the specified message and dispatches it to the children.
     *
     * @param message a message to be read and sent to the children
     * @throws NullPointerException if {@code message == null}
     * @implSpec the specified message must also be sent to the children.
     */

    @Override
    void readMessage(Message message);

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
     * Adds a new view to this group. Note that <i>duplicate views are not allowed.</i>
     *
     * @param index the position where the view should be added
     * @param view  the view to be added
     * @return true if the specified View has been added to this group
     * @throws NullPointerException      if {@code view == null}
     * @throws IndexOutOfBoundsException if {@code index < 0 or index > size()}
     */

    boolean insert(int index, View view);

    /**
     * Removes the specified view from this group.
     *
     * @param view the view to remove
     * @return true if the specified view has been removed from this group
     */

    boolean remove(View view);

    /**
     * Removes all views from this group.
     */

    void removeAll();

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
     * @return the number of views managed by this group
     */

    int size();

    /**
     * Returns the position (index) of the specified view within this group.
     *
     * @param view a view to look for
     * @return the view position (index) within this group or -1 if no view is found
     */

    int indexOf(View view);

    /**
     * Returns the view at the specified position (index).
     *
     * @param index the view position (index) within this group
     * @return the specified view or null if no view is found
     */

    View get(int index);

    /**
     * @param viewID the view ID to look for
     * @return the specified view or null if no view is found
     */

    View get(String viewID);

    /**
     * Inserts the specified view as last element in the specified group.
     *
     * @param group the group to which the view will be added
     * @param view  the view to be added
     * @throws NullPointerException if {@code group == null || view == null}
     */

    static void insert(ViewGroup group, View view) {
        Objects.requireNonNull(group);
        group.insert(group.size(), view);
    }

    /**
     * Inserts the specified views inside the specified group.
     *
     * @param group the group to which the views will be added
     * @param views the views to be added
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
     * Removes a view from the specified group.
     *
     * @param group the group to which the view will be removed
     * @param index the position (index) of the view to be removed within the group
     * @throws NullPointerException if {@code group == null}
     */

    static void remove(ViewGroup group, int index) {
        Objects.requireNonNull(group);

        View viewToRemove = group.get(index);
        group.remove(viewToRemove);
    }

    /**
     * Removes the first found view from the specified group.
     *
     * @param group  the group to which the view will be removed
     * @param viewID the ID of the view to remove
     * @throws NullPointerException if {@code group == null}
     */

    static void remove(ViewGroup group, String viewID) {
        Objects.requireNonNull(group);
        group.remove(group.get(viewID));
    }
}
