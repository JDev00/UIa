package uia.utility;

import static java.lang.Math.abs;

/**
 * Collection of utilities for trigonometric operations
 */

public final class TrigTable {
    public static final float PI = (float) java.lang.Math.PI;
    public static final float TWO_PI = 2 * PI;
    public static final float HALF_PI = PI / 2f;

    public static final float DEG_TO_RAD = PI / 180.0f;

    private static final short SIZE = 3000;
    private static final float PRE = SIZE / TWO_PI;

    private static final float[] X = new float[SIZE + 1];
    private static final float[] Y = new float[SIZE + 1];

    static {
        int index = 0;
        for (int i = 0; i <= SIZE; i++) {
            float a = i * TWO_PI / SIZE;
            X[index] = (float) java.lang.Math.cos(a);
            Y[index] = (float) java.lang.Math.sin(a);
            index++;
        }
    }

    private TrigTable() {
    }

    /**
     * Convert degrees to radians
     *
     * @param degrees the degrees to convert
     * @return the corresponding radians
     */

    public static float radians(float degrees) {
        return DEG_TO_RAD * degrees;
    }

    /**
     * Time required: T(1)
     * <br>
     * Space required: O(1)
     *
     * @param angle the angle expressed in radians
     * @return the cosine of the given angle
     */

    public static float cos(float angle) {
        return X[(int) (PRE * ((angle < 0 ? TWO_PI : 0) + angle % TWO_PI))];
    }

    /**
     * Time required: T(1)
     * <br>
     * Space required: O(1)
     *
     * @param angle the angle expressed in radians
     * @return the sine of the given angle
     */

    public static float sin(float angle) {
        return Y[(int) (PRE * ((angle < 0 ? TWO_PI : 0) + angle % TWO_PI))];
    }

    /**
     * Rotate a given point.
     * <br>
     * Time required: T(1)
     * <br>
     * Space required: O(1)
     *
     * @param x   the position along x-axis
     * @param y   the position along y-axis
     * @param cos the cosine value
     * @param sin the sine value
     * @return the rotated point's position along x-axis
     */

    public static float rotX(float x, float y, float cos, float sin) {
        return x * cos - y * sin;
    }

    /**
     * @return rotX^-1
     */

    public static float rotXneg(float rx, float ry, float cos, float sin) {
        return (rx * cos + ry * sin) / (cos * cos + sin * sin);
    }

    /**
     * Rotate a given point.
     * <br>
     * Time required: T(1)
     * <br>
     * Space required: O(1)
     *
     * @param x   the position along x-axis
     * @param y   the position along y-axis
     * @param cos the cosine value
     * @param sin the sine value
     * @return the rotated point's position along y-axis
     */

    public static float rotY(float x, float y, float cos, float sin) {
        return x * sin + y * cos;
    }

    /**
     * @return rotY^-1
     */

    public static float rotYneg(float rx, float ry, float cos, float sin) {
        return (ry * cos - rx * sin) / (cos * cos + sin * sin);
    }

    /**
     * Time required: T(1)
     * <br>
     * Space required: O(1)
     *
     * @param w   the rectangle's width
     * @param h   the rectangle's height
     * @param cos the cosine value
     * @param sin the sine value
     * @return the width of a rotated rectangle
     */

    public static float boundX(float w, float h, float cos, float sin) {
        return abs(w * cos) + abs(h * sin);
    }

    /**
     * @return boundX^-1
     */

    public static float boundXneg(float bw, float bh, float cos, float sin) {
        return abs((bh * abs(sin) - bw * abs(cos)) / (sin * sin - cos * cos));
    }

    /**
     * Time required: T(1)
     * <br>
     * Space required: O(1)
     *
     * @param w   the rectangle's width
     * @param h   the rectangle's height
     * @param cos the cosine value
     * @param sin the sine value
     * @return the height of a rotated rectangle
     */

    public static float boundY(float w, float h, float cos, float sin) {
        return abs(h * cos) + abs(w * sin);
    }

    /**
     * @return boundY^-1
     */

    public static float boundYneg(float bw, float bh, float cos, float sin) {
        return abs((bh * abs(cos) - bw * abs(sin)) / (cos * cos - sin * sin));
    }

    //

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            float x = 100 * i;
            float y = 100 * (float) Math.pow(i, 2);
            float cos = cos(0.3f);
            float sin = sin(0.3f);
            float bx = boundX(x, y, cos, sin);
            float by = boundY(x, y, cos, sin);
            float bxn = boundXneg(bx, by, cos, sin);
            float byn = boundYneg(bx, by, cos, sin);

            System.out.println(Float.compare(bxn, x) == 0);
            System.out.println(Float.compare(byn, y) == 0);
        }
    }
}
