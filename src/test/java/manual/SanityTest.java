package manual;

import uia.application.ui.component.text.edit.UIEditText;
import uia.core.rendering.geometry.GeometryCollection;
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

class SanityTest {

    /**
     * Creates the UIa concrete components.
     */

    public static ViewGroup buildComponents() {
        // component for editing text
        ViewText editText = new UIEditText(
                new Component("EDIT_TEXT", 0.25f, 0.15f, 0.4f, 0.1f)
        );
        editText.setPlaceholder("Edit me!");
        editText.getStyle()
                .setTextAlignment(TextVerticalAlignment.CENTER)
                .setBorderColor(ColorCollection.LIGHT_CORAL)
                .setGeometry(geometry -> GeometryCollection.rect(
                                geometry,
                                GeometryCollection.STD_VERT,
                                0.25f,
                                editText.getWidth() / editText.getHeight()),
                        true);

        // component for rendering text
        ViewText text = createViewText("TEXT", 0.25f, 0.65f, 0.5f, 0.6f);
        text.setText("Hello world!");
        text.getStyle()
                .setTextAlignment(TextHorizontalAlignment.RIGHT)
                .setTextAlignment(TextVerticalAlignment.CENTER)
                .setTextColor(ColorCollection.PINK)
                .setRotation(0.3f);

        // component for rendering an image
        ComponentImage image = new ComponentImage(
                new Component("IMAGE", 0.75f, 0.5f, 0.33f, 0.75f)
        );
        image.getImage().load("sample\\img0.png");

        // group of components
        ViewGroup group = createViewGroup("GROUP", 0.4f, 0.5f, 0.75f, 0.75f);
        group.getStyle()
                .setBackgroundColor(ColorCollection.LIGHT_GRAY)
                .setRotation(0.15f);
        ViewGroup.insert(group, image, text, editText);

        ViewGroup result = createRoot();
        ViewGroup.insert(result, group);
        return result;
    }

    public static void main(String[] args) {
        ViewGroup componentsView = buildComponents();

        Context context = TestUtility.createMockContext();
        context.setView(componentsView);
    }
}
