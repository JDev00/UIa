package uia.utils;

import java.io.*;
import java.util.*;
import java.util.List;

import static java.lang.Math.*;

/**
 * Collection of utilities
 */

public final class Utils {

    private Utils() {
    }

    /**
     * Read the content of the given file
     *
     * @param path a not null file path
     * @return the file's content otherwise an empty String
     */

    public static String readAll(String path) {
        StringBuilder builder = new StringBuilder(2000);

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
            if (builder.length() > 0) builder.deleteCharAt(builder.length() - 1);
        } catch (Exception ignored) {
        }

        return builder.toString();
    }

    /**
     * Read the content of the given file
     *
     * @param path a not null file path
     * @return the file's lines otherwise an empty list
     */

    public static List<String> read(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            List<String> lines = new ArrayList<>(2000);

            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            return lines;
        } catch (Exception e) {
            return Collections.emptyList();
        }
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
     * Write an array on disk.
     * <br>
     * Note that every element will be written on a new line.
     *
     * @param path a not null file path
     * @param data a not null array to write on disk
     * @return true if the file has been written
     */

    public static boolean write(String path, Object... data) {
        try (PrintWriter writer = new PrintWriter(path)) {

            for (Object i : data) {
                writer.println(i.toString());
            }

            writer.flush();

            return true;
        } catch (Exception ignored) {
            return false;
        }
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
}
