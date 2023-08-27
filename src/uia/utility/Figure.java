package uia.utility;

import uia.core.Geom;

import static uia.utility.TrigTable.*;

/**
 * Figure is a collection of functions used to easily build shapes
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
     * @param geom a not null {@link Geom} to fill
     */

    public static Geom rect(Geom geom) {
        geom.clear();
        geom.addVertices(
                -1, -1,
                1, -1,
                1, 1,
                -1, 1);
        return geom;
    }

    /**
     * Build a rectangle with rounded corners.
     * <br>
     * Note that this method must be called at runtime because of corner rounding calculation.
     *
     * @param geom     a not null {@link Geom} to fill
     * @param vertices the number of vertices
     * @param a        the radius between [0,1] used to round the upper-left corner
     * @param b        the radius between [0,1] used to round the upper-right corner
     * @param c        the radius between [0,1] used to round the lower-right corner
     * @param d        the radius between [0,1] used to round the lower-left corner
     * @param ratio    the ratio between width and height
     */

    public static Geom rect(Geom geom, int vertices,
                            float a, float b, float c, float d,
                            float ratio) {
        geom.clear();

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
            geom.addVertex(-1 + ax + ax * cos(an), -1 + ay * (1 - sin(an)));
        }

        for (int i = 0; i <= vertices; i++) {
            an = HALF_PI - HALF_PI * i / vertices;
            geom.addVertex(1 - bx + bx * cos(an), -1 + by - by * sin(an));
        }

        for (int i = 0; i <= vertices; i++) {
            an = -HALF_PI * i / vertices;
            geom.addVertex(1 - cx + cx * cos(an), 1 - cy - cy * sin(an));
        }

        for (int i = 0; i <= vertices; i++) {
            an = -HALF_PI - HALF_PI * i / vertices;
            geom.addVertex(-1 + dx + dx * cos(an), 1 - dy - dy * sin(an));
        }

        return geom;
    }

    /**
     * Build a rectangle with rounded corners
     *
     * @param geom     a {@link Geom} to fill
     * @param vertices the number of vertices
     * @param radius   the radius between [0,1] used to round each corner
     * @param ratio    the ratio between width and height
     * @see #rect(Geom, int, float, float, float, float, float)
     */

    public static Geom rect(Geom geom, int vertices, float radius, float ratio) {
        return rect(geom, vertices, radius, radius, radius, radius, ratio);
    }

    /**
     * Build a triangle
     *
     * @param geom a not null {@link Geom} to fill
     */

    public static Geom triangle(Geom geom) {
        geom.clear();
        geom.addVertices(
                -1, -1,
                1, 0,
                -1, 1);
        return geom;
    }

    /**
     * Build an oval
     *
     * @param geom     a not null {@link Geom} to fill
     * @param vertices the number of vertices used to create this figure
     */

    public static Geom oval(Geom geom, int vertices) {
        geom.clear();
        for (int i = 0; i <= vertices; i++) {
            float a = TWO_PI * i / vertices;
            geom.addVertex(cos(a), sin(a));
        }
        return geom;
    }

    /**
     * Build a pause shape
     *
     * @param geom a not null {@link Geom} to fill
     */

    public static Geom pause(Geom geom) {
        geom.clear();
        geom.addVertices(
                -1, -1,
                -0.33f, -1,
                -0.33f, 1,
                -1, 1,
                1, -1,
                0.33f, -1,
                0.33f, 1,
                1, 1);
        geom.getRaw(4).beginGeom = true;
        return geom;
    }

    /**
     * Build an arrow
     *
     * @param geom a not null {@link Geom} to fill
     */

    public static Geom arrow(Geom geom) {
        geom.clear();
        geom.addVertices(
                -1, -1,
                1, 0,
                -1, 1,
                -1f / 3, 0);
        return geom;
    }

    /**
     * Build an arrow
     *
     * @param geom a not null {@link Geom} to fill
     */

    public static Geom arrow2(Geom geom) {
        geom.clear();
        geom.addVertices(
                -0.15f, -1f,
                0.25f, -1f,
                1f, 0f,
                0.25f, 1f,
                -0.15f, 1f,
                0.5f, 0.15f,
                -1f, 0.15f,
                -1f, -0.15f,
                0.5f, -0.15f);
        return geom;
    }

    /**
     * Build a plus shape
     *
     * @param geom      a not null {@link Geom} to fill
     * @param thickness the shape thickness
     */

    public static Geom plus(Geom geom, float thickness) {
        float th = Utility.constrain(Math.abs(thickness), 0, 0.75f);
        geom.clear();
        geom.addVertices(
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
        return geom;
    }

    /**
     * Build a delete shape
     *
     * @param geom      a not null {@link Geom} to fill
     * @param thickness the shape's thickness between [0, 1]
     */

    public static Geom delete(Geom geom, float thickness) {
        float th = Utility.constrain(thickness, 0, 1f);
        geom.clear();
        geom.addVertices(
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
        return geom;
    }
}
