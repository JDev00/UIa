package uia.core.platform.policy;

import uia.core.View;
import uia.core.Page;

/**
 * Context is responsible for drawing things on the screen and handling {@link Page}s life-cycle.
 * More formally, it is intended to be a versatile layer whose implementation is platform depend.
 * Every element responsible for things rendered on the screen must be implemented according to the native
 * platform specs.
 * Platform abstraction is achieved with different objects that acts as bridges between native architecture and UIa framework.
 * Note that bridges allow platform migration without modify higher layer code.
 * <br>
 * These bridges are:
 * <ul>
 * <li>{@link Graphic},</li>
 * <li>{@link Pointer}</li>
 * <li>{@link Key}</li>
 * </ul>
 * For implementation description about each bridge, please read the description inside each file.
 * <br>
 * <br>
 * <b><i>UIa logical architecture</i></b>
 * <br>
 * As said before, the Page layer is handled by the Context layer.
 * Every new layer adds new functionality and introduces new abstraction to simplify the creation of an application.
 * The layers can be divided, according to the abstraction level, as follows:
 * <ul>
 * <li>{@link uia.core.widget.Widget}</li>
 * <li>{@link View}</li>
 * <li>{@link Page}</li>
 * <li>{@link Context}</li>
 * </ul>
 */

public interface Context {

    /**
     * HINT is used to specify the render settings
     */

    enum HINT {SMOOTH, SMOOTH_TEXT}

    /**
     * @return the native Context
     */

    Object getNative();

    /**
     * Set up the Context in an async way
     *
     * @param w the screen width
     * @param h the screen height
     */

    void asyncSetup(int w, int h);

    /**
     * Set a {@link HINT}
     *
     * @param hint a HINT; it could be null
     */

    void setHint(HINT hint);

    /**
     * Start and show this Context
     *
     * @return true if this Context begins; false if it has already started
     */

    boolean start();

    /**
     * Stop this Context
     *
     * @return true if this Context stops; false if it has already stopped
     */

    boolean stop();

    /**
     * Decide if this window must be always on top of other windows
     *
     * @param alwaysOnTop true to set always on top
     */

    void setAlwaysOnTop(boolean alwaysOnTop);

    /**
     * Decide if this window could be resizable
     *
     * @param resizable true to allow window resize
     */

    void setResizable(boolean resizable);

    /**
     * Set the frame's title
     *
     * @param title a not null String
     */

    void setTitle(String title);

    /*
     *
     * Pages
     *
     */

    /**
     * Store a new Page.
     * <b>This functionality is used to create complex applications. Every page is initially stored and then
     * accessed and displayed with {@link Context#getStoredPage(int)} and {@link Context#open(Page)}<b/>
     *
     * @param page a not null Page
     */

    void storePage(Page page);

    /**
     * Remove a stored Page.
     *
     * @param i the index of the page to remove
     */

    void removeStoredPage(int i);

    /**
     * @return the number of stored pages
     */

    int storedPages();

    /**
     * @param page a not null Page you are looking for
     * @return the position of the given page or -1 if it isn't stored
     */

    int indexOfStoredPage(Page page);

    /**
     * @param i the position of the Page you are looking for
     * @return the specified Page
     */

    Page getStoredPage(int i);

    /**
     * Remove all pages from this Context
     */

    void clear();

    /**
     * Open a {@link Page}.
     * <b>Note that:
     * 1) if a Page is already open, it will be closed and enqueued.
     * 2) if the given Page is already enqueued, it will be put on top.</b>
     *
     * @param page a not null page
     */

    void open(final Page page);

    /**
     * Close and dequeue the current {@link Page}
     *
     * @return the removed page otherwise null
     */

    Page close();

    /**
     * @return the number of enqueued pages
     */

    int size();

    /**
     * @param page a not null {@link Page} you are looking for
     * @return the position of the given Page otherwise -1
     */

    int indexOf(Page page);

    /**
     * @param i the position of the page
     * @return the specified {@link Page} otherwise null
     */

    Page get(int i);

    /**
     * @return the {@link Page} displayed when Context is loading
     */

    Page getLoadingPage();

    /**
     * @return the scene FPS (frame rate per second)
     */

    int getFrameRate();

    /**
     * @return the width of the drawable area
     */

    int width();

    /**
     * @return the height of the drawable area
     */

    int height();

    /**
     * @return the initial width of the drawable area
     */

    int initialWidth();

    /**
     * @return the initial height of the drawable area
     */

    int initialHeight();

    /**
     * @return the window ratio (current screen dimension / start screen dimension) along x-axis
     */

    float screenRatioX();

    /**
     * @return the window ratio (current screen dimension / start screen dimension) along y-axis
     */

    float screenRatioY();

    /**
     * @return true if this Context is loading
     */

    boolean isLoading();

    /**
     * @return true if this Context is running
     */

    boolean isRunning();

    /**
     * @return the number of pointers inside this window
     */

    int pointerSize();

    /**
     * Define me!
     *
     * @return an array filled with the supported pointers
     */

    Pointer[] pointers();

    /**
     * @return the last Context handled {@link Key}
     */

    Key getKey();

    /**
     * @return true if mouse is pressed
     */

    boolean isMousePressed();

    /**
     * @return true if this Context is on focus
     */

    boolean isOnFocus();

    /**
     * Copy a String inside system clipboard
     *
     * @param str a String to copy
     */

    boolean copyIntoClipboard(String str);

    /**
     * @return the String stored inside the system clipboard
     */

    String pasteFromClipboard();

    /**
     * @return the available fonts
     */

    String[] fontList();

    /**
     * @return the current {@link HINT} otherwise null
     */

    HINT getHint();

    /**
     * Exception raised if a NULL Context is passed in
     */
    IllegalStateException CONTEXT_EXCEPTION = new IllegalStateException("Context can't be NULL!");

    /**
     * Try to validate the given {@link Context}
     *
     * @param context the given Context if it is not null otherwise it raises a {@link Context#CONTEXT_EXCEPTION}
     */

    static Context validateContext(Context context) {
        if (context != null) {
            return context;
        } else {
            throw CONTEXT_EXCEPTION;
        }
    }

    /**
     * Run a new asynchronously task
     *
     * @param runnable a not null {@link Runnable} instance
     * @return the created {@link Thread}
     */

    static Thread runTask(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }
}
