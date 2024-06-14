package uia.core.ui.primitives.shape;

import uia.utility.MathUtility;

import java.util.*;

import static uia.utility.MathUtility.*;

/**
 * Geometry is responsible for handling a list of normalized vertices.
 * <br>
 * A normalized vertex has its dimensions (values) constrained between [-0.5, 0.5].
 */

public final class Geometry {
    private static final int INIT_VERTICES = 8;
    private static final int VERTICES_CHUNK_SIZE = 32;

    private float[] vertices;
    private int length = 0;

    public Geometry() {
        vertices = new float[INIT_VERTICES];
    }

    @Override
    public String toString() {
        return "Geometry{" +
                "vertices=" + Arrays.toString(vertices) +
                ", verticesNumber=" + vertices.length +
                '}';
    }

    /**
     * Removes all vertices from this geometry.
     *
     * @return this Geometry
     */

    public Geometry removeAllVertices() {
        vertices = new float[INIT_VERTICES];
        length = 0;
        return this;
    }

    /**
     * Adds a vertex to this geometry.
     *
     * @param x the vertex coordinate along the x-axis
     * @param y the vertex coordinate along the y-axis
     * @return this Geometry
     */

    public Geometry addVertex(float x, float y) {
        // increases the internal array used to hold vertices
        if (2 * length == vertices.length) {
            vertices = Arrays.copyOf(vertices, vertices.length + VERTICES_CHUNK_SIZE);
        }

        // updates geometry
        vertices[2 * length] = MathUtility.constrain(x, -0.5f, 0.5f);
        vertices[2 * length + 1] = MathUtility.constrain(y, -0.5f, 0.5f);
        // updates geometry vertices count
        length++;
        return this;
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
     * Sets the specified vertex.
     *
     * @param i the vertex position inside this geometry
     * @param x the new vertex value along the x-axis
     * @param y the new vertex value along the y-axis
     * @return this Geometry
     * @throws IndexOutOfBoundsException if {@code i < 0 || i >= vertices()}
     */

    public Geometry setVertex(int i, float x, float y) {
        if (i < 0 || i >= vertices()) {
            throw new IndexOutOfBoundsException();
        }

        // updates vertex
        vertices[2 * i] = x;
        vertices[2 * i + 1] = y;
        return this;
    }

    /**
     * Removes the specified vertex from this geometry.
     * <br>
     * If the first vertex is removed and there are multiple vertices, the next one is
     * selected as the primer vertex.
     * <br>
     * Time complexity: O(n)
     * <br>
     * Space complexity: O(1)
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

        // removes the specified element
        System.arraycopy(vertices, 2 * (i + 1), vertices, 2 * i, 2 * (length - i - 1));
        vertices[2 * length - 1] = 0f;
        vertices[2 * length - 2] = 0f;
        // updates the number of vertices
        length--;
        return this;
    }

    /**
     * @return the total number of vertices
     */

    public int vertices() {
        return length;
    }

    /**
     * Returns the specified vertex.
     * <br>
     * Time complexity: T(1)
     * <br>
     * Space complexity: O(1)
     *
     * @param i the position of the vertex
     * @return the specified vertex on the x-axis
     * @throws IndexOutOfBoundsException if {@code i < 0 || i >= vertices()}
     */

    public float getX(int i) {
        return vertices[2 * i];
    }

    /**
     * Returns the specified vertex.
     * <br>
     * Time complexity: T(1)
     * <br>
     * Space complexity: O(1)
     *
     * @param i the position of the vertex
     * @return the specified vertex on the y-axis
     * @throws IndexOutOfBoundsException if {@code i < 0 || i >= vertices()}
     */

    public float getY(int i) {
        return vertices[2 * i + 1];
    }

    /**
     * @return this Geometry as an array
     */

    public float[] toArray() {
        return vertices;
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

        for (int i = 0; i < geometry.vertices(); i++) {
            float vertexX = geometry.getX(i);
            float vertexY = geometry.getY(i);
            float newVertexX = rotateX(vertexX, vertexY, radians);
            float newVertexY = rotateY(vertexX, vertexY, radians);
            geometry.setVertex(i, newVertexX, newVertexY);
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

        for (int i = 0; i < geometry.vertices(); i++) {
            float vertexX = geometry.getX(i);
            float vertexY = geometry.getY(i);
            float newVertexX = scaleX * vertexX;
            float newVertexY = scaleY * vertexY;
            geometry.setVertex(i, newVertexX, newVertexY);
        }
        return geometry;
    }
}
