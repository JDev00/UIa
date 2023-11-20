package uia.core.basement;

import static java.lang.Math.abs;

/**
 * Collider can be seen as an invisible cloth that wrap an object. It allows for collision detection between two objects.
 */

public interface Collider {

    /**
     * ColliderPolicy defines some common-supported collisions detection mechanisms between two objects.
     * <br>
     * Some notes:
     * <ul>
     *     <li>AABB (Axis-Aligned Bounding Boxes) is the simplest form of collision detection;</li>
     *     <li>CIRCLE (Circle collision) uses a circular collider to detect collisions;</li>
     *     <li>SAT (Separating Axis Theorem) is a more powerful technique to accurately detect collisions.</li>
     * </ul>
     */

    enum ColliderPolicy {AABB, CIRCLE, SAT}

    /**
     * Set collisions detection mechanism.
     *
     * @param colliderPolicy a not null {@link ColliderPolicy} instance
     * @throws NullPointerException if {@code colliderPolicy == null}
     */

    void setColliderPolicy(ColliderPolicy colliderPolicy);

    /**
     * Return the collider boundaries as a rectangle.
     * The top left corner is based on the entire window viewport.
     *
     * @return the boundaries as an array made up of five elements:
     * <ul>
     *     <li>the top left corner on the x-axis;</li>
     *     <li>the top left corner on the y-axis;</li>
     *     <li>the rectangle width;</li>
     *     <li>the rectangle height;</li>
     *     <li>the rotation in radians</li>
     * </ul>
     */

    float[] bounds();

    /**
     * Check if the given point is inside this collider.
     * The algorithm, used to check for a collision, is selected according to the specified collision
     * policy (set with {@link #setColliderPolicy(ColliderPolicy)}).
     *
     * @param x the point's position along x-axis
     * @param y the point's position along y-axis
     * @return true if the given point is inside this collider
     */

    boolean contains(float x, float y);

    /**
     * Check if two rectangles intersect.
     * <br>
     * Time required: O(1)
     * <br>
     * Space required: O(1)
     *
     * @param x1      the center of the first rectangle along x-axis
     * @param y1      the center of the first rectangle along y-axis
     * @param width1  the dimension along x-axis of the first rectangle
     * @param height1 the dimension along y-axis of the first rectangle
     * @param x2      the center of the second rectangle along x-axis
     * @param y2      the center of the second rectangle along y-axis
     * @param width2  the dimension along x-axis of the second rectangle
     * @param height2 the dimension along y-axis of the second rectangle
     * @return true if the given rectangles intersect
     */

    static boolean intersects(float x1, float y1, float width1, float height1,
                              float x2, float y2, float width2, float height2) {
        return abs(x1 - x2) <= (width1 + width2) / 2f && abs(y1 - y2) <= (height1 + height2) / 2f;
    }
}
