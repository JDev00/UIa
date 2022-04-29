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
     * Start and show this context
     *
     * @return true if the context isn't started yet
     */

    boolean start();

    /**
     * Stop this Context
     *
     * @return true if the context has been stopped
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
     * Pages
     *
     */

    /**
     * Remove all pages from this Context
     */

    void clear();

    /**
     * Open a {@link Page} on top others.
     * <b>Note that if a Page is already stacked it will be taken to the top of others.</b>
     *
     * @param page a non-null page
     */

    void open(final Page page);

    /**
     * Close and remove the current {@link Page} from the stack
     *
     * @return if {@code {@link StdContext#size()} > 0} the removed page otherwise null
     */

    Page close();

    /**
     * @return the number of stacked pages
     */

    int size();

    /**
     * @param page a non-null {@link Page} to search
     * @return if exists the index of the page otherwise -1
     */

    int indexOf(Page page);

    /**
     * @param i the position of the page
     * @return if exists the {@link Page} specified by the given index otherwise null
     */

    Page get(int i);

    /**
     * @return the {@link Page} used when Context is loading
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
     * @return the amount of pointers over the window
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
     * @param font a non-null {@link Font}
     * @return the metrics of the given font
     */

    FontMetrics getFontMetrics(Font font);

    /**
     * @return the native Context window; it could be null
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
     * @param runnable a non-null {@link Runnable} instance
     * @return the created {@link Thread}
     */

    static Thread runTask(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    /**
     * @return the size of the screen
     */

    static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    /**
     * Load an image
     *
     * @param path the path of the image to load
     * @return if exists the image otherwise null
     */

    static Image loadImage(String path) {
        if (path != null) {
            try {
                return ImageIO.read(new File(path));
            } catch (IOException ignored) {
                //
            }
        }

        return null;
    }

    /**
     * Method used to copy a string inside the system clipboard
     *
     * @param string a string to copy
     */

    static void copy(String string) {
        if (string != null) {
            StringSelection selection = new StringSelection(string);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        }
    }

    /**
     * Method used to get the content from the system clipboard
     *
     * @return the content of the clipboard as a string
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
