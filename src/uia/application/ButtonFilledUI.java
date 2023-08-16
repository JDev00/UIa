package uia.application;

import uia.core.architecture.ui.View;
import uia.physical.ui.wrapper.WrapperView;
import uia.core.architecture.ui.ViewText;
import uia.physical.Figure;
import uia.physical.ui.Component;
import uia.physical.ui.ComponentGroup;
import uia.physical.ui.ComponentText;
import uia.utils.TrigTable;

/**
 * Filled button, with an element on the right or on the left
 */

public class ButtonFilledUI extends WrapperView {
    private final View icon;
    private final ViewText viewText;

    public ButtonFilledUI(View view, boolean right) {
        super(new ComponentGroup(view));

        setGeom((v, g) -> Figure.rect(g, Figure.STD_VERT, 1f, v.bounds()[2] / v.bounds()[3]), true);

        viewText = new ComponentText(new Component("ViewText", 0.5f + (right ? -0.05f : 0.05f), 0.5f, 0.7f, 1f)
                .setExpanseLimit(1f, 1f));
        viewText.setConsumer(CONSUMER.POINTER, false);
        viewText.setAlign(right ? ComponentText.AlignX.LEFT : ComponentText.AlignX.RIGHT);
        viewText.setAlign(ComponentText.AlignY.CENTER);
        viewText.setText("prova!");
        viewText.getPaint().setColor(0, 0, 0, 1);
        viewText.getTextPaint().setColor(0);

        icon = new Component("ViewIcon", right ? 0.9f : 0.1f, 0.5f, 0.125f, 0.4f)
                .setExpanseLimit(1.25f, 1.25f);
        icon.setColliderPolicy(true);
        icon.setConsumer(CONSUMER.POINTER, false);
        icon.setGeom((v, g) -> Figure.arrow2(g), false);
        icon.setRotation(right ? 0f : TrigTable.PI);
        icon.getPaint().setColor(0);

        ((ComponentGroup) getView()).add(viewText, icon);
    }

    /**
     * @return the {@link ViewText}
     */

    public ViewText getViewText() {
        return viewText;
    }

    /**
     * @return the icon View
     */

    public View getIcon() {
        return icon;
    }
}
