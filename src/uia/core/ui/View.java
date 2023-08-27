package uia.core.ui;

import uia.core.basement.Event;
import uia.core.basement.Graphic;
import uia.core.Geom;
import uia.core.Key;
import uia.core.Paint;
import uia.core.Pointer;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * View ADT.
 * <br>
 * A View is a special area displayed on screen with the user can interact with. Shortly, it is the basement for every
 * graphical widget built with UIa. From the architectural point of view, it is made up of three components:
 * <ul>
 *     <li>a {@link Geom} used to define its geometry;</li>
 *     <li>a {@link Paint} used to define the geometry's graphical aspects;</li>
 *     <li>a set of {@link Event}s used to interact with it when an input/action is detected.</li>
 * </ul>
 * <b>Note that a View is identified throw an ID assigned at declaration time.</b>
 */

public interface View {

    /**
     * Add a new Event to this View.
     *
     * @param event a new (not duplicated) {@link Event}
     */

    void addEvent(Event<?> event);

    /**
     * Remove the given Event from this View.
     *
     * @param event the {@link Event} to remove
     */

    void removeEvent(Event<?> event);

    /**
     * Update the specified event.
     *
     * @param type the {@link Event} type
     * @param data a not null data to pass to the called event
     */

    void updateEvent(Class<? extends Event> type, Object data);

    /**
     * Build the View's geometry
     *
     * @param builder        a not null {@link BiConsumer} used to manipulate geometry
     * @param inTimeBuilding true to build geometry at rendering time (for every frame)
     */

    void buildGeom(BiConsumer<View, Geom> builder, boolean inTimeBuilding);

    /**
     * @return the {@link Geom} object
     */

    Geom getGeom();

    /**
     * @return the {@link Paint} object
     */

    Paint getPaint();

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

    void setFocus(boolean request);

    /**
     * @return true if this View is on focus
     */

    boolean isFocused();

    enum CONSUMER {POINTER, KEY}

    /**
     * Consumer functionality consumes pointers or keys that are managed by this View.
     *
     * @param consumer       the consumer type: {@link CONSUMER#POINTER} or {@link CONSUMER#KEY}.
     * @param enableConsumer true to consume pointers/keys managed by this View; false to disable the specified consumer.
     */

    void setConsumer(CONSUMER consumer, boolean enableConsumer);

    /**
     * Set collider detection policy. By default, spatial partitioning collider is adopted.
     *
     * @param aabb true to use aligned axis bounding box policy; false the use the default one.
     */

    void setColliderPolicy(boolean aabb);

    /**
     * Set the View's position.
     *
     * @param x the new position along x-axis
     * @param y the new position along y-axis
     */

    void setPosition(float x, float y);

    /**
     * Set the View's dimension
     *
     * @param x the width {@code >= 0}
     * @param y the height {@code >= 0}
     */

    void setDimension(float x, float y);

    /**
     * Set the View's rotation.
     *
     * @param radians the amount of radians used to rotate this Element
     */

    void setRotation(float radians);

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
     * Dispatch a message to this View.
     *
     * @param message  the message to delivery to
     * @param sourceID the sender's id
     * @param destID   the destination's id
     */

    void dispatch(Object message, String sourceID, String destID);

    /**
     * Send a key to this View.
     *
     * @param key a not null {@link Key}
     */

    void dispatch(Key key);

    /**
     * Send a list of pointers to this View.
     *
     * @param pointers a not null {@link List} of Pointers
     */

    void dispatch(List<Pointer> pointers);

    /**
     * Update this View according to its parent.
     *
     * @param parent the not null parent of this View
     */

    void update(View parent);

    /**
     * Draw this View on the given Graphic
     *
     * @param graphic a not null {@link Graphic}
     */

    void draw(Graphic graphic);

    /**
     * Return the View's boundaries as a rectangle.
     * <b>Note that View's boundaries change as a consequence of a rotation.</b>
     *
     * @return the View's boundaries as an array made up of five elements:
     * <ul>
     *     <li>the View's top left corner along x-axis;</li>
     *     <li>the View's top left corner along y-axis;</li>
     *     <li>the View's rectangle width;</li>
     *     <li>the View's rectangle height;</li>
     *     <li>the View's rotation in radians: sum of current rotation + parent rotation</li>
     * </ul>
     */

    float[] bounds();

    /**
     * @return the View's width and height, with no rotation applied, and the geometry rotation
     */

    float[] desc();

    /**
     * Check if the given point is inside the View's boundaries.
     *
     * @param x the point's position along x-axis
     * @param y the point's position along y-axis
     * @return true if the given point is inside the View's area
     */

    boolean contains(float x, float y);

    /**
     * @return the View's ID
     */

    String getID();
}
