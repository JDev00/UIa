package uia.structure.tree.heap;

/**
 * Heap ADT
 */

public interface Heap<T> {

    /**
     * Add a new element to this heap
     *
     * @param key the priority key of the element
     * @param val the element
     */

    void insert(int key, T val);

    /**
     * Modify the key of the i-th element
     *
     * @param i     the position of the element
     * @param value a value to sum to the element's key
     */

    void modifyKey(int i, int value);

    /**
     * Modify the value of the specified element
     *
     * @param i   the position of the element
     * @param val a new value
     */

    void modifyValue(int i, T val);

    /**
     * @param key an array used to store the root's key; it could be null
     * @return the root element
     */

    T extract(int[] key);

    /**
     * @return the key of the specified element
     */

    int getKey(int i);

    /**
     * @param key the element's key
     * @return the value of the specified element
     */

    T getValue(int key);

    /**
     * @param i   the position of the element
     * @param key an array used to store the element's key; it could be null
     * @return the value of the specified element
     */

    T getValue(int i, int[] key);

    /**
     * @return the amount of keys inside this Heap
     */

    int size();

    /**
     * @return the maximum Heap capacity
     */

    int capacity();

    /**
     * Return the Heap's structure
     *
     * @return a String filled with the Heap's keys
     */

    static String show(Heap<?> heap, String regex) {
        StringBuilder builder = new StringBuilder(30 * heap.size());

        for (int i = 0; i < heap.size(); i++)
            builder.append(heap.getKey(i)).append(regex);

        if (builder.length() > 0)
            builder.delete(builder.length() - regex.length(), builder.length());

        return builder.toString();
    }

    /**
     * @return the left child of the given node
     */

    static int getLeft(int i) {
        return 2 * i + 1;
    }

    /**
     * @return the right child of the given node
     */

    static int getRight(int i) {
        return 2 * (i + 1);
    }

    /**
     * @return the node's parent
     */

    static int getParent(int i) {
        return (i - 1) / 2;
    }
}
