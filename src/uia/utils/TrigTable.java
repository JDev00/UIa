package uia.utils;

import static java.lang.Math.abs;

/**
 * Table used to perform trigonometric operations in constant time
 */

public class TrigTable {
    public final static float PI = (float) java.lang.Math.PI;
    public final static float TWO_PI = 2 * PI;
    public final static float HALF_PI = PI / 2f;

    public final static float DEG_TO_RAD = PI / 180.0f;
    public final static float RAD_TO_DEG = 1f / DEG_TO_RAD;

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
     * Time required: T(1)
     * <br>
     * Space required: O(1)
     *
     * @param angle the angle expressed in radians
     * @return the tangent of the given angle
     */

    public static float tan(float angle) {
        return sin(angle) / cos(angle);
    }

    /**
     * Time required: T(1)
     * <br>
     * Space required: O(1)
     *
     * @param angle the angle expressed in radians
     * @return the cotangent of the given angle
     */

    public static float cot(float angle) {
        return cos(angle) / sin(angle);
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
}
