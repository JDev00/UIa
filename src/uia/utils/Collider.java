package uia.utils;

import static java.lang.Math.abs;

/**
 * Collider contains useful methods to check for collisions
 */

public class Collider {

    /**
     * Check if two rectangles intersect
     *
     * @param x1      the position along x-axis of the first rectangle
     * @param y1      the position along y-axis of the first rectangle
     * @param width1  the dimension along x-axis of the first rectangle
     * @param height1 the dimension along y-axis of the first rectangle
     * @param x2      the position along x-axis of the second rectangle
     * @param y2      the position along y-axis of the second rectangle
     * @param width2  the dimension along x-axis of the second rectangle
     * @param height2 the dimension along y-axis of the second rectangle
     * @return true if the given rectangles intersect
     */

    public static boolean AABB(float x1, float y1, float width1, float height1,
                               float x2, float y2, float width2, float height2) {
        return abs(x1 - x2) <= (width1 + width2) / 2f && abs(y1 - y2) <= (height1 + height2) / 2f;
    }
}
