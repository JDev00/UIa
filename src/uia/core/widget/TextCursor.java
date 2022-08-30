package uia.core.widget;

import uia.core.View;
import uia.core.policy.Context;
import uia.utils.Timer;

/**
 * Text cursor object
 */

public class TextCursor extends View {
    protected final Timer timer;

    public TextCursor(Context context,
                      float px, float py,
                      float dx, float dy) {
        super(context, px, py, dx, dy);
        super.setPaint(context.createColor(getPaint(), Context.COLOR.RED));

        timer = new Timer();
    }

    public void resetTimer() {
        timer.reset();
    }

    @Override
    public void update() {
        super.setVisible(!timer.isOver(0.5f));

        if (timer.isOver(1f))
            timer.reset();
    }
}