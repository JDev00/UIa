package test;

import test.core.TestCase;
import test.core.TestSuite;
import test.core.TestUtils;
import uia.core.Paint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Unit tests
 */

public class TestPaint implements TestSuite {

    public static TestCase shouldBePossibleToSetColorAndStrokeColor() {
        return (testAssertion) -> {
            final int[] COLOR = {255, 0, 128, 100};
            final int[] COLOR_STROKE = {0, 255, 128, 100};

            Paint paint = new Paint();
            paint.setColor(new Paint.Color(COLOR[0], COLOR[1], COLOR[2], COLOR[3]));
            paint.setStrokeColor(new Paint.Color(COLOR_STROKE[0], COLOR_STROKE[1], COLOR_STROKE[2], COLOR_STROKE[3]));

            testAssertion.expect(paint.getRed()).toBeEqual(COLOR[0]);
            testAssertion.expect(paint.getGreen()).toBeEqual(COLOR[1]);
            testAssertion.expect(paint.getBlue()).toBeEqual(COLOR[2]);
            testAssertion.expect(paint.getAlpha()).toBeEqual(COLOR[3]);
            testAssertion.expect(paint.getStrokeRed()).toBeEqual(COLOR_STROKE[0]);
            testAssertion.expect(paint.getStrokeGreen()).toBeEqual(COLOR_STROKE[1]);
            testAssertion.expect(paint.getStrokeBlue()).toBeEqual(COLOR_STROKE[2]);
        };
    }

    public static TestCase paintShouldBeAppliedToAnotherPaint() {
        return (testAssertion) -> {
            Paint paintToSet = new Paint();
            paintToSet.setColor(new Paint.Color(255, 0, 0));
            paintToSet.setStrokeColor(new Paint.Color("0xff11aa"));

            Paint paint = new Paint();
            paint.set(paintToSet);

            testAssertion.expect(paint.getRed()).toBeEqual(paintToSet.getRed());
            testAssertion.expect(paint.getGreen()).toBeEqual(paintToSet.getGreen());
            testAssertion.expect(paint.getBlue()).toBeEqual(paintToSet.getBlue());
            testAssertion.expect(paint.getAlpha()).toBeEqual(paintToSet.getAlpha());
            testAssertion.expect(paint.getStrokeRed()).toBeEqual(paintToSet.getStrokeRed());
            testAssertion.expect(paint.getStrokeGreen()).toBeEqual(paintToSet.getStrokeGreen());
            testAssertion.expect(paint.getStrokeBlue()).toBeEqual(paintToSet.getStrokeBlue());
        };
    }

    @Override
    public Iterator<TestCase> iterator() {
        return new ArrayList<>(Arrays.asList(
                shouldBePossibleToSetColorAndStrokeColor(),
                paintShouldBeAppliedToAnotherPaint()
        )).iterator();
    }

    public static void main(String[] args) {
        TestUtils.runTestSuite(new TestPaint());
    }
}
