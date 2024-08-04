package uia.application.ui.component.text.edit.structure;

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

    public CharList(int initialCapacity, int maxCapacity) {
        data = new char[initialCapacity];

        size = 0;
        this.maxCapacity = maxCapacity;
        this.iCapacity = initialCapacity;
    }

    public CharList(int iCapacity) {
        this(iCapacity, 200_000);
    }

    /**
     * Resizes this list.
     *
     * @param length the new list size
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
     * Remove all chars and reset this list, but keep the capacity limit.
     */

    public void clear() {
        size = 0;
        data = new char[iCapacity];
    }

    /**
     * Adds a new char at the specified position.
     *
     * @param index   the position at which to insert the given char
     * @param newChar the char to be added
     * @return true if the operation succeeded
     */

    public boolean add(int index, char newChar) {
        if (index < 0 || index > size) {
            return false;
        }

        if (size == data.length) {
            resize(2 * size + 1);
        }

        for (int k = size; k > index; k--) {
            data[k] = data[k - 1];
        }

        data[index] = newChar;
        size = min(size + 1, maxCapacity);

        return true;
    }

    /**
     * Adds a new char to the end of this list.
     *
     * @param newChar the char to be added
     * @return true if the operation succeeded
     */

    public boolean add(char newChar) {
        return add(size, newChar);
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
                || off + length > a.length) {
            return false;
        }

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
     * Replaces the specified char.
     *
     * @param index       the position of the char to be replaced
     * @param replacement the char used as replacement
     */

    public void set(int index, char replacement) {
        if (index >= 0 && index < size) {
            data[index] = replacement;
        }
    }

    /**
     * Removes all chars between the interval [i, j].
     *
     * @return true if the operation succeeded
     */

    public boolean remove(int i, int j) {
        if (i < 0 || i > size || i > j || j >= size) {
            return false;
        }

        for (int k = j; k < size - 1; k++) {
            data[i + (k - j)] = data[k + 1];
        }

        size -= (j - i + 1);

        return true;
    }

    /**
     * Removes the specified char from this list.
     *
     * @param index the position of the char to remove
     * @return true if the operation succeeded
     */

    public boolean remove(int index) {
        return remove(index, index);
    }

    @Override
    public String toString() {
        return String.valueOf(data, 0, size);
    }

    /**
     * @param index the position of the char to be retrieved
     * @return the searched char
     * @throws IndexOutOfBoundsException if {@code index < 0 or index >= size()}
     */

    public char get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("'index' is out of bounds [0, " + size + " ]");
        }

        return data[index];
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
     * @param searchedChar the char to look for
     * @return true if the given char is contained in this list
     */

    public boolean contains(char searchedChar) {
        for (int i = 0; i < size; i++) {
            if (data[i] == searchedChar) {
                return true;
            }
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
