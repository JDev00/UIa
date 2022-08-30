package uia.test;

import uia.core.Page;
import uia.core.View;
import uia.core.geometry.Oval;
import uia.core.policy.Context;
import uia.core.policy.Render;
import uia.utils.Utils;

public class PageTest1 extends Page {

    public PageTest1(Context context) {
        super(context);
        context.setHint(Context.HINT.SMOOTH);

        int m = 10;
        int n = 19;
        int vertices = 6;

        float dx = dx() / n;
        float dy = dx;
        float apotema = dx * (1 - Utils.psin(Utils.DEG_TO_RAD * 90f * (vertices - 2) / vertices)) / 2f;

        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {
                View v = new View(context, (j + 0.5f - n / 2f) * dx, (i + 0.5f - m / 2f) * dy - i * apotema, dx, dy);
                v.setPaint(context.createColor(v.getPaint(), 255f * i / (m - 1), 0f, 0f));
                v.setFigure(new Oval(vertices));
                v.enableAutoAdjustment(false);

                add(v);
            }
        }
    }

    float counter = 0f;

    @Override
    public void draw(Render render) {
        for (int i = 0; i < size(); i++) {
            float a = counter + i / 10f;
            float r = Math.abs(255f * Utils.pcos(a / 2f) * Utils.pcos((2f * a + Utils.TWO_PI / 2f)));
            float g = Math.abs(255f * Utils.pcos(a / 2f) * Utils.psin((2f * a + Utils.TWO_PI / 3f)));
            float b = Math.abs(255f * Utils.psin(a / 2f) * Utils.psin((2f * a + Utils.TWO_PI / 3f)));

            View ti = get(i);
            ti.setRot(a);
            ti.setPaint(getContext().createColor(ti.getPaint(), r, g, b));
        }
        counter += 0.05f;

        super.draw(render);
    }
}
