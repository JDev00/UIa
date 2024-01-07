package uia.utility;

import java.util.*;
import java.util.List;

import static java.lang.Math.*;

/**
 * Collection of utilities
 */

public final class Utility {

    private Utility() {
    }

    /**
     * Merge the given List into a String.
     * <br>
     * Note that the given List won't be modified.
     *
     * @param list         a List to convert into a String
     * @param concatenator a not null String used to concatenate the List's elements
     * @return a new String
     */

    public static <T> String merge(List<T> list, String concatenator) {
        StringBuilder out = new StringBuilder();

        if (list != null && concatenator != null) {
            list.forEach(t -> out.append(t.toString()).append(concatenator));
        }

        return out.toString();
    }

    /**
     * Map a given value into a new one
     */

    public static int map(int value, int istart, int istop, int ostart, int ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }

    /**
     * Map a given value into a new one
     */

    public static float map(float value, float istart, float istop, float ostart, float ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }

    /**
     * Constrain a value between a lower and upper bound
     */

    public static int constrain(int val, int low, int high) {
        return val < low ? low : min(val, high);
    }

    /**
     * Constrain a value between a lower and upper bound
     */

    public static float constrain(float val, float low, float high) {
        return val < low ? low : min(val, high);
    }

    /**
     * Check if the given String is a number
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
     * Cut the decimals to the specified amount
     *
     * @param str a not null String
     * @param n   the number of decimals; it must be {@code >= 0}
     * @return if the given String is a number, a new formatted number, otherwise null
     */

    public static String limitDecimals(String str, int n) {
        String out = null;

        if (isNumber(str)) {
            out = str;
            int i = str.indexOf(".");
            if (i != -1 && n >= 0) out = str.substring(0, min(i + (n == 0 ? 0 : (n + 1)), str.length()));
        }

        return out;
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
}
