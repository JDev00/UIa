package develop.math;

import develop.math.data.PointDistribution;
import uia.core.Geometry;
import uia.core.Shape;
import uia.core.ui.Graphic;
import uia.core.ui.View;
import uia.physical.Component;
import uia.core.Paint;
import uia.physical.theme.Theme;
import uia.physical.wrapper.WrapperView;
import uia.utility.Figure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;

/**
 * Component designed to draw multiple data distributions on screen
 */

public class UIGraph extends WrapperView {

    /**
     * Internal data structure used to handle a pair
     */
    private static class Pair<T, V> {
        T val1;
        V val2;

        public Pair(T val1, V val2) {
            this.val1 = val1;
            this.val2 = val2;
        }
    }

    private final List<Pair<PointDistribution, DrawableDistribution>> data;

    private final DrawableDistribution stdDrawable;

    private final Paint paintAxis;

    private float xMax, yMax;
    private float xMin, yMin;

    private final float[] bounds;

    public UIGraph(View view) {
        super(view);

        buildGeometry(g -> Component.buildRect(g, getWidth(), getHeight(), Figure.STD_ROUND), true);

        bounds = new float[]{0f, 0f, 1f, 1f};

        paintAxis = new Paint()
                .setColor(Theme.RED)
                .setStrokeWidth(2);

        stdDrawable = new DrawableDistribution();

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
     * @param x     the mouse position <b>(relative to this View)</b> along x-axis
     * @param store a not null {@link List} used to store the nearest points
     */

    protected void nearest(float x, List<float[]> store) {
        store.clear();

        int node;
        int pDist;
        float[] bounds = bounds();
        float offset = bounds[0] - bounds[2] / 2f;

        for (Pair<PointDistribution, DrawableDistribution> i : data) {
            node = -1;
            pDist = Integer.MAX_VALUE;

            PointDistribution dis = i.val1;

            for (int j = 0; j < dis.size(); j++) {
                int dist = (int) abs(this.bounds[0] + this.bounds[2] * dis.get(j, 0) - (x + offset));

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
        Arrays.fill(bounds, 0);
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
     * Set a DistributionDrawable for the specified distribution
     *
     * @param i   the distribution's position
     * @param drw a not null {@link DrawableDistribution}
     * @throws IndexOutOfBoundsException if {@code i < 0 || i >= size()}
     */

    public void setDrawable(int i, DrawableDistribution drw) {
        data.get(i).val2 = drw;
    }

    @Override
    public void draw(Graphic graphic) {
        super.draw(graphic);

        if (isVisible()) {
            float[] bounds = bounds();
            float x = bounds[0] + xOff * bounds[2];
            float y = bounds[1] + yOff * bounds[3];
            float gWidth = gXScale * bounds[2];
            float gHeight = gYScale * bounds[3];

            float cx = abs(xMin) + abs(xMax);
            float cy = abs(yMin) + abs(yMax);

            if (Float.compare(cx, 0) == 0) cx = 1;
            if (Float.compare(cy, 0) == 0) cy = 1;

            bounds[2] = gWidth / cx;
            bounds[3] = gHeight / cy;
            bounds[0] = x + gWidth * ((1 - (xMax + xMin) / cx) / 2 - 0.5f);
            bounds[1] = y + gHeight * ((1 + (yMax + yMin) / cy) / 2 - 0.5f);

            for (Pair<PointDistribution, DrawableDistribution> i : data) {
                PointDistribution dis = i.val1;
                updateMaxMin(dis.getMax(0), dis.getMax(1));
                updateMaxMin(dis.getMin(0), dis.getMin(1));

                DrawableDistribution drw = i.val2;
                drw.update(bounds[0], bounds[1], bounds[2], bounds[3]);
                drw.draw(graphic, dis);
            }

            // draw axis
            graphic.setPaint(paintAxis);
            graphic.openShape(x - gWidth / 2f, bounds[1]);
            graphic.vertex(x + gWidth / 2f, bounds[1]);
            graphic.closeShape();

            graphic.openShape(bounds[0], y - gHeight / 2f);
            graphic.vertex(bounds[0], y + gHeight / 2f);
            graphic.closeShape();
        }
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
     * Return the specified {@link PointDistribution}.
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

    public PointDistribution getDistribution(int i) {
        if (i >= 0 && i < size()) {
            return data.get(i).val1;
        } else if (i == size()) {
            PointDistribution dis = new PointDistribution();
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

    //

    /**
     * TODO: define DrawableDistribution
     */

    public static class DrawableDistribution {
        private final Shape shapeMarker;

        private final Paint cLine;
        private final Paint cPoint;

        private float pointDim = 4;

        private final float[] bounds;

        private boolean enableLine = true;
        private boolean enablePoint = true;

        public DrawableDistribution() {
            bounds = new float[]{0f, 0f, 1f, 1f};

            cLine = new Paint()
                    .setColor(Theme.BLACK)
                    .setStrokeWidth(3);

            cPoint = new Paint().setColor(Theme.RED);

            shapeMarker = new Shape();
            shapeMarker.setGeometry(Figure.rect(new Geometry()));
        }

        /**
         * Update the drawable's bounds
         */

        public void update(float x, float y, float xScale, float yScale) {
            bounds[0] = x;
            bounds[1] = y;
            bounds[2] = xScale;
            bounds[3] = yScale;
        }

        /**
         * Set the point dimension
         *
         * @param pointDim a value {@code >= 0}
         */

        public DrawableDistribution setPointDim(int pointDim) {
            this.pointDim = Math.max(pointDim, 0);
            return this;
        }

        /**
         * Set the line color
         *
         * @param color a not null {@link uia.core.Paint.Color}
         */

        public DrawableDistribution setColorLine(Paint.Color color) {
            cLine.setColor(color);
            return this;
        }

        /**
         * Test
         */

        public DrawableDistribution setLineWidth(int val) {
            cLine.setStrokeWidth(val);
            return this;
        }

        /**
         * Set the point color
         *
         * @param color a not null {@link uia.core.Paint.Color}
         */

        public DrawableDistribution setColorPoint(Paint.Color color) {
            cPoint.setColor(color);
            return this;
        }

        /**
         * Set the marker's geometry
         *
         * @param marker a not null {@link Geometry}
         */

        public DrawableDistribution setMarker(Geometry marker) {
            shapeMarker.setGeometry(marker);
            return this;
        }

        /**
         * Enable or disable the line that connects each point
         *
         * @param enableLine true to enable the line
         */

        public DrawableDistribution enableLine(boolean enableLine) {
            this.enableLine = enableLine;
            return this;
        }

        /**
         * Enable or disable the drawing of points
         *
         * @param enablePoint true to draw points
         */

        public DrawableDistribution enablePoint(boolean enablePoint) {
            this.enablePoint = enablePoint;
            return this;
        }

        /**
         * Draw the given data distribution
         *
         * @param graphic           the {@link Graphic} object
         * @param pointDistribution the {@link PointDistribution} to draw
         */

        private void draw(Graphic graphic, PointDistribution pointDistribution) {
            if (enableLine) {
                graphic.setPaint(cLine);

                // draw line between points
                for (int i = 0; i < pointDistribution.size() - 1; i++) {
                    graphic.openShape(
                            (int) (bounds[0] + bounds[2] * pointDistribution.get(i, 0)),
                            (int) (bounds[1] - bounds[3] * pointDistribution.get(i, 1)));
                    graphic.vertex(
                            (int) (bounds[0] + bounds[2] * pointDistribution.get(i + 1, 0)),
                            (int) (bounds[1] - bounds[3] * pointDistribution.get(i + 1, 1)));
                    graphic.closeShape();
                }
            }

            if (enablePoint) {
                graphic.setPaint(cPoint);

                // draw points
                for (int i = 0; i < pointDistribution.size(); i++) {
                    float x = bounds[0] + bounds[2] * pointDistribution.get(i, 0);
                    float y = bounds[1] - bounds[3] * pointDistribution.get(i, 1);

                    marker.x = x;
                    marker.y = y;
                    marker.scaleX = marker.scaleY = pointDim;
                    graphic.drawGeom(marker);
                }
            }
        }
    }
}
