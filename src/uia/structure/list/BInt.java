package uia.structure.list;

import static java.lang.Math.*;

/**
 * BInt is a structure developed to easily handle a list of ints
 */

public class BInt {
    protected int[] data;

    private int size;
    private final int maxCapacity;
    private final int iCapacity;

    public BInt(int iCapacity, int maxCapacity) {
        data = new int[iCapacity];

        size = 0;
        this.iCapacity = iCapacity;
        this.maxCapacity = maxCapacity;
    }

    public BInt(int iCapacity) {
        this(iCapacity, 100_000);
    }

    /**
     * Resizes this buffer
     *
     * @param length the new buffer's size
     */

    public final void resize(int length) {
        if (length > 0) {
            length = min(length, maxCapacity);

            size = min(size, length);

            int[] temp = new int[length];

            for (int i = 0; i < size; i++) {
                temp[i] = data[i];
            }

            data = temp;
        }
    }

    /**
     * Remove all ints and reset this structure but keep the capacity limit
     */

    public void clear() {
        size = 0;

        data = null;
        data = new int[iCapacity];
    }

    /**
     * Add a new int
     *
     * @param i the position in which insert the new int
     * @param a the int to be added
     * @return true if the operation succeeded
     */

    public boolean add(int i, int a) {
        if (i < 0 || i > size) return false;

        if (size + 1 == data.length) {
            resize(2 * size);
        }

        for (int k = size; k > i; k--) {
            data[k] = data[k - 1];
        }

        data[i] = a;
        size = min(size + 1, maxCapacity);

        return true;
    }

    /**
     * Add a new array of ints
     *
     * @param i      the index in which insert the array
     * @param a      the array to add
     * @param off    the position of the first int to copy
     * @param length the amount of ints to copy
     */

    public boolean add(int i, int[] a, int off, int length) {
        if (a == null
                || i < 0 || i > size
                || off < 0 || off >= a.length
                || length < 0 || length > a.length
                || off + length > a.length) return false;


        if (size + length >= data.length) {
            resize(size + length);
        }


        int s = length - 1;

        for (int k = size; k > i; k--) {

            if (k + s < maxCapacity) {
                data[k + s] = data[k - 1];
            }
        }


        int k = 0;
        while (k < length && i + k < maxCapacity) {
            data[i + k] = a[off + k];
            size = min(size + 1, maxCapacity);
            k++;
        }

        return true;
    }

    /**
     * Add a new int
     *
     * @param a the int to be added
     * @return true if the operation succeeded
     */

    public boolean add(int a) {
        return add(size, a);
    }

    /**
     * Replace an int
     *
     * @param i the position of the integer to replace
     * @param a the int used as replacement
     */

    public void set(int i, int a) {
        if (i >= 0 && i < size) {
            data[i] = a;
        }
    }

    /**
     * Remove all integers between the interval [i, j]
     *
     * @return true if the operation succeeded
     */

    public boolean remove(int i, int j) {
        if (i < 0 || i > size || i > j || j >= size) return false;

        for (int k = j; k < size - 1; k++) {
            data[i + (k - j)] = data[k + 1];
        }

        size -= (j - i + 1);

        return true;
    }

    /**
     * Remove the specified int
     *
     * @return true if the operation succeeded
     */

    public boolean remove(int i) {
        return remove(i, i);
    }

    /**
     * @param i position of the int
     * @return the specified int
     */

    public int get(int i) {
        return data[i];
    }

    /**
     * @return the number of ints
     */

    public int size() {
        return size;
    }

    /**
     * @return the maximum amount of storable ints
     */

    public int maxCapacity() {
        return maxCapacity;
    }

    /**
     * @return true if this structure contains the given int
     */

    public boolean contains(int a) {
        for (int i = 0; i < size; i++) {

            if (data[i] == a) return true;
        }

        return false;
    }

    /**
     * Note that this method enables you to directly modify the values of BInt.
     * <br>
     * Anyway all the operations on BInt are safe for BInt.
     *
     * @return BInt as an array of ints
     */

    public int[] toArray() {
        return data;
    }
}
