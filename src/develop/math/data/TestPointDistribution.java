package develop.math.data;

import test.artefacts.TestValidation;

import java.lang.reflect.InvocationTargetException;

/**
 * Unit test
 */

public class TestPointDistribution {

    float[][] distribution = {
            new float[]{0, 0},
            new float[]{-4999.99f, 0},
            new float[]{2345, -5000f},
            new float[]{50, 4998.98f},
            new float[]{-4566, 677},
            new float[]{0.1f, 0.0001f}
    };

    PointDistribution pointDistribution = new PointDistribution();

    public TestPointDistribution() {
        for (float[] point : distribution) {
            pointDistribution.add(point[0], point[1]);
        }
    }

    public void ensureMinimumPointIsObtained() {
        TestValidation.assertThat(
                Float.compare(pointDistribution.getMin(PointDistribution.AXIS.X), -4999.99f) == 0
                        && Float.compare(pointDistribution.getMin(PointDistribution.AXIS.Y), -5000f) == 0,
                true);
    }

    public void ensureMaximumPointIsObtained() {
        TestValidation.assertThat(
                Float.compare(pointDistribution.getMax(PointDistribution.AXIS.X), 2345f) == 0
                        && Float.compare(pointDistribution.getMax(PointDistribution.AXIS.Y), 4998.98f) == 0,
                true);
    }

    public void ensureMeanIsCorrect() {
        TestValidation.assertThat(
                Float.compare(pointDistribution.getMean(PointDistribution.AXIS.X), -1195.14833f) == 0
                        && Float.compare(pointDistribution.getMean(PointDistribution.AXIS.Y), 112.66335f) == 0,
                true);
    }

    // TODO: implement test for standard deviation

    public void ensureStandardDeviationIsCorrect() {
        TestValidation.assertThat(
                Float.compare(pointDistribution.getStandardDeviation(PointDistribution.AXIS.X), 0f) == 0
                        && Float.compare(pointDistribution.getStandardDeviation(PointDistribution.AXIS.Y), 0f) == 0,
                true);
    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        TestPointDistribution testPointDistribution = new TestPointDistribution();
        //checkPointDistribution.class.getMethods()[0].invoke(null);
        testPointDistribution.ensureMeanIsCorrect();
    }
}
