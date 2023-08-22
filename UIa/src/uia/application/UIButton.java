package uia.application;

import uia.core.ui.View;
import uia.core.Paint;
import uia.core.event.OnClick;
import uia.application.theme.Theme;
import uia.physical.wrapper.WrapperView;

/**
 * Button has been developed as starting point for more complex widgets.
 * Its only behaviour is to change color when its state is modified. By default, when it is clicked it changes its state.
 */

public class UIButton extends WrapperView {
    private final Paint[] paintState;

    public UIButton(View view) {
        super(view);

        addEvent((OnClick) (v, pointers) -> enable(!isEnabled()));

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
