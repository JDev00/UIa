package uia.application;

import uia.core.Font;
import uia.core.paint.Paint;
import uia.core.basement.Drawable;
import uia.core.ui.View;
import uia.core.ui.ViewGroup;
import uia.physical.theme.Theme;
import uia.physical.WrapperView;
import uia.core.ui.ViewText;
import uia.utility.Geometries;
import uia.physical.Component;
import uia.physical.ComponentGroup;
import uia.physical.ComponentText;
import uia.utility.MathUtility;

/**
 * Standard UIa component.
 * <br>
 * Button with a text and an icon on the right or on the left.
 */

public final class UIButtonFilled extends WrapperView {
    private final View icon;
    private final ViewText viewText;

    public UIButtonFilled(View view, boolean right) {
        super(new ComponentGroup(view));

        setGeometry(g -> Drawable.buildRect(g, getWidth(), getHeight(), 1f), true);

        viewText = new ComponentText(
                new Component("BUTTON_FILLED_TEXT_" + getID(), 0.5f + (right ? -0.05f : 0.05f), 0.5f,
                        0.5f, 1f)
        );
        viewText.setConsumer(Consumer.SCREEN_TOUCH, false);
        viewText.setAlign(right ? ComponentText.AlignX.LEFT : ComponentText.AlignX.RIGHT);
        viewText.setAlign(ComponentText.AlignY.CENTER);
        viewText.getPaint().setColor(Theme.TRANSPARENT);
        viewText.getTextPaint().setColor(Theme.BLACK);

        icon = new Component("BUTTON_FILLED_ICON_" + getID(), right ? 0.875f : 0.125f, 0.5f,
                0.125f, 0.4f, 1.25f, 1.25f);
        icon.setColliderPolicy(ColliderPolicy.AABB);
        icon.setConsumer(Consumer.SCREEN_TOUCH, false);
        icon.setGeometry(Geometries::arrow, false);
        icon.setRotation(right ? 0f : MathUtility.PI);
        icon.getPaint().setColor(Theme.BLACK);

        ViewGroup.insert(getView(), viewText, icon);
    }

    /**
     * Sets the button text
     *
     * @param text a String to display; it could be null
     */

    public void setText(String text) {
        viewText.setText(text);
    }

    /**
     * @return the {@link Paint} object used to color text
     */

    public Paint getTextPaint() {
        return viewText.getTextPaint();
    }

    /**
     * @return the text {@link Font} object
     */

    public Font getFont() {
        return viewText.getFont();
    }

    /**
     * @return the {@link View} used to represent an icon
     */

    public View getIcon() {
        return icon;
    }
}
