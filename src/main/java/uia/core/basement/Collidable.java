package uia.core.basement;

import uia.utility.MathUtility;

import static java.lang.Math.abs;

/**
 * Collidable allows for collision detection with another object.
 */

public interface Collidable {

    /**
     * ColliderPolicy defines some common-supported collisions detection mechanisms between two objects.
     * <br>
     * Supported mechanisms:
     * <ul>
     *     <li>AABB (Axis-Aligned Bounding Boxes) is the simplest form of collision detection;</li>
     *     <li>CIRCLE (Circle collision) uses a circular collider to detect collisions;</li>
     *     <li>SAT (Separating Axis Theorem) is a technique to accurately detect collisions.</li>
     * </ul>
     */

    enum ColliderPolicy {AABB, CIRCLE, SAT}

    /**
     * Sets the collision detection policy.
     *
     * @param colliderPolicy a not null {@link ColliderPolicy}
     * @throws NullPointerException if {@code colliderPolicy == null}
     */

    void setColliderPolicy(ColliderPolicy colliderPolicy);

    /**
     * Checks if the specified point is inside this collider.
     * <br>
     * The algorithm, used to check for a collision, is selected according to the specified collision
     * policy (set with {@link #setColliderPolicy(ColliderPolicy)}).
     *
     * @param x the point position on the x-axis
     * @param y the point position on the y-axis
     * @return true if the specified point is inside this collider
     */

    boolean contains(float x, float y);

    /**
     * Returns the collider boundaries as a rectangle.
     * <br>
     * <b>The top left corner coordinates are based on the window viewport.</b>
     *
     * @return the boundaries as an array made up of five elements:
     * <ul>
     *     <li>the top left corner on the x-axis;</li>
     *     <li>the top left corner on the y-axis;</li>
     *     <li>the rectangle width;</li>
     *     <li>the rectangle height;</li>
     *     <li>the rotation in radians.</li>
     * </ul>
     */

    float[] getBounds();

    /**
     * Checks if two rectangles intersect.
     * <br>
     * Time required: O(1)
     * <br>
     * Space required: O(1)
     *
     * @param x1      the center of the first rectangle on the x-axis
     * @param y1      the center of the first rectangle on the y-axis
     * @param width1  the dimension on the x-axis of the first rectangle
     * @param height1 the dimension on the y-axis of the first rectangle
     * @param x2      the center of the second rectangle on the x-axis
     * @param y2      the center of the second rectangle on the y-axis
     * @param width2  the dimension on the x-axis of the second rectangle
     * @param height2 the dimension on the y-axis of the second rectangle
     * @return true if the rectangles intersect
     */

    static boolean intersects(float x1, float y1, float width1, float height1,
                              float x2, float y2, float width2, float height2) {
        return abs(x1 - x2) <= (width1 + width2) / 2f && abs(y1 - y2) <= (height1 + height2) / 2f;
    }

    /**
     * Returns the collider width of the rotated rectangle.
     * <br>
     * Time required: T(1)
     * <br>
     * Space required: O(1)
     *
     * @param width    the rectangle width
     * @param height   the rectangle height
     * @param rotation the rectangle rotation in radians
     * @return the collider width of the rotated rectangle
     */

    static float colliderWidth(float width, float height, float rotation) {
        float cos = MathUtility.cos(rotation);
        float sin = MathUtility.sin(rotation);
        return abs(width * cos) + abs(height * sin);
    }

    /**
     * Returns the collider height of the rotated rectangle.
     * <br>
     * Time required: T(1)
     * <br>
     * Space required: O(1)
     *
     * @param width    the rectangle width
     * @param height   the rectangle height
     * @param rotation the rectangle rotation in radians
     * @return the collider height of the rotated rectangle
     */

    static float colliderHeight(float width, float height, float rotation) {
        float cos = MathUtility.cos(rotation);
        float sin = MathUtility.sin(rotation);
        return abs(width * sin) + abs(height * cos);
    }
}
