package test.__tests__.artefacts;

import uia.core.Font;
import uia.core.ui.View;
import uia.core.ui.ViewText;
import uia.physical.Component;
import uia.physical.ComponentText;
import uia.physical.WrapperView;

/**
 * Component designed to display the UIa used resources
 */

public class ComponentTracker extends WrapperView {
    private final ViewText viewText;

    public ComponentTracker(float x, float y, float w, float h) {
        super(new ComponentText(
                new Component("FPS_TRACKER", x, y, w, h))
        );

        viewText = this.getView();
        viewText.setAlign(ViewText.AlignY.CENTER);
        viewText.getFont().setStyle(Font.STYLE.ITALIC);
    }

    @Override
    public void update(View parent) {
        super.update(parent);
        long usedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
        viewText.setText("FPS: untracked" + "\nMem: " + usedMemory + " MB");
    }
}
