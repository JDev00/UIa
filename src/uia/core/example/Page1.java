package uia.core.example;

import uia.core.Page;
import uia.core.View;
import uia.core.platform.independent.shape.Figure;
import uia.core.platform.policy.Context;
import uia.core.platform.policy.Graphic;

import static uia.utils.TrigTable.*;

public class Page1 extends Page {

    public Page1(Context context) {
        super(context);
        context.setHint(Context.HINT.SMOOTH);

        int m = 10;
        int n = 19;
        int vertices = 6;

        float width = width() / n;
        float height = width;
        float apothem = width * (1 - sin(DEG_TO_RAD * 90f * (vertices - 2) / vertices)) / 2f;

        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {
                View v = new View(context, (j + 0.5f - n / 2f) * width, (i + 0.5f - m / 2f) * height - i * apothem, width, height);
                v.getPaint().setColor((int) (255f * i / (m - 1)), 0, 0);
                v.setShape(Figure.buildOval(v.getShape(), vertices));

                add(v);
            }
        }
    }

    float counter = 0f;

    @Override
    public void draw(Graphic graphic) {
        for (int i = 0; i < size(); i++) {
            float a = counter + i / 10f;

            View ti = get(i);
            ti.setRotation(a);
            ti.getPaint().setColor(
                    (int) Math.abs(255f * cos(a / 2f) * cos((2f * a + TWO_PI / 2f))),
                    (int) Math.abs(255f * cos(a / 2f) * sin((2f * a + TWO_PI / 3f))),
                    (int) Math.abs(255f * sin(a / 2f) * sin((2f * a + TWO_PI / 3f)))
            );
        }
        counter += 0.05f;

        super.draw(graphic);
    }
}
