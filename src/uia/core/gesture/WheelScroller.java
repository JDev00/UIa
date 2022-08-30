package uia.core.gesture;

import uia.utils.Utils;

public class WheelScroller implements Scroller {
    private float length;
    private float scroll;
    private float factor;

    private boolean vertical;
    private boolean pause = false;

    public WheelScroller(boolean vertical) {
        factor = 0.05f;
        this.vertical = vertical;
    }

    @Override
    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    @Override
    public void setFactor(float factor) {
        this.factor = Math.max(factor, 0);
    }

    @Override
    public void setMax(float value) {
        length = Math.max(0, value);
    }

    @Override
    public void setOffset(float value) {
        scroll = Math.max(0, value);
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
    public void update() {

    }

    @Override
    public void wheelUpdate(int val) {
        if (!pause) {
            float c = factor * length;

            if (val > 0)
                scroll += c;

            if (val < 0)
                scroll -= c;

            scroll = Utils.constrain(scroll, 0, length);
        }
    }

    @Override
    public float getOffset() {
        return scroll;
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

    @Override
    public boolean isVertical() {
        return vertical;
    }
}
