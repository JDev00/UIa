package uia.physical.ui.scroller;

import uia.core.ui.primitives.ScreenTouch;

/**
 * Mono-dimensional scroller ADT.
 */

public interface Scroller {

    /**
     * Resets this scroller to its initial value.
     */

    void reset();

    /**
     * Set the multiplier scrolling factor.
     * By default, one unit is added to this scroller to move it up or down.
     * With this functionality, it is possible to increase the velocity of this scroller by multiplying the default scroller value
     * by the given factor.
     *
     * @param factor a float
     */

    void setFactor(float factor);

    /**
     * Sets the maximum value for this scroller.
     *
     * @param length the maximum distance to travel
     */

    void setMax(float length);

    /**
     * Sets the scroll value.
     *
     * @param value the scroll value
     */

    void setValue(float value);

    /**
     * Pauses this scroller.
     */

    void pause();

    /**
     * Resumes this scroller.
     */

    void resume();

    /**
     * Updates this scroller.
     *
     * @param screenTouches the screenTouches used to update this scroller
     * @return true if this scroller has been updated
     * @throws NullPointerException if {@code screenTouches == null}
     */

    boolean update(ScreenTouch... screenTouches);

    /**
     * @return the scroller value
     */

    float getValue();

    /**
     * @return the multiplier scrolling factor
     */

    float getFactor();

    /**
     * @return the maximum scroller value
     */

    float getMax();

    /**
     * @return true if this scroller is paused
     */

    boolean isPaused();

    /**
     * @return true if this scroller is scrolling
     */

    boolean isScrolling();
}
