package uia.application.math;

import uia.physical.component.utility.ComponentUtility;
import uia.physical.component.WrapperView;
import uia.physical.theme.Theme;
import uia.utility.MathUtility;
import uia.utility.Geometries;
import uia.core.paint.Color;
import uia.core.shape.Shape;
import uia.core.ui.Graphics;
import uia.core.ui.View;


import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

/**
 * Standard UIa component.
 * <br>
 * The purpose of this component is to render multiple data distributions on the screen.
 * By design, it provides a Cartesian plane on which to render the data distributions.
 * In addition, it adjusts the scale of the Cartesian plane to fit all the distribution points.
 * <br>
 * Limitations:
 * <br>
 * 1) it doesn't support zooming in or out;
 * 2) the distribution points are not interactive: this means that nothing happens when the user passes over one of them.
 * <br>
 * Usage notes:
 * <br>
 * 1) to set a new distribution or to manipulate a distribution, use the method: {@link #getDistribution(int)};
 * 2) it is possible to customise the graphical appearance of a distribution using the dedicated methods.
 */

public class UIGraphic extends WrapperView {
    private final List<DrawableDistribution> drawableDistributions;

    private final Shape clipShape;
    private final Shape axis;
    private Color axisColor;

    private float xMax;
    private float yMax;
    private float xMin;
    private float yMin;

    public UIGraphic(View view) {
        super(view);

        drawableDistributions = new ArrayList<>();

        clipShape = new Shape();

        axis = new Shape();
        axis.setGeometry(Geometries.rect(axis.getGeometry()));

        axisColor = Theme.BLACK;

        reset();
    }

    /*
     * Calculates, for each distribution, the nearest point to the mouse position.
     * <br>
     * Time required: T(n)
     * <br>
     * Space required: O(n)
     *
     * @param x     the mouse position <b>(relative to this View)</b> along x-axis
     * @param store a not null {@link List} used to store the nearest points
     *

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
        drawableDistributions.clear();
    }

    /**
     * Sets the axis color.
     *
     * @param color the axis color
     * @throws NullPointerException if {@code color == null}
     */

    public void setAxisColor(Color color) {
        Objects.requireNonNull(color);
        this.axisColor = color;
    }

    private void drawAxis(Graphics graphics, float width, float height, float rotation) {
        float[] bounds = getBounds();
        float xDist = width * (MathUtility.normalize(0f, xMin, xMax) - 0.5f);
        float yDist = -height * (MathUtility.normalize(0f, yMin, yMax) - 0.5f);
        float lineX = ComponentUtility.getPositionOnX(bounds[0], bounds[2], xDist, yDist, rotation);
        float lineY = ComponentUtility.getPositionOnY(bounds[1], bounds[3], xDist, yDist, rotation);

        ComponentUtility.makeShapeForClipRegion(this, clipShape, 0.97f, 0.97f);
        graphics.setClip(clipShape);

        // draw ordinate
        axis.setRotation(rotation);
        axis.setPosition(lineX, lineY);
        axis.setDimension(1, 2 * height);
        graphics
                .setShapeColor(axisColor)
                .setShapeBorderWidth(1)
                .drawShape(axis);

        // draw abscissa
        axis.setPosition(lineX, lineY);
        axis.setDimension(2 * width, 1);
        graphics.drawShape(axis);

        graphics.restoreClip();
    }

    @Override
    public void draw(Graphics graphics) {
        super.draw(graphics);

        if (isVisible()) {
            float[] bounds = getBounds();
            float width = 0.95f * getWidth();
            float height = 0.95f * getHeight();
            float rot = bounds[4];

            drawAxis(graphics, width, height, rot);

            for (DrawableDistribution distribution : drawableDistributions) {
                updateMinAndMax(
                        distribution.getMax(PointDistribution.AXIS.X),
                        distribution.getMax(PointDistribution.AXIS.Y)
                );
                updateMinAndMax(
                        distribution.getMin(PointDistribution.AXIS.X),
                        distribution.getMin(PointDistribution.AXIS.Y)
                );
                distribution.draw(graphics, bounds, width, height, rot);
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
}
