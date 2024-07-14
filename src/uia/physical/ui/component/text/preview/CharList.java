package uia.physical.ui.component.text.preview;

import static java.lang.Math.*;

/**
 * CharList is a structure used to easily handle an array of chars.
 * Currently, in testing.
 */

public class CharList {
    private char[] data;

    private int size;
    private final int maxCapacity;
    private final int iCapacity;

    public CharList(int iCapacity, int maxCapacity) {
        data = new char[iCapacity];

        size = 0;
        this.maxCapacity = maxCapacity;
        this.iCapacity = iCapacity;
    }

    public CharList(int iCapacity) {
        this(iCapacity, 500_000);
    }

    /**
     * Resize this structure
     *
     * @param length the new structure's size
     */

    private void resize(int length) {
        if (length > 0) {
            length = min(length, maxCapacity);

            size = min(size, length);

            char[] temp = new char[length];
            if (size > 0) System.arraycopy(data, 0, temp, 0, size);
            data = temp;
        }
    }

    /**
     * Remove all chars and reset this structure but keep the capacity limit
     */

    public void clear() {
        size = 0;
        data = new char[iCapacity];
    }

    /**
     * Add a new char
     *
     * @param i the position in which insert the given char
     * @param a the char to be added
     * @return true if the operation succeeded
     */

    public boolean add(int i, char a) {
        if (i < 0 || i > size) return false;

        if (size == data.length) {
            resize(2 * size + 1);
        }

        for (int k = size; k > i; k--) {
            data[k] = data[k - 1];
        }

        data[i] = a;
        size = min(size + 1, maxCapacity);

        return true;
    }

    /**
     * Add a new array of chars
     *
     * @param i      the index in which insert the array
     * @param a      the array to add
     * @param off    the position of the first char to copy
     * @param length the amount of chars to copy
     * @return true if the operation succeeded
     */

    public boolean add(int i, char[] a, int off, int length) {
        if (a == null
                || i < 0 || i > size
                || off < 0 || off >= a.length
                || length < 0 || length > a.length
                || off + length > a.length) return false;

        if (size + length >= data.length) {
            resize(data.length + length);
        }

        int s = length - 1;
        for (int k = size; k > i; k--) {
            if (k + s < maxCapacity) data[k + s] = data[k - 1];
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
     * Add a new char
     *
     * @param a the char to add
     * @return true if the operation succeeded
     */

    public boolean add(char a) {
        return add(size, a);
    }

    /**
     * Add a new char at the specified indices.
     * <br>
     * Complexity: Theta(n)
     *
     * @param indices an array filled with the positions of the given char
     * @param off     the start array position
     * @param len     the array length
     * @param a       a char to insert
     */

    public boolean add(int[] indices, int off, int len, char a) {
        if (indices == null) return false;

        if (len + size >= data.length) {
            resize(data.length + len + 10);
        }

        int count = 0;
        for (int i = 0; i < len; i++) {
            int j = indices[off + i];
            if (j >= 0 && j < size) count++;
        }

        int s = size - 1;
        int r = count;

        for (int i = len - 1; i >= 0; i--) {
            int j = indices[off + i];

            if (j >= 0 && j < size) {

                for (int k = s; k >= j; k--) {
                    data[k + count] = data[k];
                }

                data[j + count - 1] = a;
                s = j + count - (count + 1);
                count--;
            }
        }

        size += r;

        return true;
    }

    /**
     * Replace a char
     *
     * @param i the position of the char to replace
     * @param a the char used as replacement
     */

    public void set(int i, char a) {
        if (i >= 0 && i < size) data[i] = a;
    }

    /**
     * Remove all the occurrences of a char.
     * <br>
     * Complexity: Theta(n)
     *
     * @param c the char to remove
     */

    public void remove(char c) {
        int i = 0;
        int j = 1;

        while (j < size) {

            if (data[i] != c) i++;

            if (data[i] == c && data[j] != c) {
                data[i] = data[j];
                data[j] = c;
                i++;
            }

            j++;
        }

        // resize only if the last char is equal to c
        if (size > 0 && data[size - 1] == c) {
            size = Math.max(0, size - (j - i));
        }
    }

    /**
     * Remove all chars between the interval [i, j]
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
     * Remove the specified char
     *
     * @return true if the operation succeeded
     */

    public boolean remove(int i) {
        return remove(i, i);
    }

    /**
     * Remove the chars specified by the given array of indices
     *
     * @param off the first element of the given array
     * @param len the indices that must be used
     */

    public boolean remove(int[] indices, int off, int len) {
        if (indices == null || indices.length == 0) return false;

        // Mark the chars to delete
        int count = 0;
        for (int i = 0; i < len; i++) {
            int j = indices[off + i];

            if (j >= 0 && j < size) {
                data[j] = 0;
                count++;
            }
        }

        // remove the marked chars
        if (count > 0) remove((char) 0);

        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(data, 0, size);
    }

    /**
     * @param i position of the char
     * @return the specified char
     */

    public char get(int i) {
        return data[i];
    }

    /**
     * @return the number of chars
     */

    public int size() {
        return size;
    }

    /**
     * @return the maximum amount of storable chars
     */

    public int maxCapacity() {
        return maxCapacity;
    }

    /**
     * @return true if this structure contains the given char
     */

    public boolean contains(char a) {
        for (int i = 0; i < size; i++) {
            if (data[i] == a) return true;
        }
        return false;
    }

    /**
     * Note that this method allow you to directly modify the internal structure.
     * <br>
     * Anyway all the operations on CharList are safe for CharList.
     *
     * @return an array of chars
     */

    public char[] toArray() {
        return data;
    }
}
