package uia.utility;

import uia.core.shape.Geometry;

import static uia.utility.MathUtility.*;

/**
 * Collection of geometries.
 */

public final class Geometries {

    /**
     * Standard number of geometry vertices.
     */
    public static final int STD_VERT = 25;

    /**
     * Standard corner radius for rounded shapes (ie: rounded rect).
     */
    public static final float STD_ROUND = 0.1f;

    /**
     * Builds a rectangle.
     *
     * @param geometry the geometry where the vertices are to be stored
     * @throws NullPointerException if {@code geometry == null}
     */

    public static Geometry rect(Geometry geometry) {
        geometry.removeAllVertices();
        geometry.addVertices(
                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f, 0.5f,
                -0.5f, 0.5f);
        return geometry;
    }

    /**
     * Builds a rectangle with rounded corners.
     * <br>
     * This method must be called at runtime because of corner rounding calculation.
     *
     * @param geometry the geometry where the vertices are to be stored
     * @param vertices the amount of vertices (> 0) used to create the geometry
     * @param a        the upper-left corner radius between [0,1]
     * @param b        the upper-right corner radius between [0,1]
     * @param c        the lower-right corner radius between [0,1]
     * @param d        the lower-left corner radius between [0,1]
     * @param ratio    the width to height ration between [0,1]
     * @return the specified geometry filled with the rounded corners rectangle vertices
     * @throws NullPointerException if {@code geometry == null}
     */

    public static Geometry rect(Geometry geometry, int vertices,
                                float a, float b, float c, float d, float ratio) {
        geometry.removeAllVertices();

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
     * Builds a rectangle with rounded corners.
     *
     * @see #rect(Geometry, int, float, float, float, float, float)
     */

    public static Geometry rect(Geometry geometry, int vertices, float radius, float ratio) {
        return rect(geometry, vertices, radius, radius, radius, radius, ratio);
    }

    /**
     * Builds a triangle.
     *
     * @param geometry the geometry where the vertices are to be stored
     * @throws NullPointerException if {@code geometry == null}
     */

    public static Geometry triangle(Geometry geometry) {
        geometry.removeAllVertices();
        geometry.addVertices(
                -0.5f, -0.5f,
                0.5f, 0,
                -0.5f, 0.5f);
        return geometry;
    }

    /**
     * Builds an oval.
     *
     * @param geometry the geometry where the vertices are to be stored
     * @param vertices the amount of vertices (> 0) used to create the geometry
     * @throws NullPointerException if {@code geometry == null}
     */

    public static Geometry oval(Geometry geometry, int vertices) {
        geometry.removeAllVertices();
        for (int i = 0; i <= vertices; i++) {
            float a = TWO_PI * i / vertices;
            geometry.addVertex(cos(a) / 2f, sin(a) / 2f);
        }
        return geometry;
    }

    /**
     * Builds a pause geometry.
     *
     * @param geometry the geometry where the vertices are to be stored
     * @throws NullPointerException if {@code geometry == null}
     */

    public static Geometry pause(Geometry geometry) {
        geometry.removeAllVertices();
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
     * Builds an arrow.
     *
     * @param geometry the geometry where the vertices are to be stored
     * @throws NullPointerException if {@code geometry == null}
     */

    public static Geometry arrow(Geometry geometry) {
        geometry.removeAllVertices();
        geometry.addVertices(
                -0.5f, -0.5f,
                0.5f, 0,
                -0.5f, 0.5f,
                -0.5f / 1.5f, 0);
        return geometry;
    }

    /**
     * Builds a plus geometry.
     *
     * @param geometry  the geometry where the vertices are to be stored
     * @param thickness the geometry thickness between [0, 0.375]
     * @throws NullPointerException if {@code geometry == null}
     */

    public static Geometry plus(Geometry geometry, float thickness) {
        float th = MathUtility.constrain(Math.abs(thickness), 0, 0.375f);
        geometry.removeAllVertices();
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
     * Builds a delete geometry.
     *
     * @param geometry  the geometry where the vertices are to be stored
     * @param thickness the geometry thickness between [0, 0.5]
     * @throws NullPointerException if {@code geometry == null}
     */

    public static Geometry delete(Geometry geometry, float thickness) {
        float th = MathUtility.constrain(thickness, 0, 0.5f);
        geometry.removeAllVertices();
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
