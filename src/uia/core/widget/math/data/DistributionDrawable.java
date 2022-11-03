package uia.core.widget.math.data;

import uia.core.platform.independent.shape.Figure;
import uia.core.platform.independent.shape.Shape;
import uia.core.platform.independent.paint.Paint;
import uia.core.platform.policy.Graphic;

public class DistributionDrawable {
    private Shape marker;

    private final Paint cLine;
    private final Paint cPoint;

    private float pointDim = 4;

    private float xCenter;
    private float yCenter;
    private float xScale;
    private float yScale;

    private boolean enableLine = true;
    private boolean enablePoint = true;

    public DistributionDrawable() {
        cLine = new Paint(0, 0, 0);
        cPoint = new Paint(255, 0, 0);

        marker = Figure.rect(null);
    }

    /**
     * Test!
     */

    public void setParams(float xCenter, float yCenter,
                          float xScale, float yScale) {
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        this.xScale = xScale;
        this.yScale = yScale;
    }

    /**
     * Set the point dimension
     *
     * @param pointDim a value {@code >= 0}
     */

    public DistributionDrawable setPointDim(int pointDim) {
        this.pointDim = Math.max(pointDim, 0);
        return this;
    }

    /**
     * Set the line color
     *
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     */

    public DistributionDrawable setColorLine(int r, int g, int b) {
        cLine.setColor(r, g, b);
        return this;
    }

    /**
     * Test
     */

    public DistributionDrawable setLineWidth(int val) {
        cLine.setStrokesWidth(val);
        return this;
    }

    /**
     * Set the point color
     *
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @param a the alpha component
     */

    public DistributionDrawable setColorPoint(int r, int g, int b, int a) {
        cPoint.setColor(r, g, b, a);
        return this;
    }

    /**
     * Set the point's shape
     *
     * @param marker a not null {@link Shape}
     */

    public DistributionDrawable setPointShape(Shape marker) {
        if (marker != null) this.marker = marker;
        return this;
    }

    /**
     * Enable or disable the line that connects each point
     *
     * @param enableLine true to enable the line
     */

    public DistributionDrawable enableLine(boolean enableLine) {
        this.enableLine = enableLine;
        return this;
    }

    /**
     * Enable or disable the drawing of points
     *
     * @param enablePoint true to draw points
     */

    public DistributionDrawable enablePoint(boolean enablePoint) {
        this.enablePoint = enablePoint;
        return this;
    }

    /**
     * Draw the given data distribution
     *
     * @param graphic       the {@link Graphic} object
     * @param distribution the {@link Distribution} to draw
     */

    public void draw(Graphic graphic, Distribution distribution) {
        if (enableLine) {// draw line between points
            graphic.setPaint(cLine);

            for (int i = 0; i < distribution.size() - 1; i++) {
                int x1 = (int) (xCenter + xScale * distribution.get(i, 0));
                int y1 = (int) (yCenter - yScale * distribution.get(i, 1));// y-axis must be inverted in computer graphics!
                int x2 = (int) (xCenter + xScale * distribution.get(i + 1, 0));
                int y2 = (int) (yCenter - yScale * distribution.get(i + 1, 1));

                graphic.drawLine(x1, y1, x2, y2);
            }
        }

        if (enablePoint) {// draw points
            graphic.setPaint(cPoint);

            for (int i = 0; i < distribution.size(); i++) {
                float x = xCenter + xScale * distribution.get(i, 0);
                float y = yCenter - yScale * distribution.get(i, 1);

                marker.setPosition(x, y);
                marker.setDimension(pointDim, pointDim);
                marker.draw(graphic);
            }
        }
    }

    /**
     * @return the point's dimension in pixels
     */

    public float getPointDim() {
        return pointDim;
    }

    /**
     * @return true if the line that connects points is enabled (visible)
     */

    public boolean isLineEnabled() {
        return enableLine;
    }

    /**
     * @return true if points are visible
     */

    public boolean isPointEnabled() {
        return enablePoint;
    }
}
