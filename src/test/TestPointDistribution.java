package test;

import develop.math.PointDistribution;
import test.artefacts.TestCase;
import test.artefacts.TestSuite;
import test.artefacts.TestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Unit test
 */

public class TestPointDistribution implements TestSuite {
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

    public static TestCase minimumPointShouldBeObtained() {
        return (testAssertion) -> {
            testAssertion.expect(pointDistribution.getMin(PointDistribution.AXIS.X)).toBeEqual(-4999.99f);
            testAssertion.expect(pointDistribution.getMin(PointDistribution.AXIS.Y)).toBeEqual(-5000f);
        };
    }

    public static TestCase maximumPointShouldBeObtained() {
        return (testAssertion) -> {
            testAssertion.expect(pointDistribution.getMax(PointDistribution.AXIS.X)).toBeEqual(2345f);
            testAssertion.expect(pointDistribution.getMax(PointDistribution.AXIS.Y)).toBeEqual(4998.98f);
        };
    }

    public static TestCase meanShouldBeObtained() {
        return (testAssertion) -> {
            testAssertion.expect(pointDistribution.getMean(PointDistribution.AXIS.X)).toBeEqual(-1195.14833f);
            testAssertion.expect(pointDistribution.getMean(PointDistribution.AXIS.Y)).toBeEqual(112.66335f);
        };
    }

    public static TestCase standardDeviationShouldBeObtained() {
        return (testAssertion) -> {
            testAssertion.expect(pointDistribution.getStandardDeviation(PointDistribution.AXIS.X)).toBeEqual(2472.1162f);
            testAssertion.expect(pointDistribution.getStandardDeviation(PointDistribution.AXIS.Y)).toBeEqual(2682.5352f);
        };
    }

    @Override
    public Iterator<TestCase> iterator() {
        return new ArrayList<>(Arrays.asList(
                minimumPointShouldBeObtained(),
                maximumPointShouldBeObtained(),
                meanShouldBeObtained(),
                standardDeviationShouldBeObtained()
        )).iterator();
    }

    public static void main(String[] args) {
        TestUtils.runTestSuite(new TestPointDistribution());
    }
}
