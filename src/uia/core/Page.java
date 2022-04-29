package uia.core;

import uia.core.utility.KeyEnc;
import uia.core.utility.Pointer;
import uia.utils.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A Page is a division of the Application layout. It is a standalone UI piece and can't directly interact with other pages.
 * The interaction between pages is a {@link Context} responsibility.
 * <br>
 * Some additional information:
 * <br>
 * 1) Page, by construction, has always the same size of the window frame.
 * <br>
 * 2) Page is considered <u>open</u> when it is currently handled by Context.
 * <br>
 * 3) If a page is stacked inside the Context, or left alone, it is considered <u>closed</u>.
 * <br>
 * 4) If Context changes, views Context will be changed only when they are rendered.
 */

// @Test
public abstract class Page {
    /**
     * Standard page background color
     */
    public static final Color STD_COLOR = new Color(30, 30, 30);

    /**
     * Standard no color
     */
    public final static Color NO_COLOR = new Color(0, 0, 0, 1);

    private Context context;

    private final List<View> views = new ArrayList<>();

    private Color color = STD_COLOR;

    private boolean open = false;

    public Page(Context context) {
        this.context = Context.validateContext(context);
    }

    /**
     * Set a Context for this page
     *
     * @param context a non-null Context
     */

    private void setContext(Context context) {
        if (context != null)
            this.context = context;
    }

    /**
     * Open this page
     *
     * @return true if this page has been opened
     */

    private boolean open() {
        if (!open) {
            open = true;
            return true;
        }

        return false;
    }

    /**
     * Close this page
     *
     * @return true if this page has been closed
     */

    private boolean close() {
        if (open) {

            for (View i : views) {
                // Remove view focus
                i.removeFocus();

                // @Test
                // Reset view animation
                i.resetAnimation();

                // @Test
                // Reset view touch dispatch
                i.touchDispatcher(null, false);
            }

            open = false;

            return true;
        }

        return false;
    }

    /**
     * Inform this page if window has gained or lose focus
     *
     * @param gained true if window has gained focus
     */

    private void focusDispatcher(boolean gained) {
        for (int i = views.size() - 1; i >= 0; i--) {
            views.get(i).focusDispatcher(gained);
        }
    }

    /**
     * Send pointer through each view of this page
     */

    private void touchDispatcher(Pointer p) {
        for (int i = views.size() - 1; i >= 0; i--) {
            View v = views.get(i);
            View.updateTouchDispatcher(v, p, v.isVisible());
        }
    }

    /**
     * Send keys through each view of this page
     */

    private void keyDispatcher(KeyEnc k) {
        for (int i = views.size() - 1; i >= 0; i--) {
            View v = views.get(i);
            View.updateKeyDispatcher(v, k, v.isVisible());
        }
    }

    /**
     * Add a new view to this page
     *
     * @param view a non-null view
     */

    public final void add(View view) {
        if (view != null)
            views.add(view);
    }

    /**
     * Swap the position of two specified views
     *
     * @param i the position of the first view
     * @param j the position of the second view
     */

    public final void swap(int i, int j) {
        if (i >= 0 && i < views.size()
                && j >= 0 && j < views.size())
            Utils.swap(views, i, j);
    }

    /**
     * Remove the specified view from this page
     *
     * @param i the position of the view to remove
     */

    public final void remove(int i) {
        if (i >= 0 && i < views.size())
            views.remove(i);
    }

    /**
     * Set the background color
     *
     * @param color a non-null color
     */

    public final void setColor(Color color) {
        if (color != null)
            this.color = color;
    }

    /**
     * Draw this page
     *
     * @param canvas a non-null {@link Graphics2D}
     */

    public void draw(Graphics2D canvas) {
        int dx = context.dx();
        int dy = context.dy();
        float px = 0.5f * dx;
        float py = 0.5f * dy;

        canvas.setColor(color);
        canvas.fillRect(0, 0, dx, dy);

        for (View i : views) {

            // @Test
            // Update view context
            if (!i.getContext().equals(context))
                i.setContext(context);

            i.setCenter(px, py);
            i.draw(canvas);
        }
    }

    /**
     * @return true if this page is currently handled by {@link StdContext}
     */

    public final boolean isOpen() {
        return open;
    }

    /**
     * @return the amount of handled views
     */

    public final int size() {
        return views.size();
    }

    /**
     * @return the index of the specified view or -1 if it is not present
     */

    public final int indexOf(View view) {
        return views.indexOf(view);
    }

    /**
     * @param i the view position
     * @return the specified view
     */

    public final View get(int i) {
        return views.get(i);
    }

    /**
     * @return the page x-dimension
     */

    public final float dx() {
        return context.dx();
    }

    /**
     * @return the page y-dimension
     */

    public final float dy() {
        return context.dy();
    }

    /**
     * @return the page Context
     */

    public final Context getContext() {
        return context;
    }

    /*
     *
     * State modifiers
     *
     */

    /**
     * Try to manually open the given Page.
     * <b>Method provided for custom Context implementation.</b>
     *
     * @param page a non-null Page
     */

    public static boolean open(Page page) {
        return page != null && page.open();
    }

    /**
     * Try to manually close the given Page.
     * <b>Method provided for custom Context implementation.</b>
     *
     * @param page a non-null Page
     */

    public static boolean close(Page page) {
        return page != null && page.close();
    }

    /**
     * Manually set a Context to the given Page.
     * <b>Method provided for custom Context implementation.</b>
     *
     * @param page    a non-null Page
     * @param context a non-null Context
     */

    public static void updateContext(Page page, Context context) {
        if (page != null)
            page.setContext(context);
    }

    /**
     * Manually update focus dispatcher of the given Page.
     * <b>Method provided for custom Context implementation.</b>
     *
     * @param page   a non-null Page
     * @param gained true if window has gained focus
     */

    public static void updateFocusDispatcher(Page page, boolean gained) {
        if (page != null)
            page.focusDispatcher(gained);
    }

    /**
     * Manually update touch dispatcher of the given Page.
     * <b>Method provided for custom Context implementation.</b>
     *
     * @param page a non-null Page
     * @param p    the pointer used by this Page
     */

    public static void updateTouchDispatcher(Page page, Pointer p) {
        if (page != null)
            page.touchDispatcher(p);
    }

    /**
     * Manually update key dispatcher of the given Page.
     * <b>Method provided for custom Context implementation.</b>
     *
     * @param page a non-null Page
     * @param k    the last system handled {@link KeyEnc}
     */

    public static void updateKeyDispatcher(Page page, KeyEnc k) {
        if (page != null)
            page.keyDispatcher(k);
    }
}
