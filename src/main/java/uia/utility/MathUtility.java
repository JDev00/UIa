package uia.utility;

import static java.lang.Math.*;

/**
 * MathUtility collects mathematical functions.
 */

public final class MathUtility {
    public static final float PI = (float) java.lang.Math.PI;
    public static final float TWO_PI = 2 * PI;
    public static final float HALF_PI = PI / 2f;

    private static final float DEG_TO_RAD = PI / 180.0f;

    private static final short SIZE = 3000;
    private static final float[] COS = new float[SIZE + 1];
    private static final float[] SIN = new float[SIZE + 1];

    static {
        int index = 0;
        for (int i = 0; i <= SIZE; i++) {
            float a = i * TWO_PI / SIZE;
            COS[index] = (float) java.lang.Math.cos(a);
            SIN[index] = (float) java.lang.Math.sin(a);
            index++;
        }
    }

    private MathUtility() {
    }

    /**
     * Maps the given value into a new one
     */

    public static int map(int value, int istart, int istop, int ostart, int ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }

    /**
     * Maps the given value into a new one
     */

    public static float map(float value, float istart, float istop, float ostart, float ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }

    /**
     * Constrains a value between a lower and upper bound
     */

    public static int constrain(int val, int low, int high) {
        return val < low ? low : min(val, high);
    }

    /**
     * Constrains a value between a lower and upper bound
     */

    public static float constrain(float val, float low, float high) {
        return val < low ? low : min(val, high);
    }

    /**
     * Checks if the given String is a number.
     * <br>
     * Time complexity: T(n)
     *
     * @param input a not null String to control
     * @return true if the given String is a number
     */

    public static boolean isNumber(String input) {
        try {
            Float.parseFloat(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Limits the decimals to the specified amount.
     * <br>
     * Time complexity: T(n)
     * <br>
     * Space complexity: O(n)
     *
     * @param number   a not null number
     * @param decimals the number of decimals; it must be {@code >= 0}
     * @return if the given number is a number, a new formatted number, otherwise null
     */

    public static String limitDecimals(String number, int decimals) {
        String result = null;
        if (isNumber(number)) {
            result = number;
            int dotIndex = number.indexOf(".");
            if (dotIndex != -1 && decimals >= 0) {
                int substringStart = 0;
                int substringEnd = min(dotIndex + (decimals == 0 ? 0 : (decimals + 1)), number.length());
                result = number.substring(substringStart, substringEnd);
            }
        }
        return result;
    }

    /**
     * Normalizes the specified value between [0, 1]
     *
     * @param value the value to be normalized
     * @param min   the minimum value
     * @param max   the maximum value
     * @return the new normalized value between [0, 1]
     */

    public static float normalize(float value, float min, float max) {
        return (value - min) / (max - min);
    }

    /**
     * Converts the specified degrees to radians
     *
     * @param degree the degree to convert
     * @return the corresponding radians
     */

    public static float radians(float degree) {
        return DEG_TO_RAD * degree;
    }

    private static final float PRE = SIZE / TWO_PI;

    /**
     * Helper function. Maps the specified angle to an index used to access
     * the correspondent cosine or sine value.
     * <br>
     * Time complexity: T(1)
     * <br>
     * Space complexity: O(1)
     *
     * @param radians the angle in radians
     * @return the index used to access the cached cosine or sine value
     */

    private static int mapAngleToTrigonometricIndex(float radians) {
        return (int) (PRE * ((radians < 0 ? TWO_PI : 0) + radians % TWO_PI));
    }

    /**
     * Returns the cosine value of the specified angle.
     * <br>
     * Time required: T(1)
     * <br>
     * Space required: O(1)
     *
     * @param radians the angle in radians
     * @return the cosine of the specified angle
     */

    public static float cos(float radians) {
        int index = mapAngleToTrigonometricIndex(radians);
        return COS[index];
    }

    /**
     * Returns the sine value of the specified angle.
     * <br>
     * Time required: T(1)
     * <br>
     * Space required: O(1)
     *
     * @param radians the angle in radians
     * @return the sine of the specified angle
     */

    public static float sin(float radians) {
        int index = mapAngleToTrigonometricIndex(radians);
        return SIN[index];
    }

    /**
     * Rotates the specified point.
     * <br>
     * Time required: T(1)
     * <br>
     * Space required: O(1)
     *
     * @param x        the point position on the x-axis
     * @param y        the point position on the y-axis
     * @param rotation the rotation angle in radians
     * @return the rotated point position on the x-axis
     */

    public static float rotateX(float x, float y, float rotation) {
        float cos = cos(rotation);
        float sin = sin(rotation);
        return x * cos - y * sin;
    }

    /**
     * Rotate a given point.
     * <br>
     * Time required: T(1)
     * <br>
     * Space required: O(1)
     *
     * @param x        the position along x-axis
     * @param y        the position along y-axis
     * @param rotation the rotation angle in radians
     * @return the rotated point position on the y-axis
     */

    public static float rotateY(float x, float y, float rotation) {
        float cos = cos(rotation);
        float sin = sin(rotation);
        return x * sin + y * cos;
    }
}
