package uia.core.gesture;

/**
 * ADT to handle a mono-dimensional scroller
 */

public interface Scroller {

    /**
     * Set this scroller to be vertical
     */

    void setVertical(boolean vertical);

    /**
     * Set the multiplier factor for this scroller
     *
     * @param factor a value {@code > 0} that indicates the speed of the scrolling animation
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

    void setOffset(float value);

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
     */

    void update();

    /**
     * Update this scroller while mouse is wheeling
     */

    void wheelUpdate(int val);

    /**
     * @return the value of this scroller
     */

    float getOffset();

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

    /**
     * @return true if this scroller is used in vertical mode
     */

    boolean isVertical();
}
