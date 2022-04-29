package uia.core.widget.math;

import uia.core.Context;
import uia.core.View;
import uia.core.event.Mouse;
import uia.core.shape.Rect;
import uia.core.widget.math.struct.Curve;
import uia.core.widget.math.struct.VecS;
import uia.structure.vec.Vec;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

/**
 * Widget used to draw {@link Curve}s
 */

public class GraphicView extends View {
    private final List<Curve> curves;

    private final List<VecS> covered;

    private final GeneralPath path;

    private Curve mean = null;

    public Color colorAxis = Color.RED;

    protected int cmx = -1;
    protected int cmy = -6;

    private float maxX;
    private float minX;
    private float maxY;
    private float minY;

    private float xCenter;
    private float yCenter;
    private float xScale;
    private float yScale;

    public GraphicView(Context context,
                       float px, float py,
                       float dx, float dy) {
        super(context, px, py, dx, dy);
        super.setExpansion(0f, 0f);
        super.setFigure(new Rect());
        super.addEvent((Mouse) (v, s) -> {
            MotionEvent e = v.getMotionEvent();
            cmx = e.getX();
            cmy = e.getY();
        });

        curves = new ArrayList<>();

        covered = new ArrayList<>();

        path = new GeneralPath();

        reset();
    }

    /**
     * Update the attributes of this graphic
     *
     * @param x the last x point
     * @param y the last y point
     */

    private void updateMaxMin(float x, float y) {
        if (x > maxX) maxX = x;
        if (x < minX) minX = x;

        if (y > maxY) maxY = y;
        if (y < minY) minY = y;
    }

    /**
     * Reset the graphic attributes
     */

    private void reset() {
        maxX = maxY = -Integer.MAX_VALUE;
        minX = minY = Integer.MAX_VALUE;
        xCenter = yCenter = 0;
        xScale = yScale = 0;
    }

    /**
     * Clear all curves of this graphic
     */

    public void clear() {
        reset();

        curves.clear();

        mean = null;
    }

    /**
     * Add a new curve
     *
     * @param curve a non-null curve to draw
     */

    public void add(Curve curve) {
        if (curve != null) {
            updateMaxMin(curve.getMaxX(), curve.getMaxY());
            updateMaxMin(curve.getMinX(), curve.getMinY());

            curves.add(curve);
        }
    }

    /**
     * Remove the specified curve
     *
     * @param i the index of the curve to remove
     * @return if exists the removed curve otherwise null
     */

    public Curve remove(int i) {
        return i >= 0 && i < curves.size() ? curves.get(i) : null;
    }

    /**
     * Create/update, if exists, the curve of the mean of each point
     *
     * @return if exists the mean curve otherwise null
     */

    public Curve createMean() {
        return (mean = Curve.createMean(curves));
    }

    /**
     * Join two curves
     * T(n) = Theta(n)
     *
     * @param i the index of the first curve
     * @param j the index of the second curve
     * @return if exists the joined curve otherwise null
     */

    public Curve join(int i, int j) {
        if (i < 0 || i > j || i >= size() || j >= size()) return null;

        Curve ret = Curve.join(curves.remove(i), curves.remove(j));

        if (ret != null) {
            curves.add(ret);
        }

        return ret;
    }

    public VecS getNode(Curve curve, float xMouse) {
        int oldD1 = Integer.MAX_VALUE;
        int oldD2 = Integer.MAX_VALUE;
        int node = -1;

        for (int i = 0; i < curve.size() - 1; i++) {
            int x1 = (int) (xCenter + xScale * curve.getX(i));
            int x2 = (int) (xCenter + xScale * curve.getX(i + 1));

            int d1 = abs((int) (x1 - xCenter - xMouse));
            int d2 = abs((int) (x2 - xCenter - xMouse));

            if (d1 <= d2) {

                if (d1 < oldD1) {
                    oldD1 = d1;
                    node = i;
                }
            } else if (d2 < oldD2) {
                oldD2 = d2;
                node = i + 1;
            }
        }

        return (node == -1) ? null : new VecS(curve.getX(node), curve.getY(node), curve.getData(node));
    }

    @Override
    public void postDraw(Graphics2D canvas) {
        //v = ((max + min) / (|max| + |min|) + 1) / 2

        float cx = abs(minX) + abs(maxX);
        if (Float.compare(cx, 0) == 0)
            cx = 1;

        float cy = abs(minY) + abs(maxY);
        if (Float.compare(cy, 0) == 0)
            cy = 1;

        float dx = dx();
        float dy = dy();
        float sdx = 0.95f * dx();
        float sdy = 0.95f * dy();

        float mx = cmx - 0.5f * (dx - sdx);

        xCenter = px() - sdx / 2f + sdx * (1 + (-maxX - minX) / cx) / 2;
        yCenter = py() - sdy / 2f + sdy * (1 + (maxY + minY) / cy) / 2;
        xScale = sdx / cx;
        yScale = sdy / cy;


        covered.clear();

        for (Curve i : curves) {
            updateMaxMin(i.getMaxX(), i.getMaxY());
            updateMaxMin(i.getMinX(), i.getMinY());

            Curve.draw(i, canvas, path, xCenter, yCenter, xScale, yScale, i.getStandardDeviation());

            VecS node = getNode(i, mx);
            if (node != null)
                covered.add(node);
        }


        if (mean != null) {
            Curve.draw(mean, canvas, path, xCenter, yCenter, xScale, yScale, mean.getStandardDeviation());

            VecS node = getNode(mean, mx);
            if (node != null)
                covered.add(node);
        }


        canvas.setColor(colorAxis);
        canvas.drawLine(
                (int) (px() - 0.99f * dx / 2d), (int) yCenter,
                (int) (px() - 0.97f * dx / 2f), (int) yCenter);

        canvas.drawLine(
                (int) xCenter, (int) (py() - 0.99f * dy / 2d),
                (int) xCenter, (int) (py() - 0.97f * dy / 2d));
    }

    /**
     * @return the number of curves
     */

    public final int size() {
        return curves.size();
    }

    public final float getCenterX() {
        return xCenter;
    }

    public final float getCenterY() {
        return yCenter;
    }

    public final float getScaleX() {
        return xScale;
    }

    public final float getScaleY() {
        return yScale;
    }

    /**
     * @return points covered by user
     */

    public final List<VecS> getCovered() {
        return covered;
    }

    /**
     * @return the maximum point
     */

    public final Vec getMax() {
        return new Vec(maxX, maxY);
    }

    /**
     * @return the minimum point
     */

    public final Vec getMin() {
        return new Vec(minX, minY);
    }
}
