package uia.core;

import uia.utility.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Geometry defines the skeleton of a shape.
 * <br>
 * Geometry has the responsibility to handle a set of normalized vertices.
 * A normalized vertex has its dimensions (values) constrained between [-0.5, 0.5].
 */

public class Geometry {
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
     * Remove all vertices
     *
     * @return this Geometry
     */

    public Geometry clear() {
        vertices.clear();
        return this;
    }

    /**
     * Add a new vertex
     *
     * @param x         the vertex coordinate along x-axis
     * @param y         the vertex coordinate along y-axis
     * @param beginGeom true to start a new piece of geometry
     * @return this Geometry
     */

    public Geometry addVertex(float x, float y, boolean beginGeom) {
        vertices.add(new Vertex().set(x, y).setPrimer(vertices.isEmpty() || beginGeom));
        return this;
    }

    /**
     * Add a new vertex
     *
     * @param x the vertex coordinate along x-axis
     * @param y the vertex coordinate along y-axis
     * @return this Geometry
     */

    public Geometry addVertex(float x, float y) {
        return addVertex(x, y, false);
    }

    /**
     * Add new vertices
     *
     * @param vertices a not null array of vertices. The array's shape must be: [x1,y1, x2,y2, ..., xn,yn]
     * @return this Geometry
     * @throws NullPointerException if {@code vertices == null}
     */

    public Geometry addVertices(float... vertices) {
        if (vertices.length % 2 == 0) {
            for (int i = 0; i < vertices.length; i += 2) {
                addVertex(vertices[i], vertices[i + 1], false);
            }
        }
        return this;
    }

    /*
     * Remove the specified vertex
     *
     * @param i the position of the vertex to remove
     * @return this Geometry
     *

    public Geometry removeVertex(int i) {
        if (i >= 0 && i < vertices.size()) {
            //transformed.remove(i);
            // set the first vertex as start point for geometry
            //if (!vertices.isEmpty())
            vertices.get(0).beginGeom = true;
        }
        return this;
    }*/

    /**
     * @return the number of vertices
     */

    public int vertices() {
        return vertices.size();
    }

    /**
     * Return the specified vertex
     *
     * @param i the position of the Vertex
     * @return the specified Vertex
     * @throws IndexOutOfBoundsException if {@code i < 0 || i >= vertices()}
     */

    public Vertex get(int i) {
        return vertices.get(i);
    }

    /**
     * Vertex definition
     */

    public static class Vertex {
        private float x;
        private float y;
        private boolean primer = false;

        /**
         * Constrains the given value between [-0.5, 0.5]
         */

        private static float constrainValue(float value) {
            return Utility.constrain(value, -0.5f, 0.5f);
        }

        /**
         * Set the vertex position
         *
         * @param x the position along x-axis between [-0.5, 0.5]
         * @param y the position along y-axis between [-0.5, 0.5]
         * @return this Vertex
         */

        public Vertex set(float x, float y) {
            this.x = constrainValue(x);
            this.y = constrainValue(y);
            return this;
        }

        /**
         * A primer vertex is the one who opens a new piece of geometry
         *
         * @param primer true to start a new piece of geometry
         * @return this Vertex
         */

        public Vertex setPrimer(boolean primer) {
            this.primer = primer;
            return this;
        }

        /**
         * @return true if this Vertex is a primer vertex
         */

        public boolean isPrimer() {
            return primer;
        }

        /**
         * @return the vertex position along x-axis
         */

        public float getX() {
            return x;
        }

        /**
         * @return the vertex position along y-axis
         */

        public float getY() {
            return y;
        }

        @Override
        public String toString() {
            return "Vertex{" +
                    "x=" + x +
                    ", y=" + y +
                    ", primer=" + primer +
                    '}';
        }
    }
}
