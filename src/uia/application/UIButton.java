package uia.application;

import uia.physical.component.WrapperView;
import uia.core.ui.style.StyleFunction;
import uia.core.ui.callbacks.OnClick;
import uia.core.basement.Callback;
import uia.physical.theme.Theme;
import uia.core.ui.View;

import java.util.Objects;

/**
 * Standard UIa component.
 * <br>
 * Button has been developed as starting point for more complex widgets.
 * Its only behaviour is to switch on or off. By default, when clicked, it switches.
 */

public final class UIButton extends WrapperView {
    /**
     * Button states.
     */
    public enum State {ON, OFF}

    private final StyleFunction[] stateStyle;
    private boolean enabled = false;

    public UIButton(View view) {
        super(view);
        registerCallback((OnClick) touches -> enable(!isEnabled()));

        stateStyle = new StyleFunction[]{
                style -> style.setBackgroundColor(Theme.RED),
                style -> style.setBackgroundColor(Theme.LIME),
        };

        enable(false);
    }

    /**
     * Callback invoked when Button is turned on.
     * <br>
     * It provides the Button.
     */
    public interface OnEnabled extends Callback<View> {
    }

    /**
     * Callback invoked when Button is turned off.
     * <br>
     * It provides the Button.
     */
    public interface OnDisabled extends Callback<View> {
    }

    /**
     * Sets a styleFunction to use when this button is on the specified state.
     *
     * @param state         the state of the button when the style function is applied
     * @param styleFunction the style function
     * @throws NullPointerException if {@code state == null || styleFunction == null}
     */

    public void setStateStyleFunction(State state, StyleFunction styleFunction) {
        Objects.requireNonNull(styleFunction);
        Objects.requireNonNull(state);

        int index = state.equals(State.OFF) ? 0 : 1;
        stateStyle[index] = styleFunction;

        updateStyle();
    }

    /**
     * Enables or disables this button.
     *
     * @param enabled true to enable this button
     */

    public void enable(boolean enabled) {
        boolean oldEnabledValue = this.enabled;

        // updates button state
        this.enabled = enabled;
        updateStyle();

        if (oldEnabledValue != enabled) {
            if (enabled) {
                notifyCallbacks(OnEnabled.class, this);
            } else {
                notifyCallbacks(OnDisabled.class, this);
            }
        }
    }

    /**
     * Helper function. Updates the button style.
     */

    private void updateStyle() {
        StyleFunction styleByState = enabled ? stateStyle[1] : stateStyle[0];
        getStyle().applyStyleFunction(styleByState);
    }

    /**
     * @return true if this button is enabled
     */

    public boolean isEnabled() {
        return enabled;
    }
}
