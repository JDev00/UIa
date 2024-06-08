package uia.core.ui.primitives.shape;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static uia.utility.MathUtility.*;

/**
 * Geometry has the responsibility to handle a set of normalized vertices.
 * <br>
 * A normalized vertex has its dimensions (values) constrained between [-0.5, 0.5].
 */

public class Geometry implements Iterable<NormalizedVertex> {
    private final List<NormalizedVertex> vertices;

    public Geometry() {
        vertices = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Geometry{" +
                "vertices=" + vertices +
                ", verticesNumber=" + vertices.size() +
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
        vertices.add(new NormalizedVertex(x, y).setPrimer(vertices.isEmpty() || primer));
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
     * Adds the specified vertices to this geometry
     *
     * @param vertices a list of vertices. The array shape must be: [x1,y1, x2,y2, ..., xn,yn]
     * @return this Geometry
     * @throws NullPointerException     if {@code vertices == null}
     * @throws IllegalArgumentException if the array shape is malformed
     */

    public Geometry addVertices(float... vertices) {
        Objects.requireNonNull(vertices);
        if (vertices.length % 2 != 0) {
            throw new IllegalArgumentException("the 'vertices' shape must be [x1,y1, x2,y2, ... , xn, yn]");
        }
        for (int i = 0; i < vertices.length; i += 2) {
            addVertex(vertices[i], vertices[i + 1]);
        }
        return this;
    }

    /**
     * Removes the specified vertex from this geometry.
     * <br>
     * If the first vertex is removed and there are multiple vertices, the next one is
     * selected as the primer vertex.
     *
     * @param i the position of the vertex to remove
     * @return this Geometry
     * @throws IndexOutOfBoundsException if {@code i < 0 or i >= vertices()}
     */

    public Geometry removeVertex(int i) {
        if (i < 0 || i >= vertices()) {
            throw new IndexOutOfBoundsException("the provided index 'i' is out of range. " +
                    "Current range: [0, " + (vertices() - 1) + "]");
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
     * @return the specified {@link NormalizedVertex}
     * @throws IndexOutOfBoundsException if {@code i < 0 || i >= vertices()}
     */

    public NormalizedVertex get(int i) {
        return vertices.get(i);
    }

    @Override
    public Iterator<NormalizedVertex> iterator() {
        return vertices.iterator();
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
        for (NormalizedVertex vertex : geometry) {
            float vertexX = vertex.getX();
            float vertexY = vertex.getY();
            float newVertexX = rotateX(vertexX, vertexY, radians);
            float newVertexY = rotateY(vertexX, vertexY, radians);
            vertex.set(newVertexX, newVertexY);
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
        for (NormalizedVertex vertex : geometry) {
            float vx = vertex.getX();
            float vy = vertex.getY();
            float nx = scaleX * vx;
            float ny = scaleY * vy;
            vertex.set(nx, ny);
        }
        return geometry;
    }
}
