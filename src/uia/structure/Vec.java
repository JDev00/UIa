package uia.structure;

import java.util.Objects;

/**
 * 2D vector representation
 */

public class Vec {
    public float x;
    public float y;

    public Vec(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vec add(Vec v) {
        if (v != null) return add(v.x, v.y);
        return this;
    }

    public Vec sub(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vec sub(Vec v) {
        if (v != null) return sub(v.x, v.y);
        return this;
    }

    public Vec set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vec set(Vec v) {
        if (v != null) return set(v.x, v.y);
        return this;
    }

    public Vec mul(float x, float y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    public Vec mul(Vec v) {
        if (v != null) return mul(v.x, v.y);
        return this;
    }

    public Vec div(float x, float y) {
        if (x != 0) this.x /= x;
        if (y != 0) this.y /= y;
        return this;
    }

    public Vec div(Vec v) {
        if (v != null) return div(v.x, v.y);
        return this;
    }

    /**
     * Normalize this vector
     *
     * @return this vector normalized (x and y are bounded between [0,1])
     */

    public Vec normalize() {
        float m = magnitude();
        return div(m, m);
    }

    @Override
    public String toString() {
        return "Vec{" + "x=" + x + ", y=" + y + '}';
    }

    /**
     * @return the vector's magnitude
     */

    public float magnitude() {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the dot product
     *
     * @param v a not null {@link Vec}
     * @return the dot product
     * @throws NullPointerException if {v == null}
     */

    public float dot(Vec v) {
        Objects.requireNonNull(v);
        return x * v.x + y * v.y;
    }

    /**
     * Calculate the distance between this vector and the given one
     *
     * @param v a not null {@link Vec}
     * @return the distance between this vector and the given one
     * @throws NullPointerException if {v == null}
     */

    public float dist(Vec v) {
        Objects.requireNonNull(v);
        float dx = x - v.x;
        float dy = y - v.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Calculate the vector direction
     *
     * @return the vector direction
     */

    public float direction() {
        return (float) Math.atan2(y, x);
    }
}
