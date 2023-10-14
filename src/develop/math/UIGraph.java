package develop.math;

import develop.math.data.PointDistribution;
import uia.application.awt.ContextAWT;
import uia.core.Geometry;
import uia.core.Shape;
import uia.core.ui.Context;
import uia.core.ui.Graphic;
import uia.core.ui.View;
import uia.core.ui.callbacks.OnClick;
import uia.physical.Component;
import uia.core.Paint;
import uia.physical.theme.Theme;
import uia.physical.wrapper.WrapperView;
import uia.utility.Figure;

import java.util.ArrayList;
import java.util.List;

/**
 * Component designed to display multiple data distributions on screen
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

    private final Shape axis;
    private final Paint paintAxis;

    private float xMax, yMax;
    private float xMin, yMin;

    public UIGraph(View view) {
        super(view);

        buildGeometry(g -> View.buildRect(g, getWidth(), getHeight(), Figure.STD_ROUND), true);

        stdDrawable = new DrawableDistribution();

        data = new ArrayList<>();

        axis = new Shape();
        axis.setGeometry(Figure.rect(axis.getGeometry()));

        paintAxis = new Paint().setStrokeWidth(2);

        reset();
    }

    /*
     * Calculate, for every distribution, the nearest point respect to the mouse position.
     * <br>
     * Time required: T(n)
     * <br>
     * Space required: O(n)
     *
     * @param x     the mouse position <b>(relative to this View)</b> along x-axis
     * @param store a not null {@link List} used to store the nearest points
     *

    @Deprecated
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
                int dist = (int) abs(this.bounds[0] + this.bounds[2] * dis.get(j, PointDistribution.AXIS.X) - (x + offset));

                if (dist < pDist) {
                    node = j;
                    pDist = dist;
                }
            }

            // save data
            if (node == -1) {
                store.add(new float[0]);
            } else {
                store.add(new float[]{
                        dis.get(node, PointDistribution.AXIS.X),
                        dis.get(node, PointDistribution.AXIS.Y)
                });
            }
        }
    }*/

    /**
     * Update the attributes of this graphic
     *
     * @param x the last x point
     * @param y the last y point
     */

    private void updateMinAndMax(float x, float y) {
        if (x > xMax) xMax = x;
        if (x < xMin) xMin = x;
        if (y > yMax) yMax = y;
        if (y < yMin) yMin = y;
    }

    /**
     * Reset the graphic attributes
     */

    private void reset() {
        xMax = yMax = -Integer.MAX_VALUE;
        xMin = yMin = Integer.MAX_VALUE;
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

    private static float normalize(float val, float min, float max) {
        return (val - min) / (max - min);
    }

    private final Shape clipShape = new Shape();

    private void drawAxis(Graphic graphic, float[] bounds,
                          float width, float height, float rotation) {
        clipShape.setGeometry(getGeometry());
        clipShape.setPosition(bounds[0] + bounds[2] / 2f, bounds[1] + bounds[3] / 2f);
        clipShape.setDimension(0.97f * getWidth(), 0.97f * getHeight());
        clipShape.setRotation(bounds[4]);

        float xDist = width * (normalize(0f, xMin, xMax) - 0.5f);
        float yDist = -height * (normalize(0f, yMin, yMax) - 0.5f);

        float gx = View.getPositionOnX(bounds[0], bounds[2], xDist, yDist, rotation);
        float gy = View.getPositionOnY(bounds[1], bounds[3], xDist, yDist, rotation);

        graphic.setClip(clipShape);

        // to refactor: draw ordinate axis
        axis.setRotation(rotation);
        axis.setPosition(gx, gy);//x - gWidth / 2f, y);
        axis.setDimension(2, 2 * height);
        graphic.setPaint(paintAxis);
        graphic.drawShape(axis);

        // to refactor: draw abscissa axis
        axis.setPosition(gx, gy);//x, y + gHeight / 2f);
        axis.setDimension(2 * width, 2);
        graphic.setPaint(paintAxis);
        graphic.drawShape(axis);

        graphic.restoreClip();

        /*Shape cross = new Shape();
            cross.setGeometry(Figure.rect(cross.getGeometry()));
            cross.setPosition(gx, gy);
            cross.setDimension(15, 15);
            graphic.drawShape(cross);*/
    }

    @Override
    public void draw(Graphic graphic) {
        super.draw(graphic);

        if (isVisible()) {
            float[] bounds = bounds();
            float width = 0.95f * getWidth();
            float height = 0.95f * getHeight();
            float rot = bounds[4];

            drawAxis(graphic, bounds, width, height, rot);

            for (Pair<PointDistribution, DrawableDistribution> i : data) {
                PointDistribution dis = i.val1;
                updateMinAndMax(dis.getMax(PointDistribution.AXIS.X), dis.getMax(PointDistribution.AXIS.Y));
                updateMinAndMax(dis.getMin(PointDistribution.AXIS.X), dis.getMin(PointDistribution.AXIS.Y));

                DrawableDistribution drw = i.val2;
                drw.draw(graphic, dis, bounds, width, height, rot);
            }
        }
    }

    /**
     * @return the number of distributions
     */

    public int distributions() {
        return data.size();
    }

    /**
     * Return the specified {@link PointDistribution}.
     *
     * @param i the index of the distribution
     * @return the specified distribution if {@code i <= distributions()} else null
     */

    public PointDistribution getDistribution(int i) {
        if (i >= 0 && i < distributions()) {
            return data.get(i).val1;
        } else if (i == distributions()) {
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
     * A DrawableDistribution defines the graphical settings to draw a {@link PointDistribution} on screen.
     */

    public static class DrawableDistribution {
        private final Paint paintLine, paintPoint;
        private final Shape shapeMarker, shapeLine;

        private float pointDimension = 4;
        private boolean enableLine = true;
        private boolean enablePoint = true;

        public DrawableDistribution() {
            paintLine = new Paint()
                    .setColor(Theme.BLACK)
                    .setStrokeWidth(3);

            paintPoint = new Paint().setColor(Theme.RED);

            shapeMarker = new Shape();
            shapeMarker.setGeometry(Figure.rect(shapeMarker.getGeometry()));

            shapeLine = new Shape();
            shapeLine.setGeometry(Figure.rect(shapeLine.getGeometry()));
        }

        /**
         * Set the point dimension
         *
         * @param pointDim a value {@code >= 0}
         */

        public DrawableDistribution setPointDimension(int pointDim) {
            this.pointDimension = Math.max(pointDim, 0);
            return this;
        }

        /**
         * Set the line color
         *
         * @param color a not null {@link uia.core.Paint.Color}
         */

        public DrawableDistribution setColorLine(Paint.Color color) {
            paintLine.setColor(color);
            return this;
        }

        /**
         * Test
         */

        public DrawableDistribution setLineWidth(int val) {
            paintLine.setStrokeWidth(val);
            return this;
        }

        /**
         * Set the point color
         *
         * @param color a not null {@link uia.core.Paint.Color}
         */

        public DrawableDistribution setColorPoint(Paint.Color color) {
            paintPoint.setColor(color);
            return this;
        }

        /**
         * Set the marker's geometry
         *
         * @param marker a not null {@link Geometry}
         */

        public DrawableDistribution setMarker(Geometry marker) {
            Geometry markerGeometry = shapeMarker.getGeometry();
            markerGeometry.clear();

            for (int i = 0; i < marker.vertices(); i++) {
                Geometry.Vertex vertex = marker.get(i);
                markerGeometry.addVertex(vertex.getX(), vertex.getY());
            }
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

        public void draw(Graphic graphic, PointDistribution pointDistribution,
                         float[] bounds, float width, float height, float rotation) {
            if (enableLine) {
                graphic.setPaint(paintLine);

                /*for (int i = 0; i < pointDistribution.size() - 1; i++) {
                    graphic.openShape(
                            (int) (bounds[0] + bounds[2] * pointDistribution.get(i, 0)),
                            (int) (bounds[1] - bounds[3] * pointDistribution.get(i, 1)));
                    graphic.vertex(
                            (int) (bounds[0] + bounds[2] * pointDistribution.get(i + 1, 0)),
                            (int) (bounds[1] - bounds[3] * pointDistribution.get(i + 1, 1)));
                    graphic.closeShape();
                }*/
            }

            if (enablePoint) {
                graphic.setPaint(paintPoint);

                float xMin = pointDistribution.getMin(PointDistribution.AXIS.X);
                float yMin = pointDistribution.getMin(PointDistribution.AXIS.Y);

                float xMax = pointDistribution.getMax(PointDistribution.AXIS.X);
                float yMax = pointDistribution.getMax(PointDistribution.AXIS.Y);

                for (int i = 0; i < pointDistribution.size(); i++) {
                    float px = pointDistribution.get(i, PointDistribution.AXIS.X);
                    float py = pointDistribution.get(i, PointDistribution.AXIS.Y);

                    float xDist = width * (normalize(px, xMin, xMax) - 0.5f);
                    float yDist = -height * (normalize(py, yMin, yMax) - 0.5f);

                    shapeMarker.setPosition(
                            View.getPositionOnX(bounds[0], bounds[2], xDist, yDist, rotation),
                            View.getPositionOnY(bounds[1], bounds[3], xDist, yDist, rotation)
                    );
                    shapeMarker.setDimension(pointDimension, pointDimension);

                    graphic.drawShape(shapeMarker);
                }
            }
        }
    }

    //

    public static void main(String[] args) {
        UIGraph uiGraph = new UIGraph(
                new Component("", 0.5f, 0.5f, 0.5f, 0.5f)
        );
        uiGraph.addCallback((OnClick) p -> {
            uiGraph.setRotation(uiGraph.bounds()[4] + 0.05f);
        });
        uiGraph.getDistribution(0)
                .add(0f, 0f)
                .add(-10f, -10f);
                //.add(100, 1000);*/
        //.add(2000, 1200);
        //uiGraph.setRotation(0.15f);

        Context context = new ContextAWT(1800, 900);
        context.start();
        context.setView(uiGraph);
    }
}
