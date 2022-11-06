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
import java.util.function.Function;

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
     * Add a new View to this Widget
     *
     * @param i    the view's position inside this List
     * @param view a not null {@link View}
     * @return true if the operation succeed
     */

    public boolean add(int i, View view) {
        try {
            if (view != null && !views.contains(view)) {
                views.add(i, view);
                return true;
            }
        } catch (IndexOutOfBoundsException ignored) {
        }
        return false;
    }

    /**
     * Add a new view to this Widget
     *
     * @param view a not null {@link View}
     * @return true if the operation succeed
     * @see #add(int, View) 
     */

    public boolean add(View view) {
        return add(size(), view);
    }

    /**
     * Populate this Widget
     *
     * @param fun   a not null {@link Function}
     * @param count the number of views to create
     * @return true if the operation succeed
     * @see #add(int, View)
     */

    public boolean populate(Function<Integer, View> fun, int count) {
        try {
            for (int i = 0; i < count; i++) {
                add(size(), fun.apply(i));
            }
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Remove a view from this Widget
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

    @Override
    public Iterator<View> iterator() {
        return views.iterator();
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
     * @return true if clipping functionality is enabled
     */

    public boolean isClipped() {
        return clip;
    }

    /**
     * @param i the position of the {@link View} to return
     * @return the specified View or null
     */

    public View get(int i) {
        try {
            return views.get(i);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * @return the last {@link View} or null
     */

    public View peek() {
        return get(views.size() - 1);
    }

    /**
     * @return a List filled with the current views displayed on screen
     */

    public List<View> getOnScreen() {
        return onScreen;
    }

    /**
     * @return true if pointersConstrained functionality is enabled
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
        try {
            return views.contains(view);
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }
}
