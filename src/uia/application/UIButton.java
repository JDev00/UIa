package uia.application;

import uia.core.basement.Graphic;
import uia.core.ui.View;
import uia.core.Paint;
import uia.core.ui.event.OnClick;
import uia.physical.theme.Theme;
import uia.physical.wrapper.WrapperView;

/**
 * Button has been developed as starting point for more complex widgets.
 * Its only behaviour is to change color when its state is modified.
 * By default, when it is clicked it changes its state.
 * <br>
 * Note that button's color is defined by its state! So any change to the {@link #getPaint()} object will result in nothing.
 * To modify the Button's Paint use: {@link #getPaintEnabled()} and {@link #getPaintNotEnabled()}.
 */

public class UIButton extends WrapperView {
    private final Paint[] paintState;

    public UIButton(View view) {
        super(view);

        addEvent((OnClick) pointers -> enable(!isEnabled()));

        paintState = new Paint[]{
                new Paint().setColor(Theme.RED),
                new Paint().setColor(Theme.GREEN)
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

    /**
     * @return the {@link Paint} used when the button is not enabled
     */

    public Paint getPaintNotEnabled() {
        return paintState[0];
    }

    /**
     * @return the {@link Paint} used when the button is enabled
     */

    public Paint getPaintEnabled() {
        return paintState[1];
    }
}
