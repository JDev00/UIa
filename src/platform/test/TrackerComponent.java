package platform.test;

import uia.core.Font;
import uia.core.architecture.Context;
import uia.core.architecture.ui.View;
import uia.physical.ui.ComponentText;
import uia.physical.ui.wrapper.WrapperViewText;

/**
 * Context information tracker
 */

public class TrackerComponent extends WrapperViewText {
    private final Context context;

    public TrackerComponent(View view, Context context) {
        super(new ComponentText(view));

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
