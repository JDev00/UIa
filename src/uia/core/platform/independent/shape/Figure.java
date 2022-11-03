package uia.core.platform.independent.shape;

import uia.utils.Utils;

import static uia.utils.TrigTable.*;

/**
 * Figure class is used to easily build a standard shape
 */

public class Figure {
    /**
     * Standard number of vertices per Shape
     */
    public static final int STD_VERT = 25;

    /**
     * Standard corner radius
     */
    public static final float STD_ROUND = 0.05f;

    /**
     * Check the given Shape validity
     *
     * @param shape     a {@link Shape}
     * @param iCapacity the Shape initial capacity
     */

    private static Shape checkValidity(Shape shape, int iCapacity) {
        return shape == null ? new Shape(iCapacity) : shape;
    }

    /**
     * Build a rectangle
     *
     * @param shape a {@link Shape} to fill
     */

    public static Shape rect(Shape shape) {
        shape = checkValidity(shape, 4);
        shape.clear();
        shape.addVertices(
                -1, -1,
                1, -1,
                1, 1,
                -1, 1);
        return shape;
    }

    /**
     * Build a rectangle with rounded corners.
     * <br>
     * Note that this method must be called at runtime because of corner rounding calculation.
     *
     * @param shape    a {@link Shape} to fill
     * @param vertices the number of vertices
     * @param a        the radius between [0,1] used to round the upper-left corner
     * @param b        the radius between [0,1] used to round the upper-right corner
     * @param c        the radius between [0,1] used to round the lower-right corner
     * @param d        the radius between [0,1] used to round the lower-left corner
     */

    public static Shape rect(Shape shape, int vertices, float a, float b, float c, float d) {
        shape = checkValidity(shape, vertices + 1);
        shape.clear();

        vertices = vertices / 4;

        float div = shape.width() / shape.height();
        float f1 = 1, f2 = 1;

        if (div >= 1) {
            f1 = 1f / div;
        } else {
            f2 = div;
        }

        float ax = a * f1, ay = a * f2;
        float bx = b * f1, by = b * f2;
        float cx = c * f1, cy = c * f2;
        float dx = d * f1, dy = d * f2;

        float an;

        for (int i = 0; i <= vertices; i++) {
            an = PI - HALF_PI * i / vertices;
            shape.addVertex(-1 + ax + ax * cos(an), -1 + ay * (1 - sin(an)));
        }

        for (int i = 0; i <= vertices; i++) {
            an = HALF_PI - HALF_PI * i / vertices;
            shape.addVertex(1 - bx + bx * cos(an), -1 + by - by * sin(an));
        }

        for (int i = 0; i <= vertices; i++) {
            an = -HALF_PI * i / vertices;
            shape.addVertex(1 - cx + cx * cos(an), 1 - cy - cy * sin(an));
        }

        for (int i = 0; i <= vertices; i++) {
            an = -HALF_PI - HALF_PI * i / vertices;
            shape.addVertex(-1 + dx + dx * cos(an), 1 - dy - dy * sin(an));
        }

        return shape;
    }

    /**
     * Build a rectangle with rounded corners
     *
     * @param shape    a {@link Shape} to fill
     * @param vertices the number of vertices
     * @param radius   the radius between [0,1] used to round each corner
     * @see #rect(Shape, int, float, float, float, float)
     */

    public static Shape rect(Shape shape, int vertices, float radius) {
        return rect(shape, vertices, radius, radius, radius, radius);
    }

    /**
     * Build a triangle
     *
     * @param shape a {@link Shape} to fill
     */

    public static Shape triangle(Shape shape) {
        shape = checkValidity(shape, 3);
        shape.clear();
        shape.addVertices(
                -1, -1,
                1, 0,
                -1, 1);

        return shape;
    }

    /*
     * Build a triangle shape
     *
     * @param shape a {@link Shape} to fill
     *

    public static Shape buildTriangle(Shape shape, int vertices,
                                      float a, float b, float c) {
        shape = checkValidity(shape, 3);
        shape.clear();

        //vertices = vertices / 3;

        float div = shape.getWidth() / shape.getHeight();
        float f1 = 1, f2 = 1;

        if (div >= 1) {
            f1 = 1f / div;
        } else {
            f2 = div;
        }

        float ax = a * f1, ay = a * f2;
        float bx = b * f1, by = b * f2;
        float cx = c * f1, cy = c * f2;

        float an;

        for (int i = 0; i <= vertices; i++) {
            an = PI - HALF_PI * i / vertices;
            shape.addVertex(-1 + ax + ax * pcos(an), -1 + ay * (1 - psin(an)));
        }

        for (int i = 0; i <= vertices; i++) {
            an = 0.45f * PI - 0.9f * PI * i / vertices;
            shape.addVertex(1 - bx + bx * pcos(an), -by * psin(an));
        }

        for (int i = 0; i <= vertices; i++) {
            an = -HALF_PI - HALF_PI * i / vertices;
            shape.addVertex(-1 + cx + cx * pcos(an), 1 - cy - cy * psin(an));
        }

        return shape;
    }*/

    /**
     * Build an oval
     *
     * @param shape    a {@link Shape} to fill
     * @param vertices the number of vertices used to create this figure
     */

    public static Shape buildOval(Shape shape, int vertices) {
        shape = checkValidity(shape, vertices + 1);
        shape.clear();

        float a;
        for (int i = 0; i <= vertices; i++) {
            a = TWO_PI * i / vertices;
            shape.addVertex(cos(a), sin(a));
        }

        return shape;
    }

    /**
     * Build a pause
     *
     * @param shape a {@link Shape} to fill
     */

    public static Shape buildPause(Shape shape) {
        shape = checkValidity(shape, 8);
        shape.clear();
        shape.addVertices(
                -1, -1,
                -0.33f, -1,
                -0.33f, 1,
                -1, 1,
                1, -1,
                0.33f, -1,
                0.33f, 1,
                1, 1);
        shape.setVertex(4, true);

        return shape;
    }

    /**
     * Build an arrow
     *
     * @param shape a {@link Shape} to fill
     */

    public static Shape buildArrow(Shape shape) {
        shape = checkValidity(shape, 4);
        shape.clear();
        shape.addVertices(
                -1, -1,
                1, 0,
                -1, 1,
                -2f / 3, 0);

        return shape;
    }

    /**
     * Build an arrow
     *
     * @param shape a {@link Shape} to fill
     */

    public static Shape buildArrow2(Shape shape) {
        shape = checkValidity(shape, 4);
        shape.clear();
        shape.addVertices(
                -0.15f, -1f,
                0.25f, -1f,
                1f, 0f,
                0.25f, 1f,
                -0.15f, 1f,
                0.5f, 0.15f,
                -1f, 0.15f,
                -1f, -0.15f,
                0.5f, -0.15f
        );

        return shape;
    }

    /**
     * Build a plus
     *
     * @param shape     a {@link Shape} to fill
     * @param thickness the shape thickness
     */

    public static Shape buildPlus(Shape shape, float thickness) {
        float th = Utils.constrain(Math.abs(thickness), 0, 0.75f);

        shape = checkValidity(shape, 12);
        shape.clear();
        shape.addVertices(
                -th, -1,
                th, -1,
                th, -th,
                1, -th,
                1, th,
                th, th,
                th, 1,
                -th, 1,
                -th, th,
                -1, th,
                -1, -th,
                -th, -th);

        return shape;
    }

    /**
     * Build a delete
     *
     * @param shape     a {@link Shape} to fill
     * @param thickness the shape thickness
     */

    public static Shape buildDelete(Shape shape, float thickness) {
        float th = Utils.constrain(Math.abs(thickness), 0, 0.98f);

        shape = checkValidity(shape, 12);
        shape.clear();
        shape.addVertices(
                -1 + th, -1,
                -1, -1,
                -th / 2f, 0,
                -1, 1,
                -1 + th, 1,
                0, th / 2f,
                1 - th, 1,
                1, 1,
                th / 2f, 0,
                1, -1,
                1 - th, -1,
                0, -th / 2f);

        return shape;
    }
}
