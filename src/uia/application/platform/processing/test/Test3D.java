package uia.application.platform.processing.test;

import uia.application.platform.awt.ContextAWT;
import uia.application.platform.processing.ContextProcessing;
import processing.core.PGraphics;
import uia.core.basement.Context;
import uia.core.basement.Graphic;
import uia.physical.wrapper.WrapperView;
import uia.physical.Component;
import uia.physical.ComponentGroup;

public class Test3D extends WrapperView {
    private float rot = 0f;

    public Test3D() {
        super(new ComponentGroup(new Component("Comp", 0.5f, 0.5f, 1f, 1f)
                .setExpanseLimit(1f, 1f)));
    }

    @Override
    public void draw(Graphic graphic) {
        super.draw(graphic);

        float[] bounds = bounds();

        PGraphics pg = (PGraphics) graphic.getNative();

        pg.directionalLight(255, 255, 255, -0.5f, 0.5f, -1f);
        pg.fill(255, 0, 0);

        pg.pushMatrix();
        pg.translate(bounds[0] + bounds[2] / 2f, bounds[1] + bounds[3] / 2f, 100f);
        pg.rotateY(rot);

        pg.box(100);
        pg.popMatrix();

        rot += 0.01f;
    }

    //

    public static void main(String[] args) {
        int[] dim = ContextAWT.getScreenSize();

        Context context = new ContextProcessing(4 * dim[0] / 5, 4 * dim[1] / 5);
        context.start();
        context.setView(new Test3D());
    }
}
