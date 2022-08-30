package uia.core.widget.math;

import uia.core.View;
import uia.core.event.Mouse;
import uia.core.geometry.Rect;
import uia.core.policy.Context;
import uia.core.policy.Paint;
import uia.core.policy.Path;
import uia.core.policy.Render;
import uia.core.widget.math.struct.Curve;
import uia.core.widget.math.struct.VecS;
import uia.structure.vector.Vec;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

/**
 * Widget used to represent data on a 2D graphic.
 * <br>
 * Data here in intended as mathematical {@link Curve}s.
 */

public class GraphicView extends View {
    private final List<Curve> curves;

    private final List<VecS> covered;

    private final Path path;

    private Curve mean = null;

    public Paint colorAxis;

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

        colorAxis = context.createColor(null, 255, 0, 0);

        path = context.createPath();

        curves = new ArrayList<>();

        covered = new ArrayList<>();

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
     * Clear all curves and reset the mean one
     */

    public void clear() {
        reset();

        curves.clear();

        mean = null;
    }

    /**
     * Add a new curve
     *
     * @param curve a not null curve
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
     * @param i the position of the curve to remove
     * @return if exists the removed curve otherwise null
     */

    public Curve remove(int i) {
        return i >= 0 && i < curves.size() ? curves.get(i) : null;
    }

    /**
     * Create/update the curve of the mean of each point
     *
     * @return if exists the mean curve otherwise null
     */

    public Curve createMean() {
        return (mean = Curve.createMean(getContext(), curves));
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

        Curve ret = Curve.join(getContext(), curves.remove(i), curves.remove(j));

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
    public void postDraw(Render render) {
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

            i.draw(render, path, xCenter, yCenter, xScale, yScale);

            VecS node = getNode(i, mx);
            if (node != null)
                covered.add(node);
        }


        if (mean != null) {
            mean.draw(render, path, xCenter, yCenter, xScale, yScale);

            VecS node = getNode(mean, mx);
            if (node != null)
                covered.add(node);
        }


        render.setPaint(colorAxis);
        render.drawLine(
                (int) (px() - 0.99f * dx / 2d), (int) yCenter,
                (int) (px() - 0.97f * dx / 2f), (int) yCenter);

        render.drawLine(
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
     * @return the points covered by user
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
