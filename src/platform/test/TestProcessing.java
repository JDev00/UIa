package platform.test;

import processing.core.PGraphics;
import uia.core.architecture.Graphic;
import uia.physical.ui.wrapper.WrapperView;
import uia.physical.ui.Component;
import uia.physical.ui.ComponentGroup;
import uia.utils.TrigTable;

public class TestProcessing extends WrapperView {

    public TestProcessing() {
        super(new ComponentGroup(new Component("Comp", 0.5f, 0.5f, 1f, 1f).setExpanseLimit(1f, 1f)));

        getPaint().setColor(255);
    }

    private float rot = 0f;

    @Override
    public void draw(Graphic graphic) {
        super.draw(graphic);

        PGraphics pg = (PGraphics) graphic.getNative();

        /*pg.camera(
                TrigTable.cos(rot) * 100f, TrigTable.sin(rot) * 100f, 100f,
                0f, 0f, 0f,
                0f, 0f, -1f);*/

        float[] bounds = bounds();

        pg.directionalLight(255, 255, 255, -0.5f, 0.5f, -1f);

        pg.fill(255, 0, 0);

        pg.pushMatrix();
        pg.translate(
                bounds[0] + bounds[2] / 2f + TrigTable.cos(rot) * 100f,
                bounds[1] + bounds[3] / 2f + TrigTable.cos(rot) * 100f,
                100f);

        //pg.rotateY(rot);
        //pg.rotateX(rot / 2f);
        pg.box(100);
        pg.popMatrix();

        rot += 0.01f;
    }
}
