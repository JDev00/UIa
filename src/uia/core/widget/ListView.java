package uia.core.widget;

import uia.core.View;
import uia.core.event.Event;
import uia.core.platform.policy.Context;
import uia.core.platform.policy.Graphic;

/**
 * ListView is an implementation of {@link Widget} skeleton, it is mainly used to render
 * and handle multiple views with two scrollbars already attached.
 */

public class ListView extends Widget {
    private BarScrollView hBar;
    private BarScrollView vBar;

    private Aligner aligner;

    public ListView(Context context, float x, float y, float width, float height) {
        super(context, x, y, width, height);
        super.setExpansion(0f, 0f);
        getEventQueue()
                .addEvent((v, s) -> {
                    PointerEvent e = v.getPointerEvent();
                    if (s == Event.POINTER_HOVER && e.isMouseWheeling()) vBar.scroll(e.getWheelRotation() < 0);
                });

        hBar = new BarScrollView(context, 0f, 0f, 1f, 1f);
        hBar.setExpansion(0f, 0f);
        hBar.setVertical(false);
        hBar.setPositionAdjustment(false);
        hBar.setDimensionAdjustment(false);

        vBar = new BarScrollView(context, 0f, 0f, 1f, 1f);
        vBar.setExpansion(0f, 0f);
        vBar.setPositionAdjustment(false);
        vBar.setDimensionAdjustment(false);

        float[] sum = {0f};
        aligner = (v, i) -> {
            float h = 1.05f * v.height() / 2f;
            if (i == 0) sum[0] = -height() / 2f;
            sum[0] += h;
            v.setPosition(0f, sum[0]);
            sum[0] += h;
        };
    }

    /**
     * Aligner is used to align a set of views
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
     * @param aligner an {@link Aligner}; it could be null
     */

    public void setAligner(Aligner aligner) {
        this.aligner = aligner;
    }

    /**
     * Set a new BarScrollView to this ListView.
     * <br>
     * Old bar's value will be kept and the new bar will be automatically assigned according to its orientation.
     * <br>
     * In addition, the following methods will be set to false:
     * <br>
     * 1) {@link #setPositionAdjustment(boolean)}
     * <br>
     * 2) {@link #setDimensionAdjustment(boolean)}
     *
     * @param barScrollView a not null {@link BarScrollView}
     */

    public void setScrollerView(BarScrollView barScrollView) {
        if (barScrollView != null) {

            if (barScrollView.isVertical()) {
                float val = vBar.getValue();
                vBar = barScrollView;
                vBar.setValue(val);
                vBar.setPositionAdjustment(false);
                vBar.setDimensionAdjustment(false);
            } else {
                float val = hBar.getValue();
                hBar = barScrollView;
                hBar.setValue(val);
                hBar.setPositionAdjustment(false);
                hBar.setDimensionAdjustment(false);
            }
        }
    }

    @Override
    public void setScroll(float x, float y) {
        hBar.setValue(x);
        vBar.setValue(y);
    }

    @Override
    protected void updatePointers(boolean update) {
        View.updatePointers(vBar, update);
        View.updatePointers(hBar, update);
        super.updatePointers(update);
    }

    @Override
    protected void update() {
        super.update();

        if (aligner != null) {// apply aligner
            for (int i = 0; i < size(); i++) {
                aligner.align(get(i), i);
            }
        }
    }

    @Override
    protected void draw(Graphic graphic) {
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
     * @return the {@link Aligner} instance
     */

    public Aligner getAligner() {
        return aligner;
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
