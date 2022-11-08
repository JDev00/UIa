package uia.core;

import uia.core.platform.independent.paint.Paint;
import uia.core.platform.policy.Context;
import uia.core.platform.policy.Graphic;
import uia.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A Page is a division of the Application's layout and its responsibility is to render and update the attached views.
 * It is a standalone UI piece and can't directly interact with other pages.
 * To attach a new View use: {@link #add(View)} or {@link #add(View...)}
 * <br>
 * Some additional information:
 * <br>
 * 1) a Page has always the same Context's display dimension;
 * <br>
 * 2) a Page is considered <b>open</b> when it is currently handled by a Context.
 */

public abstract class Page implements Iterable<View> {
    private Context context;

    private Paint paint;

    private final List<View> views = new ArrayList<>();

    private boolean open = false;

    public Page(Context context) {
        this.context = Context.validate(context);

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
     * Remove all views from this Page
     */

    public void clear() {
        views.clear();
    }

    /**
     * Add a new View to this Page
     *
     * @param view a not null {@link View}
     * @return true if the operation succeed
     */

    public boolean add(View view) {
        if (view == null || views.contains(view)) return false;
        return views.add(view);
    }

    /**
     * Add some new views to this Page
     *
     * @param views a not null array of {@link View}s
     * @return true if the operation succeed
     */

    public boolean add(View... views) {
        try {
            for (View i : views) {
                add(i);
            }
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Remove the specified View from this Page
     *
     * @param i the position of the {@link View} to remove
     * @return true if the operation succeed
     */

    public boolean remove(int i) {
        try {
            views.remove(i);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Swap the specified views
     *
     * @param i the position of the first {@link View}
     * @param j the position of the second {@link View}
     * @return true if the operation succeed
     */

    public boolean swap(int i, int j) {
        return Utils.swap(views, i, j);
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
     * Draw this Page on the given Graphic
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

    @Override
    public Iterator<View> iterator() {
        return views.iterator();
    }

    /**
     * @return true if this Page is currently open
     */

    public boolean isOpened() {
        return open;
    }

    /**
     * @return the number of views inside this Page
     */

    public int size() {
        return views.size();
    }

    /**
     * Return the position (inside this Page) of the given View
     *
     * @param view a not null {@link View}
     * @return if the View exists, its position inside this Page, otherwise -1
     */

    public int indexOf(View view) {
        try {
            return views.indexOf(view);
        } catch (NullPointerException e) {
            return -1;
        }
    }

    /**
     * Return the specified View
     *
     * @param i the View's position inside this Page
     * @return if the position is correct, the specified View, otherwise null
     */

    public View get(int i) {
        try {
            return views.get(i);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
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
