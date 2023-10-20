package test;

import test.artefacts.TestValidation;
import uia.core.Paint;

/**
 * Unit test
 */

public class testPaint {

    public static void testSetColorAndStrokeColor() {
        final int[] COLOR = {255, 0, 128, 100};
        final int[] COLOR_STROKE = {0, 255, 128, 100};

        Paint paint = new Paint();
        paint.setColor(new Paint.Color(COLOR[0], COLOR[1], COLOR[2], COLOR[3]));
        paint.setStrokeColor(new Paint.Color(COLOR_STROKE[0], COLOR_STROKE[1], COLOR_STROKE[2], COLOR_STROKE[3]));

        TestValidation control = new TestValidation();
        control.expect(paint.getRed()).toBeEqual(COLOR[0]);
        control.expect(paint.getGreen()).toBeEqual(COLOR[1]);
        control.expect(paint.getBlue()).toBeEqual(COLOR[2]);
        control.expect(paint.getAlpha()).toBeEqual(COLOR[3]);
        control.expect(paint.getStrokeRed()).toBeEqual(COLOR_STROKE[0]);
        control.expect(paint.getStrokeGreen()).toBeEqual(COLOR_STROKE[1]);
        control.expect(paint.getStrokeBlue()).toBeEqual(COLOR_STROKE[2]);
    }

    public static void testCopyAndApplyPaint() {
        Paint paintToSet = new Paint();
        paintToSet.setColor(new Paint.Color(255, 0, 0));
        paintToSet.setStrokeColor(new Paint.Color("0xff11aa"));

        Paint paint = new Paint();
        paint.set(paintToSet);

        TestValidation control = new TestValidation();
        control.expect(paint.getRed()).toBeEqual(paintToSet.getRed());
        control.expect(paint.getGreen()).toBeEqual(paintToSet.getGreen());
        control.expect(paint.getBlue()).toBeEqual(paintToSet.getBlue());
        control.expect(paint.getAlpha()).toBeEqual(paintToSet.getAlpha());
        control.expect(paint.getStrokeRed()).toBeEqual(paintToSet.getStrokeRed());
        control.expect(paint.getStrokeGreen()).toBeEqual(paintToSet.getStrokeGreen());
        control.expect(paint.getStrokeBlue()).toBeEqual(paintToSet.getStrokeBlue());
    }

    //

    public static void main(String[] args) {
        testSetColorAndStrokeColor();
        testCopyAndApplyPaint();
    }
}
