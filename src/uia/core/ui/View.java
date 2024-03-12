package uia.core.ui;

import uia.core.basement.*;

import static uia.utility.MathUtility.*;

/**
 * View ADT.
 * <br>
 * A View is a special area on the screen with the user can interact with. Shortly, it is the basement for
 * every graphical widget built with UIa. From the architectural point of view, it is made up of two components:
 * <ul>
 * <li>a {@link Drawable} used to define the graphical aspects: Geometry and Paint;</li>
 * <li>{@link Callback}s used to respond to an event when it is detected.</li>
 * </ul>
 * <b>A View is identified by an ID assigned at declaration time.</b>
 */

public interface View extends Callable, Drawable, Collider {

    enum Consumer {SCREEN_TOUCH, KEY}

    /**
     * Requests or removes the View focus.
     * <br>
     * A View is allowed to interact directly with user only when it is on focus.
     *
     * @param request true to request to this View to put on focus; false to remove focus
     * @implSpec focus can't be requested if a View is not visible
     */

    void requestFocus(boolean request);

    /**
     * @return true if this View is on focus
     */

    boolean isOnFocus();

    /**
     * Makes this View visible or not visible.
     * <br>
     * A View set to invisible also loses its focus.
     *
     * @param visible true to set this View visible
     * @implSpec when set to invisible (visible = false), a View must lose its focus.
     */

    void setVisible(boolean visible);

    /**
     * @return true if this View is visible
     */

    boolean isVisible();

    /**
     * Consumer functionality consumes screen touches or keys that are dispatched to this View.
     *
     * @param consumer       the consumer type: see {@link Consumer}.
     * @param enableConsumer true to consume screen touches/keys managed by this View; false to disable the specified consumer.
     */

    void setConsumer(Consumer consumer, boolean enableConsumer);

    /**
     * Sends a message to another View.
     *
     * @param message the message to be sent
     * @throws NullPointerException if {@code message == null}
     */

    void sendMessage(Message message);

    /**
     * Reads the specified message.
     *
     * @param message a message to read
     * @throws NullPointerException if {@code message == null}
     */

    void readMessage(Message message);

    /**
     * Updates this View state
     *
     * @param parent the not null parent of this View
     * @throws NullPointerException if {@code == null}
     */

    void update(View parent);

    /**
     * @return this View ID
     */

    String getID();

    /**
     * Calculates the View position on the x-axis
     *
     * @param xContainer     the left top container corner position on the x-axis
     * @param containerWidth the container width
     * @param xDist          the View distance from the container center on the x-axis
     * @param yDist          the View distance from the container center on the y-axis
     * @param radians        the View rotation in radians
     * @return the View position on the x-axis
     */

    static float getPositionOnX(float xContainer, float containerWidth,
                                float xDist, float yDist, float radians) {
        return xContainer + 0.5f * containerWidth + rotateX(xDist, yDist, radians);
    }

    /**
     * Calculates the View position on the y-axis
     *
     * @param yContainer      the left top container corner position on the y-axis
     * @param containerHeight the container height
     * @param xDist           the View distance from the container center on the x-axis
     * @param yDist           the View distance from the container center on the y-axis
     * @param radians         the View rotation in radians
     * @return the View position on the y-axis
     */

    static float getPositionOnY(float yContainer, float containerHeight,
                                float xDist, float yDist, float radians) {
        return yContainer + 0.5f * containerHeight + rotateY(xDist, yDist, radians);
    }
}
