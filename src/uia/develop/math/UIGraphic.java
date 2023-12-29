package uia.develop.math;

import uia.application.desktop.ContextSwing;
import uia.core.Shape;
import uia.core.ui.context.Context;
import uia.core.ui.Graphic;
import uia.core.ui.View;
import uia.core.ui.callbacks.OnClick;
import uia.physical.Component;
import uia.core.Paint;
import uia.physical.WrapperView;
import uia.physical.theme.Theme;
import uia.utility.GeometryFactory;
import uia.utility.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Standard UIa component.
 * <br>
 * Component designed to render multiple data distributions.
 */

public class UIGraphic extends WrapperView {
    private final List<DrawableDistribution> drawableDistributions;

    private final Shape clipShape;
    private final Shape axis;
    private final Paint paintAxis;

    private float xMax, yMax;
    private float xMin, yMin;

    public UIGraphic(View view) {
        super(view);

        drawableDistributions = new ArrayList<>();

        clipShape = new Shape();

        axis = new Shape();
        axis.setGeometry(GeometryFactory.rect(axis.getGeometry()));

        paintAxis = new Paint().setStrokeWidth(1);

        reset();
    }

    /**
     * Calculates, for each distribution, the nearest point to the mouse position.
     * <br>
     * Time required: T(n)
     * <br>
     * Space required: O(n)
     *
     * @param x     the mouse position <b>(relative to this View)</b> along x-axis
     * @param store a not null {@link List} used to store the nearest points
     */

    @Deprecated
    public void nearest(float x, List<float[]> store) {
        store.clear();

        int node;
        int pDist;
        float[] bounds = getBounds();
        float offset = bounds[0] - bounds[2] / 2f;

        for (DrawableDistribution distribution : drawableDistributions) {
            node = -1;
            pDist = Integer.MAX_VALUE;
            for (int j = 0; j < distribution.size(); j++) {
                int dist = (int) Math.abs(bounds[0] + bounds[2] * distribution.get(j, PointDistribution.AXIS.X) - (x + offset));
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
                        distribution.get(node, PointDistribution.AXIS.X),
                        distribution.get(node, PointDistribution.AXIS.Y)
                });
            }
        }
    }

    /**
     * @return the {@link Paint} used to paint axis
     */

    public Paint getPaintAxis() {
        return paintAxis;
    }

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
        drawableDistributions.clear();
    }

    private void drawAxis(Graphic graphic, float width, float height, float rotation) {
        float[] bounds = getBounds();
        float xDist = width * (Utility.normalize(0f, xMin, xMax) - 0.5f);
        float yDist = -height * (Utility.normalize(0f, yMin, yMax) - 0.5f);
        float lineX = View.getPositionOnX(bounds[0], bounds[2], xDist, yDist, rotation);
        float lineY = View.getPositionOnY(bounds[1], bounds[3], xDist, yDist, rotation);

        Component.makeShapeForClipRegion(this, clipShape, 0.97f, 0.97f);
        graphic.setClip(clipShape);

        // draw ordinate
        axis.setRotation(rotation);
        axis.setPosition(lineX, lineY);
        axis.setDimension(1, 2 * height);
        graphic.setPaint(paintAxis);
        graphic.drawShape(axis);

        // draw abscissa
        axis.setPosition(lineX, lineY);
        axis.setDimension(2 * width, 1);
        graphic.setPaint(paintAxis);
        graphic.drawShape(axis);

        graphic.restoreClip();
    }

    @Override
    public void draw(Graphic graphic) {
        super.draw(graphic);

        if (isVisible()) {
            float[] bounds = getBounds();
            float width = 0.95f * getWidth();
            float height = 0.95f * getHeight();
            float rot = bounds[4];

            drawAxis(graphic, width, height, rot);

            for (DrawableDistribution distribution : drawableDistributions) {
                updateMinAndMax(
                        distribution.getMax(PointDistribution.AXIS.X),
                        distribution.getMax(PointDistribution.AXIS.Y)
                );
                updateMinAndMax(
                        distribution.getMin(PointDistribution.AXIS.X),
                        distribution.getMin(PointDistribution.AXIS.Y)
                );
                distribution.draw(graphic, bounds, width, height, rot);
            }
        }
    }

    /**
     * @return the number of distributions
     */

    public int distributions() {
        return drawableDistributions.size();
    }

    /**
     * Return the specified {@link DrawableDistribution}.
     *
     * @param i the index of the distribution
     * @return the specified distribution if {@code i <= distributions()} otherwise null
     */

    public DrawableDistribution getDistribution(int i) {
        if (i >= 0 && i < distributions()) {
            return drawableDistributions.get(i);
        } else if (i == distributions()) {
            DrawableDistribution result = new DrawableDistribution();
            drawableDistributions.add(result);
            return result;
        }
        return null;
    }

    public static void main(String[] args) {
        UIGraphic uiGraphic = new UIGraphic(
                new Component("", 0.5f, 0.5f, 0.5f, 0.5f)
        );
        uiGraphic.registerCallback((OnClick) p -> {
            uiGraphic.setRotation(uiGraphic.getBounds()[4] + 0.05f);
        });
        uiGraphic.getDistribution(0)
                .setLinePaint(new Paint()
                        .setStrokeWidth(6)
                        .setStrokeColor(Theme.ROYAL_BLUE))
                .add(0f, 0f)
                .add(-100f, -100f)
                .add(-100, 1000)
                .add(2000, 1200);

        Context context = ContextSwing.createAndStart(1800, 900);
        context.setView(uiGraphic);
    }
}
