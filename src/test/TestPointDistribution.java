package test;

import develop.math.data.PointDistribution;
import test.artefacts.TestValidation;

/**
 * Unit test
 */

public class TestPointDistribution {
    static PointDistribution pointDistribution = new PointDistribution();

    static {
        float[][] distribution = {
                new float[]{0, 0},
                new float[]{-4999.99f, 0},
                new float[]{2345, -5000f},
                new float[]{50, 4998.98f},
                new float[]{-4566, 677},
                new float[]{0.1f, 0.0001f}
        };

        for (float[] point : distribution) {
            pointDistribution.add(point[0], point[1]);
        }
    }

    public static void minimumPointShouldBeObtained() {
        TestValidation control = new TestValidation();
        control.expect(pointDistribution.getMin(PointDistribution.AXIS.X)).toBeEqual(-4999.99f);
        control.expect(pointDistribution.getMin(PointDistribution.AXIS.Y)).toBeEqual(-5000f);
    }

    public static void maximumPointShouldBeObtained() {
        TestValidation control = new TestValidation();
        control.expect(pointDistribution.getMax(PointDistribution.AXIS.X)).toBeEqual(2345f);
        control.expect(pointDistribution.getMax(PointDistribution.AXIS.Y)).toBeEqual(4998.98f);
    }

    public static void meanShouldBeObtained() {
        TestValidation control = new TestValidation();
        control.expect(pointDistribution.getMean(PointDistribution.AXIS.X)).toBeEqual(-1195.14833f);
        control.expect(pointDistribution.getMean(PointDistribution.AXIS.Y)).toBeEqual(112.66335f);
    }

    public static void standardDeviationShouldBeObtained() {
        TestValidation control = new TestValidation();
        control.expect(pointDistribution.getStandardDeviation(PointDistribution.AXIS.X)).toBeEqual(2472.1162f);
        control.expect(pointDistribution.getStandardDeviation(PointDistribution.AXIS.Y)).toBeEqual(2682.5352f);
    }

    public static void main(String[] args) {
        minimumPointShouldBeObtained();
        maximumPointShouldBeObtained();
        meanShouldBeObtained();
        standardDeviationShouldBeObtained();
    }
}
