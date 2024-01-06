package uia.core.shape;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static uia.utility.TrigTable.*;
import static uia.utility.TrigTable.rotY;

/**
 * Geometry defines the skeleton of a shape.
 * <br>
 * Geometry has the responsibility to handle a set of normalized vertices.
 * A normalized vertex has its dimensions (values) constrained between [-0.5, 0.5].
 */

public class Geometry implements Iterable<Vertex> {
    private final List<Vertex> vertices;

    public Geometry() {
        vertices = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Geometry{" +
                "vertices=" + vertices +
                '}';
    }

    /**
     * Removes all vertices from this geometry
     *
     * @return this Geometry
     */

    public Geometry removeAllVertices() {
        vertices.clear();
        return this;
    }

    /**
     * Adds a vertex to this geometry and decides if it is a primer vertex.
     * <br>
     * A primer vertex is the vertex used to start a new piece of geometry.
     *
     * @param x      the vertex coordinate along x-axis
     * @param y      the vertex coordinate along y-axis
     * @param primer true to mark the vertex as primer
     * @return this Geometry
     */

    public Geometry addVertex(float x, float y, boolean primer) {
        vertices.add(new Vertex(x, y).setPrimer(vertices.isEmpty() || primer));
        return this;
    }

    /**
     * Adds a vertex to this geometry
     *
     * @param x the vertex coordinate along x-axis
     * @param y the vertex coordinate along y-axis
     * @return this Geometry
     */

    public Geometry addVertex(float x, float y) {
        return addVertex(x, y, false);
    }

    /**
     * Removes the specified vertex
     *
     * @param i the position of the vertex to remove
     * @return this Geometry
     * @throws IndexOutOfBoundsException if {@code i < 0 or i >= vertices}
     */

    public Geometry removeVertex(int i) {
        if (i < 0 || i >= vertices()) {
            throw new IndexOutOfBoundsException("index is out of range!");
        }
        vertices.remove(i);
        // if needed, set the first vertex as start point for geometry
        if (i == 0 && !vertices.isEmpty()) {
            vertices.get(0).setPrimer(true);
        }
        return this;
    }

    /**
     * @return the total number of vertices
     */

    public int vertices() {
        return vertices.size();
    }

    /**
     * Returns the specified vertex
     *
     * @param i the position of the Vertex
     * @return the specified {@link Vertex}
     * @throws IndexOutOfBoundsException if {@code i < 0 || i >= vertices()}
     */

    public Vertex get(int i) {
        return vertices.get(i);
    }

    @Override
    public Iterator<Vertex> iterator() {
        return vertices.iterator();
    }

    /**
     * Adds the specified vertices to the given geometry
     *
     * @param geometry a not null {@link Geometry} to be filled with the specified vertices
     * @param vertices a list of vertices. The array shape must be: [x1,y1, x2,y2, ..., xn,yn]
     * @return the specified Geometry
     * @throws NullPointerException     if {@code vertices == null}
     * @throws IllegalArgumentException if the array shape is malformed
     */

    public static Geometry addVertices(Geometry geometry, float... vertices) {
        Objects.requireNonNull(vertices);
        if (vertices.length % 2 != 0) {
            throw new IllegalArgumentException("array shape must be [x1,y1, x2,y2, ... , xn, yn]");
        }
        for (int i = 0; i < vertices.length; i += 2) {
            geometry.addVertex(vertices[i], vertices[i + 1]);
        }
        return geometry;
    }

    /**
     * Rotates the specified geometry
     *
     * @param geometry a not null {@link Geometry} to rotate
     * @param radians  the rotation in radians
     * @return the specified Geometry
     * @throws NullPointerException if {@code geometry == null}
     */

    public static Geometry rotate(Geometry geometry, float radians) {
        Objects.requireNonNull(geometry);
        float cos = cos(radians);
        float sin = sin(radians);
        for (Vertex vertex : geometry) {
            float vx = vertex.getX();
            float vy = vertex.getY();
            float nx = rotX(vx, vy, cos, sin);
            float ny = rotY(vx, vy, cos, sin);
            vertex.set(nx, ny);
        }
        return geometry;
    }

    /**
     * Scales the specified geometry
     *
     * @param geometry a not null {@link Geometry} to scale
     * @param scaleX   the scale (> 0) on the x-axis
     * @param scaleY   the scale (> 0) on the y-axis
     * @return the specified Geometry
     * @throws NullPointerException     if {@code geometry == null}
     * @throws IllegalArgumentException if {@code scaleX <= 0 or scaleY <= 0}
     */

    public static Geometry scale(Geometry geometry, float scaleX, float scaleY) {
        Objects.requireNonNull(geometry);
        if (scaleX <= 0) {
            throw new IllegalArgumentException("scaleX must be greater than 0");
        }
        if (scaleY <= 0) {
            throw new IllegalArgumentException("scaleY must be greater than 0");
        }
        for (Vertex vertex : geometry) {
            float vx = vertex.getX();
            float vy = vertex.getY();
            float nx = scaleX * vx;
            float ny = scaleY * vy;
            vertex.set(nx, ny);
        }
        return geometry;
    }
}
