package uia.core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;

/**
 * Context is the API core-block, it is responsible for drawing things on the screen and handling pages life-cycle.
 * Context class is the first building-block of the API, indeed, on top of it, is build everything. The Logical Structure can be
 * divided into 4 layers: (starting from bottom)
 * <br>
 * 1) Context layer, whose responsibility is mainly rendering and handling Pages.
 * <br>
 * 2) {@link Page} layer, whose responsibility is rendering and handling Views.
 * <br>
 * 3) {@link View} layer, whose responsibility is to provide a common ground to draw and hand widget
 * <br>
 * 4) Widget layer
 */

public interface Context {

    /**
     * Set up the Context in an async way
     *
     * @param w the screen width
     * @param h the screen height
     */

    void asyncSetup(int w, int h);

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

    /*
     *
     * Window settings
     *
     */

    /**
     * Decide if this window must be always on top of other windows
     *
     * @param alwaysOnTop true to set always on top
     */

    void setAlwaysOnTop(boolean alwaysOnTop);

    /*
     *
     * Stored pages
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

    /*
     *
     * Pages
     *
     */

    /**
     * Remove all pages from this Context
     */

    void clear();

    /**
     * Open a {@link Page}.
     * <b>Note that:
     * 1) if there is a Page already open, it will be closed and enqueued.
     * 2) if the given Page is already enqueued, it will be taken on top.</b>
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

    Page getLoadPage();

    /**
     * @return the scene FPS (frame rate per second)
     */

    int getFrameRate();

    /**
     * @return the initial width of the drawable area
     */

    int initialDx();

    /**
     * @return the initial height of the drawable area
     */

    int initialDy();

    /**
     * @return the width of the drawable area
     */

    int dx();

    /**
     * @return the height of the drawable area
     */

    int dy();

    /**
     * @return the window ratio (current screen dimension / start screen dimension) along x-axis
     */

    float dxFactor();

    /**
     * @return the window ratio (current screen dimension / start screen dimension) along y-axis
     */

    float dyFactor();

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
     * @return an array filled with the pointers position along x-axis
     */

    int[] xPointer();

    /**
     * @return an array filled with the pointers position along y-axis
     */

    int[] yPointer();

    /**
     * @return true if mouse is pressed
     */

    boolean isMousePressed();

    /**
     * @param font a not null {@link Font}
     * @return the font metrics
     */

    FontMetrics getFontMetrics(Font font);

    /**
     * @return the native window; it could be null
     */

    Object getNative();

    /**
     * Standard exception raised if a NULL Context is passed in
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

    /**
     * @return the screen size
     */

    static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    /**
     * Load an image
     *
     * @param path the image path
     * @return the loaded image otherwise null
     */

    static Image loadImage(String path) {
        if (path != null) {
            try {
                return ImageIO.read(new File(path));
            } catch (IOException ignored) {
            }
        }

        return null;
    }

    /**
     * Copy a string inside the system clipboard
     *
     * @param string a not null string to copy
     */

    static void copy(String string) {
        if (string != null) {
            StringSelection selection = new StringSelection(string);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        }
    }

    /**
     * Return the content of the system clipboard
     *
     * @return the clipboard content
     */

    static String paste() {
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = c.getContents(null);

        if (t != null) {
            try {
                return (String) t.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception ignored) {
            }
        }

        return "";
    }
}
