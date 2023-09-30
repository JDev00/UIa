package test.artefacts;

import uia.core.Font;
import uia.core.ui.Context;
import uia.core.ui.View;
import uia.physical.Component;
import uia.physical.ComponentText;
import uia.physical.wrapper.WrapperViewText;

/**
 * Context information tracker
 */

public class ComponentTracker extends WrapperViewText {
    private final Context context;

    public ComponentTracker(Context context, float x, float y, float w, float h) {
        super(new ComponentText(new Component("FPS", x, y, w, h)));

        setAlign(AlignY.CENTER);
        getFont().setStyle(Font.STYLE.ITALIC);

        this.context = context;
    }

    @Override
    public void update(View parent) {
        super.update(parent);

        long mem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
        setText("FPS: " + context.getFrameRate() + "\nMem: " + mem + " MB");
    }
}
