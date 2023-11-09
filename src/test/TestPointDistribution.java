package test;

import uia.develop.math.PointDistribution;
import test.core.Test;
import test.core.TestAssertion;
import test.core.TestUtils;

/**
 * Unit tests
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

    @Test
    public static void minimumPointShouldBeObtained(TestAssertion testAssertion) {
        testAssertion.assertions(2);
        testAssertion.expect(pointDistribution.getMin(PointDistribution.AXIS.X)).toBe(-4999.99f);
        testAssertion.expect(pointDistribution.getMin(PointDistribution.AXIS.Y)).toBe(-5000f);
    }

    @Test
    public static void maximumPointShouldBeObtained(TestAssertion testAssertion) {
        testAssertion.assertions(2);
        testAssertion.expect(pointDistribution.getMax(PointDistribution.AXIS.X)).toBe(2345f);
        testAssertion.expect(pointDistribution.getMax(PointDistribution.AXIS.Y)).toBe(4998.98f);
    }

    @Test
    public static void meanShouldBeObtained(TestAssertion testAssertion) {
        testAssertion.assertions(2);
        testAssertion.expect(pointDistribution.getMean(PointDistribution.AXIS.X)).toBe(-1195.14833f);
        testAssertion.expect(pointDistribution.getMean(PointDistribution.AXIS.Y)).toBe(112.66335f);
    }

    @Test
    public static void standardDeviationShouldBeObtained(TestAssertion testAssertion) {
        testAssertion.assertions(2);
        testAssertion.expect(pointDistribution.getStandardDeviation(PointDistribution.AXIS.X)).toBe(2472.1162f);
        testAssertion.expect(pointDistribution.getStandardDeviation(PointDistribution.AXIS.Y)).toBe(2682.5352f);
    }

    public static void main(String[] args) {
        TestUtils.runTestSuite(new TestPointDistribution());
    }
}
