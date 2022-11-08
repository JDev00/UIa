package uia.core.gesture;

import uia.core.View;

/**
 * Scroller ADT
 */

public interface Scroller {

    /**
     * Reset this scroller but ,if paused, keeps it paused
     */

    Scroller reset();

    /**
     * Set the multiplier scrolling factor
     *
     * @param factor a Float
     * @return this scroller
     */

    Scroller setFactor(float factor);

    /**
     * Set the maximum value for this scroller
     *
     * @param length the maximum distance to travel
     * @return this scroller
     */

    Scroller setMax(float length);

    /**
     * Set the scroll value
     *
     * @param value the scroll value
     * @return this scroller
     */

    Scroller setValue(float value);

    /**
     * Enable or disable the auto-adjustment functionality.
     * <br>
     * Auto-adjustment is used to keep the scrolling percentage when a new maximum is set with {{@link #setMax(float)}}.
     *
     * @param autoAdjustment true to auto-adjust this scroller
     */

    Scroller setAutoAdjustment(boolean autoAdjustment);

    /**
     * Pause this scroller
     *
     * @return this scroller
     */

    Scroller pause();

    /**
     * Resume this scroller
     *
     * @return this scroller
     */

    Scroller resume();

    /**
     * Update this scroller
     *
     * @param pointerEvent a not null {@link uia.core.View.PointerEvent}
     * @return true if this scroller has been updated
     */

    boolean update(View.PointerEvent pointerEvent);

    /**
     * @return the value of this scroller
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
     * @return true if auto-adjustment functionality is enabled
     */

    boolean hasAutoAdjustment();

    /**
     * @return true if this scroller is paused
     */

    boolean isPaused();

    /**
     * @return true if this scroller is scrolling
     */

    boolean isScrolling();
}
