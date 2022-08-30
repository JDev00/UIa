package uia.core.widget.math.struct;

import uia.core.geometry.Figure;
import uia.core.geometry.Rect;
import uia.core.policy.Context;
import uia.core.policy.Paint;
import uia.core.policy.Path;
import uia.core.policy.Render;

public class DrawableDistribution {
    private Context context;

    private Paint colorLine;
    private Paint colorPoint;
    private Paint colorStandardDeviation;

    private float pointDim = 1;

    private Figure pointFigure;

    private boolean enableLine = true;
    private boolean enablePoint = true;
    private boolean enableStandardDeviation = false;

    public DrawableDistribution(Context context) {
        this.context = context;

        colorLine = context.createColor(null, Context.COLOR.BLACK);
        colorPoint = context.createColor(null, Context.COLOR.RED);
        colorStandardDeviation = context.createColor(null, 255, 200, 200, 200);

        pointFigure = new Rect();
    }

    /**
     * Change the Context of this distribution
     *
     * @param context a not null {@link Context}
     */

    public void setContext(Context context) {
        if (context != null) {
            this.context = context;
        }
    }

    /**
     * Set a new line color
     *
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @param a the alpha component
     */

    public void setColorLine(int r, int g, int b, int a) {
        colorLine = context.createColor(colorLine, r, g, b, a);
    }

    /**
     * Set a new point color
     *
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @param a the alpha component
     */

    public void setColorPoint(int r, int g, int b, int a) {
        colorPoint = context.createColor(colorPoint, r, g, b, a);
    }

    /**
     * Set a new standard deviation curve color
     *
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @param a the alpha component
     */

    public void setColorStandardDeviation(int r, int g, int b, int a) {
        colorStandardDeviation = context.createColor(colorStandardDeviation, r, g, b, a);
    }

    /**
     * Set a new figure used to draw a point
     *
     * @param pointFigure a not null {@link Figure}
     */

    public void setPointFigure(Figure pointFigure) {
        if (pointFigure != null)
            this.pointFigure = pointFigure;
    }

    /**
     * Enable or disable the line that connects each point
     *
     * @param enableLine true to enable the line
     */

    public void enableLine(boolean enableLine) {
        this.enableLine = enableLine;
    }

    /**
     * Enable or disable the drawing of points
     *
     * @param enablePoint true to draw points
     */

    public void enablePoint(boolean enablePoint) {
        this.enablePoint = enablePoint;
    }

    /**
     * Enable or disable the standard deviation curve
     *
     * @param enableStandardDeviation true to enable the standard deviation curve
     */

    public void enableStandardDeviation(boolean enableStandardDeviation) {
        this.enableStandardDeviation = enableStandardDeviation;
    }

    /**
     * @return true if the line that connects points is enabled (visible)
     */

    public boolean isLineEnabled() {
        return enableLine;
    }

    /**
     * @return true if points are enabled (visible)
     */

    public boolean isPointEnabled() {
        return enablePoint;
    }

    /**
     * @return true if standard deviation curve is enabled (visible)
     */

    public boolean isStandardDeviationEnabled() {
        return enableStandardDeviation;
    }

    /**
     * Draw the given data distribution
     */

    public void draw(Render render, Path path,
                     DistributionNum distribution,
                     float xCenter, float yCenter,
                     float xScale, float yScale) {
        /*if (enableStandardDeviation) {
            path.reset();

            for (int i = 0; i < distribution.size(); i++) {
                int x = (int) (xCenter + xScale * distribution.get(i, 0));
                int y = (int) (yCenter - yScale * (distribution.get(i, 1) - standardDeviation));// y-axis must be inverted in computer graphics!

                if (i == 0) {
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
            }

            for (int j = size() - 1; j >= 0; j--) {
                int x = (int) (xCenter + xScale * getX(j));
                int y = (int) (yCenter - yScale * (getY(j) + standardDeviation));

                path.lineTo(x, y);
            }

            path.close();

            render.setPaint(colorStandardDeviation);
            render.draw(path);
        }*/

        // Draw line between points

        if (enableLine) {
            render.setPaint(colorLine);

            for (int i = 0; i < distribution.size() - 1; i++) {
                int x1 = (int) (xCenter + xScale * distribution.get(i, 0));
                int y1 = (int) (yCenter - yScale * distribution.get(i, 1));// the y-axis must be inverted in computer graphics!
                int x2 = (int) (xCenter + xScale * distribution.get(i + 1, 0));
                int y2 = (int) (yCenter - yScale * distribution.get(i + 1, 1));

                render.drawLine(x1, y1, x2, y2);
            }
        }

        // Draw points

        if (enablePoint) {
            int pd = (int) (pointDim * (8d - Math.log(distribution.size())));
            int h_pd = pd / 2;

            render.setPaint(colorPoint);
            for (int i = 0; i < distribution.size(); i++) {
                int x = (int) (xCenter + xScale * distribution.get(i, 0) - h_pd);
                int y = (int) (yCenter - yScale * distribution.get(i, 1) - h_pd);

                pointFigure.build(path, x, y, pointDim, pointDim, 0f);

                //render.drawRect(x, y, pd, pd);
            }
        }
    }
}
