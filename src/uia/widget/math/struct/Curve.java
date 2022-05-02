package uia.core.widget.math.struct;

import uia.structure.vec.Vec;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.lang.Math.*;

/**
 * Mathematical curve representation
 */

public class Curve {
    private final List<VecS> points;

    public Color colorLine = Color.DARK_GRAY;
    public Color colorPoint = Color.RED;
    public Color colorVariance = new Color(255, 100, 10, 150);

    private float mean;
    private float standardDeviation;

    private float maxX;
    private float maxY;
    private float minX;
    private float minY;

    public float pointFactor = 1;

    public boolean drawLines = true;
    public boolean drawPoints = true;
    public boolean drawVariance = false;

    private Curve(int size) {
        points = new ArrayList<>(size);
    }

    /**
     * Clear this curve
     */

    public void clear() {
        points.clear();
    }

    /**
     * Analyse this curve
     */

    public void analyse() {
        mean = (float) getMean(points);
        standardDeviation = (float) sqrt(getVariance(mean, points));

        // updates min and max
        minY = minY - standardDeviation;
        maxY = maxY + standardDeviation;
    }

    /**
     * Extract the min and max values of this curve
     */

    private void extractMaxMin(float x, float y) {
        if (x > maxX) maxX = x;
        if (x < minX) minX = x;

        if (y > maxY) maxY = y;
        if (y < minY) minY = y;
    }

    /**
     * Add a new point to this curve
     *
     * @param x    the point position along x-axis
     * @param y    the point position along y-axis
     * @param data the point data
     */

    public void addPoint(float x, float y, String data) {
        points.add(new VecS(x, y, data));
        extractMaxMin(x, y);
    }

    /**
     * Add a new point to this curve
     *
     * @param i    the position where add the point
     * @param x    the point position along x-axis
     * @param y    the point position along y-axis
     * @param data the point data
     */

    public void addPoint(int i, float x, float y, String data) {
        points.add(i, new VecS(x, y, data));
        extractMaxMin(x, y);
    }

    /**
     * Update a point of this curve
     *
     * @param i    the position of the point to update
     * @param x    the point position along x-axis
     * @param y    the point position along y-axis
     * @param data the point data
     */

    public void setPoint(int i, float x, float y, String data) {
        points.get(i).set(x, y, data);
        extractMaxMin(x, y);
    }

    /**
     * Update a point of this curve
     *
     * @param i the position of the point to update
     * @param x the point position along x-axis
     * @param y the point position along y-axis
     */

    public void setPoint(int i, float x, float y) {
        points.get(i).set(x, y);
        extractMaxMin(x, y);
    }

    /**
     * @return the amount of points
     */

    public int size() {
        return points.size();
    }

    /**
     * @param i the point index
     * @return the position along x-axis of the specified point
     */

    public float getX(int i) {
        return points.get(i).x;
    }

    /**
     * @param i the point index
     * @return the position along y-axis of the specified point
     */

    public float getY(int i) {
        return points.get(i).y;
    }

    /**
     * @return the mean of the points along y-axis
     */

    public float getMean() {
        return mean;
    }

    /**
     * the standard deviation of this curve
     */

    public float getStandardDeviation() {
        return standardDeviation;
    }

    /**
     * @return the maximum position along x-axis
     */

    public float getMaxX() {
        return maxX;
    }

    /**
     * @return the maximum position along y-axis
     */

    public float getMaxY() {
        return maxY;
    }

    /**
     * @return the minimum position along x-axis
     */

    public float getMinX() {
        return minX;
    }

    /**
     * @return the minimum position along y-axis
     */

    public float getMinY() {
        return minY;
    }

    /**
     * @param i the point index
     * @return the data of the specified point
     */

    public String getData(int i) {
        return points.get(i).data;
    }

    /**
     * Draw the given curve on screen
     *
     * @param curve             a non-null {@link Curve} to draw
     * @param canvas            a non-null {@link Graphics2D} used to draw the curve
     * @param path              a non-null {@link GeneralPath} instance used to draw curve variance
     * @param xCenter           the center of the curve along x-axis
     * @param yCenter           the center of the curve along y-axis
     * @param xScale            the curve scale along x-axis
     * @param yScale            the curve scale along y-axis
     * @param standardDeviation the standard deviation of the given curve
     */

    public static void draw(Curve curve,
                            Graphics2D canvas, GeneralPath path,
                            float xCenter, float yCenter,
                            float xScale, float yScale,
                            float standardDeviation) {
        if (curve.drawVariance) {
            path.reset();

            for (int i = 0; i < curve.size(); i++) {
                int x = (int) (xCenter + xScale * curve.getX(i));
                int y = (int) (yCenter - yScale * (curve.getY(i) - standardDeviation));// y-axis must be inverted in computer graphics!

                if (i == 0) {
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
            }

            for (int j = curve.size() - 1; j >= 0; j--) {
                int x = (int) (xCenter + xScale * curve.getX(j));
                int y = (int) (yCenter - yScale * (curve.getY(j) + standardDeviation));

                path.lineTo(x, y);
            }

            path.closePath();

            canvas.setColor(curve.colorVariance);
            canvas.fill(path);
        }

        // Draw line between points

        canvas.setColor(curve.colorLine);
        for (int i = 0; i < curve.size() - 1; i++) {
            int x1 = (int) (xCenter + xScale * curve.getX(i));
            int y1 = (int) (yCenter - yScale * curve.getY(i));// the y-axis must be inverted in computer graphics!
            int x2 = (int) (xCenter + xScale * curve.getX(i + 1));
            int y2 = (int) (yCenter - yScale * curve.getY(i + 1));

            if (curve.drawLines)
                canvas.drawLine(x1, y1, x2, y2);
        }

        // Draw points

        if (curve.drawPoints) {
            int pd = (int) (curve.pointFactor * (8d - java.lang.Math.log(curve.size())));
            int h_pd = pd / 2;

            canvas.setColor(curve.colorPoint);
            for (int i = 0; i < curve.size(); i++) {
                int x = (int) (xCenter + xScale * curve.getX(i) - h_pd);
                int y = (int) (yCenter - yScale * curve.getY(i) - h_pd);

                canvas.fillOval(x, y, pd, pd);
            }
        }
    }

    /**
     * @return the mean of the given set of points
     */

    public static double getMean(List<? extends Vec> points) {
        if (points == null || points.size() == 0) return 0d;

        double mean = 0d;
        for (Vec i : points) {
            mean += i.y;
        }

        return mean / points.size();
    }

    /**
     * @return the variance of the given set of points
     */

    public static double getVariance(double mean, List<? extends Vec> points) {
        if (mean == 0 || points == null || points.size() < 2) return 0d;

        double variance = 0d;
        for (Vec i : points) {
            variance += (i.y - mean) * (i.y - mean);
        }

        return variance / (points.size() - 1);
    }

    /**
     * Create a new Curve
     *
     * @param size the number of points
     * @param fun  a function used to create each point
     */

    public static Curve create(int size, Function<Integer, VecS> fun) {
        Curve curve = new Curve(size);

        for (int i = 0; i < size; i++) {
            VecS v = fun.apply(i);

            if (v != null)
                curve.addPoint(v.x, v.y, v.data);
        }

        return curve;
    }

    /**
     * Create a new Curve
     *
     * @param size the number of points
     * @param step a gap between the previous and the current point
     * @param fun  a function used to create each point
     */

    public static Curve create(int size, float step, Function<Float, VecS> fun) {
        Curve curve = new Curve(size);

        for (float i = 0; i < size; i += step) {
            VecS v = fun.apply(i);

            if (v != null)
                curve.addPoint(v.x, v.y, v.data);
        }

        return curve;
    }

    /**
     * @return a new Curve or null if {@code list == null || list.size() == null}
     */

    public static Curve create(List<VecS> list) {
        Curve out = null;

        if (list != null && list.size() > 0) {
            out = new Curve(list.size());

            for (VecS i : list) {
                out.addPoint(i.x, i.y, i.data);
            }
        }

        return out;
    }

    /**
     * Create, if exists, the curve of the mean of each point
     *
     * @return if exists the mean curve otherwise null
     */

    public static Curve createMean(List<Curve> curves) {
        if (curves.size() > 1) {
            int size = curves.get(0).size();

            // All curves must have the same number of points
            for (int i = 1; i < curves.size(); i++) {

                if (size != curves.get(i).size()) return null;
            }

            List<VecS> list = new ArrayList<>(size);

            float d = curves.size();

            for (int j = 0; j < size; j++) {
                float meanX = 0;
                float meanY = 0;

                for (Curve i : curves) {
                    meanX += i.getX(j);
                    meanY += i.getY(j);
                }

                meanX /= d;
                meanY /= d;

                list.add(new VecS(meanX, meanY, null));
            }

            // Create and return mean curve

            Curve mean = new Curve(size);
            mean.drawVariance = true;
            mean.colorPoint = Color.BLUE;

            for (VecS i : list) {
                mean.addPoint(i.x, i.y, i.data);
            }

            mean.analyse();

            return mean;
        }

        return null;
    }

    /**
     * Join two given curves
     * Note:
     * a) if two points have the same x-coordinates, then sum the y-coords of the two
     * b) if two points have different x-coordinates, then add them to the curve in ascending order
     *
     * @param a the first curve to join
     * @param b the second curve to join
     * @return if the given curves are consistent a new curve otherwise null
     */

    public static Curve join(Curve a, Curve b) {
        if (a == null || b == null) return null;

        if (a.equals(b)) return a;

        Curve ret = new Curve(max(a.size(), b.size()));

        int iA = 0;
        int iB = 0;

        while (iA < a.size() && iB < b.size()) {
            float xA = a.getX(iA);
            float yA = a.getY(iA);

            float xB = b.getX(iB);
            float yB = b.getY(iB);

            if (xA == xB) {
                ret.addPoint(xA, (yA + yB) / 2f, a.getData(iA) + "\n" + a.getData(iB));
                iA++;
                iB++;
            } else if (xA < xB) {
                ret.addPoint(xA, yA, a.getData(iA));
                iA++;
            } else {
                ret.addPoint(xB, yB, b.getData(iA));
                iB++;
            }
        }

        while (iA < a.size()) {
            ret.addPoint(a.getX(iA), a.getY(iA), a.getData(iA));
            iA++;
        }

        while (iB < b.size()) {
            ret.addPoint(b.getX(iB), b.getY(iB), b.getData(iB));
            iB++;
        }

        ret.analyse();

        return ret;
    }
}
