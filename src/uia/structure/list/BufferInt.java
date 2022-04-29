package uia.structure.list;

import static java.lang.Math.*;

/**
 * Int buffer acts as a container for a set of ints
 */

public class BufferInt extends Buffer {
    protected int[] data;

    public BufferInt(int iCapacity) {
        data = new int[iCapacity];
    }

    /**
     * Resizes this buffer
     *
     * @param length the new buffer's size
     */

    public final void resize(int length) {
        if (length > 0) {
            length = min(limit, length);

            size = min(size, length);

            int[] temp = new int[length];

            for (int i = 0; i < size; i++) {
                temp[i] = data[i];
            }

            data = temp;
        }
    }

    @Override
    public void clear() {
        size = 0;
    }

    /**
     * Adds a new int to this buffer
     *
     * @param i the index in which insert the new int
     * @param a the int to add
     */

    public void add(int i, int a) {
        if (i < 0 || i > size) return;

        if (size + 1 == data.length) {
            resize(2 * size);
        }

        for (int k = size; k > i; k--) {
            data[k] = data[k - 1];
        }

        data[i] = a;
        size = min(size + 1, limit);
    }

    /**
     * Adds a new int array to this buffer
     *
     * @param i      the index in which insert the array
     * @param a      the array to add
     * @param off    the position of the first int to copy
     * @param length the amount of ints to copy
     */

    public void add(int i, int[] a, int off, int length) {
        if (a == null
                || i < 0 || i > size
                || off < 0 || off >= a.length
                || length < 0 || length > a.length
                || off + length > a.length) return;


        if (size + length >= data.length) {
            resize(size + length);
        }


        int s = length - 1;

        for (int k = size; k > i; k--) {

            if (k + s < limit) {
                data[k + s] = data[k - 1];
            }
        }


        int k = 0;
        while (k < length && i + k < limit) {
            data[i + k] = a[off + k];
            size = min(size + 1, limit);
            k++;
        }
    }

    /**
     * Adds a new int to this buffer
     *
     * @param a the int to add
     */

    public void add(int a) {
        add(size, a);
    }

    /**
     * Replaces an int
     *
     * @param i the buffer position
     * @param a the int used as replacement
     */

    public void set(int i, int a) {
        if (i >= 0 && i < size) {
            data[i] = a;
        }
    }

    @Override
    public void remove(int i) {
        if (i < 0 || i >= size) return;

        for (int k = i; k < size - 1; k++) {
            data[k] = data[k + 1];
        }

        size--;
    }

    @Override
    public void remove(int i, int j) {
        if (i < 0 || i > size || i > j || j >= size) return;

        if (i == j) {
            remove(i);
            return;
        }

        for (int k = j; k < size - 1; k++) {
            data[i + (k - j)] = data[k + 1];
        }

        size -= (j - i + 1);
    }

    @Override
    public void remove(int[] indices, int off, int len) {
        // rewrite
    }

    /**
     * @param i position of the int
     * @return the specified int
     */

    public int getIntAt(int i) {
        return data[i];
    }

    @Override
    public int limit() {
        return data.length;
    }

    /**
     * @return true if this buffer contains the given int
     */

    public boolean contains(int a) {
        for (int i = 0; i < size; i++) {

            if (data[i] == a) return true;
        }

        return false;
    }
}
