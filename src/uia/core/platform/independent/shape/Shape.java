package uia.core.platform.independent.shape;

import uia.core.platform.policy.Graphic;
import uia.utils.Collider;
import uia.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static uia.utils.TrigTable.*;

/**
 * A Shape encapsulates geometric points.
 * <br>
 * A Shape is intended to be built around the origin. To translate, resize or rotate a Shape use:
 * {@link Shape#setPosition(float, float)}, {@link Shape#setDimension(float, float)} or {@link Shape#setRotation(float)}.
 */

public class Shape {
    private float x;
    private float y;
    private float width;
    private float height;
    private float rotation;

    private final List<Vertex> vertices;

    public Shape(int iCapacity) {
        vertices = new ArrayList<>(iCapacity);
    }

    /**
     * Remove all vertices from this Shape
     */

    public Shape clear() {
        vertices.clear();
        return this;
    }

    /**
     * Add a new vertex to this Shape
     *
     * @param x         the vertex coordinate along x-axis between [-1,1]
     * @param y         the vertex coordinate along y-axis between [-1,1]
     * @param beginGeom true to start a new piece of geometry
     */

    public Shape addVertex(float x, float y, boolean beginGeom) {
        x = Utils.constrain(x, -1, 1);
        y = Utils.constrain(y, -1, 1);

        if (vertices.size() == 0) {
            vertices.add(new Vertex(x, y, true));
        } else {
            vertices.add(new Vertex(x, y, beginGeom));
        }

        return this;
    }

    /**
     * Add a new vertex to this Shape
     *
     * @param x the vertex coordinate along x-axis between [-1,1]
     * @param y the vertex coordinate along y-axis between [-1,1]
     */

    public Shape addVertex(float x, float y) {
        return addVertex(x, y, false);
    }

    /**
     * Add new vertices to this Shape
     *
     * @param vertices a not null array of vertices. The array's shape must be: [x1,y1, x2,y2, ..., xn,yn]
     */

    public Shape addVertices(float... vertices) {
        if (vertices != null && vertices.length % 2 == 0) {

            for (int i = 0; i < vertices.length; i += 2) {
                addVertex(vertices[i], vertices[i + 1], false);
            }
        }
        return this;
    }

    /**
     * Set the specified vertex as start point for a new piece of geometry.
     * <br>
     * <b>Note that the first vertex is ALWAYS a start point for the shape</b>
     *
     * @param i         the position of the vertex to modify
     * @param beginGeom true to start a new piece of geometry
     */

    public boolean setVertex(int i, boolean beginGeom) {
        if (i > 0 && i < vertices.size()) {
            vertices.get(i).beginGeom = beginGeom;
            return true;
        }
        return false;
    }

    /**
     * Replace the specified vertex with a new one
     *
     * @param i         the position of the vertex to replace
     * @param x         the new vertex coordinate along x-axis between [-1,1]
     * @param y         the new vertex coordinate along y-axis between [-1,1]
     * @param beginGeom true to start a new piece of geometry
     */

    public boolean setVertex(int i, float x, float y, boolean beginGeom) {
        if (i >= 0 && i < vertices.size()) {
            Vertex vec = vertices.get(i);
            vec.beginGeom = beginGeom;
            vec.x = x;
            vec.y = y;
            return true;
        }
        return false;
    }

    /**
     * Remove the specified vertex
     *
     * @param i the position of the vertex to remove
     */

    public boolean removeVertex(int i) {
        if (i >= 0 && i < vertices.size()) {
            boolean out = vertices.remove(i) != null;

            // set the first vertex as start point for geometry
            if (vertices.size() != 0) vertices.get(0).beginGeom = true;

            return out;
        }

        return false;
    }

    /**
     * Access Shape's vertices
     */

    public interface Geometry {

        /**
         * Access a vertex
         *
         * @param x         the position along x-axis
         * @param y         the position along y-axis
         * @param beginGeom true if this vertex is used to start a new piece of geometry
         */
        void vertex(float x, float y, boolean beginGeom);
    }

    /**
     * Read Shape's vertices
     *
     * @param geometry a not null {@link Geometry} used to read every vertex
     * @param raw      true to copy raw vertices; false to copy transformed vertices
     */

    public void copy(Geometry geometry, boolean raw) {
        if (raw) {

            for (Vertex i : vertices) {
                geometry.vertex(i.x, i.y, i.beginGeom);
            }

        } else {
            float cos = cos(rotation);
            float sin = sin(rotation);
            Vertex v = new Vertex();

            for (Vertex i : vertices) {
                v.set(i);
                transform(v, cos, sin);
                geometry.vertex(v.x, v.y, i.beginGeom);
            }
        }
    }

    /**
     * Set the Shape's position
     *
     * @param x the position along x-axis
     * @param y the position along y-axis
     */

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Set the Shape's dimension
     *
     * @param x the dimension along x-axis
     * @param y the dimension along y-axis
     */

    public void setDimension(float x, float y) {
        width = x;
        height = y;
    }

    /**
     * Set the Shape's rotation
     *
     * @param a the angle in radians
     */

    public void setRotation(float a) {
        rotation = a;
    }

    private final Vertex cache = new Vertex();

    /**
     * Draw this Shape on the given Graphic
     *
     * @param graphic a not null {@link Graphic}
     */

    public void draw(Graphic graphic) {
        if (vertices.size() > 0 && graphic != null) {
            float cos = cos(rotation);
            float sin = sin(rotation);

            for (Vertex i : vertices) {
                cache.set(i);
                transform(cache, cos, sin);

                if (i.beginGeom) {
                    graphic.vertexBegin(cache.x, cache.y);
                } else {
                    graphic.vertex(cache.x, cache.y);
                }
            }

            graphic.closeShape();
        }
    }

    /**
     * Transform the given vertex.
     * <br>
     * Time required: T(1)
     * <br>
     * Space required: O(1)
     */

    private void transform(Vertex v, float cos, float sin) {
        float xv = v.x * width / 2f;
        float yv = v.y * height / 2f;
        v.x = x + rotX(xv, yv, cos, sin);
        v.y = y + rotY(xv, yv, cos, sin);
    }

    /**
     * @return the number of vertices
     */

    public int vertices() {
        return vertices.size();
    }

    /**
     * Check if the given point is inside this Shape.
     * <br>
     * Time required: O(n)
     * <br>
     * Space required: O(1)
     *
     * @param x the point position along x-axis
     * @param y the point position along y-axis
     */

    public boolean contains(float x, float y) {
        // 1) check if the given point is inside the shape's rect collider
        if (vertices.size() == 0 || !containsAABB(x, y)) return false;

        boolean inside = false;
        float cos = cos(rotation);
        float sin = sin(rotation);
        Vertex vi = new Vertex();
        Vertex vj = new Vertex();

        // 2) check if the given point is inside this shape. https://wrf.ecse.rpi.edu/Research/Short_Notes/pnpoly.html
        for (int i = 0, j = vertices.size() - 1; i < vertices.size(); j = i++) {
            vi.set(vertices.get(i));
            transform(vi, cos, sin);

            vj.set(vertices.get(j));
            transform(vj, cos, sin);

            if ((vi.y > y) != (vj.y > y) && x < (vj.x - vi.x) * (y - vi.y) / (vj.y - vi.y) + vi.x)
                inside = !inside;
        }

        return inside;
    }

    /**
     * Check if the given point is inside the Shape's rectangular collider.
     * <br>
     * Note that will be taken the rotated Shape's width and height.
     * <br>
     * Time required: T(1)
     * <br>
     * Space required O(1)
     *
     * @param x the point position along x-axis
     * @param y the point position along y-axis
     */

    public boolean containsAABB(float x, float y) {
        return intersectAABB(x, y, 1, 1);
    }

    /**
     * Check if the given rectangle intersects the Shape's rectangular collider.
     * <br>
     * Note that will be taken the rotated shape's width and height.
     * <br>
     * Time required: T(1)
     * <br>
     * Space required O(1)
     *
     * @param x the point position along x-axis
     * @param y the point position along y-axis
     */

    public boolean intersectAABB(float x, float y, float width, float height) {
        float cos = cos(rotation);
        float sin = sin(rotation);
        float w = boundX(this.width, this.height, cos, sin);
        float h = boundY(this.width, this.height, cos, sin);
        return Collider.AABB(this.x, this.y, w, h, x, y, width, height);
    }

    /**
     * @return the Shape's position along x-axis
     */

    public float x() {
        return x;
    }

    /**
     * @return the Shape's position along y-axis
     */

    public float y() {
        return y;
    }

    /**
     * @return the Shape's dimension along x-axis
     */

    public float width() {
        return width;
    }

    /**
     * @return the Shape's dimension along y-axis
     */

    public float height() {
        return height;
    }

    /**
     * @return the Shape's rotation
     */

    public float rotation() {
        return rotation;
    }

    /**
     * Geometric point definition
     */

    private static class Vertex {
        private float x;
        private float y;
        private boolean beginGeom;

        public Vertex(float x, float y, boolean beginGeom) {
            this.x = x;
            this.y = y;
            this.beginGeom = beginGeom;
        }

        public Vertex() {
            this(0f, 0f, false);
        }

        /**
         * Set the params of the given vertex to the current one
         *
         * @param vertex a not null Vertex
         */

        public void set(Vertex vertex) {
            x = vertex.x;
            y = vertex.y;
            beginGeom = vertex.beginGeom;
        }
    }
}
