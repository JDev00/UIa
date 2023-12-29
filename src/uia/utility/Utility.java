package uia.utility;

import java.io.*;
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

    // Calendar

    /**
     * @return the current date expressed as an array of three elements:
     * <ul>
     *     <li>day between [1,31];</li>
     *     <li>month between [1,12];></li>
     *     <li>year</li>
     * </ul>
     */

    public static int[] getDate() {
        Calendar c = Calendar.getInstance();
        return new int[]{c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR)};
    }

    /**
     * Given an integer, returns the day of the week, where 0 is monday and 6 is sunday
     *
     * @param i an integer
     * @return the day of the week otherwise -1
     */

    public static int getDay(int i) {
        switch (i) {
            case Calendar.MONDAY:
                return 0;
            case Calendar.TUESDAY:
                return 1;
            case Calendar.WEDNESDAY:
                return 2;
            case Calendar.THURSDAY:
                return 3;
            case Calendar.FRIDAY:
                return 4;
            case Calendar.SATURDAY:
                return 5;
            case Calendar.SUNDAY:
                return 6;
            default:
                return -1;
        }
    }

    /**
     * Check if the given string equals to the given value
     *
     * @param in    a not null String to compare to
     * @param value a value to compare to
     */

    private static boolean equals(String in, int value) {
        return in != null && Integer.parseInt(in) == value;
    }

    /**
     * Check if the given date contains the given day
     *
     * @param date a not null date to control
     * @param day  the day to look for
     * @return true if the date contains the given day
     */

    public static boolean isDay(String date, int day) {
        return date != null && equals(date.substring(0, (date.charAt(1) == '/') ? 1 : 2), day);
    }

    /**
     * Check if the given date contains the given month
     *
     * @param date  a not null date to control
     * @param month the month to look for
     * @return true if the date contains the given month
     */

    public static boolean isMonth(String date, int month) {
        if (date != null) {
            int i = date.indexOf("/") + 1;
            return equals(date.substring(i, i + (date.charAt(i + 1) == '/' ? 1 : 2)), month);
        }
        return false;
    }

    /**
     * Check if the given date contains the given year
     *
     * @param date a not null date to control
     * @param year the year to look for
     * @return true if the date contains the given year
     */

    public static boolean isYear(String date, int year) {
        return date != null && equals(date.substring(date.lastIndexOf("/") + 1), year);
    }
}
