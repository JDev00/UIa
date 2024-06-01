package uia.application;

import uia.physical.component.WrapperView;
import uia.core.ui.callbacks.OnClick;
import uia.core.basement.Callback;
import uia.physical.theme.Theme;
import uia.core.paint.Paint;
import uia.core.ui.View;

/**
 * Standard UIa component.
 * <br>
 * Button has been developed as starting point for more complex widgets.
 * Its only behaviour is to switch on or off. By default, when clicked, it switches.
 *
 * @apiNote The color of a Button is defined by its state. As a result, any change to the object retrieved with
 * {@link #getPaint()} will have no effect. To change the Button color (or stroke) you must modify the Paint
 * associated with the state with {@link #getPaint(STATE)}.
 */

public final class UIButton extends WrapperView {
    /**
     * Button states.
     */
    public enum STATE {ON, OFF}

    private final Paint[] paintState;
    private boolean enabled = false;

    public UIButton(View view) {
        super(view);
        registerCallback((OnClick) touches -> enable(!isEnabled()));

        paintState = new Paint[]{
                new Paint().setColor(Theme.RED),
                new Paint().setColor(Theme.LIME)
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
     * Enables or disables this button.
     *
     * @param enabled true to enable this button
     */

    public void enable(boolean enabled) {
        boolean oldEnabledValue = this.enabled;

        // updates button state
        this.enabled = enabled;
        applyPaintByState();

        if (oldEnabledValue != enabled) {
            if (enabled) {
                notifyCallbacks(OnEnabled.class, this);
            } else {
                notifyCallbacks(OnDisabled.class, this);
            }
        }
    }

    /**
     * @return true if this button is enabled
     */

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Helper function. Applies the appropriate Paint according to the button state.
     */

    private void applyPaintByState() {
        Paint paintByState = enabled ? paintState[1] : paintState[0];
        if (!getPaint().equals(paintByState)) {
            getPaint().set(paintByState);
        }
    }

    @Override
    public void update(View parent) {
        super.update(parent);
        applyPaintByState();
    }

    /**
     * @param state the button state, see {@link STATE}
     * @return the {@link Paint} associated to the button state
     */

    public Paint getPaint(STATE state) {
        return STATE.ON.equals(state) ? paintState[1] : paintState[0];
    }
}
