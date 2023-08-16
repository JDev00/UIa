package uia.application;

import uia.core.Paint;
import uia.core.architecture.ui.ViewText;
import uia.physical.ui.ComponentText;
import uia.physical.ui.wrapper.WrapperView;
import uia.core.architecture.ui.View;
import uia.core.architecture.ui.event.EventClick;
import uia.physical.Figure;
import uia.physical.ui.Component;
import uia.physical.ui.ComponentGroup;

/**
 * Two states switchable button
 */

public class ButtonSwitchUI extends WrapperView {
    private final ViewText[] states;

    private final Paint activePaint;

    public ButtonSwitchUI(View view) {
        super(new ComponentGroup(view));

        setGeom((v, g) -> Figure.rect(g, Figure.STD_VERT, 1f, v.bounds()[2] / v.bounds()[3]), true);
        addEvent((EventClick) (v, pointers) -> setState(!isFirstState()));

        activePaint = new Paint().setColor(0, 100, 255);

        ViewText v1 = createViewText("LeftView", 0.25f, 0.5f, 0.5f, 1f);
        v1.setText("Left");
        v1.getPaint().set(activePaint);

        ViewText v2 = createViewText("RightView", 0.75f, 0.5f, 0.5f, 1f);
        v2.setText("Right");

        states = new ViewText[]{v1, v2};

        ((ComponentGroup) getView()).add(states);
    }

    /**
     * Helper function
     *
     * @return a new {@link ViewText}
     */

    private static ViewText createViewText(String id, float x, float y, float w, float h) {
        ViewText out = new ComponentText(new Component(id, x, y, w, h));
        out.setAlign(ViewText.AlignY.CENTER);
        out.setConsumer(CONSUMER.POINTER, false);
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
        return states[0].getPaint().looseEquals(activePaint);
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
