package uia.core.ui;

/**
 * Context acts as the basement of an application. It manages a window and is responsible for updating and rendering
 * the managed {@link View}. It is intended to be a versatile layer whose implementation is platform dependent.
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
     * @return true if this Context is running
     */

    boolean isRunning();

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

    int getWidth();

    /**
     * @return the current height of the drawable area
     */

    int getHeight();

    /**
     * @return the current frame rate per second
     */

    int getFrameRate();

    enum CLIPBOARD_OPERATION {COPY, PASTE}

    /**
     * Copy a String inside the system clipboard or paste a String from the System clipboard.
     *
     * @param operation a not null {@link CLIPBOARD_OPERATION}
     * @param str       a String to copy inside the system clipboard; it could be null
     * @return null or system's clipboard content
     */

    String clipboard(CLIPBOARD_OPERATION operation, String str);
}
