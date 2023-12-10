package uia.core.ui;

import uia.core.basement.*;

import static uia.utility.TrigTable.*;

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
     */

    void requestFocus(boolean request);

    /**
     * @return true if this View is on focus
     */

    boolean isOnFocus();

    /**
     * Makes this View visible or not visible.
     *
     * @param visible true to set this View visible
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
     * <br>
     * If a message is sent to a group, then it will be also delivered to all its children.
     *
     * @param message the message to be sent
     */

    void sendMessage(Message message);

    /**
     * Dispatches a Message to this View
     *
     * @param message a not null {@link Message}
     * @throws NullPointerException if {@code == null}
     */

    void dispatchMessage(Message message);

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
     * @param xLeft   the container position on x-axis
     * @param width   the container width
     * @param xDist   the View distance from the container center on the x-axis
     * @param yDist   the View distance from the container center on the y-axis
     * @param radians the View rotation in radians
     * @return the View position on x-axis
     */

    static float getPositionOnX(float xLeft, float width, float xDist, float yDist, float radians) {
        return xLeft + 0.5f * width + rotX(xDist, yDist, cos(radians), sin(radians));
    }

    /**
     * Calculates the View position on the y-axis
     *
     * @param yTop    the container position on y-axis
     * @param height  the container height
     * @param xDist   the View distance from the container center on the x-axis
     * @param yDist   the View distance from the container center on the y-axis
     * @param radians the View rotation in radians
     * @return the View position on x-axis
     */

    static float getPositionOnY(float yTop, float height, float xDist, float yDist, float radians) {
        return yTop + 0.5f * height + rotY(xDist, yDist, cos(radians), sin(radians));
    }
}
