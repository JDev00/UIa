package test;

import test.core.Test;
import test.core.TestAssertion;
import test.core.TestUtils;
import uia.core.Paint;

/**
 * Unit tests
 */

public class TestPaint {

    @Test
    public static void shouldBePossibleToSetColorAndStrokeColor(TestAssertion testAssertion) {
        testAssertion.assertions(7);

        final int[] COLOR = {255, 0, 128, 100};
        final int[] COLOR_STROKE = {0, 255, 128, 100};

        Paint paint = new Paint();
        paint.setColor(new Paint.Color(COLOR[0], COLOR[1], COLOR[2], COLOR[3]));
        paint.setStrokeColor(new Paint.Color(COLOR_STROKE[0], COLOR_STROKE[1], COLOR_STROKE[2], COLOR_STROKE[3]));

        testAssertion.expect(paint.getRed()).toBe(COLOR[0]);
        testAssertion.expect(paint.getGreen()).toBe(COLOR[1]);
        testAssertion.expect(paint.getBlue()).toBe(COLOR[2]);
        testAssertion.expect(paint.getAlpha()).toBe(COLOR[3]);
        testAssertion.expect(paint.getStrokeRed()).toBe(COLOR_STROKE[0]);
        testAssertion.expect(paint.getStrokeGreen()).toBe(COLOR_STROKE[1]);
        testAssertion.expect(paint.getStrokeBlue()).toBe(COLOR_STROKE[2]);
    }

    @Test
    public static void paintShouldBeAppliedToAnotherPaint(TestAssertion testAssertion) {
        testAssertion.assertions(7);

        Paint paintToSet = new Paint();
        paintToSet.setColor(new Paint.Color(255, 0, 0));
        paintToSet.setStrokeColor(new Paint.Color("0xff11aa"));

        Paint paint = new Paint();
        paint.set(paintToSet);

        testAssertion.expect(paint.getRed()).toBe(paintToSet.getRed());
        testAssertion.expect(paint.getGreen()).toBe(paintToSet.getGreen());
        testAssertion.expect(paint.getBlue()).toBe(paintToSet.getBlue());
        testAssertion.expect(paint.getAlpha()).toBe(paintToSet.getAlpha());
        testAssertion.expect(paint.getStrokeRed()).toBe(paintToSet.getStrokeRed());
        testAssertion.expect(paint.getStrokeGreen()).toBe(paintToSet.getStrokeGreen());
        testAssertion.expect(paint.getStrokeBlue()).toBe(paintToSet.getStrokeBlue());
    }

    public static void main(String[] args) {
        TestUtils.runTestSuite(new TestPaint());
    }
}
