package test;

import test.artefacts.TestValidation;
import uia.application.platform.awt.ContextAWT;
import uia.core.Paint;
import uia.core.ui.Context;
import uia.physical.Component;
import uia.physical.wrapper.WrapperView;

public class testViewPaintSetAndGetColor extends WrapperView {
    private static final int[] COLOR = {255, 0, 128, 100};
    private static final int[] COLOR_STROKE = {0, 255, 128, 100};

    public testViewPaintSetAndGetColor() {
        super(new Component("TEST", 0.5f, 0.5f, 1f, 1f));

        Paint paint = getPaint();
        paint.setColor(new Paint.Color(COLOR[0], COLOR[1], COLOR[2], COLOR[3]));
        paint.setStrokeColor(new Paint.Color(COLOR_STROKE[0], COLOR_STROKE[1], COLOR_STROKE[2], COLOR_STROKE[3]));

        TestValidation.assertThat(
                paint.getRed() == COLOR[0]
                        && paint.getGreen() == COLOR[1]
                        && paint.getBlue() == COLOR[2]
                        && paint.getAlpha() == COLOR[3]
                        && paint.getStrokeRed() == COLOR_STROKE[0]
                        && paint.getStrokeGreen() == COLOR_STROKE[1]
                        && paint.getStrokeBlue() == COLOR_STROKE[2], true);
    }

    //

    public static void main(String[] args) {
        Context context = new ContextAWT(200, 200);
        context.start();
        context.setView(new testViewRotationAndDimension());
    }
}
