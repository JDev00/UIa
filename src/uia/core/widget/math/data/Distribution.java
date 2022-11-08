package uia.core.widget.math.data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 2D Vector's distribution
 */

public class Distribution {
    private final List<float[]> data;

    private final float[] minValues;
    private final float[] maxValues;

    private final float[] mean;
    private final float[] standardDeviation;

    public Distribution() {
        minValues = new float[2];
        maxValues = new float[2];
        mean = new float[2];
        standardDeviation = new float[2];

        data = new ArrayList<>();
    }

    /**
     * Copy a given point
     *
     * @return a new array filled with the given point values
     */

    private static float[] copy(float[] point) {
        int size = Math.min(2, point.length);
        float[] copy = new float[2];

        for (int i = 0; i < size; i++) {
            copy[i] = point[i];
        }

        for (int i = size; i < 2; i++) {
            copy[i] = 0;
        }

        return copy;
    }

    /**
     * Calculate the new min and max values according to the given point
     * <br>
     * Time required: T(n) where n is the vector's dimensionality
     * <br>
     * Space required: O(1)
     */

    private void extractMaxMin(float[] vector) {
        // Calculate new min and max values
        for (int i = 0; i < vector.length; i++) {
            float val = vector[i];
            if (val < minValues[i]) minValues[i] = val;
            if (val > maxValues[i]) maxValues[i] = val;
        }
    }

    /**
     * Extract the min and max values from this distribution.
     * <br>
     * Time required: T(n*m) where m is the vector's dimensionality
     * <br>
     * Space required: O(1)
     */

    private void extractMaxMin() {
        // clear min and max values
        for (int i = 0; i < minValues.length; i++) {
            minValues[i] = maxValues[i] = 0;
        }

        for (float[] i : data) {

            // Calculate new min and max values
            for (int j = 0; j < i.length; j++) {
                float val = i[j];
                if (val < minValues[j]) minValues[j] = val;
                if (val > maxValues[j]) maxValues[j] = val;
            }
        }
    }

    /**
     * Remove all data contained inside this distribution
     */

    public Distribution clear() {
        data.clear();

        // clear min and max values
        for (int i = 0; i < minValues.length; i++) {
            minValues[i] = maxValues[i] = 0;
        }

        return this;
    }

    /**
     * Add a new point to this distribution
     *
     * @param point a not null array
     * @return this distribution
     */

    public Distribution add(float[] point) {
        if (point != null) {
            // make a copy to avoid external manipulation
            float[] copy = copy(point);
            data.add(copy);
            extractMaxMin(copy);
        }
        return this;
    }

    /**
     * Add a set of points to this distribution
     *
     * @param function a not null {@link Function} used to generate new points
     * @return this distribution
     */

    public Distribution add(Function<Integer, float[]> function, int start, int points) {
        if (function != null) {
            int size = start + points;
            for (int i = start; i < size; i++) {
                add(function.apply(i));
            }
        }
        return this;
    }

    /**
     * Replace a point with a new one
     *
     * @param i     the position of the point to replace
     * @param point a not null array filled with the point's coordinates
     * @return this distribution
     */

    public Distribution set(int i, float[] point) {
        if (i >= 0 && i < data.size() && point != null) {
            // make a copy to prevent external manipulation
            float[] copy = copy(point);
            data.set(i, copy);
            extractMaxMin(copy);
        }
        return this;
    }

    /**
     * Remove the specified point
     *
     * @param i the position of the point to remove
     * @return this distribution
     */

    public Distribution remove(int i) {
        if (i >= 0 && i < data.size()) {
            data.remove(i);
            extractMaxMin();
        }
        return this;
    }

    /**
     * Return the specified point's dimension
     *
     * @param i the point position inside this distribution
     * @param j the dimension
     * @return the dimension's value
     */

    public float get(int i, int j) {
        return data.get(i)[j];
    }

    /**
     * @param i the point's dimension
     * @return the minimum value for the specified dimension
     */

    public float getMin(int i) {
        return minValues[i];
    }

    /**
     * @param i the point's dimension
     * @return the maximum value for the specified dimension
     */

    public float getMax(int i) {
        return maxValues[i];
    }

    /**
     * Calculate the mean of every point for every dimension
     */

    public float[] getMean() {
        // reset mean value for every dimension
        for (int i = 0; i < 2; i++) {
            mean[i] = 0;
        }

        for (float[] p : data) {

            for (int j = 0; j < 2; j++) {
                mean[j] += p[j];
            }
        }

        int size = data.size();
        for (int i = 0; i < 2; i++) {
            mean[i] /= size;
        }

        return copy(mean);
    }

    /**
     * @param useCorrectVariance true to calculate variance as sum( (xi - ux)^2 ) / (n+1)
     * @return the standard deviation, for every dimension, of this data distribution
     */

    public float[] getStandardDeviation(boolean useCorrectVariance) {
        for (int i = 0; i < 2; i++) {
            standardDeviation[i] = 0;
        }

        // sum( (xi - ux)^2 ) / n
        for (float[] p : data) {

            for (int j = 0; j < 2; j++) {
                float arg = p[j] - mean[j];
                standardDeviation[j] += arg * arg;
            }
        }

        int size = data.size() + (useCorrectVariance ? 1 : 0);
        for (int i = 0; i < 2; i++) {
            standardDeviation[i] = (float) Math.sqrt(standardDeviation[i] / size);
        }

        return copy(standardDeviation);
    }

    /**
     * @return the amount of points
     */

    public int size() {
        return data.size();
    }

    /**
     * @return the position inside this distribution of the specified point
     */

    public int indexOf(float[] point) {
        return data.indexOf(point);
    }

    /**
     * @param i the dimension
     * @return the mean for the specified dimension
     */

    public float getMean(int i) {
        return mean[i];
    }

    /**
     * @param i the dimension
     * @return the standard deviation for the specified dimension
     */

    public float getStandardDeviation(int i) {
        return standardDeviation[i];
    }

    /**
     * @return true if the given point exists inside this distribution
     */

    public boolean contains(float[] point) {
        return indexOf(point) != -1;
    }
}
