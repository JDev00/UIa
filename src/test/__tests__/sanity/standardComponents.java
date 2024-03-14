package test.__tests__.sanity;

import test.__tests__.utility.TestUtility;
import uia.core.ui.ViewGroup;
import uia.core.ui.ViewText;
import uia.core.ui.callbacks.OnClick;
import uia.core.ui.context.Context;
import uia.physical.component.Component;
import uia.physical.ComponentImage;
import uia.physical.theme.Theme;

import static test.__tests__.utility.TestUtility.*;

/**
 * Sanity test.
 */

public class standardComponents {

    /**
     * Shows the base components
     */

    public static ViewGroup buildBasementsComponents() {
        ViewText text = createViewText("TEXT", 0.33f, 0.45f, 0.5f, 0.75f);
        text.setAlign(ViewText.AlignX.RIGHT);
        text.setAlign(ViewText.AlignY.CENTER);
        text.setText("Hello world!");
        text.setRotation(0.3f);

        ComponentImage image = new ComponentImage(
                new Component("IMAGE", 0.7f, 0.5f, 0.33f, 0.5f)
        );
        image.getImage().load("sample\\img0.png");
        image.registerCallback((OnClick) touches -> System.out.println("ComponentImage clicked!"));

        ViewGroup group = createViewGroup("GROUP", 0.4f, 0.5f, 0.5f, 0.5f);
        group.getPaint().setColor(Theme.LIGHT_GRAY);
        group.setRotation(0.1f);

        ViewGroup.insert(group, image, text);

        ViewGroup result = createRoot();
        ViewGroup.insert(result, group);
        return result;
    }

    public static void main(String[] args) {
        ViewGroup group = buildBasementsComponents();

        Context context = TestUtility.createMockContext();
        context.setView(group);
    }
}
