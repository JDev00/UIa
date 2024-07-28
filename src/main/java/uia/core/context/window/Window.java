package uia.core.context.window;

import uia.core.basement.Callable;

/**
 * Window ADT.
 * <br>
 * Window is responsible for managing a native window (the one the user sees and interacts with
 * when an application is running). Such a Window can be either system or third-party generated.
 * <br>
 * A window has a specific part dedicated to rendering things. This area is automatically managed;
 * by design only it's width and height can be accessed externally via {@link #getViewportWidth()}
 * and {@link #getViewportHeight()}.
 */

public interface Window extends Callable {

    /**
     * Shows or hides this window.
     *
     * @param visible true to make this Window visible
     * @return this Window
     */

    Window setVisible(boolean visible);

    /**
     * @return true if this Window is visible
     */

    boolean isVisible();

    /**
     * Sets this window to stay always on top of other system or third-party windows, even when it isn't in focus.
     *
     * @param alwaysOnTop true to keep this window always be on top
     * @return this Window
     */

    Window setAlwaysOnTop(boolean alwaysOnTop);

    /**
     * Makes this window resizable. This functionality allows the user to resize the window via the GUI.
     *
     * @param resizable true to make this windows resizable
     * @return this Window
     */

    Window setResizable(boolean resizable);

    /**
     * Sets the window title.
     *
     * @param title a title; it could be null
     * @return this Window
     */

    Window setTitle(String title);

    /**
     * Resizes this window to the specified width and height.
     *
     * @param width  the new window width; it must be greater than 200 pixels
     * @param height the new window height; it must be greater than 200 pixels
     * @return this Window
     * @throws IllegalArgumentException if {@code width <= 200 or height <= 200}
     */

    Window resize(int width, int height);

    /**
     * @return the width of the area in which the GUI components can be seen
     */

    int getViewportWidth();

    /**
     * @return the height of the area in which the GUI components can be seen
     */

    int getViewportHeight();

    /**
     * @return true if this window is on focus
     */

    boolean isFocused();

    /**
     * @return the window frame insets:
     * <ul>
     *     <li>title bar height</li>
     *     <li>viewport left and right padding</li>
     * </ul>
     */

    int[] getInsets();
}
