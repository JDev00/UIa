package manual;

import uia.application.ui.component.ComponentImage;
import uia.core.ui.style.TextHorizontalAlignment;
import uia.core.rendering.color.ColorCollection;
import uia.core.ui.style.TextVerticalAlignment;
import uia.application.ui.component.Component;
import uia.core.context.Context;
import uia.core.ui.ViewGroup;
import uia.core.ui.ViewText;

import utility.TestUtility;

import static utility.TestUtility.*;

/**
 * Sanity test.
 */

class ApplicationComponentsTest {

    private ApplicationComponentsTest() {
    }

    /**
     * Shows the base components
     */

    public static ViewGroup buildBasementsComponents() {
        ViewText text = createViewText("TEXT", 0.33f, 0.45f, 0.5f, 0.75f);
        text.setText("Hello world!");
        text.getStyle()
                .setTextAlignment(TextHorizontalAlignment.RIGHT)
                .setTextAlignment(TextVerticalAlignment.CENTER)
                .setTextColor(ColorCollection.PINK)
                .setRotation(0.3f);

        ComponentImage image = new ComponentImage(
                new Component("IMAGE", 0.7f, 0.5f, 0.33f, 0.5f)
        );
        image.getImage().load("sample\\img0.png");

        ViewGroup group = createViewGroup("GROUP", 0.4f, 0.5f, 0.5f, 0.5f);
        group.getStyle()
                .setBackgroundColor(ColorCollection.LIGHT_GRAY)
                .setRotation(0.1f);

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