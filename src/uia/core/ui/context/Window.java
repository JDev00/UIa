package uia.core.ui.context;

/**
 * Window ADT.
 * <br>
 * Window is responsible for managing a native window (the one the user sees and interacts with when an application is running).
 * Such a Window can be either system or third-party generated.
 * <br>
 * By default, a window as a specific piece dedicated to rendering. This area is automatically managed and
 * only it's width and height can be accessed externally via {@link #getWidth()} and {@link #getHeight()}.
 */

public interface Window {

    /**
     * Make this Window visible on the screen
     */

    Window show();

    /**
     * Hide this Window
     */

    Window hide();

    /**
     * Set the window to always be on top of other system or third-party windows, even when it isn't in focus.
     *
     * @param alwaysOnTop true to set this window to always be on top
     * @return this {@link Window}
     */

    Window setAlwaysOnTop(boolean alwaysOnTop);

    /**
     * Make the window resizable. It allows the user to resize the window in a canonical way through the GUI.
     *
     * @param resizable true to make this windows resizable
     * @return this {@link Window}
     */

    Window setResizable(boolean resizable);

    /**
     * Set the window title
     *
     * @param title a title; it could be null
     * @return this {@link Window}
     */

    Window setTitle(String title);

    /**
     * Resize this window to the specified width and height
     *
     * @param width  the new window's width; it must be greater than 150 pixels
     * @param height the new window's height; it must be greater than 150 pixels
     * @return this {@link Window}
     */

    Window resize(int width, int height);

    /**
     * @return the width of the drawable area
     */

    int getWidth();

    /**
     * @return the height of the drawable area
     */

    int getHeight();

    /**
     * @return true if this Window is on focus
     */

    boolean isFocused();

    /**
     * @return the window frame insets (title bar height and left and right padding of the drawing area)
     */

    int[] getInsets();
}
