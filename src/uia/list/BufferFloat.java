package uia.structure.list;

import static java.lang.Math.min;

/**
 * Float buffer acts as a container for a set of floats
 */

public class BufferFloat extends Buffer {
    protected float[] data;

    public BufferFloat(int iCapacity) {
        data = new float[iCapacity];
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

            float[] temp = new float[length];

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
     * Adds a new float to this buffer
     *
     * @param i the index in which insert the new float
     * @param a the float to add
     */

    public void add(int i, float a) {
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
     * Adds a new float array to this buffer
     *
     * @param i      the index in which insert the array
     * @param a      the array to add
     * @param off    the position of the first float to copy
     * @param length the amount of floats to copy
     */

    public void add(int i, float[] a, int off, int length) {
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
     * Adds a new float to this buffer
     *
     * @param a the float to add
     */

    public void add(float a) {
        add(size, a);
    }

    /**
     * Replaces a float
     *
     * @param i the buffer position
     * @param a the float used as replacement
     */

    public void set(int i, float a) {
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
     * @param i position of the float
     * @return the specified float
     */

    public float getFloatAt(int i) {
        return data[i];
    }

    @Override
    public int limit() {
        return data.length;
    }

    /**
     * @return true if this buffer contains the given float
     */

    public boolean contains(float a) {
        for (int i = 0; i < size; i++) {

            if (Float.compare(data[i], a) == 0) return true;
        }

        return false;
    }
}
