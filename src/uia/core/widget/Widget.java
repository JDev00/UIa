package uia.core.widget;

import uia.core.View;
import uia.core.platform.policy.Context;
import uia.core.platform.policy.Pointer;
import uia.core.platform.policy.Graphic;
import uia.utils.Collider;
import uia.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Widget has been designed to simplify the creation of custom widgets without worrying about internal details.
 * <br>
 * A bottom-up approach has been adopted, meaning that, the first view to handle events is the last added one.
 * <br>
 * Some notes:
 * <br>
 * 1) view's context will be updated only at rendering time,
 * <br>
 * 2) use {@link #setPointersConstrain(boolean)} to force views to interact only if pointers are inside the widget's area.
 */

public abstract class Widget extends View implements Iterable<View> {
    private float xScroll;
    private float yScroll;
    private float xScrollLength;
    private float yScrollLength;
    private float xScrollFactor = 1f;
    private float yScrollFactor = 1f;

    private final List<View> views;
    private final List<View> onScreen;

    private boolean clip = true;
    private boolean constrainPointers = true;

    public Widget(Context context, float x, float y, float width, float height) {
        super(context, x, y, width, height);

        views = new ArrayList<>(4);
        onScreen = new ArrayList<>(4);
    }

    /**
     * Functionality used to force views to interact only if pointers are inside the widget's area
     *
     * @param constrainPointers true to enable this functionality
     */

    public void setPointersConstrain(boolean constrainPointers) {
        this.constrainPointers = constrainPointers;
    }

    /**
     * Enable or disable the clipping functionality.
     * <br>
     * More formally, a View laying outside the Widget's area isn't drawn or only partially drawn.
     *
     * @param clip true to enable clipping functionality
     */

    public void setClipping(boolean clip) {
        this.clip = clip;
    }

    /**
     * @return true if clipping functionality is enabled
     */

    public boolean isClipped() {
        return clip;
    }

    /**
     * Scroll the elements of this widget
     *
     * @param x the scroll value along x-axis
     * @param y the scroll value along y-axis
     */

    public void setScroll(float x, float y) {
        xScroll = x;
        yScroll = y;
    }

    /**
     * Remove all views from this widget
     */

    public void clear() {
        views.clear();
        onScreen.clear();

        xScroll = yScroll = 0f;
        xScrollLength = yScrollLength = 0f;
        xScrollFactor = yScrollFactor = 1f;
    }

    /**
     * Add a new view to this Widget
     *
     * @param view a not null {@link View}
     * @return true if the operation succeed
     */

    public boolean add(View view) {
        if (view != null && !views.contains(view)) {
            views.add(view);
            return true;
        }
        return false;
    }

    /**
     * Add a new view to this Widget
     *
     * @param i    the new view position
     * @param view a not null {@link View} or its subclass
     * @return true if the operation succeed
     */

    public boolean add(int i, View view) {
        if (i >= 0 && i <= views.size() && !views.contains(view)) {
            views.add(i, view);
            return true;
        }
        return false;
    }

    /**
     * Remove a view from this Widget
     *
     * @param i the position of the {@link View} to remove
     * @return true if the operation succeed
     */

    public boolean remove(int i) {
        if (i >= 0 && i < views.size()) {
            views.remove(i);
            return true;
        }
        return false;
    }

    /**
     * Remove a set of views from this widget
     *
     * @param ind the positions of the views to remove
     * @return true if the operation succeed
     */

    public boolean remove(int... ind) {
        if (ind != null) {
            Utils.removeRange(views, ind);
            return true;
        }
        return false;
    }

    /**
     * Change the visibility of this widget and of all the attached views
     *
     * @param isVisible true to set this widget visible
     */

    @Override
    public void setVisible(boolean isVisible) {
        if (!isVisible) onScreen.clear();

        for (int i = views.size() - 1; i >= 0; i--) {
            views.get(i).setVisible(isVisible);
        }

        super.setVisible(isVisible);
    }

    @Override
    protected void updatePointers(boolean update) {
        boolean u = update;

        if (constrainPointers) {// update View's pointers only if pointers are inside the Widget's area
            Pointer p = getContext().pointers()[0];
            u = update && (!hasShape() || contains(p.getX(), p.getY()));
        }

        for (int i = views.size() - 1; i >= 0; i--) {
            View.updatePointers(views.get(i), u);
        }

        super.updatePointers(update);
    }

    @Override
    protected void updateKeys(boolean update) {
        for (int i = views.size() - 1; i >= 0; i--) {
            View.updateKeys(views.get(i), update);
        }
        super.updateKeys(update);
    }

    @Override
    protected void draw(Graphic graphic) {
        super.draw(graphic);

        onScreen.clear();

        float x = x();
        float y = y();
        float width = width();
        float height = height();
        float hPx = 0, hPy = 0;
        float hdx = 0, hdy = 0;
        float xs = x - xScroll;
        float ys = y - yScroll;

        boolean shape = hasShape();
        boolean clip = this.clip && hasShape();

        Context context = getContext();

        // clip when necessary
        if (clip) graphic.setClip(getShape());

        for (View i : views) {

            // update view's context
            if (!context.equals(i.getContext())) i.setContext(context);

            i.setCenter(0f, 0f);

            float xi = i.x();
            float yi = i.y();

            if (xi > hPx) {
                hPx = xi;
                hdx = 0.5f * i.width();
            }
            if (yi > hPy) {
                hPy = yi;
                hdy = 0.5f * i.height();
            }

            i.setCenter(xs, ys);

            if (clip) {
                i.setVisible(Collider.AABB(x, y, width, height, i.x(), i.y(), i.width(), i.height()));
            } else {
                i.setVisible(shape);
            }

            View.update(i);
            View.draw(i, graphic);

            if (i.isVisible()) onScreen.add(i);
        }

        // restore old clip
        if (clip) graphic.restoreClip();

        // set scroller length
        xScrollLength = hPx + hdx - width / 2f;
        yScrollLength = hPy + hdy - height / 2f;

        xScrollFactor = xScrollLength / Math.max(1, size());
        yScrollFactor = yScrollLength / Math.max(1, size());
    }

    @Override
    public String toString() {
        return "Widget{size=" + views.size() + '}';
    }

    /**
     * @return the number of views
     */

    public int size() {
        return views.size();
    }

    /**
     * @param view a not null {@link View}
     * @return the index of the given View
     */

    public int indexOf(View view) {
        return views.indexOf(view);
    }

    /**
     * @return the current scroll value along x-axis
     */

    public float getScrollX() {
        return xScroll;
    }

    /**
     * @return the current scroll value along y-axis
     */

    public float getScrollY() {
        return yScroll;
    }

    /**
     * @return the scroller factor along x-axis
     */

    public float getXScrollFactor() {
        return xScrollFactor;
    }

    /**
     * @return the scroller factor along y-axis
     */

    public float getYScrollFactor() {
        return yScrollFactor;
    }

    /**
     * @return the maximum value that the scroller could reach along x-axis
     */

    public float getXScrollLength() {
        return xScrollLength;
    }

    /**
     * @return the maximum value that the scroller could reach along y-axis
     */

    public float getYScrollLength() {
        return yScrollLength;
    }

    /**
     * @param i the position of the {@link View} to return
     * @return the specified View or null
     */

    public View get(int i) {
        return (i >= 0 && i < views.size()) ? views.get(i) : null;
    }

    /**
     * @return the last {@link View} or null
     */

    public View peek() {
        int size = views.size();
        return size > 0 ? views.get(size - 1) : null;
    }

    /*
     * Return the first user visited view
     *
     * @return the visited view otherwise null
     *

    public View getOver() {
        for (View i : onScreen) {
            if (i.arePointersOver()) return i;
        }
        return null;
    }*/

    /**
     * @return a list filled with the current views displayed on screen
     */

    public List<View> getOnScreen() {
        return onScreen;
    }

    /**
     * @return true if mouse pointer is constrained inside this widget's area
     */

    public boolean isPointersConstrained() {
        return constrainPointers;
    }

    /**
     * Check if exists the given view
     *
     * @param view a not null view
     * @return true if this widget contains the given view
     */

    public boolean contains(View view) {
        return views.contains(view);
    }

    @Override
    public Iterator<View> iterator() {
        return views.iterator();
    }

    /*
     * Align all views inside a {@link Layout}
     *
     * @param params   a float array made of three elements:
     *                 <br>
     *                 1) initial x-offset
     *                 <br>
     *                 2) initial y-offset
     *                 <br>
     *                 3) a value {@code >= 1} used to create gaps between views
     * @param vertical true to align views vertically
     * @param dynamic  true to automatically resize gaps when views scales
     *

    /*public static Property place(float[] params,
                                 boolean vertical, boolean dynamic) {
        float[] p = {0f};

        if (vertical && dynamic) {
            return (g, v, i) -> {
                float h = (params[2] * v.height()) / 2f;
                p[0] = (i == 0) ? params[1] : (p[0] + h);
                v.setPosition(params[0], p[0]);
                p[0] += h;
            };
        } else if (vertical) {
            return (g, v, i) -> {
                float h = (params[2] * v.boundsMax()[3]) / 2f;
                p[0] = (i == 0) ? params[1] : (p[0] + h);
                v.setPosition(params[0], p[0]);
                p[0] += h;
            };
        } else if (dynamic) {
            return (g, v, i) -> {
                float h = (params[2] * v.width()) / 2f;
                p[0] = (i == 0) ? params[0] : (p[0] + h);
                v.setPosition(p[0], params[1]);
                p[0] += h;
            };
        } else {
            return (g, v, i) -> {
                float h = (params[2] * v.boundsMax()[2]) / 2f;
                p[0] = (i == 0) ? params[0] : (p[0] + h);
                v.setPosition(p[0], params[1]);
                p[0] += h;
            };
        }
    }*/
}
