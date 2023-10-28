package uia.core.ui.context;

import uia.core.ui.View;

/**
 * Context is the base of the UIa framework. It is responsible for managing a single {@link View} displayed on
 * a {@link Window}. It is designed to be a versatile layer whose implementation is platform dependent.
 * <br>
 * <br>
 * <b>Lifecycle</b>
 * <p>
 * Like any application, Context has its own lifecycle. The key idea is that the single lifecycle stage capture
 * a well-defined Context state. There are a few rules that must be followed when implementing lifecycle mechanism:
 * <ul>
 *     <li>at the {@link LifecycleStage#STOP} stage, Context stops rendering and doesn't update the view's state.
 *     It also stops handling user input and artificially generated input;
 *     </li>
 *     <li>at the {@link LifecycleStage#RUN} stage, Context is fully operational: it manages a View and handles user input and
 *     artificially generated input;
 *     </li>
 *     <li>at the {@link LifecycleStage#TERMINATE} stage, Context stops all its operations, including View management and closes
 *     the window frame.
 *     </li>
 * </ul>
 * <br>
 * <b>Input emulation</b>
 * <p>
 * Sometimes it might be useful to emulate some events (ie pressing a key or moving the mouse around) to test some assumptions
 * about event handling without having to generate the same events manually. For this reason, Context provides the {@link InputEmulator}
 * class, which is responsible for emulating input from the mouse and keyboard.
 * </p>
 */

public interface Context {

    /**
     * RenderingHint defines the rendering settings
     */

    enum RenderingHint {
        ANTIALIASING_ON, ANTIALIASING_OFF,
        TEXT_ANTIALIASING_ON, TEXT_ANTIALIASING_OFF,
        COLOR_QUALITY_HIGH, COLOR_QUALITY_LOW
    }

    /**
     * ClipboardOperation defines the operations allowed on system clipboard
     */

    enum ClipboardOperation {COPY, PASTE}

    /**
     * Lifecycle Context stage
     */

    enum LifecycleStage {RUN, STOP, TERMINATE}

    /**
     * Set the Context lifecycle stage
     *
     * @param lifecycleStage a not null {@link LifecycleStage}
     * @throws NullPointerException if {@code lifecycleStage == null}
     */

    void setLifecycleStage(LifecycleStage lifecycleStage);

    /**
     * @return the current {@link LifecycleStage}
     */

    LifecycleStage getLifecycleStage();

    /**
     * Set the given rendering hints
     *
     * @param hint a not null array of {@link RenderingHint}s
     * @throws NullPointerException if {@code hint == null}
     */

    void setRenderingHint(RenderingHint... hint);

    /**
     * Let this Context managing the given View.
     * <br>
     * More formally, it updates, dispatches messages/inputs and renders the given View.
     *
     * @param view a {@link View}; it could be null
     */

    void setView(View view);

    /**
     * Copy a String in the clipboard or paste a String from the clipboard
     *
     * @param operation a not null {@link ClipboardOperation}
     * @param str       a String to copy in the clipboard; it could be null
     * @return null or the clipboard content
     */

    String clipboard(ClipboardOperation operation, String str);

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
}
