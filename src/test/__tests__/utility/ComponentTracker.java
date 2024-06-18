package test.__tests__.utility;

import uia.core.ui.style.TextHorizontalAlignment;
import uia.physical.ui.component.ComponentText;
import uia.physical.ui.component.WrapperView;
import uia.physical.ui.component.Component;
import uia.core.ui.ViewText;
import uia.core.rendering.font.Font;
import uia.core.ui.View;

/**
 * Component designed to display the UIa used resources.
 */

public class ComponentTracker extends WrapperView {
    private final ViewText viewText;

    public ComponentTracker(float x, float y, float w, float h) {
        super(new ComponentText(
                new Component("FPS_TRACKER", x, y, w, h))
        );

        viewText = this.getView();
        viewText.getStyle()
                .setTextAlignment(TextHorizontalAlignment.CENTER)
                .setFontStyle(Font.FontStyle.ITALIC);
    }

    @Override
    public void update(View parent) {
        super.update(parent);

        long usedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
        viewText.setText("FPS: untracked" + "\nMem: " + usedMemory + " MB");
    }
}
