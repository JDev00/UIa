package uia.application;

import uia.core.architecture.ui.View;
import uia.core.Paint;
import uia.core.architecture.ui.event.EventClick;
import uia.physical.ui.wrapper.WrapperView;

/**
 * UI button.
 * <br>
 * This Button has been developed as starting point for more complex widgets.
 * Its only behaviour is to change color when it is enabled.
 * <br>
 * By default, when it is clicked it changes its state.
 */

public class ButtonUI extends WrapperView {
    private Paint prevPaint;
    private Paint paintEnabled;

    private boolean enabled = false;

    /**
     * Lock or unlock the button
     */
    public boolean locked = false;

    public ButtonUI(View view) {
        super(view);

        addEvent((EventClick) (v, pointers) -> {
            if (!locked) enable(!enabled);
        });

        paintEnabled = new Paint().setColor(0, 255, 0);
    }

    /**
     * Enable or disable this button
     *
     * @param enabled true to enable it
     */

    public void enable(boolean enabled) {
        this.enabled = enabled;

        if (paintEnabled != null)
            setPaint(enabled ? paintEnabled : prevPaint);
    }

    /**
     * Set the Paint to use when the button is enabled
     *
     * @param paint a {@link Paint}; it could be null
     */

    public void setPaintEnabled(Paint paint) {
        paintEnabled = paint;
    }

    @Override
    public void update(View container) {
        super.update(container);

        if (!getPaint().equals(paintEnabled)) prevPaint = getPaint();
    }

    /**
     * @return true if this button is locked
     */

    public boolean isLocked() {
        return locked;
    }

    /**
     * @return true if this button is enabled
     */

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @return the {@link Paint} used when the button is enabled; it could be null
     */

    public Paint getPaintEnabled() {
        return paintEnabled;
    }
}
