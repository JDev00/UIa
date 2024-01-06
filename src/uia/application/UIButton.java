package uia.application;

import uia.core.basement.Callback;
import uia.core.ui.View;
import uia.core.Paint.Paint;
import uia.core.ui.callbacks.OnClick;
import uia.physical.theme.Theme;
import uia.physical.WrapperView;

/**
 * Standard UIa component.
 * <br>
 * Button has been developed as starting point for more complex widgets.
 * Its only behaviour is to switch on or off. By default, when clicked, it switches.
 *
 * @apiNote The color of a Button is defined by its state. As a result, any change to the object retrieved with
 * {@link #getPaint()} will have no effect. To change the Button color (or stroke) you must modify the Paint associated
 * with the state with {@link #getPaint(STATE)}.
 */

public final class UIButton extends WrapperView {
    /**
     * Button state representation
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
    public interface onEnabled extends Callback<View> {
    }

    /**
     * Callback invoked when Button is turned off.
     * <br>
     * It provides the Button.
     */
    public interface onDisabled extends Callback<View> {
    }

    /**
     * Enables or disables this button
     *
     * @param enabled true to enable this button
     */

    public void enable(boolean enabled) {
        if (this.enabled != enabled) {
            if (enabled) {
                notifyCallbacks(onEnabled.class, this);
            } else {
                notifyCallbacks(onDisabled.class, this);
            }
        }
        this.enabled = enabled;
        applyPaintByState();
    }

    /**
     * @return true if this button is enabled
     */

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Helper function. Applies the appropriate Paint according to the button state
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
