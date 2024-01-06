package uia.application;

import uia.core.Font;
import uia.core.shape.Geometry;
import uia.core.basement.Drawable;
import uia.core.ui.ViewGroup;
import uia.physical.theme.Theme;
import uia.physical.theme.ThemeDarcula;
import uia.core.Paint;
import uia.core.ui.ViewText;
import uia.physical.ComponentText;
import uia.physical.WrapperView;
import uia.core.ui.View;
import uia.core.ui.callbacks.OnClick;
import uia.physical.Component;
import uia.physical.ComponentGroup;

/**
 * Standard UIa component.
 * <br>
 * Two states switchable button.
 */

public final class UIToggleButton extends WrapperView {

    /**
     * Toggle button states
     */
    public enum State {FIRST, SECOND}

    private final ViewText[] states;

    private final Paint activePaint;
    private final Paint defaultPaint;

    private boolean firstState = true;

    public UIToggleButton(View view) {
        super(new ComponentGroup(view));
        setGeometry(g -> Drawable.buildRect(g, getWidth(), getHeight(), 1f), true);
        registerCallback((OnClick) touches -> setState(isFirstState() ? State.SECOND : State.FIRST));

        activePaint = new Paint().setColor(ThemeDarcula.BLUE);
        defaultPaint = new Paint().setColor(Theme.TRANSPARENT);

        states = new ViewText[]{
                createView("BUTTON_TOGGLE_LEFT_" + getID(), 0.25f, "Left"),
                createView("BUTTON_TOGGLE_RIGHT_" + getID(), 0.75f, "Right")
        };

        ViewGroup.insert(getView(), states);

        setState(State.FIRST);
    }

    /**
     * Helper function. Creates the internal View.
     *
     * @return a new {@link ViewText}
     */

    private static ViewText createView(String id, float x, String text) {
        ViewText out = new ComponentText(
                new Component(id, x, 0.5f, 0.5f, 1f)
        );
        out.setGeometry(
                g -> Drawable.buildRect(g, out.getWidth(), out.getHeight(), 1f),
                true
        );
        out.setConsumer(Consumer.SCREEN_TOUCH, false);
        out.setAlign(ViewText.AlignY.CENTER);
        out.setText(text);
        return out;
    }

    /**
     * @return the {@link Paint} used to color che active state
     */

    public Paint getActivePaint() {
        return activePaint;
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
     * Helper function. Applies che specified source color.
     */

    private void applyColor(Paint target, Paint source) {
        target.setColor(new Paint.Color(
                source.getRed(),
                source.getGreen(),
                source.getBlue(),
                source.getAlpha()
        ));
    }

    /**
     * Switches this button to the first or second state
     *
     * @param state true to set this button on the first state
     */

    public void setState(State state) {
        this.firstState = State.FIRST.equals(state);
        if (firstState) {
            states[0].getPaint().set(activePaint);
            applyColor(states[0].getTextPaint(), getPaint());

            states[1].getPaint().set(defaultPaint);
            applyColor(states[1].getTextPaint(), activePaint);
        } else {
            states[0].getPaint().set(defaultPaint);
            applyColor(states[0].getTextPaint(), activePaint);

            states[1].getPaint().set(activePaint);
            applyColor(states[1].getTextPaint(), getPaint());
        }
    }

    /**
     * @return true if the button is on the first state
     */

    public boolean isFirstState() {
        return firstState;
    }

    /**
     * Sets the geometry for the specified state
     *
     * @param state          the button state, see {@link State}
     * @param builder        the geometry builder
     * @param inTimeBuilding true to build geometry for every frame
     */

    public void setGeometry(State state,
                            java.util.function.Consumer<Geometry> builder, boolean inTimeBuilding) {
        int index = 0;
        if (State.SECOND.equals(state)) {
            index = 1;
        }
        states[index].setGeometry(builder, inTimeBuilding);
    }

    /**
     * Helper function. Refreshes Paint when necessary.
     */

    private void refreshPaint() {
        if (isFirstState()) {

            if (!Paint.haveTheSameColor(states[0].getTextPaint(), getPaint())
                    || !Paint.haveTheSameColor(states[1].getTextPaint(), activePaint)) {
                setState(State.FIRST);
            }
        } else {

            if (!Paint.haveTheSameColor(states[0].getTextPaint(), activePaint)
                    || !Paint.haveTheSameColor(states[1].getTextPaint(), getPaint())) {
                setState(State.SECOND);
            }
        }
    }

    @Override
    public void update(View parent) {
        super.update(parent);
        refreshPaint();
    }

    /**
     * @return the {@link Font} associated with the specified state
     */

    public Font getFont(State state) {
        return State.FIRST.equals(state)
                ? states[0].getFont()
                : states[1].getFont();
    }
}
