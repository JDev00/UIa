package uia.core;

import java.util.ArrayList;
import java.util.List;

import static uia.utility.TrigTable.*;

/**
 * A Geom object is used to define the shape's geometry.
 * <br>
 * By default, Geom assumes that geometry is centered based.
 */

public class Geom {

    /**
     * Bounds defines geometry's position, scale and rotation.
     * <br>
     * Bounds array is structured as follows:
     * <ul>
     *     <li>bounds[0]: position along x-axis;</li>
     *     <li>bounds[1]: position along y-axis;</li>
     *     <li>bounds[2]: scale along x-axis;</li>
     *     <li>bounds[3]: scale along y-axis;</li>
     *     <li>bounds[4]: rotation</li>
     * </ul>
     */
    private final float[] bounds;

    private final List<Vertex> vertices;
    private final List<Vertex> transformed;

    public Geom() {
        bounds = new float[5];

        vertices = new ArrayList<>();
        transformed = new ArrayList<>();
    }

    /**
     * Set the geometry's bounds
     *
     * @param x        the geometry position along x-axis;
     * @param y        the geometry position along y-axis;
     * @param scaleX   the geometry scale along x-axis;
     * @param scaleY   the geometry scale along y-axis;
     * @param rotation the geometry rotation;
     */

    public void setBounds(float x, float y, float scaleX, float scaleY, float rotation) {
        bounds[0] = x;
        bounds[1] = y;
        bounds[2] = scaleX;
        bounds[3] = scaleY;
        bounds[4] = rotation;
    }

    /**
     * Transform the given vertex.
     * <br>
     * Time required: T(1)
     * <br>
     * Space required: O(1)
     */

    private void transform(Vertex source, Vertex target) {
        float x = 0.5f * bounds[2] * source.x;
        float y = 0.5f * bounds[3] * source.y;
        float cos = cos(bounds[4]);
        float sin = sin(bounds[4]);

        target.beginGeom = source.beginGeom;
        target.x = bounds[0] + rotX(x, y, cos, sin);
        target.y = bounds[1] + rotY(x, y, cos, sin);
    }

    /**
     * Remove all vertices
     *
     * @return this Geom
     */

    public Geom clear() {
        vertices.clear();
        transformed.clear();
        return this;
    }

    /**
     * Add a new vertex
     *
     * @param x         the vertex coordinate along x-axis
     * @param y         the vertex coordinate along y-axis
     * @param beginGeom true to start a new piece of geometry
     * @return this Geom
     */

    public Geom addVertex(float x, float y, boolean beginGeom) {
        vertices.add(new Vertex(x, y, vertices.isEmpty() || beginGeom));
        transformed.add(new Vertex(0, 0, false));
        return this;
    }

    /**
     * Add a new vertex
     *
     * @param x the vertex coordinate along x-axis
     * @param y the vertex coordinate along y-axis
     * @return this Geom
     */

    public Geom addVertex(float x, float y) {
        return addVertex(x, y, false);
    }

    /**
     * Add new vertices
     *
     * @param vertices a not null array of vertices. The array's shape must be: [x1,y1, x2,y2, ..., xn,yn]
     * @return this Geom
     * @throws NullPointerException if {@code vertices == null}
     */

    public Geom addVertices(float... vertices) {
        if (vertices.length % 2 == 0) {
            for (int i = 0; i < vertices.length; i += 2) {
                addVertex(vertices[i], vertices[i + 1], false);
            }
        }
        return this;
    }

    /**
     * Remove the specified vertex
     *
     * @param i the position of the vertex to remove
     * @return this Geom
     */

    public Geom removeVertex(int i) {
        if (i >= 0 && i < vertices.size()) {
            transformed.remove(i);
            // set the first vertex as start point for geometry
            if (!vertices.isEmpty()) vertices.get(0).beginGeom = true;
        }
        return this;
    }

    /**
     * @return the number of vertices
     */

    public int vertices() {
        return vertices.size();
    }

    /**
     * Return the specified transformed vertex
     *
     * @param i the position of the Vertex
     * @return the specified Vertex
     * @throws IndexOutOfBoundsException if {@code i < 0 || i >= vertices()}
     */

    public Vertex get(int i) {
        transform(vertices.get(i), transformed.get(i));
        return transformed.get(i);
    }

    /**
     * Return the specified skeleton vertex (not transformed)
     *
     * @param i the position of the Vertex
     * @return the specified Vertex
     * @throws IndexOutOfBoundsException if {@code i < 0 || i >= vertices()}
     */

    public Vertex getRaw(int i) {
        return vertices.get(i);
    }

    /**
     * Check if the given point is inside this geometry.
     * <br>
     * Time required: O(n)
     * <br>
     * Space required: O(1)
     *
     * @param x the point position along x-axis
     * @param y the point position along y-axis
     */

    public boolean contains(float x, float y) {
        boolean inside = false;

        // check if the given point is inside this shape.
        // https://wrf.ecse.rpi.edu/Research/Short_Notes/pnpoly.html
        for (int i = 0, j = vertices.size() - 1; i < vertices.size(); j = i++) {
            Vertex vi = get(i);
            Vertex vj = get(j);

            if ((vi.y > y) != (vj.y > y) && x < (vj.x - vi.x) * (y - vi.y) / (vj.y - vi.y) + vi.x)
                inside = !inside;
        }

        return inside;
    }

    /**
     * Geometric point definition
     */

    public static class Vertex {
        public float x;
        public float y;
        public boolean beginGeom;

        public Vertex(float x, float y, boolean beginGeom) {
            this.x = x;
            this.y = y;
            this.beginGeom = beginGeom;
        }
    }
}
