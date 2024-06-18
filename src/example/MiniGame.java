package example;

import uia.core.context.Context;
import uia.core.rendering.Graphics;
import uia.core.rendering.Transform;
import uia.core.rendering.color.Color;
import uia.core.ui.View;
import uia.core.ui.callbacks.OnKeyPressed;
import uia.physical.ui.Theme;
import uia.physical.ui.component.Component;
import uia.physical.ui.component.WrapperView;
import uia.platform.swing.ContextSwing;

/**
 * Demonstrative example.
 */

// TODO: to complete

public class MiniGame extends WrapperView {
    private static final int RIGHT_ARROW = 39;
    private static final int LEFT_ARROW = 37;

    private Transform transform;
    private final float[] vertices = {
            -0.5f, -0.5f,
            0.5f, -0.5f,
            0.5f, 0.5f,
            -0.5f, 0.5f
    };

    private final Color shapeColor = Theme.LIME;

    protected MiniGame() {
        super(new Component("MINI_GAME", 0.5f, 0.5f, 1f, 1f));
        registerCallback((OnKeyPressed) key -> {
            int keyCode = key.getKeyCode();
            if (keyCode == RIGHT_ARROW) {
                transform
                        .translate(2, 0)
                        .rotate(0.05f);
            } else if (keyCode == LEFT_ARROW) {
                transform
                        .translate(-2, 0)
                        .rotate(-0.05f);
            }
        });

        transform = new Transform()
                .setTranslation(200f, 200f)
                .setScale(100f, 100f);
    }

    @Override
    public void update(View parent) {
        super.update(parent);
    }

    @Override
    public void draw(Graphics graphics) {
        super.draw(graphics);

        graphics
                .setShapeColor(shapeColor)
                .drawShape(transform, vertices.length / 2, vertices);
    }

    public static void main(String[] args) {
        Context context = ContextSwing.createAndStart(1000, 500);
        context.setView(new MiniGame());
    }
}
