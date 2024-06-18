package uia.application.math;

import uia.physical.component.utility.ComponentUtility;
import uia.core.ui.primitives.geometry.Transform;
import uia.core.ui.primitives.geometry.Geometry;
import uia.core.ui.primitives.color.Color;
import uia.physical.theme.Theme;
import uia.utility.MathUtility;
import uia.utility.Geometries;
import uia.core.rendering.Graphics;

import java.util.Objects;

/**
 * The DrawableDistribution defines the graphical settings to draw a {@link PointDistribution} on screen.
 */

public class DrawableDistribution extends PointDistribution {
    private final Transform markerTransform;
    private final Geometry geometryMarker;
    private Color lineColor;
    private Color pointColor;
    private float lineWidth = 2;
    private float pointSize = 4;

    private boolean enableLine = true;
    private boolean enablePoint = true;

    public DrawableDistribution() {
        geometryMarker = Geometries.rect(new Geometry());

        markerTransform = new Transform();

        lineColor = Theme.BLACK;
        pointColor = Theme.RED;
    }

    // point

    /**
     * Enables or disables the drawing of points.
     *
     * @param enablePoint true to draw points
     * @return this DrawableDistribution
     */

    public DrawableDistribution enablePoint(boolean enablePoint) {
        this.enablePoint = enablePoint;
        return this;
    }

    /**
     * Sets the point size.
     *
     * @param pointSize the point size greater than or equal to zero
     * @return this DrawableDistribution
     */

    public DrawableDistribution setPointSize(float pointSize) {
        this.pointSize = Math.max(pointSize, 0);
        return this;
    }

    /**
     * Sets the point color.
     *
     * @param color the point color
     * @return this DrawableDistribution
     * @throws NullPointerException if {@code color == null}
     */

    public DrawableDistribution setPointColor(Color color) {
        Objects.requireNonNull(color);
        this.pointColor = color;
        return this;
    }

    // line

    /**
     * Enables or disables the line that connects each point.
     *
     * @param enableLine true to enable the line
     * @return this DrawableDistribution
     */

    public DrawableDistribution enableLine(boolean enableLine) {
        this.enableLine = enableLine;
        return this;
    }

    /**
     * Sets the color of the line connecting two points.
     *
     * @param color the line color
     * @return this DrawableDistribution
     * @throws NullPointerException if {@code color == null}
     */

    public DrawableDistribution setLineColor(Color color) {
        Objects.requireNonNull(color);
        this.lineColor = color;
        return this;
    }

    /**
     * Sets the width of the line connecting two points.
     *
     * @param lineWidth the line width greater than or equal to zero
     * @return this DrawableDistribution
     * @throws IllegalArgumentException if {@code lineWidth < 0}
     */

    public DrawableDistribution setLineWidth(float lineWidth) {
        if (lineWidth < 0) {
            throw new IllegalArgumentException("lineWidth can not be < 0");
        }
        this.lineWidth = lineWidth;
        return this;
    }

    /**
     * Sets the geometry used to render the point marker.
     *
     * @param markerGeometry a not null {@link Geometry}
     * @return this DrawableDistribution
     */

    public DrawableDistribution setMarker(Geometry markerGeometry) {
        geometryMarker.removeAllVertices();
        for (int i = 0; i < markerGeometry.vertices(); i++) {
            float vertexX = markerGeometry.getX(i);
            float vertexY = markerGeometry.getY(i);
            geometryMarker.addVertex(vertexX, vertexY);
        }
        return this;
    }

    /**
     * Calculates the marker position according to the component viewport.
     */

    private void calculateMarkerPosition(float[] target,
                                         float[] componentBounds,
                                         float viewportWidth, float viewportHeight,
                                         float pointX, float pointY, float rotation,
                                         float minPointX, float maxPointX,
                                         float minPointY, float maxPointY) {
        float xDist = viewportWidth * (MathUtility.normalize(pointX, minPointX, maxPointX) - 0.5f);
        float yDist = -viewportHeight * (MathUtility.normalize(pointY, minPointY, maxPointY) - 0.5f);
        target[0] = ComponentUtility.getPositionOnX(componentBounds[0], componentBounds[2], xDist, yDist, rotation);
        target[1] = ComponentUtility.getPositionOnY(componentBounds[1], componentBounds[3], xDist, yDist, rotation);
    }

    private final float[] markerPosition = {0f, 0f};
    private final float[] nextMarkerPosition = {0f, 0f};

    /**
     * Draws the data distribution on the given Graphic.
     *
     * @param graphics a {@link Graphics} used to display the distribution
     * @throws NullPointerException if {@code graphics == null}
     */

    public void draw(Graphics graphics, float[] bounds, float width, float height, float rotation) {
        Objects.requireNonNull(graphics);

        float xMin = getMin(PointDistribution.AXIS.X);
        float yMin = getMin(PointDistribution.AXIS.Y);
        float xMax = getMax(PointDistribution.AXIS.X);
        float yMax = getMax(PointDistribution.AXIS.Y);

        // draws line between points
        if (enableLine) {
            graphics
                    .setShapeBorderWidth(lineWidth)
                    .setShapeColor(lineColor);

            for (int i = 0; i < size() - 1; i++) {
                float pointX = get(i, PointDistribution.AXIS.X);
                float pointY = get(i, PointDistribution.AXIS.Y);
                float nextPointX = get(i + 1, PointDistribution.AXIS.X);
                float nextPointY = get(i + 1, PointDistribution.AXIS.Y);

                calculateMarkerPosition(markerPosition,
                        bounds, width, height,
                        pointX, pointY, rotation,
                        xMin, xMax, yMin, yMax);

                calculateMarkerPosition(nextMarkerPosition,
                        bounds, width, height,
                        nextPointX, nextPointY, rotation,
                        xMin, xMax, yMin, yMax);

                graphics.drawShape(null, 2,
                        markerPosition[0], markerPosition[1],
                        nextMarkerPosition[0], nextMarkerPosition[1]
                );
            }
        }

        // draws point
        if (enablePoint) {
            graphics
                    .setShapeColor(pointColor)
                    .setShapeBorderWidth(0);

            for (int i = 0; i < size(); i++) {
                float pointX = get(i, PointDistribution.AXIS.X);
                float pointY = get(i, PointDistribution.AXIS.Y);

                calculateMarkerPosition(markerPosition,
                        bounds, width, height,
                        pointX, pointY, rotation,
                        xMin, xMax, yMin, yMax);

                markerTransform
                        .setTranslation(markerPosition[0], markerPosition[1])
                        .setScale(pointSize, pointSize);
                graphics.drawShape(markerTransform, geometryMarker.vertices(), geometryMarker.toArray());
            }
        }
    }
}
