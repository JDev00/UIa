package uia.core;

import uia.core.policy.Context;
import uia.core.policy.Paint;
import uia.core.policy.Render;
import uia.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A Page is a division of the Application layout. It is a standalone UI piece and can't directly interact with other pages.
 * The interaction between pages is a {@link Context} responsibility.
 * <br>
 * Some additional information:
 * <br>
 * 1) a Page has always the size of the window frame,
 * <br>
 * 2) a Page is considered <u>open</u> when it is currently handled by Context,
 * <br>
 * 3) If a page is stacked inside the Context, or left alone, it is considered <u>closed</u>,
 * <br>
 * 4) If Context changes, views' Context will be changed only when they are rendered.
 */

public abstract class Page {
    private Context context;

    private Paint paint;

    private boolean open = false;

    private final List<View> views = new ArrayList<>();

    public Page(Context context) {
        this.context = Context.validateContext(context);

        paint = context.createColor(null, Context.COLOR.STD);
    }

    /**
     * Open this page
     *
     * @return true if this Page opens; false if it has already opened
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
     * @return true if this page closes; false if it has already closed
     */

    private boolean close() {
        if (open) {

            for (View i : views) {
                // Remove view focus
                i.removeFocus();

                // Reset view animation
                i.resetAnimation();
            }

            open = false;

            return true;
        }

        return false;
    }

    /**
     * Set the Page Context
     *
     * @param context a not null Context
     */

    private void setContext(Context context) {
        if (context != null)
            this.context = context;
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
     * Send pointers through each view of this page
     */

    private void pointerDispatcher() {
        for (int i = views.size() - 1; i >= 0; i--) {
            View v = views.get(i);
            View.updatePointerDispatcher(v, v.isVisible());
        }
    }

    /**
     * Send keys through each view of this page
     */

    private void keyDispatcher() {
        for (int i = views.size() - 1; i >= 0; i--) {
            View v = views.get(i);
            View.updateKeyDispatcher(v, v.isVisible());
        }
    }

    /**
     * Add a new view to this Page
     *
     * @param view a not null view
     */

    public final void add(View view) {
        if (view != null)
            views.add(view);
    }

    /**
     * Swap the position of two given views
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
     * Remove the specified view from this Page
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
     * @param paint a not null Paint object
     */

    public final void setPaint(Paint paint) {
        if (paint != null)
            this.paint = paint;
    }

    /**
     * Draw this page
     *
     * @param render a not null {@link Render} instance
     */

    public void draw(Render render) {
        int dx = context.dx();
        int dy = context.dy();
        float px = 0.5f * dx;
        float py = 0.5f * dy;

        render.setPaint(paint);
        render.drawRect(0, 0, dx, dy);

        for (View i : views) {

            // Update view context
            if (!i.getContext().equals(context))
                i.setContext(context);

            i.setCenter(px, py);
            i.draw(render);
        }
    }

    /**
     * @return true if this page is open
     */

    public final boolean isOpen() {
        return open;
    }

    /**
     * @return the number of handled views
     */

    public final int size() {
        return views.size();
    }

    /**
     * @return the position of the given view otherwise -1
     */

    public final int indexOf(View view) {
        return views.indexOf(view);
    }

    /**
     * @param i the position of the view you are looking for
     * @return the specified view
     */

    public final View get(int i) {
        return views.get(i);
    }

    /**
     * @return the page dimension along x-axis
     */

    public final float dx() {
        return context.dx();
    }

    /**
     * @return the page dimension along y-axis
     */

    public final float dy() {
        return context.dy();
    }

    /**
     * @return the Paint object used by this Page
     */

    public final Paint getPaint() {
        return paint;
    }

    /**
     * @return the Page Context
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
     * @param page a not null Page
     */

    public static boolean open(Page page) {
        return page != null && page.open();
    }

    /**
     * Try to manually close the given Page.
     * <b>Method provided for custom Context implementation.</b>
     *
     * @param page a not null Page
     */

    public static boolean close(Page page) {
        return page != null && page.close();
    }

    /**
     * Manually set a Context to the given Page.
     * <b>Method provided for custom Context implementation.</b>
     *
     * @param page    a not null Page
     * @param context a not null Context
     */

    public static void updateContext(Page page, Context context) {
        if (page != null)
            page.setContext(context);
    }

    /**
     * Manually update focus dispatcher of the given Page.
     * <b>Method provided for custom Context implementation.</b>
     *
     * @param page   a not null Page
     * @param gained true if window has gained focus
     */

    public static void updateFocusDispatcher(Page page, boolean gained) {
        if (page != null)
            page.focusDispatcher(gained);
    }

    /**
     * Manually update pointer dispatcher of the given Page.
     * <b>Method provided for custom Context implementation.</b>
     *
     * @param page a not null Page
     */

    public static void updatePointerDispatcher(Page page) {
        if (page != null)
            page.pointerDispatcher();
    }

    /**
     * Manually update key dispatcher of the given Page.
     * <b>Method provided for custom Context implementation.</b>
     *
     * @param page a not null Page
     */

    public static void updateKeyDispatcher(Page page) {
        if (page != null)
            page.keyDispatcher();
    }
}
