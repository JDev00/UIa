package example.processing;

import uia.application.awt.ContextAWT;
import processing.core.PGraphics;
import uia.core.ui.Context;
import uia.core.ui.Graphic;
import uia.physical.wrapper.WrapperView;
import uia.physical.Component;
import uia.physical.ComponentGroup;

public class Spike3DRender extends WrapperView {
    private float rot = 0f;

    public Spike3DRender() {
        super(new ComponentGroup(new Component("SPIKE_3D", 0.5f, 0.5f, 1f, 1f)
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

    public static void main(String[] args) {
        int[] dim = ContextAWT.getScreenSize();

        Context context = new ContextProcessing(4 * dim[0] / 5, 4 * dim[1] / 5);
        context.start();
        context.setView(new Spike3DRender());
    }
}
