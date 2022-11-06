package uia.core.gesture;

import uia.core.View;
import uia.utils.Utils;

public class WheelScroller implements Scroller {
    private float length;
    private float value;
    private float factor = 1f;

    private boolean pause = false;
    private boolean autoAdjustment = false;

    @Override
    public Scroller reset() {
        length = 0f;
        value = 0f;
        factor = 1f;
        return this;
    }

    @Override
    public Scroller setFactor(float factor) {
        this.factor = factor;
        return this;
    }

    @Override
    public Scroller setMax(float value) {
        float v = Math.max(0, value);
        if (autoAdjustment && length > 0 && length != v) {
            float p = this.value / length;
            this.value = v * p;
        }
        length = v;
        this.value = Math.min(this.value, length);
        return this;
    }

    @Override
    public Scroller setValue(float value) {
        this.value = Math.max(0, value);
        return this;
    }

    @Override
    public Scroller setAutoAdjustment(boolean autoAdjustment) {
        this.autoAdjustment = autoAdjustment;
        return null;
    }

    @Override
    public Scroller pause() {
        pause = true;
        return this;
    }

    @Override
    public Scroller resume() {
        pause = false;
        return this;
    }

    @Override
    public boolean update(View.PointerEvent pointerEvent) {
        if (!pause && pointerEvent.isMouseWheeling()) {
            value = Utils.constrain(value + factor * pointerEvent.getWheelRotation(), 0, length);
            return true;
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
    public boolean hasAutoAdjustment() {
        return autoAdjustment;
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
