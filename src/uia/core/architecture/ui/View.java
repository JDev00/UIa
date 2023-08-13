package uia.core.architecture.ui;

import uia.core.architecture.Event;
import uia.core.architecture.Graphic;
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
 * graphical widget built with UIa. From the architectural point of view, it is made up of three essential components:
 * <ul>
 *     <li>a {@link Geom} used to define the its geometry;</li>
 *     <li>a {@link Paint} used to define the geometry graphical aspects;</li>
 *     <li>a set of {@link Event}s used to interact with it when an input/action is detected.</li>
 * </ul>
 * <b>Note that a View is uniquely identified throw a unique ID assigned at declaration time.</b>
 */

public interface View {

    /**
     * Add a new Event to this View.
     *
     * @param event a new (not duplicated) {@link Event}
     */

    void addEvent(Event<View, ?> event);

    /**
     * Remove the given Event from this View.
     *
     * @param event the {@link Event} to remove
     */

    void removeEvent(Event<View, ?> event);

    /**
     * Update the specified event.
     *
     * @param type the {@link Event} type
     * @param data a not null data to pass to the called event
     */

    void updateEvent(Class<? extends Event> type, Object data);

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

    /*
     * @param consumer the {@link CONSUMER} to check
     * @return true if this View has the specified consumer
     *

    boolean hasConsumer(CONSUMER consumer);*/

    /**
     * Set collider detection policy. By default, spatial partitioning collider is adopted.
     *
     * @param aabb true to use aligned axis bounding box policy; false the use the default one.
     */

    void setColliderPolicy(boolean aabb);

    /**
     * Build the View's geometry.
     *
     * @param builder        a not null function required to build the View's geometry
     * @param inTimeBuilding true to rebuild the View's geometry every time it is rendered
     */

    void setGeom(BiConsumer<View, Geom> builder, boolean inTimeBuilding);

    /**
     * @return the {@link Geom} object
     */

    Geom getGeom();

    /**
     * Set the View's Paint.
     *
     * @param paint a not null {@link Paint}
     */

    void setPaint(Paint paint);

    /**
     * @return the {@link Paint} object
     */

    Paint getPaint();

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
     * <b>Note that View's boundaries can change as consequence of a rotation.</b>
     *
     * @return the View's boundaries as an array made up of five elements:
     * <ul>
     *     <li>the View's left top corner along x-axis;</li>
     *     <li>the View's left top corner along y-axis;</li>
     *     <li>the View's width;</li>
     *     <li>the View's height;</li>
     *     <li>the View's rotation in radians.</li>
     * </ul>
     */

    float[] bounds();

    /**
     * Return the shape's boundaries as a rectangle.
     * <b>Note that shape's width and height don't change as consequence of a rotation.</b>
     *
     * @return the shape's boundaries as an array made up of five elements:
     * <ul>
     *     <li>the shape's left top corner along x-axis;</li>
     *     <li>the shape's left top corner along y-axis;</li>
     *     <li>the shape's width;</li>
     *     <li>the shape's height;</li>
     *     <li>the shape's rotation in radians.</li>
     * </ul>
     */

    float[] boundsShape();

    /**
     * Check if the given point is inside the View's boundaries.
     *
     * @param x the point's position along x-axis
     * @param y the point's position along y-axis
     * @return true if the given point is inside the View's area
     */

    boolean contains(float x, float y);

    /**
     * @return the View's unique ID
     */

    String getID();
}
