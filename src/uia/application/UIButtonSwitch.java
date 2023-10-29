package uia.application;

import uia.core.basement.Drawable;
import uia.physical.theme.ThemeDarcula;
import uia.core.Paint;
import uia.core.ui.ViewText;
import uia.physical.ComponentText;
import uia.physical.wrapper.WrapperView;
import uia.core.ui.View;
import uia.core.ui.callbacks.OnClick;
import uia.physical.Component;
import uia.physical.ComponentGroup;

/**
 * Two states switchable button
 */

public class UIButtonSwitch extends WrapperView {
    private final ViewText[] states;

    private final Paint activePaint;

    public UIButtonSwitch(View view) {
        super(new ComponentGroup(view));

        buildGeometry(g -> Drawable.buildRect(g, getWidth(), getHeight(), 1f), true);
        registerCallback((OnClick) touches -> setState(!isFirstState()));

        activePaint = new Paint().setColor(ThemeDarcula.W_FOREGROUND);

        ViewText v1 = createView("LEFT", 0.25f);
        v1.setText("Left");
        v1.getPaint().set(activePaint);

        ViewText v2 = createView("RIGHT", 0.75f);
        v2.setText("Right");

        states = new ViewText[]{v1, v2};

        ((ComponentGroup) getView()).add(states);
    }

    /**
     * Helper function
     *
     * @return a new {@link ViewText}
     */

    private static ViewText createView(String id, float x) {
        ViewText out = new ComponentText(new Component(id, x, 0.5f, 0.5f, 1f));
        out.setAlign(ViewText.AlignY.CENTER);
        out.setConsumer(Consumer.SCREEN_TOUCH, false);
        return out;
    }

    /**
     * Set the button's text
     *
     * @param s1 the text on the left
     * @param s2 the text on the right
     */

    public void setText(String s1, String s2) {
        states[0].setText(s1);
        states[1].setText(s2);
    }

    /**
     * Switch the button to the first or second state
     *
     * @param first true to set the button to the first state
     */

    public void setState(boolean first) {
        if (first) {
            states[0].getPaint().set(activePaint);
            states[1].getPaint().set(getPaint());
        } else {
            states[0].getPaint().set(getPaint());
            states[1].getPaint().set(activePaint);
        }
    }

    /**
     * @return true if the button is on the first state
     */

    public boolean isFirstState() {
        return states[0].getPaint().equals(activePaint);
    }

    /**
     * @return the {@link Paint} that colors the active state
     */

    public Paint getActivePaint() {
        return activePaint;
    }

    /**
     * @return the left {@link ViewText}
     */

    public ViewText getLeftView() {
        return states[0];
    }

    /**
     * @return the right {@link ViewText}
     */

    public ViewText getRightView() {
        return states[1];
    }
}
