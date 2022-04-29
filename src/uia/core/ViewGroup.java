package uia.core;

import uia.core.utility.KeyEnc;
import uia.core.utility.Pointer;
import uia.utils.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

/**
 * <b>Skeleton layout.</b>
 * <br>
 * A ViewGroup is a single background visible view that handles the views drawn on top of it.
 * Every added view is initially centered on the group's center, eventually they can be translated
 * with {@link ViewGroup#setProperty(Property)} method.
 * In addition, note that when an element is out from the group shape limits, it won't be drawn.
 * A top-down handling strategy has been implemented behind the scene;
 * in this way top views have priority for event handling (like mouse and key) respect to the background views.
 * <br>
 * <b>Note that, if group Context changes, views Context will be changed only when they are rendered.</b>
 */

// @Test
public abstract class ViewGroup<T extends View> extends View {
    public static final int LAST = -1;

    private final List<T> list;

    private final List<T> onScreen;

    private Property<T> property;

    private float xLength;
    private float yLength;
    private float xScroll;
    private float yScroll;

    public ViewGroup(Context context,
                     float px, float py,
                     float dx, float dy) {
        super(context, px, py, dx, dy);
        super.setExpansion(0f, 0f);
        super.setColor(Page.STD_COLOR);

        list = new ArrayList<>();

        onScreen = new ArrayList<>();
    }

    /**
     * Function used to set properties to a group in runtime
     */

    public interface Property<T extends View> {

        void apply(ViewGroup<T> group, T view, int i);
    }

    /**
     * Set a {@link Property} to this group
     *
     * @param property a Property instance; it could be null
     */

    public void setProperty(Property<T> property) {
        this.property = property;
    }

    /**
     * Scroll the elements of this group
     *
     * @param x scroll value along x-axis
     * @param y scroll value along y-axis
     */

    public void setScroll(float x, float y) {
        xScroll = x;
        yScroll = y;
    }

    /**
     * Clear the content of this group and reset scroller value
     */

    public void clear() {
        xScroll = 0;
        yScroll = 0;
        xLength = 0;
        yLength = 0;

        list.clear();

        onScreen.clear();
    }

    /**
     * Add a view to this group.
     * <u>Note that, for integrity reasons, the operation can be done only if the group is visible.</u>
     *
     * @param i    the position where to insert the view; if {@code i == {@link ViewGroup#LAST}} the view will be added on top of others
     * @param view a non-null view to add
     */

    public void add(int i, T view) {
        if (isVisible() && view != null) {

            if (i == LAST) {
                list.add(view);
            } else {
                list.add(i, view);
            }
        }
    }

    /**
     * Remove a set of views from this group.
     * <u>Note that, for integrity reasons, the operation can be done only if the group is visible.</u>
     *
     * @param ind the positions of the views to remove
     */

    public void remove(int... ind) {
        if (isVisible() && ind != null) {

            if (ind.length == 1) {
                int i = ind[0];
                if (i >= 0 && i < list.size())
                    list.remove(i);
            } else if (ind.length > 1) {
                Utils.removeRange(list, ind);
            }
        }
    }

    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);

        if (!isVisible) {

            for (T t : list) {
                t.removeFocus();
            }

            onScreen.clear();
        }
    }

    @Override
    protected void focusDispatcher(boolean gained) {
        for (int i = list.size() - 1; i >= 0; i--) {
            list.get(i).focusDispatcher(gained);
        }

        super.focusDispatcher(gained);
    }

    @Override
    protected void touchDispatcher(Pointer p, boolean update) {
        boolean u = update
                && (p != null && !p.isConsumed())
                && (!hasFigure() || containsPoint(p.getX(), p.getY()));

        for (int i = list.size() - 1; i >= 0; i--) {
            View.updateTouchDispatcher(list.get(i), p, u);
        }

        super.touchDispatcher(p, update);
    }

    @Override
    protected void keyDispatcher(KeyEnc k, boolean update) {
        for (int i = list.size() - 1; i >= 0; i--) {
            View.updateKeyDispatcher(list.get(i), k, update);
        }

        super.keyDispatcher(k, update);
    }

    @Override
    public void postDraw(Graphics2D canvas) {
        onScreen.clear();

        Shape oldClip = canvas.getClip();
        boolean hasFigure = hasFigure();

        // Clip this group
        if (hasFigure)
            canvas.clip(getFigure());

        float px = px();
        float py = py();
        float dx = dx();
        float dy = dy();
        float hPx = 0, hPy = 0;
        float hdx = 0, hdy = 0;

        Context context = getContext();

        for (int i = 0; i < list.size(); i++) {
            T t = list.get(i);

            t.setCenter(0, 0);

            // Update view context
            if (!t.getContext().equals(context))
                t.setContext(context);

            if (t.px() > hPx) {
                hPx = t.px();
                hdx = 0.5f * t.dx();
            }

            if (t.py() > hPy) {
                hPy = t.py();
                hdy = 0.5f * t.dy();
            }

            if (property != null)
                property.apply(this, t, i);

            t.setCenter(px - xScroll, py - yScroll);

            if (!hasFigure ||
                    (abs(px - t.px()) <= 0.5f * (dx + t.dx()) && abs(py - t.py()) <= 0.5f * (dy + t.dy()))) {
                t.draw(canvas);

                if (t.isVisible())
                    onScreen.add(t);
            }
        }

        // Reset old clip
        if (hasFigure)
            canvas.setClip(oldClip);

        // Set scroller attributes
        xLength = hPx + hdx - dx / 2f;
        yLength = hPy + hdy - dy / 2f;
    }

    @Override
    public String toString() {
        return "ViewGroup{" +
                "size=" + list.size() +
                ", on screen now=" + onScreen.size() +
                '}';
    }

    /**
     * @return the number of views contained by this group
     */

    public final int size() {
        return list.size();
    }

    /**
     * @param t a view instance
     * @return if exists the index of the view otherwise -1
     */

    public final int indexOf(T t) {
        return list.indexOf(t);
    }

    /**
     * @return if exits, the i-th view of this group, otherwise null
     */

    public final T get(int i) {
        return (i >= 0 && i < list.size()) ? list.get(i) : null;
    }

    /**
     * @return the maximum value that the scroller could reach along x-axis
     */

    public final float getXScrollerMax() {
        return xLength;
    }

    /**
     * @return the maximum value that the scroller could reach along y-axis
     */

    public final float getYScrollerMax() {
        return yLength;
    }

    /**
     * @return the current scroll value along x-axis
     */

    public final float getScrollX() {
        return xScroll;
    }

    /**
     * @return the current scroll value along y-axis
     */

    public final float getScrollY() {
        return yScroll;
    }

    /**
     * Check if exists the given view
     *
     * @param t a view instance
     * @return true if this group contains the view
     */

    public final boolean contains(T t) {
        return list.contains(t);
    }

    /**
     * Return the first user visited view
     *
     * @return the visited view otherwise null
     */

    public final T getOver() {
        for (T t : onScreen) {

            if (t.isMouseOver())
                return t;
        }

        return null;
    }

    /**
     * @return a list filled with the current visible views
     */

    public final List<T> getOnScreen() {
        return onScreen;
    }

    /*
     *
     * Views placers
     *
     */

    /**
     * Automatically align all views inside a {@link ViewGroup}
     *
     * @param params   a float array made of three elements:
     *                 1) initial x-offset
     *                 2) initial y-offset
     *                 3) a value {@code >= 1} used to create gaps between views
     * @param vertical true to align views vertically
     * @param dynamic  true to automatically resize gaps when views scales
     */

    public static <T extends View> Property<T> place(float[] params,
                                                     boolean vertical, boolean dynamic) {
        float[] p = {0f};

        if (vertical && dynamic) {
            return (g, v, i) -> {
                float h = (params[2] * v.dy()) / 2f;
                p[0] = (i == 0) ? params[1] : (p[0] + h);
                v.setPos(params[0], p[0]);
                p[0] += h;
            };
        } else if (vertical) {
            return (g, v, i) -> {
                float h = (params[2] * v.yUpperBound()) / 2f;
                p[0] = (i == 0) ? params[1] : (p[0] + h);
                v.setPos(params[0], p[0]);
                p[0] += h;
            };
        } else if (dynamic) {
            return (g, v, i) -> {
                float h = (params[2] * v.dx()) / 2f;
                p[0] = (i == 0) ? params[0] : (p[0] + h);
                v.setPos(p[0], params[1]);
                p[0] += h;
            };
        } else {
            return (g, v, i) -> {
                float h = (params[2] * v.xUpperBound()) / 2f;
                p[0] = (i == 0) ? params[0] : (p[0] + h);
                v.setPos(p[0], params[1]);
                p[0] += h;
            };
        }
    }

    /*
     *
     * Effects
     *
     */

    /*private static float fAbsorption(float pos, float dist) {
        if (pos < 0.5f * dist || pos > 0.5f * dist) {
            return 0f;
        } else {
            return Utils.pcos(Utils.PI * pos / dist);
        }
    }

    public static <T extends View> void absorption(ViewGroup<T> group, View t) {
        t.setDim(
                t.boundX() * fAbsorption(t.px() - group.px(), 0.5f * group.dx()),
                t.boundY() * fAbsorption(t.py() - group.py(), 0.5f * group.dy()));
    }

    public static <T extends View> void absorptionFull(ViewGroup<T> group, View t) {
        float v = min(
                fAbsorption(t.px() - group.px(), 0.5f * group.dx()),
                fAbsorption(t.py() - group.py(), 0.5f * group.dy()));

        t.setDim(t.boundX() * v, t.boundY() * v);
    }*/
}
