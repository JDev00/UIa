package uia.core.basement;

/**
 * Movable defines the primitives to allow an object to move.
 */

public interface Movable {

    /**
     * Set the object position
     *
     * @param x the position along x-axis
     * @param y the position along y-axis
     */

    void setPosition(float x, float y);

    /**
     * Set the object dimension
     *
     * @param width  the object's width
     * @param height the object's height
     */

    void setDimension(float width, float height);

    /**
     * Set the object rotation
     *
     * @param radians the rotation expressed in radians
     */

    void setRotation(float radians);
}
