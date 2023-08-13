package uia.core;

import java.util.ArrayList;
import java.util.List;

import static uia.utils.TrigTable.*;

/**
 * A Geom object encapsulates geometric points and allows for operations on such points
 */

public class Geom {
    public float x;
    public float y;
    public float scaleX = 1f;
    public float scaleY = 1f;
    public float rotation;

    private final List<Vertex> vertices;
    private final List<Vertex> transformed;

    public Geom() {
        vertices = new ArrayList<>();
        transformed = new ArrayList<>();
    }

    /**
     * Transform the given vertex.
     * <br>
     * Time required: T(1)
     * <br>
     * Space required: O(1)
     */

    private void transform(Vertex source, Vertex target, float cos, float sin) {
        float xt = scaleX * source.x;
        float yt = scaleY * source.y;

        target.beginGeom = source.beginGeom;
        target.x = x + rotX(xt, yt, cos, sin);
        target.y = y + rotY(xt, yt, cos, sin);
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
        vertices.add(new Vertex(x, y, vertices.size() == 0 || beginGeom));
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
            if (vertices.size() != 0) vertices.get(0).beginGeom = true;
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
        transform(vertices.get(i), transformed.get(i), cos(rotation), sin(rotation));
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
