package uia.structure.list;

public abstract class Buffer {
    protected int size = 0;
    protected int limit = 10_000_000;

    /**
     * Sets the buffer's capacity limit
     *
     * @param limit the buffer's capacity limit; negative values will be converted into positive ones
     */

    public void setLimit(int limit) {
        this.limit = Math.abs(limit);
    }

    /**
     * Clears this buffer
     */

    public abstract void clear();

    /**
     * Removes the specified elements.
     * Complexity: T(n) = Theta(n)
     *
     * @param indices the indices of the elements to remove
     * @param off     the array start position
     * @param len     the array length
     */

    public abstract void remove(int[] indices, int off, int len);

    /**
     * Removes the specified element
     *
     * @param i the index of the element to remove
     */

    public abstract void remove(int i);

    /**
     * Removes a set of elements
     *
     * @param i the start index of the element to remove
     * @param j the last index of the element to remove
     */

    public abstract void remove(int i, int j);

    /**
     * @return a string representation of this buffer
     */

    @Override
    public String toString() {
        return "Buffer{size=" + size + '}';
    }

    /**
     * @return the buffer's size
     */

    public int size() {
        return size;
    }

    /**
     * @return the buffer's capacity
     */

    public int limit() {
        return limit;
    }

    /*
     *
     * Utilities
     *
     */

    /**
     * @param buffer a non-null {@link BufferByte}
     * @return the internal data of the given buffer
     */

    public static byte[] getData(BufferByte buffer) {
        return buffer.data;
    }

    /**
     * @param buffer a non-null {@link BufferChar}
     * @return the internal data of the given buffer
     */

    public static char[] getData(BufferChar buffer) {
        return buffer.data;
    }

    /**
     * @param buffer a non-null {@link BufferInt}
     * @return the internal data of the given buffer
     */

    public static int[] getData(BufferInt buffer) {
        return buffer.data;
    }

    /**
     * @param buffer a non-null {@link BufferFloat}
     * @return the internal data of the given buffer
     */

    public static float[] getData(BufferFloat buffer) {
        return buffer.data;
    }

    /**
     * @param buffer a non-null {@link BufferDouble}
     * @return the internal data of the given buffer
     */

    public static double[] getData(BufferDouble buffer) {
        return buffer.data;
    }
}
