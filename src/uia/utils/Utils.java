package uia.utils;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

import static java.lang.Math.*;

/**
 * Collection of utilities
 */

public final class Utils {

    private Utils() {
    }

    /*
     * Read & Write operations
     */

    /**
     * Read a file
     *
     * @param path the file path
     * @return if file exists, its content, otherwise null
     */

    public static String read(String path) {
        if (path != null) {
            StringBuilder out = new StringBuilder(2000);

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
                List<String> out = new ArrayList<>(2000);

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
     * Numbers & functions
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
     * Convert a float into a fraction
     *
     * @return a fraction
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
     * Convert a double into a fraction
     *
     * @return a fraction
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
     * Try to convert a String into a float
     *
     * @param f a not null String
     * @return a float initialize with the String value
     * @throws NullPointerException  if {@code f == null}
     * @throws NumberFormatException if the string does not contain a parsable float.
     */

    public static float toFloat(String f) {
        if (f.contains("/")) {
            String[] token = f.split(Pattern.quote("/"));
            if (token.length == 2) return Float.parseFloat(token[0]) / Float.parseFloat(token[1]);
        }
        return Float.parseFloat(f);
    }

    /**
     * Try to convert a String into a double
     *
     * @param f a not null String
     * @return a double initialize with the String value
     * @throws NullPointerException  if {@code f == null}
     * @throws NumberFormatException if the string does not contain a parsable double.
     */

    public static double toDouble(String f) {
        if (f.contains("/")) {
            String[] token = f.split(Pattern.quote("/"));
            if (token.length == 2) return Double.parseDouble(token[0]) / Double.parseDouble(token[1]);
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
     * Calculate the contrast color
     *
     * @param color the color used to calculate the contrast
     * @return a new array filled with the contrast color
     */

    public static int[] contrast(int[] color) {
        double lum = (0.299 * color[0] + 0.587 * color[1] + 0.114 * color[2]) / 255;
        return lum > 0.5d ? new int[]{0, 0, 0} : new int[]{255, 255, 255};
    }

    /*
     * Arrays & lists
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
     * Extract a random element from a given array
     *
     * @param array a not null array
     * @return an array's element otherwise null
     */

    public static <T> T extract(T[] array) {
        return (array == null || array.length == 0) ? null : array[(int) (array.length * random())];
    }

    /**
     * Check if the given String is a number
     *
     * @param input a not null String to control
     * @return true if the given String is a number
     */

    public static boolean isNumber(String input) {
        if (input == null) return false;

        try {
            toFloat(input);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    private static final String P_SPACE = Pattern.quote(" ");

    /**
     * Extract the numbers inside a given String.
     * <p>
     * ie:
     * 100   -> ok
     * e100  -> not a number
     * 100e  -> not a number
     * e100e -> not a number
     *
     * @param input  a not null String
     * @param length a value {@code >= 0} used to indicate the max amount of numbers to extract
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
                if (isNumber(str)) out[k++] = toDouble(str);
                i++;
            }
        }

        return resize(out, k);
    }

    /**
     * Remove the given indices from the given list in linear time
     *
     * @param list    a not null list
     * @param indices a not null array of indices
     */

    @SuppressWarnings("unchecked")
    public static <T> void removeRange(List<T> list, int[] indices) {
        if (list != null && list.size() > 0
                && indices != null && indices.length > 0) {
            int size = list.size();
            T[] array = ((T[]) list.toArray()).clone();

            for (int i : indices) {
                if (i >= 0 && i < size) array[i] = null;
            }

            list.clear();

            for (T i : array) {
                if (i != null) list.add(i);
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
     * Split a String according to the given regex
     *
     * @param input a not null String
     * @param regex a not null array filled with regexes
     * @return a new List filled with the String parts
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
     * Swap two of the given list's elements
     *
     * @param list a not null {@link List}
     * @param i    the position of the first element to swap
     * @param j    the position of the second element to swap
     * @return true if the operation succeed
     */

    public static <T> boolean swap(List<T> list, int i, int j) {
        try {
            T temp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, temp);
            return true;
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Swap two of the given List's elements
     *
     * @param list a not null List
     * @param i    the first element to swap
     * @param j    the second element to swap
     */

    public static <T> void swap(List<T> list, T i, T j) {
        swap(list, list.indexOf(i), list.indexOf(j));
    }

    /**
     * Remove all numbers from the given List
     *
     * @param list a not null List
     */

    public static <T> void removeNumbers(List<T> list) {
        if (list != null) {
            int count = 0;
            int size = list.size();
            int[] indices = new int[size];

            for (int i = 0; i < size; i++) {
                if (isNumber(list.get(i).toString())) indices[count++] = i;
            }

            if (count > 0) removeRange(list, indices);
        }
    }

    /**
     * Remove all the elements which toString() method doesn't match the interval = [minLength, maxLength]
     *
     * @param list      a not null List
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
                if (length < minLength || length > maxLength) indices[count++] = i;
            }

            if (count > 0) removeRange(list, indices);
        }
    }

    /**
     * Return the indices of the common elements
     *
     * @param list  a not null List
     * @param array a not null array
     * @return a new array filled with the indices of the common elements or null
     */

    public static <T> int[] getCommon(List<T> list, T[] array) {
        int[] out = null;

        if (list != null && array != null) {
            int size = 0;

            for (T i : array) {
                if (list.contains(i)) size++;
            }

            int k = 0;
            out = new int[size];

            for (int i = 0; i < array.length; i++) {
                if (list.contains(array[i])) out[k++] = i;
            }
        }

        return out;
    }

    /**
     * Return the numbers inside the given List
     *
     * @param list a not null List
     * @return a new array filled with the found numbers
     */

    public static <T> double[] getNumbers(List<T> list) {
        int size = 0;
        double[] out = new double[(list == null) ? 0 : list.size()];

        for (int i = 0; i < out.length; i++) {
            String str = list.get(i).toString();
            if (isNumber(str)) out[size++] = toDouble(str);
        }

        return resize(out, size);
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
     * Strings
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
     * Count the number of lines of the given file/directory of files
     *
     * @param file a not null {@link File}
     */

    public static int countLines(File file) {
        Stack<File> stack = new Stack<>();
        stack.push(file);

        int out = 0;
        while (!stack.empty()) {
            File pop = stack.pop();
            File[] list = pop.listFiles();

            if (list != null) {

                for (File i : list) {
                    stack.push(i);
                }
            } else {
                out += readLines(pop.getAbsolutePath()).size();
            }
        }

        return out;
    }

    /**
     * Build the array's textual representation.
     * <br>
     * Time required: T(n),
     * <br>
     * Space required: O(n).
     *
     * @param pattern a not null String used to concatenate the array's elements
     * @param array   a not null array
     * @return a String made of array's elements
     */

    public static String toString(String pattern, Object[] array) {
        // Return immediately if no element is present inside the given array
        if (pattern == null || array == null || array.length == 0) return "";

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

        // Then fills the char array with the rest of the given array and the pattern in the correct position
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
     * Build the List's textual representation.
     * <br>
     * Time required: T(n),
     * <br>
     * Space required: O(n).
     *
     * @param pattern a not null String used to concatenate the list's elements
     * @param list    a not null List
     * @return a String made of List's elements
     */

    public static <T> String toString(String pattern, List<T> list) {
        return list == null ? "" : toString(pattern, list.toArray());
    }

    /**
     * Remove numbers and substrings that don't match the bound = [min, max] with min inclusive and max inclusive
     *
     * @param input   a not null String
     * @param pattern a not null String used to split the given input
     * @param min     the minimum substring's length
     * @param max     the maximum substring's length
     * @return a purified String otherwise null
     */

    public static String normalize(String input, String pattern, int min, int max) {
        if (input != null && pattern != null && min >= 0 && min < max) {
            StringBuilder out = new StringBuilder(input.length());

            String[] array = input.split(Pattern.quote(pattern));

            for (String i : array) {
                int length = i.length();
                if (!isNumber(i) && length >= min && length <= max) out.append(i).append(pattern);
            }

            if (out.length() > 0)
                out.delete(out.length() - pattern.length(), out.length());

            return out.toString();
        }

        return null;
    }

    /**
     * Number's decimal limitation. This method cut the decimals to the specified value.
     *
     * @param str a not null String
     * @param n   the number of decimals; it must be {@code >= 0}
     * @return if the given String is a number, a new formatted number, otherwise null
     */

    public static String decLimit(String str, int n) {
        String out = null;

        if (isNumber(str)) {
            out = str;
            int i = str.indexOf(".");
            if (i != -1 && n >= 0) out = str.substring(0, min(i + (n == 0 ? 0 : (n + 1)), str.length()));
        }

        return out;
    }
}
