package uia.develop.math;

import uia.application.desktop.ContextSwing;
import uia.core.Geometry;
import uia.core.Shape;
import uia.core.basement.Drawable;
import uia.core.ui.context.Context;
import uia.core.ui.Graphic;
import uia.core.ui.View;
import uia.core.ui.callbacks.OnClick;
import uia.physical.Component;
import uia.core.Paint;
import uia.physical.theme.Theme;
import uia.physical.WrapperView;
import uia.utility.GeometryFactory;
import uia.utility.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Standard UIa component.
 * <br>
 * Component designed to display multiple data distributions.
 */

public class UIGraphic extends WrapperView {

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

    public UIGraphic(View view) {
        super(view);

        setGeometry(g -> Drawable.buildRect(g,
                getWidth(),
                getHeight(),
                GeometryFactory.STD_ROUND
        ), true);

        stdDrawable = new DrawableDistribution();

        data = new ArrayList<>();

        axis = new Shape();
        axis.setGeometry(GeometryFactory.rect(axis.getGeometry()));

        paintAxis = new Paint().setStrokeWidth(1);

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
     * Clears all the distributions
     */

    public void clear() {
        reset();
        data.clear();
    }

    /**
     * Sets a DistributionDrawable for the specified distribution
     *
     * @param i   the distribution's position
     * @param drw a not null {@link DrawableDistribution}
     * @throws IndexOutOfBoundsException if {@code i < 0 || i >= size()}
     */

    public void setDrawable(int i, DrawableDistribution drw) {
        data.get(i).val2 = drw;
    }

    private final Shape clipShape = new Shape();

    private void drawAxis(Graphic graphic, float[] bounds,
                          float width, float height, float rotation) {
        clipShape.setGeometry(getGeometry());
        clipShape.setPosition(bounds[0] + bounds[2] / 2f, bounds[1] + bounds[3] / 2f);
        clipShape.setDimension(0.97f * getWidth(), 0.97f * getHeight());
        clipShape.setRotation(bounds[4]);

        float xDist = width * (Utility.normalize(0f, xMin, xMax) - 0.5f);
        float yDist = -height * (Utility.normalize(0f, yMin, yMax) - 0.5f);

        float gx = View.getPositionOnX(bounds[0], bounds[2], xDist, yDist, rotation);
        float gy = View.getPositionOnY(bounds[1], bounds[3], xDist, yDist, rotation);

        graphic.setClip(clipShape);

        // to refactor: draw ordinate axis
        axis.setRotation(rotation);
        axis.setPosition(gx, gy);//x - gWidth / 2f, y);
        axis.setDimension(1, 2 * height);
        graphic.setPaint(paintAxis);
        graphic.drawShape(axis);

        // to refactor: draw abscissa axis
        axis.setPosition(gx, gy);//x, y + gHeight / 2f);
        axis.setDimension(2 * width, 1);
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
            float[] bounds = getBounds();
            float width = 0.95f * getWidth();
            float height = 0.95f * getHeight();
            float rot = bounds[4];

            drawAxis(graphic, bounds, width, height, rot);

            for (Pair<PointDistribution, DrawableDistribution> i : data) {
                PointDistribution dis = i.val1;
                updateMinAndMax(dis.getMax(PointDistribution.AXIS.X), dis.getMax(PointDistribution.AXIS.Y));
                updateMinAndMax(dis.getMin(PointDistribution.AXIS.X), dis.getMin(PointDistribution.AXIS.Y));

                DrawableDistribution drw = i.val2;
                drw.draw(graphic, bounds, width, height, rot);
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

    public static void main(String[] args) {
        UIGraphic uiGraphic = new UIGraphic(
                new Component("", 0.5f, 0.5f, 0.5f, 0.5f)
        );
        uiGraphic.registerCallback((OnClick) p -> {
            uiGraphic.setRotation(uiGraphic.getBounds()[4] + 0.05f);
        });
        uiGraphic.getDistribution(0)
                .add(0f, 0f)
                .add(-100f, -100f)
                .add(-100, 1000)
                .add(2000, 1200);
        //uiGraph.setRotation(0.15f);

        Context context = ContextSwing.createAndStart(1800, 900);
        context.setView(uiGraphic);
    }
}
