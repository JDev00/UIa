package uia.core.ui.primitives.shape;

import uia.utility.MathUtility;

import java.util.Objects;

/**
 * A normalized vertex is a single geometric point whose values are constrained between [-0.5, 0.5].
 */

public class NormalizedVertex {
    private float x;
    private float y;
    private boolean primer = false;

    public NormalizedVertex(float x, float y) {
        this.set(x, y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, primer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NormalizedVertex vertex = (NormalizedVertex) o;
        return Float.compare(x, vertex.x) == 0 && Float.compare(y, vertex.y) == 0 && primer == vertex.primer;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "x=" + x +
                ", y=" + y +
                ", primer=" + primer +
                '}';
    }

    /**
     * Constrains the given value between [-0.5, 0.5]
     */

    private static float constrainValue(float value) {
        return MathUtility.constrain(value, -0.5f, 0.5f);
    }

    /**
     * Set the vertex position
     *
     * @param x the position along x-axis between [-0.5, 0.5]
     * @param y the position along y-axis between [-0.5, 0.5]
     * @return this Vertex
     */

    public NormalizedVertex set(float x, float y) {
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

    public NormalizedVertex setPrimer(boolean primer) {
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

    /**
     * @return the vertex values on the x-axis and y-axis as an array
     */

    public float[] toArray() {
        return new float[]{x, y};
    }
}