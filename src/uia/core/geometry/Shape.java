package uia.core.geometry;

import uia.core.policy.Path;
import uia.structure.vector.Vec;
import uia.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic {@link Figure} implementation.
 * <br>
 * The aim of this class is to simplify the creation and manipulation of a shape, in addition,
 * the shape rotation has been already implemented.
 */

public abstract class Shape implements Figure {
    private final List<Vector> vertices;

    public Shape(int iCapacity) {
        vertices = new ArrayList<>(iCapacity);
    }

    /**
     * Remove all vertices from this shape
     */

    public void clear() {
        vertices.clear();
    }

    /**
     * Add a new vertex to this shape
     *
     * @param x         a value (preferably) between [-1,1] used to express the vertex position along x-axis
     * @param y         a value (preferably) between [-1,1] used to express the vertex position along y-axis
     * @param beginGeom true to start a new piece of geometry
     */

    public void addVertex(float x, float y, boolean beginGeom) {
        if (vertices.size() == 0) {
            vertices.add(new Vector(x, y, true));
        } else {
            vertices.add(new Vector(x, y, beginGeom));
        }
    }

    /**
     * Add a new vertex to this shape
     *
     * @param x a value (preferably) between [-1,1] used to express the vertex position along x-axis
     * @param y a value (preferably) between [-1,1] used to express the vertex position along y-axis
     */

    public void addVertex(float x, float y) {
        addVertex(x, y, false);
    }

    /**
     * Add some new vertices to this shape
     *
     * @param vertices an array of vertices. Its shape is: [x1,y1, x2,y2, ..., xn,yn]
     */

    public void addVertices(float... vertices) {
        if (vertices != null && vertices.length % 2 == 0) {

            for (int i = 0; i < vertices.length; i += 2)
                addVertex(vertices[i], vertices[i + 1], false);
        }
    }

    /**
     * Replace the specified vertex with a new one
     *
     * @param i         the position of the vertex to replace
     * @param x         the new vertex position along x-axis
     * @param y         the new vertex position along y-axis
     * @param beginGeom true to start a new piece of geometry
     */

    public boolean setVertex(int i, float x, float y, boolean beginGeom) {
        if (i >= 0 && i < vertices.size()) {
            Vector vec = vertices.get(i);
            vec.beginGeom = beginGeom;
            vec.x = x;
            vec.y = y;
            return true;
        }
        return false;
    }

    /**
     * Set the specified vertex as start point for a new piece of geometry.
     * <br>
     * <b>Note that the first vertex is ALWAYS a start point for figure geometry</b>
     *
     * @param i         the position of the vertex to modify
     * @param beginGeom true to start a new piece of geometry
     */

    public boolean beginGeom(int i, boolean beginGeom) {
        if (i > 0 && i < vertices.size()) {
            vertices.get(i).beginGeom = beginGeom;
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
            if (vertices.size() != 0)
                vertices.get(0).beginGeom = true;

            return out;
        }

        return false;
    }

    /**
     * Copy the vertices of the given shape and apply them to the current one.
     * <br>
     * Note that every beginGeom of the given shape will be applied as a non beginGeom vertex to the current shape.
     *
     * @param shape a not null shape
     */

    public void copyGeometry(Shape shape) {
        if (shape != null) {

            for (Vector i : shape.vertices) {
                addVertex(i.x, i.y);
            }
        }
    }

    @Override
    public void build(Path path, float px, float py, float dx, float dy, float rot) {
        if (vertices.size() > 0) {
            float cos = Utils.pcos(rot);
            float sin = Utils.psin(rot);

            path.reset();

            for (Vector i : vertices) {
                float xv = i.x * dx / 2;
                float yv = i.y * dy / 2;
                float xn = getX(xv, yv, cos, sin);
                float yn = getY(xv, yv, cos, sin);

                if (i.beginGeom) {
                    path.moveTo(px + xn, py + yn);
                } else {
                    path.lineTo(px + xn, py + yn);
                }
            }

            path.close();
        }
    }

    /**
     * @return the number of vertices
     */

    public int vertices() {
        return vertices.size();
    }

    private static float getX(float x, float y, float cos, float sin) {
        return x * cos - y * sin;
    }

    private static float getY(float x, float y, float cos, float sin) {
        return x * sin + y * cos;
    }

    /*
     *
     */

    private static class Vector extends Vec {
        protected boolean beginGeom;

        public Vector(float x, float y, boolean beginGeom) {
            super(x, y);
            this.beginGeom = beginGeom;
        }
    }
}
