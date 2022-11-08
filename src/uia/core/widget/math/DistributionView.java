package uia.core.widget.math;

import uia.core.View;
import uia.core.platform.independent.paint.Paint;
import uia.core.platform.policy.Context;
import uia.core.platform.policy.Graphic;
import uia.core.widget.math.data.Distribution;
import uia.core.widget.math.data.DistributionDrawable;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

/**
 * Widget used to draw data on a 2D graphic
 */

public class DistributionView extends View {

    /**
     * Internal data structure used to represent a pair
     */
    private static class Pair<T, V> {
        T val1;
        V val2;

        public Pair(T val1, V val2) {
            this.val1 = val1;
            this.val2 = val2;
        }
    }

    private final List<Pair<Distribution, DistributionDrawable>> data;

    private final DistributionDrawable stdDrawable;

    private final Paint paintAxis;

    private float xMax;
    private float xMin;
    private float yMax;
    private float yMin;

    private float xCenter;
    private float yCenter;
    private float xScale;
    private float yScale;
    private float gXScale = 0.95f;
    private float gYScale = 0.95f;
    private float xOff;
    private float yOff;

    public DistributionView(Context context, float x, float y, float width, float height) {
        super(context, x, y, width, height);
        super.setExpansion(0f, 0f);

        paintAxis = new Paint(255, 0, 0).setStrokesWidth(2);

        stdDrawable = new DistributionDrawable();

        data = new ArrayList<>();

        reset();
    }

    /**
     * Calculate, for every distribution, the nearest point respect to the mouse position.
     * <br>
     * Time required: T(n)
     * <br>
     * Space required: O(n)
     *
     * @param pointerX the mouse position <b>(relative to this View)</b> along x-axis
     * @param store    a not null {@link List} used to store the nearest points
     */

    protected void nearest(float pointerX, List<float[]> store) {
        store.clear();

        int node;
        int pDist;
        float offset = x() - width() / 2f;

        for (Pair<Distribution, DistributionDrawable> i : data) {
            node = -1;
            pDist = Integer.MAX_VALUE;

            Distribution dis = i.val1;

            for (int j = 0; j < dis.size(); j++) {
                int dist = (int) abs(xCenter + xScale * dis.get(j, 0) - (pointerX + offset));

                if (dist < pDist) {
                    node = j;
                    pDist = dist;
                }
            }

            // save data
            if (node == -1) {
                store.add(new float[0]);
            } else {
                store.add(new float[]{dis.get(node, 0), dis.get(node, 1)});
            }
        }
    }

    /**
     * Reset the graphic attributes
     */

    private void reset() {
        xMax = yMax = -Integer.MAX_VALUE;
        xMin = yMin = Integer.MAX_VALUE;
        xCenter = yCenter = 0;
        xScale = yScale = 0;
    }

    /**
     * Update the attributes of this graphic
     *
     * @param x the last x point
     * @param y the last y point
     */

    private void updateMaxMin(float x, float y) {
        if (x > xMax) xMax = x;
        if (x < xMin) xMin = x;
        if (y > yMax) yMax = y;
        if (y < yMin) yMin = y;
    }

    /**
     * Clear all distributions
     */

    public void clear() {
        reset();
        data.clear();
    }

    /**
     * Set the graphic scale
     *
     * @param xScale the scale along x-axis
     * @param yScale the scale along y-axis
     */

    public void setGraphicScale(float xScale, float yScale) {
        gXScale = Math.max(0, xScale);
        gYScale = Math.max(0, yScale);
    }

    /**
     * Move graphic across this view.
     * <br>
     * Note that the params are relative to this view's dimension.
     *
     * @param x the relative offset along x-axis
     * @param y the relative offset along y-axis
     */

    public void setGraphicOffset(float x, float y) {
        xOff = x;
        yOff = y;
    }

    /**
     * Set a DistributionDrawable for the specified distribution
     *
     * @param i   the distribution's position
     * @param drw a not null {@link DistributionDrawable}
     */

    public void setDrawable(int i, DistributionDrawable drw) {
        if (i >= 0 && i < data.size() && drw != null) data.get(i).val2 = drw;
    }

    @Override
    protected void draw(Graphic graphic) {
        super.draw(graphic);

        float x = x() + xOff * width();
        float y = y() + yOff * height();
        float gWidth = gXScale * width();
        float gHeight = gYScale * height();

        float cx = abs(xMin) + abs(xMax);
        float cy = abs(yMin) + abs(yMax);

        if (Float.compare(cx, 0) == 0) cx = 1;
        if (Float.compare(cy, 0) == 0) cy = 1;

        xScale = gWidth / cx;
        yScale = gHeight / cy;
        xCenter = x + gWidth * ((1 - (xMax + xMin) / cx) / 2 - 0.5f);
        yCenter = y + gHeight * ((1 + (yMax + yMin) / cy) / 2 - 0.5f);

        for (Pair<Distribution, DistributionDrawable> i : data) {
            Distribution dis = i.val1;
            updateMaxMin(dis.getMax(0), dis.getMax(1));
            updateMaxMin(dis.getMin(0), dis.getMin(1));

            DistributionDrawable drw = i.val2;
            drw.setParams(xCenter, yCenter, xScale, yScale);
            drw.draw(graphic, dis);
        }

        graphic.setPaint(paintAxis);
        graphic.drawLine(
                x - gWidth / 2f, yCenter,
                x + gWidth / 2f, yCenter);
        graphic.drawLine(
                xCenter, y - gHeight / 2f,
                xCenter, y + gHeight / 2f);
    }

    /**
     * @return the axis center along x-axis
     */

    protected float getCenterX() {
        return xCenter;
    }

    /**
     * @return the axis center along y-axis
     */

    protected float getCenterY() {
        return yCenter;
    }

    /**
     * @return the distributions scale along x-axis used to fit this view
     */

    protected float getScaleX() {
        return xScale;
    }

    /**
     * @return the distributions scale along y-axis used to fit this view
     */

    protected float getScaleY() {
        return yScale;
    }

    /**
     * @return the graphic's scale along x-axis
     */

    public float getXScaleGraph() {
        return gXScale;
    }

    /**
     * @return the graphic's scale along y-axis
     */

    public float getYScaleGraph() {
        return gYScale;
    }

    /**
     * @return the number of distributions
     */

    public int size() {
        return data.size();
    }

    /**
     * @return a new array filled with the maximum point's coordinates
     */

    public float[] getMax() {
        return new float[]{xMax, yMax};
    }

    /**
     * @return a new array filled with the minimum point's coordinates
     */

    public float[] getMin() {
        return new float[]{xMin, yMin};
    }

    /**
     * Return the specified {@link Distribution}.
     * <br>
     * If the specified distribution doesn't exist, two policies are adopted:
     * <br>
     * 1) if {@code i} is equal to {@link #size()}, a new distribution will be added and then returned,
     * <br>
     * 2) if {@code i} is greater than {@link #size()}, {@code null} will be returned.
     *
     * @param i the distribution's position
     * @return the specified distribution or null
     */

    public Distribution getDistribution(int i) {
        if (i >= 0 && i < size()) {
            return data.get(i).val1;
        } else if (i == size()) {
            Distribution dis = new Distribution();
            data.add(new Pair<>(dis, stdDrawable));
            return dis;
        }
        return null;
    }

    /**
     * @return the {@link Paint} used to color axis
     */

    public Paint getPaintAxis() {
        return paintAxis;
    }
}
