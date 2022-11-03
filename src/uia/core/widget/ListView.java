package uia.core.widget;

import uia.core.View;
import uia.core.platform.policy.Context;
import uia.core.platform.policy.Graphic;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView is an implementation of {@link Widget} skeleton, it is mainly used to render
 * and handle multiple views with two scrollbars already attached.
 */

public class ListView extends Widget {// Test
    private BarScrollView hBar;
    private BarScrollView vBar;

    private Aligner aligner;

    private final List<View> store;

    public ListView(Context context, float x, float y, float width, float height) {
        super(context, x, y, width, height);
        super.setExpansion(0f, 0f);

        hBar = new BarScrollView(context, 0, 0, 1, 1);
        hBar.setExpansion(0f, 0f);
        hBar.setVertical(false);
        hBar.setPositionAdjustment(false);
        hBar.setDimensionAdjustment(false);

        vBar = new BarScrollView(context, 0, 0, 1, 1);
        vBar.setExpansion(0f, 0f);
        vBar.setPositionAdjustment(false);
        vBar.setDimensionAdjustment(false);

        store = new ArrayList<>();

        float[] sum = {0f};
        aligner = (v, i) -> {
            if (i == 0) sum[0] = 0f;
            float h = v.height() / 2f;
            sum[0] += h;
            v.setPosition(0f, -height() / 2f + sum[0]);
            sum[0] += h;
        };
    }

    /**
     * Aligner is used to easily align a set of views
     */

    public interface Aligner {

        /**
         * Align the given View
         *
         * @param view a not null {@link View}
         * @param i    the View's position inside the List
         */

        void align(View view, int i);
    }

    /**
     * Set a new Aligner
     *
     * @param aligner a not null {@link Aligner}
     */

    public void setAligner(Aligner aligner) {
        this.aligner = aligner;
    }

    /**
     * Set a new BarScroll to this listView.
     * Note that the old bar's value will be kept and the new bar will be automatically assigned according to its orientation.
     *
     * @param barScrollView a not null {@link BarScrollView}
     */

    public void setBarScroll(BarScrollView barScrollView) {
        if (barScrollView != null) {

            if (barScrollView.isVertical()) {
                float val = vBar.getValue();
                vBar = barScrollView;
                vBar.setValue(val);
            } else {
                float val = hBar.getValue();
                hBar = barScrollView;
                hBar.setValue(val);
            }
        }
    }

    @Override
    public void setScroll(float x, float y) {
        hBar.setValue(x);
        vBar.setValue(y);
    }

    /**
     * Store a new view at the end of this list
     *
     * @param view a not null view to store
     */

    @Override
    public boolean add(View view) {
        if (view != null && !store.contains(view)) {
            view.setPointerConsumer(false);
            view.setPositionAdjustment(false);
            view.setDimensionAdjustment(false);
            store.add(view);
            return true;
        }
        return false;
    }

    /**
     * Store a new view at the given position
     *
     * @param i    the position where store the view
     * @param view a not null view to store
     */

    @Override
    public boolean add(int i, View view) {
        if (view != null && !store.contains(view)) {
            view.setPointerConsumer(false);
            view.setPositionAdjustment(false);
            view.setDimensionAdjustment(false);
            store.add(i, view);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(int i) {
        if (i >= 0 && i < size()) {
            View view = get(i);
            store.remove(view);
            store.add(view);
        }
        return super.remove(i);
    }

    @Override
    public boolean remove(int... ind) {
        if (ind != null && ind.length > 0) {
            List<View> link = new ArrayList<>();

            for (int i : ind) {
                int j = store.indexOf(get(i));
                if (j != -1) link.add(store.get(j));
            }

            store.removeAll(link);
            store.addAll(link);
        }

        return super.remove(ind);
    }

    /**
     * Show a stored View
     *
     * @param i the position where insert the stored View
     * @return if exists, the stored View, otherwise null
     */

    public View show(int i) {
        int ls = size();
        int cs = store.size();
        if (i >= 0 && i <= ls && cs > 0 && ls < cs && super.add(i, store.get(ls))) {
            return store.get(ls);
        }
        return null;
    }

    /**
     * Show a stored View
     *
     * @return if exists, the stored View, otherwise null
     */

    public View show() {
        return show(size());
    }

    @Override
    protected void updatePointers(boolean update) {
        View.updatePointers(vBar, update);
        View.updatePointers(hBar, update);
        super.updatePointers(update);
    }

    @Override
    protected void draw(Graphic graphic) {
        // apply aligner
        if (aligner != null) {
            for (int i = 0; i < size(); i++) {
                aligner.align(get(i), i);
            }
        }

        super.draw(graphic);

        float w = width();
        float h = height();
        float lengthX = Math.max(0, getXScrollLength());
        float lengthY = Math.max(0, getYScrollLength());
        boolean drawBar = hasShape();

        // update horizontal bar
        hBar.setScrollFactor(getXScrollFactor());
        hBar.setRange(0, lengthX);
        hBar.setDimension(13f, 0.9f * w);
        hBar.setCenter(x() - 0.05f * w, y() + (h - hBar.height()) / 2f);
        hBar.setVisible(drawBar && lengthX > 1);

        View.update(hBar);
        View.draw(hBar, graphic);

        // update vertical bar
        vBar.setScrollFactor(getYScrollFactor());
        vBar.setRange(0, lengthY);
        vBar.setDimension(13f, 0.9f * h);
        vBar.setCenter(x() + (w - vBar.width()) / 2f, y() - 0.05f * h);
        vBar.setVisible(drawBar && lengthY > 1);

        View.update(vBar);
        View.draw(vBar, graphic);

        // update scrolling
        super.setScroll(
                lengthX > 0 ? hBar.getValue() : 0,
                lengthY > 0 ? vBar.getValue() : 0);
    }

    /**
     * @return the amount of stored views
     */

    public int sizeStore() {
        return store.size();
    }

    /**
     * @return the {@link Aligner} instance
     */

    public Aligner getAligner() {
        return aligner;
    }

    /**
     * @return the stored views
     */

    public List<View> getStore() {
        return store;
    }

    /**
     * @return the vertical {@link BarScrollView}
     */

    public BarScrollView getVerticalBar() {
        return vBar;
    }

    /**
     * @return the horizontal {@link BarScrollView}
     */

    public BarScrollView getHorizontalBar() {
        return hBar;
    }
}
