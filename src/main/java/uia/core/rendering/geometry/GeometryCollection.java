package uia.core.rendering.geometry;

import uia.utility.MathUtility;

import java.util.Objects;

import static uia.utility.MathUtility.*;

/**
 * The GeometryCollection collects a list of geometries.
 */

public final class GeometryCollection {

    private GeometryCollection() {
    }

    /**
     * Standard number of geometry vertices.
     */
    public static final int STD_VERT = 25;

    /**
     * Standard corner radius for rounded geometry (ie: rounded rect).
     */
    public static final float STD_ROUND = 0.1f;

    /**
     * Resets the given geometry and creates a rectangle.
     *
     * @param geometry the geometry where the vertices are to be stored
     * @return the given geometry object filled with geometry vertices
     * @throws NullPointerException if {@code geometry == null}
     */

    public static Geometry rect(Geometry geometry) {
        geometry.removeAllVertices();
        geometry.addVertices(
                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f, 0.5f,
                -0.5f, 0.5f
        );
        return geometry;
    }

    /**
     * Resets the given geometry and creates a rectangle with rounded corners.
     *
     * @param geometry         the geometry where the vertices are to be stored
     * @param vertices         the amount of vertices (> 0) used to create the geometry
     * @param upperLeftRadius  the upper-left corner radius between [0, 1]
     * @param upperRightRadius the upper-right corner radius between [0, 1]
     * @param lowerRightRadius the lower-right corner radius between [0, 1]
     * @param lowerLeftRadius  the lower-left corner radius between [0, 1]
     * @param ratio            the width to height ration between [0, 1]
     * @return the given geometry object filled with geometry vertices
     * @throws NullPointerException if {@code geometry == null}
     */

    public static Geometry rect(Geometry geometry, int vertices,
                                float upperLeftRadius,
                                float upperRightRadius,
                                float lowerRightRadius,
                                float lowerLeftRadius,
                                float ratio) {
        geometry.removeAllVertices();

        vertices = vertices / 4;

        float f1 = 1;
        float f2 = 1;
        if (ratio >= 1) {
            f1 = 1f / ratio;
        } else {
            f2 = ratio;
        }

        float ax = upperLeftRadius * f1;
        float ay = upperLeftRadius * f2;
        float bx = upperRightRadius * f1;
        float by = upperRightRadius * f2;
        float cx = lowerRightRadius * f1;
        float cy = lowerRightRadius * f2;
        float dx = lowerLeftRadius * f1;
        float dy = lowerLeftRadius * f2;
        float angle;

        for (int i = 0; i <= vertices; i++) {
            angle = PI - HALF_PI * i / vertices;
            geometry.addVertex((-1 + ax + ax * cos(angle)) / 2f, (-1 + ay * (1 - sin(angle))) / 2f);
        }
        for (int i = 0; i <= vertices; i++) {
            angle = HALF_PI - HALF_PI * i / vertices;
            geometry.addVertex((1 - bx + bx * cos(angle)) / 2f, (-1 + by - by * sin(angle)) / 2f);
        }
        for (int i = 0; i <= vertices; i++) {
            angle = -HALF_PI * i / vertices;
            geometry.addVertex((1 - cx + cx * cos(angle)) / 2f, (1 - cy - cy * sin(angle)) / 2f);
        }
        for (int i = 0; i <= vertices; i++) {
            angle = -HALF_PI - HALF_PI * i / vertices;
            geometry.addVertex((-1 + dx + dx * cos(angle)) / 2f, (1 - dy - dy * sin(angle)) / 2f);
        }

        return geometry;
    }

    /**
     * Resets the given geometry and creates a rectangle with rounded corners.
     *
     * @param geometry the geometry where the vertices are to be stored
     * @param vertices the amount of vertices (> 0) used to create the geometry
     * @param radius   the corner radius between [0, 1]
     * @param ratio    the width to height ration between [0,1]
     * @return the given geometry object filled with geometry vertices
     * @see #rect(Geometry, int, float, float, float, float, float)
     */

    public static Geometry rect(Geometry geometry, int vertices, float radius, float ratio) {
        return rect(geometry, vertices, radius, radius, radius, radius, ratio);
    }

    /**
     * Resets the given geometry and creates a triangle.
     *
     * @param geometry the geometry where the vertices are to be stored
     * @return the given geometry object filled with geometry vertices
     * @throws NullPointerException if {@code geometry == null}
     */

    public static Geometry triangle(Geometry geometry) {
        geometry.removeAllVertices();
        geometry.addVertices(
                -0.5f, -0.5f,
                0.5f, 0,
                -0.5f, 0.5f
        );
        return geometry;
    }

    /**
     * Resets the given geometry and creates an oval.
     *
     * @param geometry the geometry where the vertices are to be stored
     * @param vertices the amount of vertices (> 0) used to create the geometry
     * @return the given geometry object filled with geometry vertices
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
     * Resets the given geometry and creates an arrow.
     *
     * @param geometry the geometry where the vertices are to be stored
     * @return the given geometry object filled with geometry vertices
     * @throws NullPointerException if {@code geometry == null}
     */

    public static Geometry arrow(Geometry geometry) {
        geometry.removeAllVertices();
        geometry.addVertices(
                -0.5f, -0.5f,
                0.5f, 0,
                -0.5f, 0.5f,
                -0.5f / 1.5f, 0
        );
        return geometry;
    }

    /**
     * Resets the given geometry and creates a plus geometry.
     *
     * @param geometry  the geometry where the vertices are to be stored
     * @param thickness the geometry thickness between [0, 0.375]
     * @return the given geometry object filled with geometry vertices
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
                -th, -th
        );
        return geometry;
    }

    /**
     * Resets the given geometry and creates a delete geometry.
     *
     * @param geometry  the geometry where the vertices are to be stored
     * @param thickness the geometry thickness between [0, 0.5]
     * @return the given geometry object filled with geometry vertices
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
                0, -th / 2f
        );
        return geometry;
    }

    /**
     * Resets the given geometry and creates an arc.
     *
     * @param geometry  the geometry where the vertices are to be stored
     * @param vertices  the number of vertices (> 0) used to create the geometry
     * @param angle     the angle of the arc
     * @param thickness the arc thickness between [0, 0.5]
     * @return the given geometry object filled with geometry vertices
     * @throws NullPointerException if {@code geometry == null}
     */

    public static Geometry arc(Geometry geometry, int vertices, float angle, float thickness) {
        Objects.requireNonNull(geometry);
        geometry.removeAllVertices();

        // outer part
        for (int i = vertices; i >= 0; i--) {
            float tempAngle = angle * i / vertices;
            geometry.addVertex(
                    cos(tempAngle) / 2f,
                    sin(tempAngle) / 2f
            );
        }

        // internal part
        float normThickness = 0.5f - MathUtility.constrain(thickness, 0f, 0.5f);
        for (int i = 0; i <= vertices; i++) {
            float tempAngle = angle * i / vertices;
            geometry.addVertex(
                    normThickness * cos(tempAngle),
                    normThickness * sin(tempAngle)
            );
        }

        return geometry;
    }
}
