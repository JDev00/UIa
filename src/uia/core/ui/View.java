package uia.core.ui;

import uia.core.Geometry;
import uia.core.Paint;
import uia.core.basement.Callback;
import uia.core.basement.Callable;
import uia.core.basement.Collider;
import uia.core.basement.Movable;
import uia.utility.Figure;

import java.util.function.Consumer;

import static uia.utility.TrigTable.*;
import static uia.utility.TrigTable.sin;

/**
 * View ADT.
 * <br>
 * A View is a special area on the screen with the user can interact with. Shortly, it is the basement for
 * every graphical widget built with UIa. From the architectural point of view, it is made up of three components:
 * <ul>
 * <li>a {@link Geometry} used to define the graphical appearance;</li>
 * <li>a {@link Paint} used to define the geometry's graphical settings;</li>
 * <li>{@link Callback}s used to execute something when an event is detected.</li>
 * </ul>
 * <b>A View is identified by an ID assigned at declaration time.</b>
 */

public interface View extends Callable, Movable, Collider {

    enum CONSUMER {POINTER, KEY}

    enum DISPATCHER {KEY, POINTERS, MESSAGE, OTHER}

    /**
     * Build the View's geometry
     *
     * @param builder        a not null {@link Consumer} used to manipulate geometry
     * @param inTimeBuilding true to build geometry at rendering time (for every frame)
     */

    void buildGeometry(Consumer<Geometry> builder, boolean inTimeBuilding);

    /**
     * @return the {@link Paint} object
     */

    Paint getPaint();

    /**
     * @return the View's {@link Geometry}
     */

    Geometry getGeometry();

    /**
     * Make this View visible or not visible.
     * An invisible View can't interact with user.
     *
     * @param visible true to set this View visible
     */

    void setVisible(boolean visible);

    /**
     * @return true if this View is visible
     */

    boolean isVisible();

    /**
     * Request or remove the View's focus.
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
     * Send a message to another View.
     * <br>
     * If a message is sent to a group, then it will be also delivered to all its children.
     *
     * @param message the message to be sent
     * @param destID  the recipient's id; if it is null, the message will be sent to all Views (broadcast)
     */

    void sendMessage(Object message, String destID);

    /**
     * Consumer functionality consumes pointers or keys that are dispatched to this View.
     *
     * @param consumer       the consumer type: {@link CONSUMER#POINTER} or {@link CONSUMER#KEY}.
     * @param enableConsumer true to consume pointers/keys managed by this View; false to disable the specified consumer.
     */

    void setConsumer(CONSUMER consumer, boolean enableConsumer);

    /**
     * Dispatch an object to this View.
     * <br>
     * The dispatched object could be: screen pointers, keyboard's key, a message and so on.
     *
     * @param data a not null Object
     */

    void dispatch(DISPATCHER dispatcher, Object data);

    /**
     * Update the View's state.
     *
     * @param parent the not null parent of this View
     */

    void update(View parent);

    /**
     * Draw this View on a Graphic
     *
     * @param graphic a not null {@link Graphic}
     */

    void draw(Graphic graphic);

    /**
     * Returns the View's width. Note that width won't change as consequence of a rotation.
     *
     * @return the View's width
     */

    float getWidth();

    /**
     * Returns the View's height. Note that height won't change as consequence of a rotation.
     *
     * @return the View's height
     */

    float getHeight();

    /**
     * @return the View's ID
     */

    String getID();

    /**
     * Calculate the component position on the x-axis
     *
     * @param xLeft   the container position on x-axis
     * @param width   the container width
     * @param xDist   the component distance from the container center on the x-axis
     * @param yDist   the component distance from the container center on the y-axis
     * @param radians the component rotation in radians
     * @return the component position on x-axis
     */

    static float getPositionOnX(float xLeft, float width,
                                float xDist, float yDist, float radians) {
        return xLeft + 0.5f * width + rotX(xDist, yDist, cos(radians), sin(radians));
    }

    /**
     * Calculate the component position on the y-axis
     *
     * @param yTop    the container position on y-axis
     * @param height  the container height
     * @param xDist   the component distance from the container center on the x-axis
     * @param yDist   the component distance from the container center on the y-axis
     * @param radians the component rotation in radians
     * @return the component position on x-axis
     */

    static float getPositionOnY(float yTop, float height,
                                float xDist, float yDist, float radians) {
        return yTop + 0.5f * height + rotY(xDist, yDist, cos(radians), sin(radians));
    }

    /**
     * Build a rounded rectangle
     */

    static void buildRect(Geometry geometry, float width, float height, float radius) {
        Figure.rect(geometry, Figure.STD_VERT, radius, width / height);
    }
}
