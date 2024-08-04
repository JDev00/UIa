package uia.application.ui.component.text.edit;

import uia.core.rendering.color.ColorCollection;
import uia.application.ui.component.Component;
import uia.core.rendering.Graphics;

import uia.application.ui.component.WrapperView;
import uia.utility.Timer;

/**
 * UITextCursor represents the text cursor used when editing text.
 */

public class UITextCursor extends WrapperView {
    private final Timer timer;

    public UITextCursor(String id) {
        super(new Component(id, 0f, 0f, 1f, 1f));
        getStyle().setBackgroundColor(ColorCollection.BLACK);

        timer = new Timer();
    }

    public void resetTimer() {
        timer.reset();
    }

    @Override
    public void draw(Graphics graphics) {
        float seconds = timer.seconds();
        if (seconds <= 0.5f) {
            super.draw(graphics);
        } else if (seconds >= 1f) {
            timer.reset();
        }
    }
}