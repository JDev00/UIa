package test;

import test.artefacts.TestValidation;
import uia.application.awt.ContextAWT;
import uia.core.ui.Context;
import uia.core.ui.View;
import uia.physical.Component;
import uia.physical.theme.Theme;
import uia.physical.wrapper.WrapperView;

import static uia.utility.TrigTable.*;

public class testViewRotationAndDimension extends WrapperView {
    private static final float ROTATION = 2.145f;
    private static final float[] DIMENSION = {200f, 200f};

    public testViewRotationAndDimension() {
        super(new Component("TEST", 0.5f, 0.5f, 1f, 1f));

        getPaint().setColor(Theme.DARK_GREY);
        setRotation(ROTATION);
    }

    @Override
    public void update(View parent) {
        super.update(parent);


        float width = getWidth();
        float height = getHeight();
        float[] bounds = bounds();

        float rotatedWidth = boundX(width, height, cos(bounds[4]), sin(bounds[4]));
        float rotatedHeight = boundY(width, height, cos(bounds[4]), sin(bounds[4]));

        TestValidation.assertThat(
                Float.compare(bounds[4], ROTATION) == 0
                        && Float.compare(width, DIMENSION[0]) == 0
                        && Float.compare(height, DIMENSION[1]) == 0
                        && Float.compare(bounds[2], rotatedWidth) == 0
                        && Float.compare(bounds[3], rotatedHeight) == 0, true);
    }

    //

    public static void main(String[] args) {
        Context context = new ContextAWT((int) DIMENSION[0], (int) DIMENSION[1]);
        context.start();
        context.setView(new testViewRotationAndDimension());
    }
}
