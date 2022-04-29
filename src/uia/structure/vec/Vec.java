package uia.structure.vec;

import static java.lang.Math.*;

/**
 * Class designed to handle a 2D-vector
 */

public class Vec {
    public float x;
    public float y;

    public Vec(float x, float y) {
        this.x = x;
        this.y = y;
    }


    public Vec add(float v) {
        x += v;
        y += v;
        return this;
    }

    public Vec add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vec add(Vec v) {
        x += v.x;
        y += v.y;
        return this;
    }


    public Vec sub(float v) {
        x -= v;
        y -= v;
        return this;
    }

    public Vec sub(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vec sub(Vec v) {
        x -= v.x;
        y -= v.y;
        return this;
    }


    public Vec set(float v) {
        x = v;
        y = v;
        return this;
    }

    public Vec set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vec set(Vec v) {
        x = v.x;
        y = v.y;
        return this;
    }


    public Vec mul(float v) {
        x *= v;
        y *= v;
        return this;
    }

    public Vec mul(float x, float y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    public Vec mul(Vec v) {
        x *= v.x;
        y *= v.y;
        return this;
    }


    public Vec div(float v) {
        x /= v;
        y /= v;
        return this;
    }

    public Vec div(float x, float y) {
        this.x /= x;
        this.y /= y;
        return this;
    }

    public Vec div(Vec v) {
        x /= v.x;
        y /= v.y;
        return this;
    }

    /**
     * @return this Vector normalized (so all values are sets to ,at maximum, 1)
     */

    public Vec normalize() {
        return div(magnitude());
    }

    public float magnitude() {
        return (float) sqrt(x * x + y * y);
    }

    public float dot(Vec Vec) {
        return x * Vec.x + y * Vec.y;
    }

    public float dist(Vec Vec) {
        float dx = x - Vec.x;
        float dy = y - Vec.y;
        return (float) sqrt(dx * dx + dy * dy);
    }

    public float direction() {
        return (float) atan2(y, x);
    }

    @Override
    public String toString() {
        return "Vec{" + "x=" + x + ", y=" + y + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Vec)) return false;

        Vec Vec = (Vec) o;

        return Float.compare(Vec.x, x) == 0 && Float.compare(Vec.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result = Float.hashCode(x);
        result = 31 * result + Float.hashCode(y);
        return result;
    }
}
