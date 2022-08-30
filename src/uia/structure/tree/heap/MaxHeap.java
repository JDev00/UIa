package uia.structure.tree.heap;

import uia.structure.list.BInt;

/**
 * Max Heap implementation
 */

public class MaxHeap<T> implements Heap<T> {
    private final HeapNode<T>[] nodes;

    private int heapSize;

    @SuppressWarnings("unchecked")
    public MaxHeap(int length) {
        nodes = new HeapNode[length];
        heapSize = 0;
    }

    @Override
    public void insert(int key, T value) {
        if (heapSize < nodes.length) {
            nodes[heapSize++] = new HeapNode<>(key, value);
            restoreUp(heapSize - 1);
        }
    }

    @Override
    public void modifyKey(int i, int value) {
        if (value != 0 && i >= 0 && i < heapSize) {
            nodes[i].key += value;

            if (value > 0) {
                restoreUp(i);
            } else {
                heapify(i);
            }
        }
    }

    @Override
    public void modifyValue(int i, T val) {
        if (i >= 0 && i < heapSize)
            nodes[i].value = val;
    }

    @Override
    public T extract(int[] key) {
        if (heapSize > 0) {
            HeapNode.swap(nodes, 0, heapSize - 1);
            heapSize--;
            heapify(0);

            HeapNode<T> root = nodes[heapSize];
            if (key != null && key.length > 0) key[0] = root.key;

            return root.value;
        }

        return null;
    }

    /**
     * @throws NullPointerException if {@code i < 0 or i >= size()}
     */

    @Override
    public int getKey(int i) {
        if (i < 0 && i >= heapSize) throw new NullPointerException("");
        return nodes[i].key;
    }

    @Override
    public T getValue(int key) {
        BInt stack = new BInt(heapSize);
        stack.add(0);

        while (stack.size() > 0) {
            int p = stack.get(0);
            stack.remove(0);

            if (p < heapSize) {

                if (nodes[p].key == key) return nodes[p].value;

                stack.add(Heap.getLeft(p));
                stack.add(Heap.getRight(p));
            }
        }

        return null;
    }

    @Override
    public T getValue(int i, int[] key) {
        T r = null;

        if (i >= 0 && i < heapSize) {
            HeapNode<T> node = nodes[i];
            r = node.value;

            if (key != null && key.length > 0) key[0] = node.key;
        }

        return r;
    }

    @Override
    public int size() {
        return heapSize;
    }

    @Override
    public int capacity() {
        return nodes.length;
    }

    private void restoreUp(int i) {
        int j = Heap.getParent(i);

        while (nodes[i].key > nodes[j].key) {
            HeapNode.swap(nodes, i, j);
            i = j;
            j = Heap.getParent(i);
        }
    }

    protected void heapify(int i) {
        int l = Heap.getLeft(i);
        int r = Heap.getRight(i);
        int m;

        if (l < heapSize && nodes[l].key > nodes[i].key) {
            m = l;
        } else {
            m = i;
        }

        if (r < heapSize && nodes[r].key > nodes[m].key) {
            m = r;
        }

        if (m != i) {
            HeapNode.swap(nodes, i, m);
            heapify(m);
        }
    }

    /*
     * Create a new MaxHeap based on an array
     *
     * @return a new MaxHeap
     *

    public static <T> Heap<T> buildMaxHeap(int[] key, T[] val) {
        Heap<T> heap = null;

        if (key != null && val != null && key.length == val.length) {
            heap = new MaxHeap<>(key.length);
            heap.heapSize = array.length;
            heap.array = array;

            for (int i = array.length / 2; i >= 0; i--) {
                heap.heapify(i);
            }
        }

        return heap;
    }*/

    /*
     * Ascending order algorithm based on a MaxHeap.
     * <br>
     * Notable features:
     * <br>
     * 1) in-place
     * <br>
     * 2) unstable
     * <br>
     * 3) Time required: T(nlog(n))
     *
     * @param maxHeap a not null {@link MaxHeap}
     *

    public static <T> void heapSort(MaxHeap<T> maxHeap) {
        if (maxHeap != null) {
            HeapNode<T>[] nodes = maxHeap.array;

            for (int i = nodes.length - 1; i > 0; i--) {
                Heap.swap(nodes, 0, i);
                maxHeap.heapSize--;
                maxHeap.heapify(0);
            }
        }
    }*/
}
