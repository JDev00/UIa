package uia.core.widget.math.struct;

import java.util.ArrayList;
import java.util.List;

/**
 * DistributionNum is a canonical vector's distribution designed to be handled by {@link uia.core.widget.math.GraphicView}
 */

public class DistributionNum {
    private final int dimensions;

    private final List<double[]> data;

    private final double[] minValues;
    private final double[] maxValues;


    private final double[] mean;
    private final double[] standardDeviation;

    public DistributionNum(int dimensions) {
        this.dimensions = dimensions;

        data = new ArrayList<>();

        minValues = new double[dimensions];
        maxValues = new double[dimensions];

        mean = new double[dimensions];
        standardDeviation = new double[dimensions];
    }

    /**
     * Copy a given point
     *
     * @return a new array filled with the given point values
     */

    private static double[] copy(double[] point, int length) {
        double[] copy = new double[length];

        int size = Math.min(length, point.length);

        for (int i = 0; i < size; i++) {
            copy[i] = point[i];
        }

        for (int i = size; i < length; i++) {
            copy[i] = 0;
        }

        return copy;
    }

    /**
     * Fill the first given array with the second one
     *
     * @param toFill an array to fill
     * @param filler an array used to fill the first given one
     */

    private void fill(double[] toFill, double[] filler) {
        for (int i = 0; i < toFill.length; i++) {
            toFill[i] = filler[i];
        }
    }

    /**
     * Calculate the new min and max values according to the given point
     * <br>
     * Time required: T(1)
     * <br>
     * Space required: O(1)
     */

    private void extractMaxMin(double[] vector) {
        // Calculate new min and max values
        for (int i = 0; i < vector.length; i++) {
            double val = vector[i];
            if (val < minValues[i]) minValues[i] = val;
            if (val > maxValues[i]) maxValues[i] = val;
        }
    }

    /**
     * Extract the min and max values from this distribution.
     * <br>
     * Time required: T(n)
     * <br>
     * Space required: O(1)
     */

    private void extractMaxMin() {
        // clear min and max values
        for (int i = 0; i < minValues.length; i++) {
            minValues[i] = maxValues[i] = 0;
        }

        for (double[] i : data) {

            // Calculate new min and max values
            for (int j = 0; j < i.length; j++) {
                double val = i[j];
                if (val < minValues[j]) minValues[j] = val;
                if (val > maxValues[j]) maxValues[j] = val;
            }
        }
    }

    /**
     * Remove all data contained inside this distribution
     */

    public void clear() {
        data.clear();

        // clear min and max values
        for (int i = 0; i < minValues.length; i++) {
            minValues[i] = maxValues[i] = 0;
        }
    }

    /**
     * Add a new point to this distribution
     *
     * @param point a not null array which length must be equal to the distribution's dimension
     * @return true if the point has been correctly added
     */

    public boolean add(double[] point) {
        if (point != null) {
            // make a copy to prevent external manipulation
            double[] copy = copy(point, dimensions);
            data.add(copy);
            extractMaxMin(copy);
        }

        return false;
    }

    /**
     * Replace a point with a new one.
     * <br>
     * Note that the new point must have the same dimensionality of the data distribution
     *
     * @param i     the position of the point to replace
     * @param point the new point
     * @return true if the new point has been correctly set
     */

    public boolean set(int i, double[] point) {
        if (i >= 0 && i < data.size() &&
                point != null && point.length == dimensions) {
            // make a copy to prevent external manipulation
            double[] copy = copy(point, dimensions);
            data.set(i, copy);
            extractMaxMin(copy);
        }
        return false;
    }

    /**
     * Remove the specified point from this data distribution
     *
     * @param i the position of the point to remove
     * @return true if the specified point has been removed
     */

    public boolean remove(int i) {
        if (i >= 0 && i < data.size()) {
            data.remove(i);
            // maybe can be improved?
            extractMaxMin();
        }
        return false;
    }

    /**
     * Return dimension's value of the specified point
     *
     * @param i the point position inside this distribution
     * @param j the point dimension
     * @return the dimension's value
     */

    public double get(int i, int j) {
        return data.get(i)[j];
    }

    public double getMin(int i) {
        return minValues[i];
    }

    public double getMax(int i) {
        return maxValues[i];
    }

    /**
     * Calculate the mean for every dimension of every point
     */

    public double[] getMean() {
        // reset mean value for every dimension
        for (int i = 0; i < dimensions; i++) {
            mean[i] = 0;
        }

        for (double[] p : data) {

            for (int j = 0; j < dimensions; j++) {
                mean[j] += p[j];
            }
        }

        int size = data.size();
        for (int i = 0; i < dimensions; i++) {
            mean[i] /= size;
        }

        return copy(mean, dimensions);
    }

    /**
     * @param useCorrectVariance true to calculate variance as sum( (xi - ux)^2 ) / (n+1)
     * @return the standard deviation, for every dimension, of this data distribution
     */

    public double[] getStandardDeviation(boolean useCorrectVariance) {
        for (int i = 0; i < dimensions; i++) {
            standardDeviation[i] = 0;
        }

        // sum( (xi - ux)^2 ) / n
        for (double[] p : data) {

            for (int j = 0; j < dimensions; j++) {
                double arg = p[j] - mean[j];
                standardDeviation[j] += arg * arg;
            }
        }

        int size = data.size() + (useCorrectVariance ? 1 : 0);
        for (int i = 0; i < dimensions; i++) {
            standardDeviation[i] = Math.sqrt(standardDeviation[i] / size);
        }

        return copy(standardDeviation, dimensions);
    }

    /**
     * @return the amount of points
     */

    public int size() {
        return data.size();
    }

    /**
     * @return the point's dimensionality contained inside this data distribution
     */

    public int dimensionality() {
        return dimensions;
    }

    /**
     * @return the position inside this distribution of the specified point
     */

    public int indexOf(double[] point) {
        return data.indexOf(point);
    }

    /**
     * @return true if the given point exists inside this distribution
     */

    public boolean contains(double[] point) {
        return indexOf(point) != -1;
    }

    /**
     * @param i the dimension
     * @return the mean for the specified dimension
     */

    public double getMean(int i) {
        return mean[i];
    }

    /**
     * @param i the dimension
     * @return the standard deviation for the specified dimension
     */

    public double getStandardDeviation(int i) {
        return standardDeviation[i];
    }
}
