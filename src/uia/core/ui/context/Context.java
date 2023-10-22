package uia.core.ui.context;

import uia.core.ui.View;

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
     * @return true if this Context is currently running
     */

    boolean isRunning();

    /**
     * Stop this Context, which causes the View to stop refreshing and rendering
     */

    void stop();

    /**
     * Stop and kill the Context process
     */

    void kill();

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
     * @return the {@link Window} managed by this Context
     */

    Window getWindow();

    /**
     * Return the Context managed InputEmulator.
     * <br>
     * According to effective implementation, it could return a null instance.
     *
     * @return the {@link InputEmulator} of this Context
     */

    InputEmulator getInputEmulator();

    /**
     * Copy a String in the clipboard or paste a String from the clipboard
     *
     * @param operation a not null {@link CLIPBOARD_OPERATION}
     * @param str       a String to copy in the clipboard; it could be null
     * @return null or the clipboard content
     */

    String clipboard(CLIPBOARD_OPERATION operation, String str);
}
