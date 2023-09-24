package tutorial;

import uia.application.platform.awt.ContextAWT;
import uia.core.Paint;
import uia.core.Shape;
import uia.core.ui.Context;
import uia.core.ui.Graphic;
import uia.physical.Component;
import uia.physical.ComponentText;
import uia.physical.theme.Theme;
import uia.physical.wrapper.WrapperView;
import uia.physical.wrapper.WrapperViewText;
import uia.utility.Figure;

public class Example extends WrapperViewText {
    private final Paint paint;
    private final Shape shape;

    public Example() {
        super(new ComponentText(
                new Component("MAIN", 0.5f, 0.5f, 0.5f, 0.5f)
        ));

        getPaint().setColor(Theme.GREEN);
        setText("CIAO!");

        paint = new Paint().setColor(Theme.BLUE);

        shape = new Shape();
        shape.setPosition(210, 210);
        shape.setDimension(400, 400);
        //shape.setRotation(0.3f);
        Figure.rect(shape.getGeometry());

        //System.out.println(shape.contains(10, 10));
    }

    @Override
    public void draw(Graphic graphic) {
        super.draw(graphic);

        graphic.setPaint(paint);
        //graphic.drawShape(shape);
    }

    /*
     */

    public static void main(String[] args) {
        Example example = new Example();

        Context context = new ContextAWT(1900, 900);
        context.start();
        context.setView(example);

        context = new ContextAWT(1900, 900);
        context.start();
        context.setView(example);
    }
}
