package test.__tests__;

import test.core.BeforeEachTest;
import test.core.TestExecutor;
import uia.application.math.PointDistribution;
import test.core.Test;
import test.core.TestAssertion;

/**
 * Unit tests
 */

public class TestPointDistribution {
    PointDistribution pointDistribution;

    @BeforeEachTest
    public void beforeEach() {
        pointDistribution = new PointDistribution();

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
    public void theMinimumPointShouldBeObtainedFromPointDistribution(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        float[] expectedMinimumPoint = {
                pointDistribution.getMin(PointDistribution.AXIS.X),
                pointDistribution.getMin(PointDistribution.AXIS.Y)
        };
        testAssertion.expect(expectedMinimumPoint).toHaveValues(-4999.99f, -5000f);
    }

    @Test
    public void theMaximumPointShouldBeObtainedFromPointDistribution(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        float[] expectedMaximumPoint = {
                pointDistribution.getMax(PointDistribution.AXIS.X),
                pointDistribution.getMax(PointDistribution.AXIS.Y)
        };
        testAssertion.expect(expectedMaximumPoint).toHaveValues(2345f, 4998.98f);
    }

    @Test
    public void theMeanShouldBeObtainedFromPointDistribution(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        float[] expectedMean = {
                pointDistribution.getMean(PointDistribution.AXIS.X),
                pointDistribution.getMean(PointDistribution.AXIS.Y)
        };
        testAssertion.expect(expectedMean).toHaveValues(-1195.14833f, 112.66335f);
    }

    @Test
    public void theStandardDeviationShouldBeObtainedFromPointDistribution(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        float[] expectedStandardDeviation = {
                pointDistribution.getStandardDeviation(PointDistribution.AXIS.X),
                pointDistribution.getStandardDeviation(PointDistribution.AXIS.Y)
        };
        testAssertion.expect(expectedStandardDeviation).toHaveValues(2472.1162f, 2682.5352f);
    }

    public static void main(String[] args) {
        TestExecutor.runTests(new TestPointDistribution());
    }
}
