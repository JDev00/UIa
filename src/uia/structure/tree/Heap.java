package uia.structure.tree;

/**
 * Abstract Heap representation
 */

public abstract class Heap<T> {
    protected Node<T>[] array;
    protected int heapSize;

    @SuppressWarnings("unchecked")
    public Heap(int length) {
        array = new Heap.Node[length];
        heapSize = 0;
    }

    /**
     * A Heap node contains a value and a priority key
     */

    public static final class Node<T> {
        private int key;

        private final T value;

        public Node(int key, T value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Change the key of this node
         *
         * @param key an integer
         */

        public void setKey(int key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return "Node{key=" + key + ", value=" + value + '}';
        }

        /**
         * @return the key of this node
         */

        public int getKey() {
            return key;
        }

        /**
         * @return the value of this node
         */

        public T getValue() {
            return value;
        }
    }

    /**
     * Add a new element to this heap
     *
     * @param nodeHeap a non-null {@link Node} to add to this heap
     */

    public abstract void insert(Node<T> nodeHeap);

    /**
     * @return the root element
     */

    public abstract Node<T> extract();

    /**
     * Modify the key of the i-th element
     *
     * @param i     the position of the element
     * @param value a value to sum to the element's key
     */

    public abstract void modifyKey(int i, int value);

    /**
     * Restore the heap when a new node is added or a key changes
     */

    public abstract void restoreUp(int i);

    /**
     * Restore heap when root is extracted or a key changes
     */

    public abstract void heapify(int i);

    /**
     * @return the number of nodes
     */

    public int size() {
        return heapSize;
    }

    /**
     * @return the heap's array representation
     */

    public Node<T>[] getArray() {
        return array;
    }

    /**
     * Prints this heap
     */

    public String print(String regex) {
        StringBuilder builder = new StringBuilder(100 * heapSize);

        for (int i = 0; i < heapSize; i++) {
            builder.append(array[i].toString()).append(regex);
        }

        if (builder.length() > 0) {
            builder.delete(builder.length() - regex.length(), builder.length());
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        return "MaxHeap{size=" + heapSize + "\nheap=" + print(",") + '}';
    }

    /**
     * Swaps two elements
     *
     * @param array the target array
     * @param i     the position of the first element to swap
     * @param j     the position of the second element to swap
     */

    public static <T> void swap(Node<T>[] array, int i, int j) {
        Node<T> temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * @return the left child
     */

    public static int getLeft(int i) {
        return 2 * i;
    }

    /**
     * @return the right child
     */

    public static int getRight(int i) {
        return 2 * i + 1;
    }

    /**
     * @return the parent
     */

    public static int getParent(int i) {
        return i / 2;
    }

    /*
     *
     * Implementations
     *
     */

    /**
     * Creates a new Max Heap
     */

    public static <T> Heap<T> createMaxHeap(int length) {
        return new Heap<T>(length) {

            @Override
            public void insert(Node<T> node) {
                if (node != null && heapSize < array.length) {
                    array[heapSize++] = node;
                    restoreUp(heapSize - 1);
                }
            }

            @Override
            public Node<T> extract() {
                if (heapSize > 0) {
                    swap(array, 0, heapSize - 1);
                    heapSize--;
                    heapify(0);
                    return array[heapSize];
                }

                return null;
            }

            @Override
            public void modifyKey(int i, int value) {
                if (value != 0 && i >= 0 && i < heapSize) {
                    array[i].setKey(array[i].getKey() + value);

                    if (value > 0) {
                        restoreUp(i);
                    } else {
                        heapify(i);
                    }
                }
            }

            @Override
            public void restoreUp(int i) {
                int j = getParent(i);

                while (j > 0 && array[j].getKey() < array[i].getKey()) {
                    swap(array, i, j);

                    i = j;
                    j = getParent(i);
                }
            }

            @Override
            public void heapify(int i) {
                int l = getLeft(i);
                int r = getRight(i);
                int m;

                if (l < heapSize && array[l].getKey() > array[i].getKey()) {
                    m = l;
                } else {
                    m = i;
                }

                if (r < heapSize && array[r].getKey() > array[m].getKey()) {
                    m = r;
                }

                if (m != i) {
                    swap(array, i, m);
                    heapify(m);
                }
            }
        };
    }

    /**
     * Creates a new MaxHeap based on an array
     *
     * @param array a non-null array
     * @return a new MaxHeap
     */

    public static <T> Heap<T> buildMaxHeap(Node<T>[] array) {
        Heap<T> heap = createMaxHeap(array.length);
        heap.heapSize = array.length;
        heap.array = array;

        for (int i = array.length / 2; i >= 0; i--) {
            heap.heapify(i);
        }

        return heap;
    }

    /**
     * Ascending order algorithm based on {@link Heap#buildMaxHeap(Node[])}
     * Notes:
     * 1) in-place
     * 2) not-stable
     *
     * @param array a valid array
     */

    public static <T> void heapSort(Node<T>[] array) {
        Heap<T> heap = buildMaxHeap(array);

        for (int i = array.length - 1; i > 0; i--) {
            Heap.swap(heap.array, 0, i);
            heap.heapSize -= 1;
            heap.heapify(0);
        }
    }

    /**
     * Creates a new Min Heap
     */

    public static <T> Heap<T> createMinHeap(int length) {
        return new Heap<T>(length) {

            @Override
            public void insert(Node<T> node) {
                if (node != null && heapSize < array.length) {
                    array[heapSize++] = node;
                    restoreUp(heapSize - 1);
                }
            }

            @Override
            public Node<T> extract() {
                if (heapSize > 0) {
                    swap(array, 0, heapSize - 1);
                    heapSize -= 1;
                    heapify(0);
                    return array[heapSize];
                }

                return null;
            }

            @Override
            public void modifyKey(int i, int value) {
                if (value != 0 && i >= 0 && i < heapSize) {
                    array[i].setKey(array[i].getKey() + value);

                    if (value > 0) {
                        heapify(i);
                    } else {
                        restoreUp(i);
                    }
                }
            }

            @Override
            public void restoreUp(int i) {
                int j = getParent(i);

                while (j > 0 && array[j].getKey() > array[i].getKey()) {
                    swap(array, i, j);

                    i = j;
                    j = getParent(i);
                }
            }

            @Override
            public void heapify(int i) {
                int l = getLeft(i);
                int r = getRight(i);
                int m;

                if (l < heapSize && array[l].getKey() < array[i].getKey()) {
                    m = l;
                } else {
                    m = i;
                }

                if (r < heapSize && array[r].getKey() < array[m].getKey()) {
                    m = r;
                }

                if (m != i) {
                    swap(array, m, i);
                    heapify(m);
                }
            }
        };
    }

    /**
     * Builds a new min heap
     */

    public static <T> Heap<T> buildMinHeap(Node<T>[] array) {
        Heap<T> heap = createMinHeap(array.length);
        heap.array = array;
        heap.heapSize = array.length;

        for (int i = heap.heapSize / 2; i >= 0; i--) {
            heap.heapify(i);
        }

        return heap;
    }
}
