package uia.application;

import uia.core.ui.Graphic;
import uia.core.ui.View;
import uia.core.Paint;
import uia.core.ui.callbacks.OnClick;
import uia.physical.theme.Theme;
import uia.physical.WrapperView;

/**
 * Button has been developed as starting point for more complex widgets.
 * Its only behaviour is to change color when its state is modified.
 * By default, when it is clicked it changes its state.
 * <br>
 * Note that button's color is defined by its state! So any change to the {@link #getPaint()} object will result in nothing.
 * To modify the Button's Paint use: {@link #getPaint(STATE)}.
 */

public class UIButton extends WrapperView {
    private final Paint[] paintState;

    public UIButton(View view) {
        super(view);

        registerCallback((OnClick) touches -> enable(!isEnabled()));

        paintState = new Paint[]{
                new Paint().setColor(Theme.RED),
                new Paint().setColor(Theme.LIME)
        };
    }

    /**
     * Enable or disable this button
     *
     * @param enabled true to enable it
     */

    public void enable(boolean enabled) {
        getPaint().set(enabled ? paintState[1] : paintState[0]);
    }

    /**
     * @return true if this button is enabled
     */

    public boolean isEnabled() {
        return getPaint().equals(paintState[1]);
    }

    @Override
    public void draw(Graphic graphic) {
        enable(isEnabled());
        super.draw(graphic);
    }

    public enum STATE {ENABLED, DISABLED}

    /**
     * @param state the button's state, see {@link STATE}
     * @return the {@link Paint} associated to the button's state
     */

    public Paint getPaint(STATE state) {
        return STATE.ENABLED.equals(state) ? paintState[1] : paintState[0];
    }
}
