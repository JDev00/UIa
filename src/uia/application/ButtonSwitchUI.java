package uia.application;

import uia.physical.ui.wrapper.WrapperView;
import uia.core.architecture.Graphic;
import uia.core.architecture.ui.View;
import uia.core.architecture.ui.event.EventClick;
import uia.core.Font;
import uia.core.Paint;
import uia.physical.Figure;
import uia.physical.ui.Component;
import uia.physical.ui.ComponentGroup;

/**
 * UI Button switchable between two states
 */

public class ButtonSwitchUI extends WrapperView {
    private final View switchView;

    private final Font font;
    private final Paint textPaint;

    private final String[] stateString = {"left", "right"};

    public ButtonSwitchUI(View view) {
        super(new ComponentGroup(view));

        setGeom((v, g) -> Figure.rect(g, Figure.STD_VERT, 1f, v.bounds()[2] / v.bounds()[3]), true);
        addEvent((EventClick) (v, pointers) -> setState(!isFirstState()));

        switchView = new Component("SwitchView", 0.25f, 0.5f, 0.5f, 1f);
        switchView.setConsumer(CONSUMER.POINTER, false);
        switchView.setGeom((v, g) -> Figure.rect(g, Figure.STD_VERT, 1f, v.bounds()[2] / v.bounds()[3]), true);
        switchView.getPaint().setColor(80, 80, 255);

        font = new Font("Arial", Font.STYLE.ITALIC, Font.FONT_SIZE_DESKTOP);

        textPaint = new Paint().setColor(0);

        ((ComponentGroup) getView()).add(switchView);
    }

    /**
     * Set the state text
     *
     * @param s1 the text on the left
     * @param s2 the text on the right
     */

    public void setText(String s1, String s2) {
        if (s1 != null) stateString[0] = s1;
        if (s2 != null) stateString[1] = s2;
    }

    /**
     * Switch the button to the first or second state
     *
     * @param first true to set the button to the first state
     */

    public void setState(boolean first) {
        float offset = first ? 0.25f : 0.75f;
        switchView.setPosition(offset, 0.5f);
    }

    @Override
    public void draw(Graphic graphic) {
        super.draw(graphic);

        float[] bounds = bounds();

        char[] c0 = stateString[0].toCharArray();
        char[] c1 = stateString[1].toCharArray();

        float l1 = font.getWidth(c0, 0, c0.length) / 2f;
        float l2 = font.getWidth(c1, 0, c1.length) / 2f;

        float x_off_1 = bounds[0] + 0.25f * bounds[2] - l1;
        float x_off_2 = bounds[0] + 0.75f * bounds[2] - l2;
        float y_off = bounds[1] + bounds[3] / 2f + 0.33f * font.getSize();

        graphic.setFont(font);
        graphic.setPaint(textPaint);

        // TODO: 21/07/2023 implement text rotation
        graphic.drawText(c0, 0, c0.length, x_off_1, y_off, 0f);
        graphic.drawText(c1, 0, c1.length, x_off_2, y_off, 0f);
    }

    /**
     * @return true if the button is on the first state
     */

    public boolean isFirstState() {
        float[] switchBounds = switchView.bounds();
        return switchBounds[0] + switchBounds[2] / 2f < bounds()[0] + bounds()[2] / 2f;
    }

    /**
     * @return the Button's Font
     */

    public Font getFont() {
        return font;
    }

    /**
     * @return the Paint used to color text
     */

    public Paint getPaintText() {
        return textPaint;
    }

    /**
     * @return the View used to highlight the current state
     */

    public View getSwitchView() {
        return switchView;
    }
}
