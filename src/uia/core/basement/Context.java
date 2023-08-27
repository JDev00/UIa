package uia.core.basement;

import uia.core.ui.View;

/**
 * Context ADT.
 * <br>
 * Context acts as application's basement. It manages a window and is responsible to update the state
 * and render the attached {@link View}. It has been intended to be a versatile layer whose implementation
 * is uia.application.platform dependent.
 */

public interface Context {

    /**
     * HINT is used to specify render settings
     */

    enum HINT {
        ANTIALIASING_ON, ANTIALIASING_OFF,
        ANTIALIASING_TEXT_ON, ANTIALIASING_TEXT_OFF,
        RENDER_QUALITY_COLOR_HIGH, RENDER_QUALITY_COLOR_LOW
    }

    /**
     * @return the native Context object
     */

    Object getNative();

    /**
     * Update and render the given View.
     * <br>
     * <b>Only the last given View is handled.</b>
     *
     * @param view a {@link View}; it could be null
     */

    void setView(View view);

    /**
     * Set some render hints
     *
     * @param hint a not null array of {@link HINT}s
     */

    void setHints(HINT... hint);

    /**
     * Start this Context
     */

    void start();

    /**
     * Stop this Context
     */

    void stop();

    /**
     * Set some window frame parameters
     *
     * @param alwaysOnTop true to set window's frame always on top
     * @param resizable   true to allow window's frame to resize
     * @param title       a not null window's frame title
     */

    void setWindowParams(boolean alwaysOnTop, boolean resizable, String title);

    /**
     * @return the current width of the drawable area
     */

    int width();

    /**
     * @return the current height of the drawable area
     */

    int height();

    /**
     * @return the current frame rate per second
     */

    int getFrameRate();

    /**
     * @return true if this Context is running
     */

    boolean isRunning();

    /**
     * Copy a String inside the system clipboard or paste a String from the System clipboard.
     *
     * @param str    a String to copy inside the system clipboard; it could be null
     * @param toCopy true to copy the String inside the clipboard; false to paste the String from the clipboard
     * @return null or system's clipboard content
     */

    String clipboard(String str, boolean toCopy);
}
