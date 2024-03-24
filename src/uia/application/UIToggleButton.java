package uia.application;

import uia.core.Font;
import uia.core.shape.Geometry;
import uia.core.basement.Drawable;
import uia.core.ui.ViewGroup;
import uia.physical.theme.Theme;
import uia.physical.theme.ThemeDarcula;
import uia.core.paint.Paint;
import uia.core.ui.ViewText;
import uia.physical.component.ComponentText;
import uia.physical.component.WrapperView;
import uia.core.ui.View;
import uia.core.ui.callbacks.OnClick;
import uia.physical.component.Component;
import uia.physical.group.ComponentGroup;

/**
 * Standard UIa component.
 * <br>
 * UIToggleButton is a toggle button with two states.
 */

public final class UIToggleButton extends WrapperView {

    /**
     * Toggle button states.
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
        ViewText result = new ComponentText(
                new Component(id, x, 0.5f, 0.5f, 1f)
        );
        result.setConsumer(Consumer.SCREEN_TOUCH, false);
        result.setAlign(ViewText.AlignY.CENTER);
        result.setText(text);
        result.setGeometry(
                g -> Drawable.buildRect(g, result.getWidth(), result.getHeight(), 1f),
                true);
        return result;
    }

    /**
     * @return the {@link Paint} used to color che active state
     */

    public Paint getActivePaint() {
        return activePaint;
    }

    /**
     * Sets the text for the two states.
     *
     * @param textFirstState  the text for the first state
     * @param textSecondState the text for the second state
     */

    public void setText(String textFirstState, String textSecondState) {
        states[0].setText(textFirstState);
        states[1].setText(textSecondState);
    }

    /**
     * Switches this button to the first or second state.
     *
     * @param state true to set this button on the first state
     */

    public void setState(State state) {
        // sets the text color for the two states
        activePaint.setTextColor(getPaint().getColor());
        defaultPaint.setTextColor(activePaint.getColor());

        this.firstState = State.FIRST.equals(state);
        if (firstState) {
            states[0].getPaint().set(activePaint);
            states[1].getPaint().set(defaultPaint);
        } else {
            states[0].getPaint().set(defaultPaint);
            states[1].getPaint().set(activePaint);
        }
    }

    /**
     * @return true if the button is on the first state
     */

    public boolean isFirstState() {
        return firstState;
    }

    /**
     * Sets the geometry for the specified state.
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
     * Helper function. Refreshes the state color when necessary.
     */

    private void refreshStateColor() {
        if (firstState) {
            if (!states[0].getPaint().getTextColor().equals(getPaint().getColor())
                    || !states[1].getPaint().getTextColor().equals(activePaint.getColor())) {
                setState(State.FIRST);
            }
        } else if (!states[0].getPaint().getTextColor().equals(activePaint.getColor())
                || !states[1].getPaint().getTextColor().equals(getPaint().getColor())) {
            setState(State.SECOND);
        }
    }

    @Override
    public void update(View parent) {
        super.update(parent);
        refreshStateColor();
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
