package uia.application;

import uia.core.basement.Drawable;
import uia.core.ui.View;
import uia.physical.theme.Theme;
import uia.physical.wrapper.WrapperView;
import uia.core.ui.ViewText;
import uia.utility.GeometryFactory;
import uia.physical.Component;
import uia.physical.ComponentGroup;
import uia.physical.ComponentText;
import uia.utility.TrigTable;

/**
 * Filled button, with an element on the right or on the left
 */

public class UIButtonFilled extends WrapperView {
    private final View icon;
    private final ViewText viewText;

    public UIButtonFilled(View view, boolean right) {
        super(new ComponentGroup(view));

        setGeometry(g -> Drawable.buildRect(g, getWidth(), getHeight(), 1f), true);

        viewText = new ComponentText(new Component("TEXT", 0.5f + (right ? -0.05f : 0.05f), 0.5f, 0.5f, 1f)
                .setExpanseLimit(1f, 1f));
        viewText.setConsumer(Consumer.SCREEN_TOUCH, false);
        viewText.setAlign(right ? ComponentText.AlignX.LEFT : ComponentText.AlignX.RIGHT);
        viewText.setAlign(ComponentText.AlignY.CENTER);
        viewText.getPaint().setColor(Theme.TRANSPARENT);
        viewText.getTextPaint().setColor(Theme.BLACK);

        icon = new Component("ICON", right ? 0.875f : 0.125f, 0.5f, 0.15f, 0.4f)
                .setExpanseLimit(1.25f, 1.25f);
        icon.setColliderPolicy(ColliderPolicy.AABB);
        icon.setConsumer(Consumer.SCREEN_TOUCH, false);
        icon.setGeometry(GeometryFactory::arrow, false);
        icon.setRotation(right ? 0f : TrigTable.PI);
        icon.getPaint().setColor(Theme.BLACK);

        ((ComponentGroup) getView()).add(viewText, icon);
    }

    /**
     * @return the {@link ViewText} used to render text
     */

    public ViewText getViewText() {
        return viewText;
    }

    /**
     * @return the {@link View} used to represent an icon
     */

    public View getIcon() {
        return icon;
    }
}
