package uia.application;

import uia.physical.component.utility.ComponentUtility;
import uia.core.ui.style.TextVerticalAlignment;
import uia.core.ui.primitives.shape.Geometry;
import uia.physical.component.ComponentText;
import uia.physical.component.WrapperView;
import uia.physical.group.ComponentGroup;
import uia.physical.component.Component;
import uia.core.ui.style.StyleFunction;
import uia.physical.theme.ThemeDarcula;
import uia.core.ui.callbacks.OnClick;
import uia.physical.theme.Theme;
import uia.core.ui.style.Style;
import uia.core.ui.ViewGroup;
import uia.core.ui.ViewText;
import uia.core.ui.View;

import java.util.Objects;

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

    private StyleFunction enabledStateStyleFunction;
    private StyleFunction disabledStateStyleFunction;

    private boolean firstState = true;

    public UIToggleButton(View view) {
        super(new ComponentGroup(view));

        registerCallback((OnClick) touches -> setState(isFirstState() ? State.SECOND : State.FIRST));
        getStyle().setGeometry(
                geometry -> ComponentUtility.buildRect(geometry, getWidth(), getHeight(), 1f),
                true
        );

        states = new ViewText[]{
                createView("BUTTON_TOGGLE_LEFT_" + getID(), 0.25f, "Left"),
                createView("BUTTON_TOGGLE_RIGHT_" + getID(), 0.75f, "Right")
        };

        ViewGroup.insert(getView(), states);

        enabledStateStyleFunction = style -> {
            Style containerStyle = getStyle();
            style.setTextColor(containerStyle.getBackgroundColor())
                    .setBackgroundColor(ThemeDarcula.BLUE);
        };

        disabledStateStyleFunction = style -> {
            Style enabledStateStyle = getEnabledStateStyle();
            style.setTextColor(enabledStateStyle.getBackgroundColor())
                    .setBackgroundColor(Theme.TRANSPARENT);
        };

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
        result.setInputConsumer(InputConsumer.SCREEN_TOUCH, false);
        result.setText(text);
        result.getStyle()
                .setTextAlignment(TextVerticalAlignment.CENTER)
                .setGeometry(
                        geometry -> ComponentUtility.buildRect(geometry, result.getWidth(), result.getHeight(), 1f),
                        true
                );
        return result;
    }

    /**
     * @return the style of the enabled state
     */

    private Style getEnabledStateStyle() {
        int index = firstState ? 0 : 1;
        return states[index].getStyle();
    }

    /**
     * Sets the styleFunction to use to set the style of the enabled state.
     *
     * @param styleFunction a styleFunction
     * @throws NullPointerException if {@code styleFunction == null}
     */

    public void setEnabledStateStyleFunction(StyleFunction styleFunction) {
        Objects.requireNonNull(styleFunction);

        this.enabledStateStyleFunction = styleFunction;
        updateStateStyle();
    }

    /**
     * Sets the styleFunction to use to set the style of the disabled state.
     *
     * @param styleFunction a styleFunction
     * @throws NullPointerException if {@code styleFunction == null}
     */

    public void setDisabledStateStyleFunction(StyleFunction styleFunction) {
        Objects.requireNonNull(styleFunction);

        this.disabledStateStyleFunction = styleFunction;
        updateStateStyle();
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
     * @param state one of {@link State}
     * @throws NullPointerException if {@code state == null}
     */

    public void setState(State state) {
        Objects.requireNonNull(state);

        // sets the text color for the two states
        this.firstState = State.FIRST.equals(state);
        updateStateStyle();
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
        states[index].getStyle().setGeometry(builder, inTimeBuilding);
    }

    /**
     * Helper function. Refreshes the state color when necessary.
     */

    private void updateStateStyle() {
        Style leftStateStyle = states[0].getStyle();
        Style rightStateStyle = states[1].getStyle();
        if (firstState) {
            leftStateStyle.applyStyleFunction(enabledStateStyleFunction);
            rightStateStyle.applyStyleFunction(disabledStateStyleFunction);
        } else {
            rightStateStyle.applyStyleFunction(enabledStateStyleFunction);
            leftStateStyle.applyStyleFunction(disabledStateStyleFunction);
        }
    }

    @Override
    public void update(View parent) {
        super.update(parent);
        updateStateStyle();
    }

    /**
     * @return the {@link Style} of the specified state
     */

    public Style getStateStyle(State state) {
        return State.FIRST.equals(state)
                ? states[0].getStyle()
                : states[1].getStyle();
    }
}
