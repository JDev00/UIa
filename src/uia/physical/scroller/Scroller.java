package uia.physical.scroller;

import uia.core.ScreenPointer;

import java.util.List;

/**
 * Mono-dimensional scroller ADT.
 */

public interface Scroller {

    /**
     * Reset this scroller to its initial value.
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
     * Set the maximum value for this scroller
     *
     * @param length the maximum distance to travel
     */

    void setMax(float length);

    /**
     * Set the scroll value
     *
     * @param value the scroll value
     */

    void setValue(float value);

    /**
     * Pause this scroller
     */

    void pause();

    /**
     * Resume this scroller
     */

    void resume();

    /**
     * Update this scroller
     *
     * @param screenPointer a not null List of {@link ScreenPointer}s
     * @return true if this scroller has been updated
     */

    boolean update(List<ScreenPointer> screenPointer);

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
