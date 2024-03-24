package uia.application.loading;

import test.__tests__.utility.TestUtility;
import uia.core.paint.Paint;
import uia.core.shape.Geometry;
import uia.core.shape.Shape;
import uia.core.ui.Graphics;
import uia.core.ui.View;
import uia.core.ui.context.Context;
import uia.physical.component.Component;
import uia.physical.component.WrapperView;
import uia.physical.component.utility.ComponentUtility;
import uia.physical.theme.Theme;
import uia.utility.Geometries;

/**
 * Standard UIa component.
 * <br>
 * Loading View.
 */

public class UILoading extends WrapperView {
    private final Shape shape;
    private final Paint paint;
    private final Geometry geometry;

    public UILoading(View view) {
        super(view);

        geometry = Geometries.rect(new Geometry());

        paint = new Paint().setColor(Theme.RED);

        shape = new Shape();
    }

    @Override
    public void update(View parent) {
        super.update(parent);
        ComponentUtility.makeShapeForClipRegion(this, shape, 0.75f, 0.75f);
        shape.setDimension(100f, 100f);
        shape.setGeometry(geometry);
    }

    float angle = 0f;

    @Override
    public void draw(Graphics graphics) {
        super.draw(graphics);

        angle += 0.01f;
        shape.setRotation(angle);

        graphics.setPaint(paint);
        graphics.drawShape(shape);
    }

    public static void main(String[] args) {
        Context context = TestUtility.createMockContext();
        context.setView(new UILoading(
                new Component("", 0.5f, 0.5f, 1f, 1f)
        ));
    }
}
