package example;

import uia.application.ui.component.text.ComponentText;
import uia.application.ui.component.WrapperView;
import uia.core.rendering.color.ColorCollection;
import uia.application.ui.group.ComponentGroup;
import uia.application.ui.component.Component;
import uia.core.rendering.color.Color;
import uia.core.context.Context;
import uia.core.ui.ViewGroup;
import uia.core.ui.ViewText;
import uia.core.ui.View;

import adaptor.swing.ContextSwing;

/**
 * Demonstrative example. Shows a structured layout with hundreds of views.
 */

public class ComplexLayout extends WrapperView {

    public ComplexLayout() {
        super(new ComponentGroup(
                new Component("complex_layout", 0.5f, 0.5f, 1f, 1f)
        ));

        ViewGroup firstList = createList(0.25f, 0.5f, 0.45f, 0.95f);
        firstList.getStyle().setBackgroundColor(ColorCollection.LIGHT_CORAL);

        ViewGroup secondList = createList(0.75f, 0.5f, 0.45f, 0.5f);
        secondList.getStyle().setBackgroundColor(ColorCollection.DARK_GRAY);

        ViewGroup.insert(getView(), firstList, secondList);
    }

    /**
     * Helper function. Creates a list of views.
     */

    private static ViewGroup createList(float x, float y, float width, float height) {
        ViewGroup result = new ComponentGroup(new Component(
                "list", x, y, width, height)
        );

        final float viewHeight = 0.02f;
        View[] views = new ViewText[1_000];
        for (int i = 0; i < views.length; i++) {
            ViewText viewText = new ComponentText(
                    new Component("text_" + i, 0.5f, 0.5f * viewHeight * (i + 0.5f), 0.85f, viewHeight)
                            .setExpanseLimit(1f, 2f)
            );
            viewText.getStyle()
                    .setBackgroundColor(Color.createColor(255, 255, 255, 100))
                    .setFontSize(5f);
            viewText.setText("Hey " + i);

            views[i] = viewText;
        }

        ViewGroup.insert(result, views);

        return result;
    }

    public static void main(String[] args) {
        Context context = ContextSwing.createAndStart(1250, 740);
        context.setView(new ComplexLayout());
    }
}
