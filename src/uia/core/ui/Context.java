package uia.core.ui;

/**
 * Context is the base of the UIa framework. It is responsible for updating and rendering a single {@link View} on a {@link Window}.
 * It is intended to be a versatile layer whose implementation is platform dependent.
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
     * @return the {@link ArtificialInput} of this Context
     */

    ArtificialInput getArtificialInput();

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

    /**
     * ArtificialInput is designed to artificially simulate an input from mouse and from keyboard.
     * <br>
     * It is useful to simulate the interaction between user and a graphical component.
     */

    interface ArtificialInput {

        /**
         * Simulate a click on screen
         *
         * @param x the click position on the x-axis
         * @param y the click position on the y-axis
         * @return this ArtificialInput
         */

        ArtificialInput click(int x, int y);

        /**
         * Simulate a mouse moving (without pressing buttons) on screen.
         * <br>
         * It is guaranteed to generate at least 30 event emissions between the starting and ending point.
         *
         * @param xStart   the movement starting point on the x-axis
         * @param yStart   the movement starting point on the y-axis
         * @param xEnd     the movement ending point on the x-axis
         * @param yEnd     the movement ending point on the y-axis
         * @param duration the time > 0 required to complete the movement in seconds
         * @return this ArtificialInput
         */

        ArtificialInput moveOnScreen(int xStart, int yStart, int xEnd, int yEnd, float duration);

        /**
         * Simulate a mouse dragging on screen.
         * <br>
         * It is guaranteed to generate at least 30 event emissions between the starting and ending point.
         *
         * @param xStart   the dragging starting point on the x-axis
         * @param yStart   the dragging starting point on the y-axis
         * @param xEnd     the dragging ending point on the x-axis
         * @param yEnd     the dragging ending point on the y-axis
         * @param duration the time > 0 required to complete the movement in seconds
         * @return this ArtificialInput
         */

        ArtificialInput dragOnScreen(int xStart, int yStart, int xEnd, int yEnd, float duration);

        /**
         * Simulate a key pressed on keyboard
         *
         * @param key the pressed key
         * @return this ArtificialInput
         */

        ArtificialInput sendKey(char key);

        /**
         * Simulate a keyCode pressed on keyboard
         *
         * @param keyCode the pressed keyCode
         * @return this ArtificialInput
         */

        ArtificialInput sendKeyCode(int keyCode);
    }
}
