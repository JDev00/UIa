package test.__tests__.Paint;

import test.core.BeforeEachTest;
import test.core.Test;
import test.core.TestAssertion;
import test.core.TestExecutor;
import uia.core.paint.Color;
import uia.core.paint.Paint;

/**
 * Unit tests
 */

public class PaintTest {
    int strokeWidth = 2;
    Color color = Color.createColor(255, 0, 128, 100);
    Color strokeColor = Color.createColor(0, 255, 128, 100);
    Paint paint;

    @BeforeEachTest
    public void beforeEach() {
        paint = new Paint();
    }

    @Test
    public void shouldBePossibleToSetThePaintColorTheStrokeColorAndTheStrokeWidth(TestAssertion testAssertion) {
        testAssertion.assertions(4);

        // test setup
        paint.setColor(color);
        paint.setStrokeColor(strokeColor);
        paint.setStrokeWidth(strokeWidth);

        // test controls
        int[] expectedColorValues = paint.getColor().getRGBA();
        testAssertion.expect(expectedColorValues).toHaveValues(color.getRGBA());

        int[] expectedStrokeColorValues = paint.getStrokeColor().getRGBA();
        testAssertion.expect(expectedStrokeColorValues).toHaveValues(
                strokeColor.getRed(),
                strokeColor.getGreen(),
                strokeColor.getBlue(),
                255
        );

        int expectedStrokeWidth = paint.getStrokeWidth();
        testAssertion.expect(expectedStrokeWidth).toBe(strokeWidth);

        testAssertion.expect(paint.hasStroke()).toBe(true);
    }

    @Test
    public void shouldBePossibleToApplyAPaintToAnotherPaint(TestAssertion testAssertion) {
        testAssertion.assertions(3);

        // test setup
        Paint paintToSet = new Paint();
        paintToSet.setColor(color);
        paintToSet.setStrokeColor(strokeColor);
        paintToSet.setStrokeWidth(strokeWidth);

        paint.set(paintToSet);

        // test controls
        int[] expectedColorValues = paint.getColor().getRGBA();
        testAssertion.expect(expectedColorValues).toHaveValues(color.getRGBA());

        int[] expectedStrokeColorValues = paint.getStrokeColor().getRGBA();
        testAssertion.expect(expectedStrokeColorValues).toHaveValues(
                strokeColor.getRed(),
                strokeColor.getGreen(),
                strokeColor.getBlue(),
                255
        );

        int expectedStrokeWidth = paint.getStrokeWidth();
        testAssertion.expect(expectedStrokeWidth).toBe(strokeWidth);
    }

    public static void main(String[] args) {
        TestExecutor.runTests(new PaintTest());
    }
}
