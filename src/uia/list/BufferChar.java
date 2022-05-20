package uia.structure.list;

import static java.lang.Math.*;

/**
 * Chars buffer acts as a container for a set of chars
 */

public class BufferChar extends Buffer {
    protected char[] data;

    public BufferChar(int iCapacity) {
        data = new char[iCapacity];
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

            char[] temp = new char[length];

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
     * Adds a new char to this buffer
     *
     * @param i the index in which insert the given char
     * @param a the char to add
     */

    public void add(int i, char a) {
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
     * Adds a new char array to this buffer
     *
     * @param i      the index in which insert the array
     * @param a      the array to add
     * @param off    the position of the first char to copy
     * @param length the amount of chars to copy
     */

    public void add(int i, char[] a, int off, int length) {
        if (a == null
                || i < 0 || i > size
                || off < 0 || off >= a.length
                || length < 0 || length > a.length
                || off + length > a.length) return;


        if (size + length >= data.length) {
            resize(data.length + length);
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
     * Adds a new char to this buffer
     *
     * @param a the char to add
     */

    public void add(char a) {
        add(size, a);
    }

    /**
     * Adds a new char at the specified indices.
     * Complexity: T(n) = Theta(n)
     *
     * @param indices an array filled with the positions of the given char
     * @param off     the start array position
     * @param len     the array length
     * @param a       a char to insert
     */

    public void add(int[] indices, int off, int len, char a) {
        if (indices != null) {

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
        }

        // * *
        //ciaoci
        //1. ciaoci--
        //2. cia--oci
        //3. cia-*oci
        //4. c-ia*oci
        //5. c*ia*oci

        // * *  *
        //ciaociao
        //1. ciaociao---
        //2. ciaoci---ao
        //3. ciaoci--*ao
        //4. cia--oci*ao
        //5. cia-*oci*ao
        //6. c-ia*oci*ao
        //7. c*ia*oci*ao
    }

    /**
     * Replaces a char
     *
     * @param i the buffer position
     * @param a the char used as replacement
     */

    public void set(int i, char a) {
        if (i >= 0 && i < size) {
            data[i] = a;
        }
    }

    /**
     * Removes all the occurrences of a char from this buffer.
     * Complexity: T(n) = Theta(n)
     *
     * @param c the char to remove
     */

    public void remove(char c) {
        int i = 0;
        int j = 1;

        while (j < size) {

            if (data[i] != c) {
                i++;
            }

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

    @Override
    public void remove(int i, int j) {
        if (i < 0 || i > size || i > j || j >= size) return;

        for (int k = j; k < size - 1; k++) {
            data[i + (k - j)] = data[k + 1];
        }

        size -= (j - i + 1);
    }

    @Override
    public void remove(int i) {
        remove(i, i);
    }

    @Override
    public void remove(int[] indices, int off, int len) {
        if (indices == null || indices.length == 0) return;

        // Marks the chars to delete
        int count = 0;
        for (int i = 0; i < len; i++) {
            int j = indices[off + i];

            if (j >= 0 && j < size) {
                data[j] = 0;
                count++;
            }
        }

        // removes marked chars
        if (count > 0) {
            remove((char) 0);
        }
    }

    /**
     * @return a string representation of this buffer
     */

    @Override
    public final String toString() {
        return String.valueOf(data, 0, size);
    }

    /**
     * @param i position of the char
     * @return the specified char
     */

    public char getCharAt(int i) {
        return data[i];
    }

    @Override
    public int limit() {
        return data.length;
    }

    /**
     * @return true if this buffer contains the given char
     */

    public boolean contains(char a) {
        for (int i = 0; i < size; i++) {

            if (data[i] == a) {
                return true;
            }
        }

        return false;
    }

    /*public static void main(String[] args) {
        char[] array = "provacomeva?".toCharArray();
        int[] indices = {1, 5, 9, 11};

        CharBuffer charBuffer = new CharBuffer(1000);
        charBuffer.add(0, array, 0, array.length);
        charBuffer.add(indices, 0, indices.length, ' ');

        System.out.println(charBuffer);
    }*/
}
