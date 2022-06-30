package uia;

import uia.core.Page;
import uia.core.View;
import uia.core.geometry.Figure;
import uia.core.geometry.Oval;
import uia.core.geometry.Rect;
import uia.core.geometry.Triangle;
import uia.core.platform.ContextAWT;
import uia.core.policy.Context;
import uia.core.policy.Path;
import uia.core.policy.Render;
import uia.utils.Utils;

import java.awt.*;

public class Test2 extends ContextAWT {

    public Test2(int x, int y) {
        super(x, y);

        open(new PageTwo(this));
    }

    /*
     *
     *
     */

    public static class PageOne extends Page {

        public PageOne(Context context) {
            super(context);
            context.setHint(HINT.SMOOTH);

            int m = 10;
            int n = 19;
            int vertices = 6;

            float dx = dx() / n;
            float dy = dx;
            float apotema = dx * (1 - Utils.psin(Utils.DEG_TO_RAD * 90f * (vertices - 2) / vertices)) / 2f;

            for (int i = 0; i < m; i++) {

                for (int j = 0; j < n; j++) {
                    Oval oval = new Oval(vertices);
                    //oval.setAngleOffset(-Utils.PI / 4);

                    View triangle = new View(context, (j + 0.5f - n / 2f) * dx, (i + 0.5f - m / 2f) * dy - i * apotema, dx, dy);
                    triangle.setPaint(context.createColor(triangle.getPaint(), 255f * i / (m - 1), 0f, 0f));
                    triangle.setFigure(oval);
                    triangle.enableAutoAdjustment(false);

                    add(triangle);
                }
            }
        }

        float counter = 0f;

        @Override
        public void draw(Render render) {
            /*for (int i = 0; i < size(); i++) {
                float a = counter + i / 10f;

                View ti = get(i);
                Oval figure = (Oval) ti.getFigure();
                figure.setAngleOffset(a);

                float r = Math.abs(255f * Utils.pcos(a / 2f) * Utils.pcos((2f * a + Utils.TWO_PI / 2f)));
                float g = Math.abs(255f * Utils.pcos(a / 2f) * Utils.psin((2f * a + Utils.TWO_PI / 3f)));
                float b = Math.abs(255f * Utils.psin(a / 2f) * Utils.psin((2f * a + Utils.TWO_PI / 3f)));
                ti.setPaint(getContext().createColor(ti.getPaint(), r, g, b));
            }
            counter += 0.05f;*/

            super.draw(render);
        }
    }

    /*
     *
     *
     */

    public static class PageTwo extends Page {

        public PageTwo(Context context) {
            super(context);

            int m = 10;
            int n = 19;

            float dx = dx() / n;
            float dy = dx;

            for (int i = 0; i < m; i++) {

                for (int j = 0; j < n; j++) {
                    View view = new View(context, (j + 0.5f - n / 2f) * dx, (i + 0.5f - m / 2f) * dy, dx, dy);
                    view.setFigure(new Rect());
                    view.enableAutoAdjustment(false);

                    add(view);
                }

                get(n * i).getPaint().enableStrokeColor(true);
                get(n * i).getPaint().setStrokeColor(0, 0, 0);
                get(n * i).setPaint(context.createColor(get(n * i).getPaint(), 255f * i / (m - 1), 0f, 0f));
            }
        }
    }

    /*
     *
     *
     */

    public static void main(String[] args) {
        int[] d = getScreenSize();
        Test2 test2 = new Test2(d[0], (int) (0.8f * d[1]));
        test2.start();
    }
}
