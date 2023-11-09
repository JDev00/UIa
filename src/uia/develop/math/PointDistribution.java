package uia.develop.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Distribution of bi-dimensional points.
 * <br>
 * Distribution is responsible to handle a distribution of 2D points.
 * <br>
 * At the moment, only mean and standard deviation have been implemented.
 */

public class PointDistribution {

    /**
     * Cartesian axis
     */

    public enum AXIS {X, Y}

    private final List<float[]> data;

    private final FloatArray minPoint, maxPoint;

    private final FloatArray mean;
    private final FloatArray standardDeviation;

    public PointDistribution() {
        minPoint = new FloatArray();
        maxPoint = new FloatArray();
        mean = new FloatArray();
        standardDeviation = new FloatArray();

        data = new ArrayList<>();
    }

    /**
     * Map a {@link AXIS} to an index
     *
     * @param component a not null {@link AXIS}
     * @return 0 if {@code CARTESIAN_COMPONENT == X}, 1 otherwise
     */

    private static int mapCartesianComponentToIndex(AXIS component) {
        Objects.requireNonNull(component, "Trying to operate on a null CARTESIAN_COMPONENT instance");
        return component == AXIS.X ? 0 : 1;
    }

    /**
     * Extract the minimum point from this distribution.
     * <br>
     * Time required: T(n)
     * <br>
     * Space required: O(1)
     */

    private void extractMinimumPoint() {
        Arrays.fill(minPoint.data, Integer.MAX_VALUE);

        data.forEach(point -> {
            float x = point[0];
            float y = point[1];
            float[] data = minPoint.data;
            if (x < data[0]) data[0] = x;
            if (y < data[1]) data[1] = y;
        });
    }

    /**
     * Extract the maximum point from this distribution.
     * <br>
     * Time required: T(n)
     * <br>
     * Space required: O(1)
     */

    private void extractMaximumPoint() {
        Arrays.fill(maxPoint.data, Integer.MIN_VALUE);

        data.forEach(point -> {
            float x = point[0];
            float y = point[1];
            float[] data = maxPoint.data;
            if (x > data[0]) data[0] = x;
            if (y > data[1]) data[1] = y;
        });
    }

    /**
     * Calculate the distribution mean value
     */

    private void calculateMean() {
        float[] meanData = mean.data;

        Arrays.fill(meanData, 0f);

        data.forEach(point -> {
            meanData[0] += point[0];
            meanData[1] += point[1];
        });

        int size = data.size();
        meanData[0] /= size;
        meanData[1] /= size;
    }

    /**
     * @param correctVariance true to calculate variance as sum( (xi - ux)^2 ) / (n+1)
     */

    private void calculateStandardDeviation(boolean correctVariance) {
        calculateMean();

        float[] sdData = standardDeviation.data;
        float[] meanData = mean.data;

        Arrays.fill(sdData, 0);

        // sum( (xi - ux)^2 ) / n

        data.forEach(point -> {
            for (int i = 0; i < 2; i++) {
                float arg = point[i] - meanData[i];
                sdData[i] += arg * arg;
            }
        });

        int size = data.size() + (correctVariance ? 1 : 0);
        for (int i = 0; i < 2; i++) {
            sdData[i] = (float) Math.sqrt(sdData[i] / size);
        }
    }

    /**
     *
     */

    private void invalidateState() {
        minPoint.invalidateState();
        maxPoint.invalidateState();
        mean.invalidateState();
        standardDeviation.invalidateState();
    }

    /**
     * Remove all vectors in this distribution
     */

    public PointDistribution clear() {
        data.clear();
        invalidateState();
        return this;
    }

    /**
     * Add a new vector to this distribution
     *
     * @param x the vector's value on x-axis
     * @param y the vector's value on y-axis
     * @return this distribution
     */

    public PointDistribution add(float x, float y) {
        data.add(new float[]{x, y});
        invalidateState();
        return this;
    }

    /**
     * Replace a vector with a new one
     *
     * @param i the index of the vector to replace
     * @param x the vector's value on x-axis
     * @param y the vector's value on y-axis
     * @return this distribution
     * @throws IndexOutOfBoundsException if {@code i < 0 || i >= size()}
     */

    public PointDistribution set(int i, float x, float y) {
        data.set(i, new float[]{x, y});
        invalidateState();
        return this;
    }

    /**
     * Remove the specified vector
     *
     * @param i the index of the vector to remove
     * @return this distribution
     * @throws IndexOutOfBoundsException if {@code i < 0 || i >= size()}
     */

    public PointDistribution remove(int i) {
        data.remove(i);
        invalidateState();
        return this;
    }

    /**
     * Return the point's value on the specified axis
     *
     * @param i         the index of the point in this distribution
     * @param component the not null {@link AXIS}
     * @return the point's value on the specified axis
     * @throws IndexOutOfBoundsException if {@code i < 0 || i >= size()}
     * @throws NullPointerException      if {@code component == null}
     */

    public float get(int i, AXIS component) {
        return data.get(i)[mapCartesianComponentToIndex(component)];
    }

    /**
     * Returns the point with the smallest values
     *
     * @param component the not null {@link AXIS}
     * @return the point's value on the specified axis
     * @throws NullPointerException if {@code component == null}
     */

    public float getMin(AXIS component) {
        if (minPoint.isCorrupted()) {
            minPoint.validateState();
            extractMinimumPoint();
        }
        return minPoint.data[mapCartesianComponentToIndex(component)];
    }

    /**
     * Returns the point with the highest values
     *
     * @param component the not null {@link AXIS}
     * @return the point's value on the specified axis
     * @throws NullPointerException if {@code component == null}
     */

    public float getMax(AXIS component) {
        if (maxPoint.isCorrupted()) {
            maxPoint.validateState();
            extractMaximumPoint();
        }
        return maxPoint.data[mapCartesianComponentToIndex(component)];
    }

    /**
     * Returns the distribution's mean
     *
     * @param component the not null {@link AXIS}
     * @return the mean's value on the specified axis
     * @throws NullPointerException if {@code component == null}
     */

    public float getMean(AXIS component) {
        if (mean.isCorrupted()) {
            mean.validateState();
            calculateMean();
        }
        return mean.data[mapCartesianComponentToIndex(component)];
    }

    /**
     * Returns the correct standard deviation of this distribution
     *
     * @param component the not null {@link AXIS}
     * @return the standard deviation value on the specified axis
     * @throws NullPointerException if {@code component == null}
     */

    public float getStandardDeviation(AXIS component) {
        if (standardDeviation.isCorrupted()) {
            standardDeviation.validateState();
            calculateStandardDeviation(true);
        }
        return standardDeviation.data[mapCartesianComponentToIndex(component)];
    }

    /**
     * @return the amount of points
     */

    public int size() {
        return data.size();
    }

    /**
     * @return the point's index in this distribution or -1 if it isn't
     */

    public int indexOf(float x, float y) {
        return data.indexOf(new float[]{x, y});
    }

    /**
     * @return true if the given point is in this distribution
     */

    public boolean contains(float x, float y) {
        return indexOf(x, y) != -1;
    }

    /**
     * FloatArray is a wrapper for an array of float. It allows to track array's state.
     */

    private static class FloatArray {
        private boolean corruptedState;
        private final float[] data = new float[2];

        public void validateState() {
            corruptedState = false;
        }

        public void invalidateState() {
            corruptedState = true;
        }

        private boolean isCorrupted() {
            return corruptedState;
        }
    }
}
