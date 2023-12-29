package uia.develop.math;

import uia.core.Geometry;
import uia.core.Paint;
import uia.core.Shape;
import uia.core.ui.Graphic;
import uia.core.ui.View;
import uia.physical.theme.Theme;
import uia.utility.GeometryFactory;
import uia.utility.Utility;

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
        shapeMarker.setGeometry(GeometryFactory.rect(shapeMarker.getGeometry()));
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
     * Sets a new Paint to customize the line graphical appearance
     *
     * @param paint a not null {@link Paint}
     */

    public DrawableDistribution setLinePaint(Paint paint) {
        paintLine.set(paint);
        return this;
    }

    /**
     * Sets a new Paint to customize the point graphical appearance
     *
     * @param paint a not null {@link Paint}
     */

    public DrawableDistribution setColorPoint(Paint paint) {
        paintPoint.set(paint);
        return this;
    }

    /**
     * Sets the geometry used to render the point marker
     *
     * @param markerGeometry a not null {@link Geometry}
     */

    public DrawableDistribution setMarker(Geometry markerGeometry) {
        Geometry internalGeometry = shapeMarker.getGeometry();
        internalGeometry.clear();
        for (int i = 0; i < markerGeometry.vertices(); i++) {
            Geometry.Vertex vertex = markerGeometry.get(i);
            internalGeometry.addVertex(vertex.getX(), vertex.getY());
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
     * Calculates the marker position according to the component viewport
     */

    private void calculateMarkerPosition(float[] target,
                                         float[] componentBounds,
                                         float viewportWidth, float viewportHeight,
                                         float pointX, float pointY, float rotation,
                                         float minPointX, float maxPointX,
                                         float minPointY, float maxPointY) {
        float xDist = viewportWidth * (Utility.normalize(pointX, minPointX, maxPointX) - 0.5f);
        float yDist = -viewportHeight * (Utility.normalize(pointY, minPointY, maxPointY) - 0.5f);
        target[0] = View.getPositionOnX(componentBounds[0], componentBounds[2], xDist, yDist, rotation);
        target[1] = View.getPositionOnY(componentBounds[1], componentBounds[3], xDist, yDist, rotation);
    }

    private final float[] markerPosition = {0f, 0f};
    private final float[] nextMarkerPosition = {0f, 0f};

    /**
     * Draws the data distribution on the specified Graphic
     *
     * @param graphic a not null {@link Graphic} used to display the distribution
     */

    public void draw(Graphic graphic, float[] bounds, float width, float height, float rotation) {
        float xMin = getMin(PointDistribution.AXIS.X);
        float yMin = getMin(PointDistribution.AXIS.Y);
        float xMax = getMax(PointDistribution.AXIS.X);
        float yMax = getMax(PointDistribution.AXIS.Y);

        if (enableLine) {
            graphic.setPaint(paintLine);

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

                graphic.drawShape(
                        markerPosition[0], markerPosition[1],
                        nextMarkerPosition[0], nextMarkerPosition[1]
                );
            }
        }

        if (enablePoint) {
            graphic.setPaint(paintPoint);

            for (int i = 0; i < size(); i++) {
                float pointX = get(i, PointDistribution.AXIS.X);
                float pointY = get(i, PointDistribution.AXIS.Y);

                calculateMarkerPosition(markerPosition,
                        bounds, width, height,
                        pointX, pointY, rotation,
                        xMin, xMax, yMin, yMax);

                shapeMarker.setPosition(markerPosition[0], markerPosition[1]);
                shapeMarker.setDimension(pointDimension, pointDimension);
                graphic.drawShape(shapeMarker);
            }
        }
    }
}
