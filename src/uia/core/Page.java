package uia.core;

import uia.core.platform.independent.paint.Paint;
import uia.core.platform.policy.Context;
import uia.core.platform.policy.Graphic;
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
 * 3) If a Page is queued inside Context, or left alone, it is considered <u>closed</u>.
 */

public abstract class Page {
    private Context context;

    private Paint paint;

    private final List<View> views = new ArrayList<>();

    private boolean open = false;

    public Page(Context context) {
        this.context = Context.validateContext(context);

        paint = new Paint(30, 30, 30);
    }

    /**
     * Open this Page
     *
     * @return true if this Page opens; false if it has been already opened
     */

    public boolean open() {
        if (!open) {
            open = true;
            return true;
        }
        return false;
    }

    /**
     * Close this Page
     *
     * @return true if this Page closes; false if it has been already closed
     */

    public boolean close() {
        if (open) {

            for (View i : views) {
                // remove view's focus
                i.removeFocus();
                // reset view's animation
                i.resetExpansion();
            }

            open = false;

            return true;
        }

        return false;
    }

    /**
     * Set the Page's Context
     *
     * @param context a not null {@link Context}
     */

    public void setContext(Context context) {
        if (context != null) this.context = context;
    }

    /**
     * Remove all Views from this Page
     */

    public void clear() {
        views.clear();
    }

    /**
     * Add a new View to this Page
     *
     * @param view a not null {@link View}
     */

    public void add(View view) {
        if (view != null && !views.contains(view)) views.add(view);
    }

    /**
     * Remove the specified View from this Page
     *
     * @param i the position of the {@link View} to remove
     */

    public void remove(int i) {
        if (i >= 0 && i < views.size()) views.remove(i);
    }

    /**
     * Swap the given Views
     *
     * @param i the position of the first view
     * @param j the position of the second view
     */

    public void swap(int i, int j) {
        if (i >= 0 && i < views.size() && j >= 0 && j < views.size()) Utils.swap(views, i, j);
    }

    /**
     * Set the Page's Paint
     *
     * @param paint a not null {@link Paint}
     */

    public void setPaint(Paint paint) {
        if (paint != null) this.paint = paint;
    }

    /**
     * Update views pointers
     */

    public void updatePointers() {
        for (int i = views.size() - 1; i >= 0; i--) {
            View.updatePointers(views.get(i), true);
        }
    }

    /**
     * Update views keys
     */

    public void updateKeys() {
        for (int i = views.size() - 1; i >= 0; i--) {
            View.updateKeys(views.get(i), true);
        }
    }

    /**
     * Draw this Page inside the given Graphic
     *
     * @param graphic a not null {@link Graphic}
     */

    public void draw(Graphic graphic) {
        int width = context.width();
        int height = context.height();
        float x = 0.5f * width;
        float y = 0.5f * height;

        graphic.setPaint(paint);
        graphic.vertexBegin(0, 0);
        graphic.vertex(width, 0);
        graphic.vertex(width, height);
        graphic.vertex(0, height);
        graphic.closeShape();

        for (View i : views) {

            // update view's context
            if (!i.getContext().equals(context)) i.setContext(context);

            i.setCenter(x, y);

            View.update(i);
            View.draw(i, graphic);
        }
    }

    /**
     * @return true if this Page is currently open
     */

    public boolean isOpened() {
        return open;
    }

    /**
     * @return the number of Views inside this Page
     */

    public int size() {
        return views.size();
    }

    /**
     * @param view a not null {@link View}
     * @return the position of the given View otherwise -1
     */

    public int indexOf(View view) {
        return view == null ? -1 : views.indexOf(view);
    }

    /**
     * @param i the position of the View you are looking for
     * @return if it exists, the specified View, otherwise null
     */

    public View get(int i) {
        return i >= 0 && i < views.size() ? views.get(i) : null;
    }

    /**
     * @return the Page's dimension along x-axis
     */

    public float width() {
        return context.width();
    }

    /**
     * @return the Page's dimension along y-axis
     */

    public float height() {
        return context.height();
    }

    /**
     * @return the {@link Paint} used by this Page
     */

    public Paint getPaint() {
        return paint;
    }

    /**
     * @return the Page's {@link Context}
     */

    public Context getContext() {
        return context;
    }
}
