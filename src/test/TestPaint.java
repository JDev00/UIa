package test;

import test.artefacts.TestCase;
import test.artefacts.TestUtils;
import test.artefacts.TestValidation;
import uia.core.Paint;

/**
 * Unit test
 */

public class TestPaint {

    public static TestCase shouldBePossibleToSetColorAndStrokeColor() {
        return () -> {
            final int[] COLOR = {255, 0, 128, 100};
            final int[] COLOR_STROKE = {0, 255, 128, 100};

            Paint paint = new Paint();
            paint.setColor(new Paint.Color(COLOR[0], COLOR[1], COLOR[2], COLOR[3]));
            paint.setStrokeColor(new Paint.Color(COLOR_STROKE[0], COLOR_STROKE[1], COLOR_STROKE[2], COLOR_STROKE[3]));

            TestValidation validation = new TestValidation();
            validation.expect(paint.getRed()).toBeEqual(COLOR[0]);
            validation.expect(paint.getGreen()).toBeEqual(COLOR[1]);
            validation.expect(paint.getBlue()).toBeEqual(COLOR[2]);
            validation.expect(paint.getAlpha()).toBeEqual(COLOR[3]);
            validation.expect(paint.getStrokeRed()).toBeEqual(COLOR_STROKE[0]);
            validation.expect(paint.getStrokeGreen()).toBeEqual(COLOR_STROKE[1]);
            validation.expect(paint.getStrokeBlue()).toBeEqual(COLOR_STROKE[2]);
            return validation;
        };
    }

    public static TestCase paintShouldBeAppliedToAnotherPaint() {
        return () -> {
            Paint paintToSet = new Paint();
            paintToSet.setColor(new Paint.Color(255, 0, 0));
            paintToSet.setStrokeColor(new Paint.Color("0xff11aa"));

            Paint paint = new Paint();
            paint.set(paintToSet);

            TestValidation validation = new TestValidation();
            validation.expect(paint.getRed()).toBeEqual(paintToSet.getRed());
            validation.expect(paint.getGreen()).toBeEqual(paintToSet.getGreen());
            validation.expect(paint.getBlue()).toBeEqual(paintToSet.getBlue());
            validation.expect(paint.getAlpha()).toBeEqual(paintToSet.getAlpha());
            validation.expect(paint.getStrokeRed()).toBeEqual(paintToSet.getStrokeRed());
            validation.expect(paint.getStrokeGreen()).toBeEqual(paintToSet.getStrokeGreen());
            validation.expect(paint.getStrokeBlue()).toBeEqual(paintToSet.getStrokeBlue());
            return validation;
        };
    }

    public static void main(String[] args) {
        TestUtils.runTest(shouldBePossibleToSetColorAndStrokeColor());
        TestUtils.runTest(paintShouldBeAppliedToAnotherPaint());
    }
}
