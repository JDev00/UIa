package uia.structure.tree.heap;

class HeapNode<T> {
    public int key;
    public T value;

    public HeapNode(int key, T value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Swaps two elements
     *
     * @param array the target array
     * @param i     the position of the first element to swap
     * @param j     the position of the second element to swap
     */

    public static <T> void swap(HeapNode<T>[] array, int i, int j) {
        HeapNode<T> temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}