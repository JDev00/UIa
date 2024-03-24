package uia.application.math;

import uia.core.shape.Geometry;
import uia.core.paint.Paint;
import uia.core.shape.Shape;
import uia.core.shape.NormalizedVertex;
import uia.core.ui.Graphics;
import uia.core.ui.View;
import uia.physical.theme.Theme;
import uia.utility.Geometries;
import uia.utility.MathUtility;

import java.util.Objects;

/**
 * A DrawableDistribution defines the graphical settings to draw a {@link PointDistribution} on screen.
 */

public class DrawableDistribution extends PointDistribution {
    private final Paint paintLine;
    private final Paint paintPoint;
    private final Shape shapeMarker;

    private float pointDimension = 4;
    private boolean enableLine = true;
    private boolean enablePoint = true;

    public DrawableDistribution() {
        paintLine = new Paint()
                .setColor(Theme.BLACK)
                .setStrokeWidth(2);

        paintPoint = new Paint().setColor(Theme.RED);

        shapeMarker = new Shape();
        shapeMarker.setGeometry(Geometries.rect(shapeMarker.getGeometry()));
    }

    /**
     * Sets the point dimension.
     *
     * @param pointDim a value {@code >= 0}
     */

    public DrawableDistribution setPointDimension(int pointDim) {
        this.pointDimension = Math.max(pointDim, 0);
        return this;
    }

    /**
     * @return the {@link Paint} used to customize the line (between points) appearance
     */

    public Paint getLinePaint() {
        return paintLine;
    }

    /**
     * @return the {@link Paint} used to customize the point markers appearance
     */

    public Paint getPointPaint() {
        return paintPoint;
    }

    /**
     * Sets the geometry used to render the point marker.
     *
     * @param markerGeometry a not null {@link Geometry}
     */

    public DrawableDistribution setMarker(Geometry markerGeometry) {
        Geometry internalGeometry = shapeMarker.getGeometry();
        internalGeometry.removeAllVertices();
        for (int i = 0; i < markerGeometry.vertices(); i++) {
            NormalizedVertex vertex = markerGeometry.get(i);
            internalGeometry.addVertex(vertex.getX(), vertex.getY());
        }
        return this;
    }

    /**
     * Enables or disables the line that connects each point.
     *
     * @param enableLine true to enable the line
     */

    public DrawableDistribution enableLine(boolean enableLine) {
        this.enableLine = enableLine;
        return this;
    }

    /**
     * Enables or disables the drawing of points.
     *
     * @param enablePoint true to draw points
     */

    public DrawableDistribution enablePoint(boolean enablePoint) {
        this.enablePoint = enablePoint;
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
        target[0] = View.getPositionOnX(componentBounds[0], componentBounds[2], xDist, yDist, rotation);
        target[1] = View.getPositionOnY(componentBounds[1], componentBounds[3], xDist, yDist, rotation);
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

        if (enableLine) {
            graphics.setPaint(paintLine);

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

                graphics.drawShape(
                        markerPosition[0], markerPosition[1],
                        nextMarkerPosition[0], nextMarkerPosition[1]
                );
            }
        }

        if (enablePoint) {
            graphics.setPaint(paintPoint);

            for (int i = 0; i < size(); i++) {
                float pointX = get(i, PointDistribution.AXIS.X);
                float pointY = get(i, PointDistribution.AXIS.Y);

                calculateMarkerPosition(markerPosition,
                        bounds, width, height,
                        pointX, pointY, rotation,
                        xMin, xMax, yMin, yMax);

                shapeMarker.setPosition(markerPosition[0], markerPosition[1]);
                shapeMarker.setDimension(pointDimension, pointDimension);
                graphics.drawShape(shapeMarker);
            }
        }
    }
}
