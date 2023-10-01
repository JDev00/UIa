package uia.core.ui;

/**
 * Context is the base of the UIa framework. It is responsible for updating and rendering a single {@link View} on a {@link Window}.
 * It is intended to be a versatile layer whose implementation is platform dependent.
 * <br>
 * More description to come.
 */

public interface Context {

    /**
     * HINTS are used to change rendering settings
     */

    enum HINT {
        ANTIALIASING_ON, ANTIALIASING_OFF,
        ANTIALIASING_TEXT_ON, ANTIALIASING_TEXT_OFF,
        RENDER_QUALITY_COLOR_HIGH, RENDER_QUALITY_COLOR_LOW
    }

    /**
     * Clipboard operations
     */

    enum CLIPBOARD_OPERATION {COPY, PASTE}

    /**
     * Start this Context and make the managed Window visible. If the Context was previously stopped, then
     * resume the execution.
     */

    void start();

    /**
     * @return true if this Context is running
     */

    boolean isRunning();

    /**
     * Stop this Context, which causes the View to stop refreshing and rendering
     */

    void stop();

    /**
     * Set the given rendering hints
     *
     * @param hint a not null array of {@link HINT}s
     */

    void setHints(HINT... hint);

    /**
     * Attach a View to this Context.
     * <br>
     * The given View will be updated and rendered by this Context.
     *
     * @param view a {@link View}; it could be null
     */

    void setView(View view);

    /**
     * @return the current frame rendering rate (frame rate per second)
     */

    int getFrameRate();

    /**
     * @return the {@link Window} used to draw the attached View
     */

    Window getWindow();

    /**
     * Copy a String in the clipboard or paste a String from the clipboard
     *
     * @param operation a not null {@link CLIPBOARD_OPERATION}
     * @param str       a String to copy in the clipboard; it could be null
     * @return null or the clipboard content
     */

    String clipboard(CLIPBOARD_OPERATION operation, String str);

    /**
     * Window ADT.
     * <br>
     * Window is responsible for managing a native window (the one the user sees and interacts with when an application is running).
     * Such a Window can be either system or third-party generated.
     * <br>
     * By default, a window as a specific piece dedicated to rendering. This area is automatically managed and
     * only it's width and height can be accessed externally via {@link #getWidth()} and {@link #getHeight()}.
     */

    interface Window {

        /**
         * @return the native window object
         */

        Object getNative();

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
    }
}
