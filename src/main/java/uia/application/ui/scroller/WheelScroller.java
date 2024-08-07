package uia.application.ui.scroller;

import uia.core.ui.primitives.ScreenTouch;
import uia.utility.MathUtility;

/**
 * {@link Scroller} implementation.
 */

public class WheelScroller implements Scroller {
    private float factor = 1f;
    private float length;
    private float value;

    private boolean pause = false;

    @Override
    public void reset() {
        length = 0f;
        value = 0f;
        factor = 1f;
    }

    @Override
    public void setFactor(float factor) {
        this.factor = factor;
    }

    @Override
    public void setMax(float value) {
        length = Math.max(0, value);
        this.value = Math.min(this.value, length);
    }

    @Override
    public void setValue(float value) {
        this.value = Math.max(0, value);
    }

    @Override
    public void pause() {
        pause = true;
    }

    @Override
    public void resume() {
        pause = false;
    }

    @Override
    public boolean update(ScreenTouch... screenTouches) {
        try {
            ScreenTouch screenTouch = screenTouches[0];
            if (!pause && screenTouch.getWheelRotation() != 0) {
                value = MathUtility.constrain(value + factor * screenTouch.getWheelRotation(), 0, length);
                return true;
            }
        } catch (Exception ignored) {
            //
        }
        return false;
    }

    @Override
    public float getValue() {
        return value;
    }

    @Override
    public float getFactor() {
        return factor;
    }

    @Override
    public float getMax() {
        return length;
    }

    @Override
    public boolean isPaused() {
        return pause;
    }

    @Override
    public boolean isScrolling() {
        return false;
    }
}
