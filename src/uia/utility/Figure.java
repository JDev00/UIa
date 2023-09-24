package uia.utility;

import uia.core.Geometry;

import static uia.utility.TrigTable.*;

/**
 * Collection of figures used to easily build shapes
 */

public final class Figure {

    /**
     * Standard number of shape's vertices
     */
    public static final int STD_VERT = 25;

    /**
     * Standard corner radius for rounded shapes (ie: rounded rect)
     */
    public static final float STD_ROUND = 0.1f;

    /**
     * Build a rectangle
     *
     * @param geometry a not null {@link Geometry} to fill
     */

    public static Geometry rect(Geometry geometry) {
        geometry.clear();
        geometry.addVertices(
                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f, 0.5f,
                -0.5f, 0.5f);
        return geometry;
    }

    /**
     * Build a rectangle with rounded corners.
     * <br>
     * Note that this method must be called at runtime because of corner rounding calculation.
     *
     * @param geometry a not null {@link Geometry} to fill
     * @param vertices the number of vertices
     * @param a        the radius between [0,1] used to round the upper-left corner
     * @param b        the radius between [0,1] used to round the upper-right corner
     * @param c        the radius between [0,1] used to round the lower-right corner
     * @param d        the radius between [0,1] used to round the lower-left corner
     * @param ratio    the ratio between width and height
     */

    public static Geometry rect(Geometry geometry, int vertices,
                                float a, float b, float c, float d,
                                float ratio) {
        geometry.clear();

        vertices = vertices / 4;

        float f1 = 1, f2 = 1;

        if (ratio >= 1) {
            f1 = 1f / ratio;
        } else {
            f2 = ratio;
        }

        float ax = a * f1, ay = a * f2;
        float bx = b * f1, by = b * f2;
        float cx = c * f1, cy = c * f2;
        float dx = d * f1, dy = d * f2;
        float an;

        for (int i = 0; i <= vertices; i++) {
            an = PI - HALF_PI * i / vertices;
            geometry.addVertex((-1 + ax + ax * cos(an)) / 2f, (-1 + ay * (1 - sin(an))) / 2f);
        }

        for (int i = 0; i <= vertices; i++) {
            an = HALF_PI - HALF_PI * i / vertices;
            geometry.addVertex((1 - bx + bx * cos(an)) / 2f, (-1 + by - by * sin(an)) / 2f);
        }

        for (int i = 0; i <= vertices; i++) {
            an = -HALF_PI * i / vertices;
            geometry.addVertex((1 - cx + cx * cos(an)) / 2f, (1 - cy - cy * sin(an)) / 2f);
        }

        for (int i = 0; i <= vertices; i++) {
            an = -HALF_PI - HALF_PI * i / vertices;
            geometry.addVertex((-1 + dx + dx * cos(an)) / 2f, (1 - dy - dy * sin(an)) / 2f);
        }

        return geometry;
    }

    /**
     * Build a rectangle with rounded corners
     *
     * @param geometry a {@link Geometry} to fill
     * @param vertices the number of vertices
     * @param radius   the radius between [0,1] used to round each corner
     * @param ratio    the ratio between width and height
     * @see #rect(Geometry, int, float, float, float, float, float)
     */

    public static Geometry rect(Geometry geometry, int vertices, float radius, float ratio) {
        return rect(geometry, vertices, radius, radius, radius, radius, ratio);
    }

    /**
     * Build a triangle
     *
     * @param geometry a not null {@link Geometry} to fill
     */

    public static Geometry triangle(Geometry geometry) {
        geometry.clear();
        geometry.addVertices(
                -0.5f, -0.5f,
                0.5f, 0,
                -0.5f, 0.5f);
        return geometry;
    }

    /**
     * Build an oval
     *
     * @param geometry a not null {@link Geometry} to fill
     * @param vertices the number of vertices used to create this figure
     */

    public static Geometry oval(Geometry geometry, int vertices) {
        geometry.clear();
        for (int i = 0; i <= vertices; i++) {
            float a = TWO_PI * i / vertices;
            geometry.addVertex(cos(a) / 2f, sin(a) / 2f);
        }
        return geometry;
    }

    /**
     * Build a pause shape
     *
     * @param geometry a not null {@link Geometry} to fill
     */

    public static Geometry pause(Geometry geometry) {
        geometry.clear();
        geometry.addVertices(
                -0.5f, -0.5f,
                -0.165f, -0.5f,
                -0.165f, 0.5f,
                -0.5f, 0.5f,
                0.5f, -0.5f,
                0.165f, -0.5f,
                0.165f, 0.5f,
                0.5f, 0.5f);
        geometry.get(4).setPrimer(true);
        return geometry;
    }

    /**
     * Build an arrow
     *
     * @param geometry a not null {@link Geometry} to fill
     */

    public static Geometry arrow(Geometry geometry) {
        geometry.clear();
        geometry.addVertices(
                -0.5f, -0.5f,
                0.5f, 0,
                -0.5f, 0.5f,
                -0.5f / 1.5f, 0);
        return geometry;
    }

    /**
     * Build an arrow
     *
     * @param geometry a not null {@link Geometry} to fill
     */

    // TODO: sistemare vertici
    public static Geometry arrow2(Geometry geometry) {
        geometry.clear();
        geometry.addVertices(
                -0.15f, -1f,
                0.25f, -1f,
                1f, 0f,
                0.25f, 1f,
                -0.15f, 1f,
                0.5f, 0.15f,
                -1f, 0.15f,
                -1f, -0.15f,
                0.5f, -0.15f);
        return geometry;
    }

    /**
     * Build a plus shape
     *
     * @param geometry  a not null {@link Geometry} to fill
     * @param thickness the shape thickness
     */

    public static Geometry plus(Geometry geometry, float thickness) {
        float th = Utility.constrain(Math.abs(thickness), 0, 0.375f);
        geometry.clear();
        geometry.addVertices(
                -th, -0.5f,
                th, -0.5f,
                th, -th,
                0.5f, -th,
                0.5f, th,
                th, th,
                th, 0.5f,
                -th, 0.5f,
                -th, th,
                -0.5f, th,
                -0.5f, -th,
                -th, -th);
        return geometry;
    }

    /**
     * Build a delete shape
     *
     * @param geometry  a not null {@link Geometry} to fill
     * @param thickness the shape's thickness between [0, 1]
     */

    public static Geometry delete(Geometry geometry, float thickness) {
        float th = Utility.constrain(thickness, 0, 0.5f);
        geometry.clear();
        geometry.addVertices(
                -0.5f + th, -0.5f,
                -0.5f, -0.5f,
                -th / 2f, 0,
                -0.5f, 0.5f,
                -0.5f + th, 0.5f,
                0, th / 2f,
                0.5f - th, 0.5f,
                0.5f, 0.5f,
                th / 2f, 0,
                0.5f, -0.5f,
                0.5f - th, -0.5f,
                0, -th / 2f);
        return geometry;
    }
}
