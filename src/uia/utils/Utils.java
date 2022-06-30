package uia.utils;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

import static java.lang.Math.*;

/**
 * Collection of utilities
 */

public final class Utils {
    public final static float PI = (float) java.lang.Math.PI;
    public final static float TWO_PI = 2 * PI;
    public final static float HALF_PI = PI / 2f;

    public final static float DEG_TO_RAD = PI / 180.0f;
    public final static float RAD_TO_DEG = 1f / DEG_TO_RAD; //180.0f / PI;

    private static final String P_SPACE = Pattern.quote(" ");

    private Utils() {
        // this object must not be initialized
    }

    /*
     *
     * IO operations
     *
     */

    /**
     * Read a file
     *
     * @param path the file path
     * @return if file exists, its content, otherwise null
     */

    public static String read(String path) {
        if (path != null) {
            StringBuilder out = new StringBuilder(10000);

            try (BufferedReader Reader = new BufferedReader(new FileReader(path))) {
                String line;

                while ((line = Reader.readLine()) != null) {
                    out.append(line).append("\n");
                }

                return out
                        .delete(out.length() - 1, out.length())
                        .toString();
            } catch (IOException ignored) {
            }
        }

        return null;
    }

    /**
     * Read the lines of the given file
     *
     * @param path the file path
     * @return if file exists, its lines, otherwise null
     */

    public static List<String> readLines(String path) {
        if (path != null) {

            try (BufferedReader Reader = new BufferedReader(new FileReader(path))) {
                List<String> out = new ArrayList<>(1000);

                String line;
                while ((line = Reader.readLine()) != null) {
                    out.add(line);
                }

                return out;
            } catch (IOException ignored) {
            }
        }

        return null;
    }

    /**
     * Write the given data on disk
     *
     * @param path the file path
     * @param data the data to write on disk
     * @return true if the file has been correctly written
     */

    public static boolean write(String path, Object data) {
        if (path != null && data != null) {

            try (PrintWriter writer = new PrintWriter(path)) {
                writer.print(data);
                writer.flush();
                return true;
            } catch (FileNotFoundException ignored) {
            }
        }

        return false;
    }

    /**
     * Write an array on disk.
     * <br>
     * Note that every element will be written on a new line.
     *
     * @param path  the file path
     * @param array an array to write on disk
     * @return true if the file has been correctly written
     */

    public static boolean write(String path, Object[] array) {
        if (path != null && array != null) {

            try (PrintWriter writer = new PrintWriter(path)) {

                for (Object i : array) {
                    writer.println(i.toString());
                }

                writer.flush();

                return true;
            } catch (FileNotFoundException ignored) {
            }
        }

        return false;
    }

    /*
     *
     * Colliders
     *
     */

    /**
     * @return true if the given points intersect
     */

    public static boolean AABB(float px1, float py1, float dx1, float dy1,
                               float px2, float py2, float dx2, float dy2) {
        return abs(px1 - px2) <= (dx1 + dx2) / 2f && abs(py1 - py2) <= (dy1 + dy2) / 2f;
    }

    /*
     *
     * Math
     *
     */

    /**
     * Euclidean MCD
     */

    public static int mcd(int x, int y) {
        int a = x;
        int b = y;
        int r;

        while (b != 0) {
            r = a % b;
            a = b;
            b = r;
        }

        return abs(a);
    }

    /**
     * Euclidean MCD
     */

    public static long mcd(long x, long y) {
        long a = x;
        long b = y;
        long r;

        while (b != 0) {
            r = a % b;
            a = b;
            b = r;
        }

        return abs(a);
    }

    /**
     * Converts a float to a fraction
     *
     * @return a number presented as a fraction
     */

    public static String toFraction(float w) {
        // removes a possible exponential
        String out = String.format("%f", w);

        // removes the useless zeros at the end of the number
        int k = out.length() - 1;
        while (k >= 0 && out.charAt(k) == '0') k--;

        // recalculates the string
        out = out.substring(0, k + 1);

        // computes the fraction
        int i = out.indexOf(",");
        int e = (i == -1) ? 0 : (out.length() - i - 1);

        long b = (long) pow(10, e);
        long a = (long) (w * b);
        long mcd = mcd(a, b);

        return (a / mcd) + "/" + (b / mcd);
    }

    /**
     * Converts a double to a fraction
     *
     * @return a number presented as a fraction
     */

    public static String toFraction(double w) {
        // removes a possible exponential
        String out = String.format("%.16f", w);

        // removes the useless zeros at the end of the number
        int k = out.length() - 1;
        while (k >= 0 && out.charAt(k) == '0') k--;

        // recalculates the string
        out = out.substring(0, k + 1);

        // computes the fraction
        int i = out.indexOf(",");
        int e = (i == -1) ? 0 : (out.length() - i - 1);

        long b = (long) pow(10, e);
        long a = (long) (w * b);
        long mcd = mcd(a, b);

        return (a / mcd) + "/" + (b / mcd);
    }

    /**
     * Converts a string number representation into a float
     */

    public static float toFloat(String f) {
        if (f.contains("/")) {
            String[] token = f.split(Pattern.quote("/"));

            if (token.length == 2)
                return Float.parseFloat(token[0]) / Float.parseFloat(token[1]);
        }

        return Float.parseFloat(f);
    }

    /**
     * Converts a string number representation into a double
     */

    public static double toDouble(String f) {
        if (f.contains("/")) {
            String[] token = f.split(Pattern.quote("/"));

            if (token.length == 2)
                return Double.parseDouble(token[0]) / Double.parseDouble(token[1]);
        }

        return Double.parseDouble(f);
    }

    /**
     * Maps a given value into a new one
     */

    public static short map(short value, short istart, short istop, short ostart, short ostop) {
        return (short) (ostart + (ostop - ostart) * ((value - istart) / (istop - istart)));
    }

    /**
     * Maps a given value into a new one
     */

    public static int map(int value, int istart, int istop, int ostart, int ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }

    /**
     * Maps a given value into a new one
     */

    public static long map(long value, long istart, long istop, long ostart, long ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }

    /**
     * Maps a given value into a new one
     */

    public static float map(float value, float istart, float istop, float ostart, float ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }

    /**
     * Maps a given value into a new one
     */

    public static double map(double value, double istart, double istop, double ostart, double ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }

    /**
     * Square a given number
     */

    public static short sq(short a) {
        return (short) (a * a);
    }

    /**
     * Square a given number
     */

    public static int sq(int a) {
        return a * a;
    }

    /**
     * Square a given number
     */

    public static long sq(long a) {
        return a * a;
    }

    /**
     * Square a given number
     */

    public static float sq(float a) {
        return a * a;
    }

    /**
     * Square a given number
     */

    public static double sq(double a) {
        return a * a;
    }

    /**
     * Constrains a value between a lower and upper bound
     */

    public static short constrain(short val, short low, short high) {
        return (short) (val < low ? low : min(val, high));
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

    public static long constrain(long val, long low, long high) {
        return val < low ? low : min(val, high);
    }

    /**
     * Constrains a value between a lower and upper bound
     */

    public static float constrain(float val, float low, float high) {
        return val < low ? low : min(val, high);
    }

    /**
     * Constrains a value between a lower and upper bound
     */

    public static double constrain(double val, double low, double high) {
        return val < low ? low : min(val, high);
    }

    /**
     * MinMax normalization
     *
     * @param val the value to normalize
     * @param min the minimum value
     * @param max the maximum value
     * @return a number between [0, 1]
     */

    public static float norm(short val, short min, short max) {
        return (float) (val - min) / (max - min);
    }

    /**
     * MinMax normalization
     *
     * @param val the value to normalize
     * @param min the minimum value
     * @param max the maximum value
     * @return a number between [0, 1]
     */

    public static float norm(int val, int min, int max) {
        return (float) (val - min) / (max - min);
    }

    /**
     * MinMax normalization
     *
     * @param val the value to normalize
     * @param min the minimum value
     * @param max the maximum value
     * @return a number between [0, 1]
     */

    public static double norm(long val, long min, long max) {
        return (double) (val - min) / (max - min);
    }

    /**
     * MinMax normalization
     *
     * @param val the value to normalize
     * @param min the minimum value
     * @param max the maximum value
     * @return a number between [0, 1]
     */

    public static float norm(float val, float min, float max) {
        return (val - min) / (max - min);
    }

    /**
     * MinMax normalization
     *
     * @param val the value to normalize
     * @param min the minimum value
     * @param max the maximum value
     * @return a number between [0, 1]
     */

    public static double norm(double val, double min, double max) {
        return (val - min) / (max - min);
    }

    /**
     * Calculates the euclidean distance between two points
     *
     * @param x1 the first point position along x-axis
     * @param y1 the first point position along y-axis
     * @param x2 the second point position along x-axis
     * @param y2 the second point position along y-axis
     * @return the euclidean distance
     */

    public static float dist(short x1, short y1, short x2, short y2) {
        return (float) sqrt(sq(x2 - x1) + sq(y2 - y1));
    }

    /**
     * Calculates the euclidean distance between two points
     *
     * @param x1 the first point position along x-axis
     * @param y1 the first point position along y-axis
     * @param x2 the second point position along x-axis
     * @param y2 the second point position along y-axis
     * @return the euclidean distance
     */

    public static float dist(int x1, int y1, int x2, int y2) {
        return (float) sqrt(sq(x2 - x1) + sq(y2 - y1));
    }

    /**
     * Calculates the euclidean distance between two points
     *
     * @param x1 the first point position along x-axis
     * @param y1 the first point position along y-axis
     * @param x2 the second point position along x-axis
     * @param y2 the second point position along y-axis
     * @return the euclidean distance
     */

    public static double dist(long x1, long y1, long x2, long y2) {
        return sqrt(sq(x2 - x1) + sq(y2 - y1));
    }

    /**
     * Calculates the euclidean distance between two points
     *
     * @param x1 the first point position along x-axis
     * @param y1 the first point position along y-axis
     * @param x2 the second point position along x-axis
     * @param y2 the second point position along y-axis
     * @return the euclidean distance
     */

    public static float dist(float x1, float y1, float x2, float y2) {
        return (float) sqrt(sq(x2 - x1) + sq(y2 - y1));
    }

    /**
     * Calculates the euclidean distance between two points
     *
     * @param x1 the first point position along x-axis
     * @param y1 the first point position along y-axis
     * @param x2 the second point position along x-axis
     * @param y2 the second point position along y-axis
     * @return the euclidean distance
     */

    public static double dist(double x1, double y1, double x2, double y2) {
        return sqrt(sq(x2 - x1) + sq(y2 - y1));
    }

    /**
     * Linear interpolation of a mono-dimensional point
     *
     * @param val a value between [0,1]
     * @param x1  the position of the first (minimum) point
     * @param x2  the position of the second (maximum) point
     * @return a new point between [x1, x2]
     */

    public static short lerp(float val, short x1, short x2) {
        return (short) (x1 + val * (x2 - x1));
    }

    /**
     * Linear interpolation of a mono-dimensional point
     *
     * @param val a value between [0,1]
     * @param x1  the position of the first (minimum) point
     * @param x2  the position of the second (maximum) point
     * @return a new point between [x1, x2]
     */

    public static int lerp(float val, int x1, int x2) {
        return (int) (x1 + val * (x2 - x1));
    }

    /**
     * Linear interpolation of a mono-dimensional point
     *
     * @param val a value between [0,1]
     * @param x1  the position of the first (minimum) point
     * @param x2  the position of the second (maximum) point
     * @return a new point between [x1, x2]
     */

    public static long lerp(double val, long x1, long x2) {
        return (long) (x1 + val * (x2 - x1));
    }

    /**
     * Linear interpolation of a mono-dimensional point
     *
     * @param val a value between [0,1]
     * @param x1  the position of the first (minimum) point
     * @param x2  the position of the second (maximum) point
     * @return a new point between [x1, x2]
     */

    public static float lerp(float val, float x1, float x2) {
        return x1 + val * (x2 - x1);
    }

    /**
     * Linear interpolation of a mono-dimensional point
     *
     * @param val a value between [0,1]
     * @param x1  the position of the first (minimum) point
     * @param x2  the position of the second (maximum) point
     * @return a new point between [x1, x2]
     */

    public static double lerp(double val, double x1, double x2) {
        return x1 + val * (x2 - x1);
    }

    /**
     * Pre-computed sin function
     */

    public static float psin(float angle) {
        return TrigTable.sin(angle);
    }

    /**
     * Pre-computed cos function
     */

    public static float pcos(float angle) {
        return TrigTable.cos(angle);
    }

    /**
     * Converts degrees into radians
     */

    public static float radians(float degrees) {
        return degrees * DEG_TO_RAD;
    }

    /**
     * Converts degrees into radians
     */

    public static double radians(double degrees) {
        return degrees * DEG_TO_RAD;
    }

    /*
     *
     * Colors
     *
     */

    /**
     * Calculates the contrast color
     *
     * @param color the color used to calculate the contrast
     * @return the contrast color
     */

    public static Color contrast(Color color) {
        double lum = ((0.299 * color.getRed()) + (0.587 * color.getGreen()) + (0.114 * color.getBlue())) / 255;
        return lum > 0.5d ? Color.BLACK : Color.WHITE;
    }

    /**
     * Linear interpolation of two colors
     *
     * @param val a value between [0,1]
     * @param c1  the first color
     * @param c2  the second color
     * @return a new color between [c1, c2]
     */

    public static Color lerp(float val, Color c1, Color c2) {
        return new Color(
                lerp(val, min(c1.getRed(), c2.getRed()), max(c1.getRed(), c2.getRed())),
                lerp(val, min(c1.getGreen(), c2.getGreen()), max(c1.getGreen(), c2.getGreen())),
                lerp(val, min(c1.getBlue(), c2.getBlue()), max(c1.getBlue(), c2.getBlue())),
                lerp(val, min(c1.getAlpha(), c2.getAlpha()), max(c1.getAlpha(), c2.getAlpha()))
        );
    }

    /**
     * @return true if the given colors are equal
     */

    public static boolean equals(Color a, Color b) {
        return a.getRed() == b.getRed() &&
                a.getGreen() == b.getGreen() &&
                a.getBlue() == b.getBlue() &&
                a.getAlpha() == b.getAlpha();
    }

    /*
     *
     * Arrays & lists
     *
     */

    /**
     * Resizes the given array
     *
     * @param array   the array to resize
     * @param newSize the new length of the array
     * @return a new resized array filled with the elements of the old one
     */

    public static char[] resize(char[] array, int newSize) {
        char[] out = new char[newSize];
        System.arraycopy(array, 0, out, 0, min(array.length, newSize));
        return out;
    }

    /**
     * Resizes the given array
     *
     * @param array   the array to resize
     * @param newSize the new length of the array
     * @return a new resized array filled with the elements of the old one
     */

    public static byte[] resize(byte[] array, int newSize) {
        byte[] out = new byte[newSize];
        System.arraycopy(array, 0, out, 0, min(array.length, newSize));
        return out;
    }

    /**
     * Resizes the given array
     *
     * @param array   the array to resize
     * @param newSize the new length of the array
     * @return a new resized array filled with the elements of the old one
     */

    public static boolean[] resize(boolean[] array, int newSize) {
        boolean[] out = new boolean[newSize];
        System.arraycopy(array, 0, out, 0, min(array.length, newSize));
        return out;
    }

    /**
     * Resizes the given array
     *
     * @param array   the array to resize
     * @param newSize the new length of the array
     * @return a new resized array filled with the elements of the old one
     */

    public static short[] resize(short[] array, int newSize) {
        short[] out = new short[newSize];
        System.arraycopy(array, 0, out, 0, min(array.length, newSize));
        return out;
    }

    /**
     * Resizes the given array
     *
     * @param array   the array to resize
     * @param newSize the new length of the array
     * @return a new resized array filled with the elements of the old one
     */

    public static int[] resize(int[] array, int newSize) {
        int[] out = new int[newSize];
        System.arraycopy(array, 0, out, 0, min(array.length, newSize));
        return out;
    }

    /**
     * Resizes the given array
     *
     * @param array   the array to resize
     * @param newSize the new length of the array
     * @return a new resized array filled with the elements of the old one
     */

    public static long[] resize(long[] array, int newSize) {
        long[] out = new long[newSize];
        System.arraycopy(array, 0, out, 0, min(array.length, newSize));
        return out;
    }

    /**
     * Resizes the given array
     *
     * @param array   the array to resize
     * @param newSize the new length of the array
     * @return a new resized array filled with the elements of the old one
     */

    public static float[] resize(float[] array, int newSize) {
        float[] out = new float[newSize];
        System.arraycopy(array, 0, out, 0, min(array.length, newSize));
        return out;
    }

    /**
     * Resizes the given array
     *
     * @param array   the array to resize
     * @param newSize the new length of the array
     * @return a new resized array filled with the elements of the old one
     */

    public static double[] resize(double[] array, int newSize) {
        double[] out = new double[newSize];
        System.arraycopy(array, 0, out, 0, min(array.length, newSize));
        return out;
    }

    /**
     * Appends the second array at the end of the first one.
     * Time complexity: T(n) = Theta(n)
     * Space complexity: O(n)
     *
     * @param array1 the first array
     * @param off1   the start position of the first array
     * @param len1   the length to copy of the first array
     * @param array2 the second array
     * @param off2   the start position of the second array
     * @param len2   the length to copy of the second array
     * @return a new array filled with array1 + array2
     */

    public static short[] merge(short[] array1, int off1, int len1,
                                short[] array2, int off2, int len2) {
        short[] out = new short[len1 + len2];

        for (int i = 0; i < len1; i++) {
            out[i] = array1[off1 + i];
        }

        for (int i = 0; i < len2; i++) {
            out[len1 + i] = array2[off2 + i];
        }

        return out;
    }

    /**
     * Appends the second array at the end of the first one.
     * Time complexity: T(n) = Theta(n)
     * Space complexity: O(n)
     *
     * @param array1 the first array
     * @param off1   the start position of the first array
     * @param len1   the length to copy of the first array
     * @param array2 the second array
     * @param off2   the start position of the second array
     * @param len2   the length to copy of the second array
     * @return a new array filled with array1 + array2
     */

    public static int[] merge(int[] array1, int off1, int len1,
                              int[] array2, int off2, int len2) {
        int[] out = new int[len1 + len2];

        for (int i = 0; i < len1; i++) {
            out[i] = array1[off1 + i];
        }

        for (int i = 0; i < len2; i++) {
            out[len1 + i] = array2[off2 + i];
        }

        return out;
    }

    /**
     * Appends the second array at the end of the first one.
     * Time complexity: T(n) = Theta(n)
     * Space complexity: O(n)
     *
     * @param array1 the first array
     * @param off1   the start position of the first array
     * @param len1   the length to copy of the first array
     * @param array2 the second array
     * @param off2   the start position of the second array
     * @param len2   the length to copy of the second array
     * @return a new array filled with array1 + array2
     */

    public static long[] merge(long[] array1, int off1, int len1,
                               long[] array2, int off2, int len2) {
        long[] out = new long[len1 + len2];

        for (int i = 0; i < len1; i++) {
            out[i] = array1[off1 + i];
        }

        for (int i = 0; i < len2; i++) {
            out[len1 + i] = array2[off2 + i];
        }

        return out;
    }

    /**
     * Appends the second array at the end of the first one.
     * Time complexity: T(n) = Theta(n)
     * Space complexity: O(n)
     *
     * @param array1 the first array
     * @param off1   the start position of the first array
     * @param len1   the length to copy of the first array
     * @param array2 the second array
     * @param off2   the start position of the second array
     * @param len2   the length to copy of the second array
     * @return a new array filled with array1 + array2
     */

    public static char[] merge(char[] array1, int off1, int len1,
                               char[] array2, int off2, int len2) {
        char[] out = new char[len1 + len2];

        for (int i = 0; i < len1; i++) {
            out[i] = array1[off1 + i];
        }

        for (int i = 0; i < len2; i++) {
            out[len1 + i] = array2[off2 + i];
        }

        return out;
    }

    /**
     * Appends the second array at the end of the first one.
     * Time complexity: T(n) = Theta(n)
     * Space complexity: O(n)
     *
     * @param array1 the first array
     * @param off1   the start position of the first array
     * @param len1   the length to copy of the first array
     * @param array2 the second array
     * @param off2   the start position of the second array
     * @param len2   the length to copy of the second array
     * @return a new array filled with array1 + array2
     */

    public static float[] merge(float[] array1, int off1, int len1,
                                float[] array2, int off2, int len2) {
        float[] out = new float[len1 + len2];

        for (int i = 0; i < len1; i++) {
            out[i] = array1[off1 + i];
        }

        for (int i = 0; i < len2; i++) {
            out[len1 + i] = array2[off2 + i];
        }

        return out;
    }

    /**
     * Appends the second array at the end of the first one.
     * Time complexity: T(n) = Theta(n)
     * Space complexity: O(n)
     *
     * @param array1 the first array
     * @param off1   the start position of the first array
     * @param len1   the length to copy of the first array
     * @param array2 the second array
     * @param off2   the start position of the second array
     * @param len2   the length to copy of the second array
     * @return a new array filled with array1 + array2
     */

    public static double[] merge(double[] array1, int off1, int len1,
                                 double[] array2, int off2, int len2) {
        double[] out = new double[len1 + len2];

        for (int i = 0; i < len1; i++) {
            out[i] = array1[off1 + i];
        }

        for (int i = 0; i < len2; i++) {
            out[len1 + i] = array2[off2 + i];
        }

        return out;
    }

    /**
     * Appends the second array at the end of the first one.
     * Time complexity: T(n) = Theta(n)
     * Space complexity: O(n)
     *
     * @param array1 the first array
     * @param off1   the start position of the first array
     * @param len1   the length to copy of the first array
     * @param array2 the second array
     * @param off2   the start position of the second array
     * @param len2   the length to copy of the second array
     * @return a new array filled with array1 + array2
     */

    public static boolean[] merge(boolean[] array1, int off1, int len1,
                                  boolean[] array2, int off2, int len2) {
        boolean[] out = new boolean[len1 + len2];

        for (int i = 0; i < len1; i++) {
            out[i] = array1[off1 + i];
        }

        for (int i = 0; i < len2; i++) {
            out[len1 + i] = array2[off2 + i];
        }

        return out;
    }

    /**
     * Appends the second array at the end of the first one.
     * Time complexity: T(n) = Theta(n)
     * Space complexity: O(n)
     *
     * @param array1 the first array
     * @param off1   the start position of the first array
     * @param len1   the length to copy of the first array
     * @param array2 the second array
     * @param off2   the start position of the second array
     * @param len2   the length to copy of the second array
     * @return a new array filled with array1 + array2
     */

    public static byte[] merge(byte[] array1, int off1, int len1,
                               byte[] array2, int off2, int len2) {
        byte[] out = new byte[len1 + len2];

        for (int i = 0; i < len1; i++) {
            out[i] = array1[off1 + i];
        }

        for (int i = 0; i < len2; i++) {
            out[len1 + i] = array2[off2 + i];
        }

        return out;
    }

    /**
     * Appends the second array at the end of the first one.
     * Time complexity: T(n) = Theta(n)
     * Space complexity: O(n)
     *
     * @param array1 the first array
     * @param off1   the start position of the first array
     * @param len1   the length to copy of the first array
     * @param array2 the second array
     * @param off2   the start position of the second array
     * @param len2   the length to copy of the second array
     * @return a new array filled with array1 + array2
     */

    @SuppressWarnings("unchecked")
    public static <T> T[] merge(T[] array1, int off1, int len1,
                                T[] array2, int off2, int len2) {
        T[] out = (T[]) new Object[len1 + len2];

        for (int i = 0; i < len1; i++) {
            out[i] = array1[off1 + i];
        }

        for (int i = 0; i < len2; i++) {
            out[len1 + i] = array2[off2 + i];
        }

        return out;
    }

    /**
     * Extracts a random element from a given array
     *
     * @param array a non-null array
     * @return if possible an array's element otherwise null
     */

    public static Object extract(Object[] array) {
        return (array == null || array.length == 0) ? null : array[(int) (array.length * random())];
    }

    /**
     * Checks if the first object is equal to the second one
     *
     * @param a a non-null object
     * @param b a non-null object
     * @return the first object if it is equal to the second one otherwise null
     * @throws NullPointerException if {@code a == null || b == null}
     */

    public static <T> T equals(T a, T b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        return a.equals(b) ? a : null;
    }

    /**
     * Checks if the array contains at least one object equal to one of his elements
     *
     * @param input a non-null object to search
     * @param array a non-null array
     * @return the first array's element that matches the input object otherwise null
     * @throws NullPointerException if {@code input == null || array == null}
     */

    public static <T> T equals(T input, T[] array) {
        Objects.requireNonNull(input);
        Objects.requireNonNull(array);

        for (T i : array) {

            if (input.equals(i))
                return i;
        }

        return null;
    }

    /**
     * Returns the first element contained inside both arrays
     *
     * @param array1 a non-null array
     * @param array2 a non-null array
     * @return if exists the first element in common otherwise null
     * @throws NullPointerException if {@code array1 == null || array2 == null}
     */

    public static <T> T getFirstContained(T[] array1, T[] array2) {
        Objects.requireNonNull(array1);
        Objects.requireNonNull(array2);

        if (array1.length > 0 && array2.length > 0) {

            for (T i : array1) {

                for (T l : array2) {

                    if (i.equals(l))
                        return i;
                }
            }
        }

        return null;
    }

    /**
     * Checks if the given string is a number
     *
     * @param input a non-null string to control
     * @return true if the given string is a number
     */

    public static boolean isNumber(String input) {
        if (input == null) {
            return false;
        }

        try {
            toFloat(input);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    /**
     * Extracts the numbers inside a given string
     * <p>
     * ie:
     * 100   -> ok
     * e100  -> not a number
     * 100e  -> not a number
     * e100e -> not a number
     *
     * @param input  a non-null string
     * @param length a value {@code >= 0} used to indicate the max amount of numbers to return
     * @return a new array filled with the found numbers
     */

    public static double[] getNumbers(String input, int length) {
        int k = 0;
        double[] out = new double[length];

        if (input != null) {
            String[] token = input.split(P_SPACE);

            int i = 0;
            while (i < token.length && k < length) {
                String str = token[i];

                if (isNumber(str))
                    out[k++] = toDouble(str);

                i++;
            }
        }

        return resize(out, k);
    }

    /**
     * Removes a specified range of indices from the given list in linear time
     *
     * @param list    a non-null list
     * @param indices a non-null array of indices
     */

    @SuppressWarnings("unchecked")
    public static <T> void removeRange(List<T> list, int[] indices) {
        if (list != null && list.size() > 0
                && indices != null && indices.length > 0) {
            int size = list.size();
            T[] array = ((T[]) list.toArray()).clone();

            for (int i : indices) {

                if (i >= 0 && i < size)
                    array[i] = null;
            }

            list.clear();

            for (T i : array) {

                if (i != null)
                    list.add(i);
            }
        }
    }

    /**
     * Removes an array from a given list in linear time
     *
     * @param list  a non-null {@link List}
     * @param array a non-null array of objects to remove
     */

    public static <T> void removeRange(List<T> list, T[] array) {
        removeRange(list, getCommon(list, array));
    }

    /**
     * Splits a string according to the given regex
     *
     * @param input a non-null string to split
     * @param regex a non-null array filled with regexes
     * @return a non-null list filled with the string parts
     */

    public static List<String> asList(String input, String[] regex) {
        List<String> out = new ArrayList<>();

        if (input != null && regex != null) {
            out.add(input);

            if (regex.length > 0) {
                String str = input;

                for (String s : regex) {
                    str = str.replace(s, regex[0]);
                }

                out.clear();
                out.addAll(Arrays.asList(str.split(Pattern.quote(regex[0]))));
            }
        }

        return out;
    }

    /**
     * Swaps the given list elements
     *
     * @param list a non-null list
     * @param i    the position of the first element to swap
     * @param j    the position of the second element to swap
     */

    public static <T> void swap(List<T> list, int i, int j) {
        if (list != null) {
            T temp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, temp);
        }
    }

    /**
     * Swaps the given list's elements
     *
     * @param list a valid list
     * @param i    the first element to swap
     * @param j    the second element to swap
     */

    public static <T> void swap(List<T> list, T i, T j) {
        swap(list, list.indexOf(i), list.indexOf(j));
    }

    /**
     * Removes all numbers from the given list
     *
     * @param list a non-null list
     */

    public static <T> void removeNumbers(List<T> list) {
        if (list != null) {
            int count = 0;
            int size = list.size();
            int[] indices = new int[size];

            for (int i = 0; i < size; i++) {

                if (isNumber(list.get(i).toString()))
                    indices[count++] = i;
            }

            if (count > 0)
                removeRange(list, indices);
        }
    }

    /**
     * Removes all the elements which toString() method doesn't match the interval = [minLength, maxLength]
     *
     * @param list      a non-null list
     * @param minLength the minimum element length
     * @param maxLength the maximum element length
     */

    public static <T> void remove(List<T> list, int minLength, int maxLength) {
        if (list != null) {
            int count = 0;
            int size = list.size();
            int[] indices = new int[size];

            for (int i = 0; i < size; i++) {
                int length = list.get(i).toString().length();

                if (length < minLength || length > maxLength)
                    indices[count++] = i;
            }

            if (count > 0)
                removeRange(list, indices);
        }
    }

    /**
     * Counts how many elements match the interval = [minLength, maxLength]
     *
     * @param list      a non-null list
     * @param minLength the minimum element length
     * @param maxLength the maximum element length
     * @return the number of elements that match the interval
     */

    public static <T> int size(List<T> list, int minLength, int maxLength) {
        int count = 0;

        if (list != null) {

            for (T t : list) {
                int length = t.toString().length();
                if (length >= minLength && length <= maxLength)
                    count++;
            }
        }

        return count;
    }

    /**
     * Returns the first array element that matches with a given list element
     *
     * @param list  a non-null list
     * @param array a non-null array
     * @return a list element otherwise null
     * @throws NullPointerException if {@code list == null || array == null}
     */

    public static <T> T getFirstContained(List<T> list, T[] array) {
        Objects.requireNonNull(list);
        Objects.requireNonNull(array);

        for (T i : array) {

            if (list.contains(i))
                return i;
        }

        return null;
    }

    /**
     * Returns the indices of the common elements
     *
     * @param list  a non-null list
     * @param array a non-null array
     * @return a new array filled with the indices of the common elements
     * @throws NullPointerException if {@code list == null || array == null}
     */

    public static <T> int[] getCommon(List<T> list, T[] array) {
        Objects.requireNonNull(list);
        Objects.requireNonNull(array);

        int size = 0;

        for (T i : array) {

            if (list.contains(i))
                size++;
        }

        int k = 0;
        int[] out = new int[size];

        for (int i = 0; i < array.length; i++) {

            if (list.contains(array[i]))
                out[k++] = i;
        }

        return out;
    }

    /**
     * Returns the first array's index that equals the given object
     *
     * @param array a non-null array
     * @param t     an object to search into array
     * @return an array's index or -1 if the t doesn't match
     * @throws NullPointerException if {@code array == null || t == null}
     */

    public static <T> int indexOf(T[] array, T t) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(t);

        for (int i = 0; i < array.length; i++) {

            if (array[i].equals(t))
                return i;
        }

        return -1;
    }

    /**
     * Returns the last array's index that equals the given object
     *
     * @param array a non-null array
     * @param t     an object to search into array
     * @return an array's index or -1 if the t doesn't match
     * @throws NullPointerException if {@code array == null || t == null}
     */

    public static <T> int lastIndexOf(T[] array, T t) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(t);

        for (int i = array.length - 1; i >= 0; i--) {

            if (array[i].equals(t))
                return i;
        }

        return -1;
    }

    /**
     * Returns the numbers contained in the given list
     *
     * @param list a non-null list
     * @return a new array filled with the found numbers
     */

    public static <T> double[] getNumbers(List<T> list) {
        int size = 0;
        double[] out = new double[(list == null) ? 0 : list.size()];

        for (int i = 0; i < out.length; i++) {
            String str = list.get(i).toString();

            if (isNumber(str))
                out[size++] = toDouble(str);
        }

        return resize(out, size);
    }

    /**
     * Checks if the given list contains all the array elements
     *
     * @param list  a non-null list
     * @param array a non-null array
     * @return true if all array's elements are contained inside the given list
     * @throws NullPointerException if {@code list == null || array == null}
     */

    public static <T> boolean containsAll(List<T> list, T[] array) {
        Objects.requireNonNull(list);
        Objects.requireNonNull(array);

        if (list.size() == 0) return false;

        // Just because an empty set in contained into a non-empty one
        if (array.length == 0) return true;

        for (T i : array) {

            if (!list.contains(i))
                return false;
        }

        return true;
    }

    /**
     * Checks if a list contains at least one array's element
     *
     * @param list  a non-null list
     * @param array a non-null array
     * @return true if at least one array's element is contained inside the given list
     * @throws NullPointerException if {@code list == null || array == null}
     */

    public static <T> boolean contains(List<T> list, T[] array) {
        Objects.requireNonNull(list);
        Objects.requireNonNull(array);

        if (list.size() == 0) return false;

        // Just because an empty set in contained into a non-empty one
        if (array.length == 0) return true;

        for (T i : array) {

            if (list.contains(i))
                return true;
        }

        return false;
    }

    /**
     * Randomizes elements order of the given list
     *
     * @param list a non-null list
     */

    public static <T> void shuffle(List<T> list) {
        if (list != null && list.size() > 1) {
            Random r = new Random();

            int num = list.size();

            while (num > 1) {
                int value = r.nextInt(num);

                num--;

                swap(list, num, value);
            }
        }
    }

    /*
     *
     * Strings
     *
     */

    /**
     * Counts the occurrences of the given pattern
     *
     * @param input   a string to control
     * @param pattern a pattern to look for
     * @return the count of the pattern's occurrences
     */

    public static int count(String input, String pattern) {
        int count = 0;
        int fromIndex = 0;

        if (input != null && pattern != null) {

            while ((fromIndex = input.indexOf(pattern, fromIndex)) != -1) {
                count++;
                fromIndex++;
            }
        }

        return count;
    }

    /**
     * Builds a new string in linear time
     *
     * @param pattern a string used to concatenate the array's elements
     * @param array   an array used to fill the return string
     * @return a non-null string made of array's elements
     * @throws NullPointerException if {@code pattern == null || array == null}
     */

    public static String asString(String pattern, Object[] array) {
        Objects.requireNonNull(pattern);
        Objects.requireNonNull(array);

        // Return immediately if no element is present inside the given array
        if (array.length == 0) return "";

        int size = 0;
        int index = 0;

        // Calculates the cumulative length of the array's elements
        for (Object i : array) size += i.toString().length();

        // size + (n - 1) * pattern.length()
        // Quite verbose but useful when array is very large
        char[] store = new char[size + (array.length - 1) * pattern.length()];


        char[] tempO = array[0].toString().toCharArray();
        char[] tempP = pattern.toCharArray();

        //First, copy the char arrays of the first element
        for (char l : tempO) store[index++] = l;


        // And then fills the char array with the rest of the given array and the pattern in the correct position
        for (int i = 1; i < array.length; i++) {

            // Copy the pattern string in the correct position
            for (char l : tempP) store[index++] = l;

            // Copy the current array's element in the correct position
            tempO = array[i].toString().toCharArray();
            for (char l : tempO) store[index++] = l;
        }

        return String.valueOf(store);
    }

    /**
     * Creates a new string (in linear time) based on the given list
     *
     * @param pattern a string used to concatenate the array's elements
     * @param list    a list used to fill the return string
     * @return a non-null string made of list's elements
     * @throws NullPointerException if {@code pattern == null || array == null}
     */

    public static <T> String asString(String pattern, List<T> list) {
        Objects.requireNonNull(list);
        return asString(pattern, list.toArray());
    }

    /**
     * Generate a new random string
     *
     * @param maxValue a value {@code > 0} used to set encode; note that for ASCII is 128
     * @param length   the length of the new string
     * @return a new string
     */

    public static String asStringRandom(int maxValue, int length) {
        char[] ret = new char[max(0, length)];

        Random random = new Random();

        for (int i = 0; i < ret.length; i++) {
            ret[i] = (char) (maxValue * random.nextInt());
        }

        return String.valueOf(ret);
    }

    /**
     * Removes numbers and substrings that don't match the bound = [min, max] with min inclusive and max inclusive
     *
     * @param input   a non-null string to purify
     * @param pattern a non-null string used to split the given one
     * @param min     the minimum substring length
     * @param max     the maximum substring length
     * @return a new purified string otherwise null
     */

    public static String normalize(String input, String pattern, int min, int max) {
        if (input != null && pattern != null && min >= 0 && min < max) {
            StringBuilder out = new StringBuilder(input.length());

            String[] array = input.split(Pattern.quote(pattern));

            for (String i : array) {
                int length = i.length();

                if (!isNumber(i) && length >= min && length <= max)
                    out.append(i).append(pattern);
            }

            if (out.length() > 0)
                out.delete(out.length() - pattern.length(), out.length());

            return out.toString();
        }

        return null;
    }

    /**
     * Number's decimal limitation. This method cut the decimals to the specified value
     *
     * @param str a string
     * @param n   a value {@code >= 0} used to indicate the number of decimals
     * @return if the given string is a number, a new formatted number, otherwise null
     */

    public static String decLimit(String str, int n) {
        String out = null;

        if (isNumber(str)) {
            out = str;
            int i = str.indexOf(".");
            if (i != -1 && n >= 0)
                out = str.substring(0, min(i + (n == 0 ? 0 : (n + 1)), str.length()));
        }

        return out;
    }

    /**
     * Transforms a specified char to uppercase
     *
     * @param input a non-null string
     * @param i     the position of the element to change to uppercase
     * @return a new string
     */

    public static String toUpperCase(String input, int i) {
        char[] out = (input != null && i >= 0 && i < input.length()) ? input.toCharArray() : new char[0];

        if (out.length > 0)
            out[i] = Character.toUpperCase(out[i]);

        return String.valueOf(out);
    }

    /**
     * Transforms a specified char to lowercase
     *
     * @param input a non-null string
     * @param i     the position of the element to change to lowercase
     * @return a new string
     */

    public static String toLowerCase(String input, int i) {
        char[] out = (input != null && i >= 0 && i < input.length()) ? input.toCharArray() : new char[0];

        if (out.length > 0)
            out[i] = Character.toLowerCase(out[i]);

        return String.valueOf(out);
    }

    /**
     * Table used to store precomputed cosine and sine values
     */

    private static class TrigTable {
        private static final int SIZE = 2000;
        private static final float PRE = SIZE / TWO_PI;

        private static final float[] X = new float[SIZE];
        private static final float[] Y = new float[SIZE];

        static {
            int index = 0;
            float c = TWO_PI / (SIZE - 1);

            for (float i = 0; i <= TWO_PI; i += c) {
                X[index] = (float) java.lang.Math.cos(i);
                Y[index] = (float) java.lang.Math.sin(i);
                index++;
            }
        }

        public static float cos(float angle) {
            int m = angle < 0 ? SIZE : 0;
            return X[m + (int) (PRE * (angle % TWO_PI))];
        }

        public static float sin(float angle) {
            int m = angle < 0 ? SIZE : 0;
            return Y[m + (int) (PRE * (angle % TWO_PI))];
        }
    }
}
