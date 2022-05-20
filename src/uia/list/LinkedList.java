package uia.structure.list;

import java.util.*;

public class LinkedList<T> implements List<T> {
    private Node<T> root;
    private int size;

    public LinkedList() {
        root = null;
        size = 0;
    }

    /*
     *
     *
     */

    public static final class Node<T> {
        private T value;
        public Node<T> next;

        private Node(Node<T> next, T value) {
            this.next = next;
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "next=" + next +
                    ", value=" + value +
                    '}';
        }
    }

    /*
     *
     *
     */

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean add(T t) {
        if (root == null) {
            root = new Node<>(null, t);
        } else {
            Node<T> node = root;

            while (node.next != null) {
                node = node.next;
            }

            node.next = new Node<>(null, t);
        }

        size++;

        return true;
    }

    @Override
    public void add(int i, T t) {
        int index = 0;
        Node<T> node = root;

        // Caso base
        if (i == 0) {

            if (root == null)
                root = new Node<>(null, t);
            else
                root = new Node<>(node, t);

            size++;
            return;
        }

        // Passo induttivo
        while (node != null) {

            if (index == i - 1) {
                node.next = new Node<>(node.next, t);
                size++;
                break;
            }

            index++;
            node = node.next;
        }
    }

    @Override
    public T set(int i, T t) {
        Node<T> node = root;
        int index = 0;

        while (node != null) {

            if (i == index) {
                node.value = t;
                return node.value;
            } else {
                index++;
                node = node.next;
            }
        }

        return null;
    }

    @Override
    public boolean remove(Object o) {
        if (isEmpty() || o == null) return false;

        if (root.value.equals(o)) {
            root = root.next;
            size--;
            return true;
        }

        Node<T> prev = root;
        Node<T> node = root.next;

        while (node != null) {

            if (node.value.equals(o)) {
                prev.next = node.next;
                size--;
                return true;
            } else {
                prev = node;
                node = node.next;
            }
        }

        return false;
    }

    @Override
    public T remove(int i) {
        if (isEmpty() || i < 0 || i >= size) return null;

        if (i == 0) {
            T temp = root.value;
            root = root.next;
            size--;
            return temp;
        }

        Node<T> prev = root;
        Node<T> node = root.next;
        int index = 0;

        while (node != null) {

            if (index == i) {
                T temp = node.value;
                prev.next = node.next;
                size--;
                return temp;
            } else {
                prev = node;
                node = node.next;
            }

            index++;
        }

        return null;
    }

    @Override
    public String toString() {
        Node<T> node = root;
        StringBuilder builder = new StringBuilder(10000);

        while (node != null) {
            builder.append(node.value);
            node = node.next;

            if (node != null)
                builder.append(", ");
        }

        return "LinkedList{" + builder.toString() + '}';
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Node<T> node = root;

            @Override
            public boolean hasNext() {
                return node != null;
            }

            @Override
            public T next() {
                T t = node.value;
                node = node.next;
                return t;
            }
        };
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        throw new UnsupportedOperationException("Unsupported operation!");
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        throw new UnsupportedOperationException("Unsupported operation!");
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> collection) {
        throw new UnsupportedOperationException("Unsupported operation!");
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException("Unsupported operation!");
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return false;
    }

    /**
     * @return the root the of the linked list
     */

    public Node<T> getRoot() {
        return root;
    }

    @Override
    public T get(int i) {
        Node<T> node = root;
        int index = 0;

        while (node != null) {

            if (i == index) {
                return node.value;
            } else {
                index++;
                node = node.next;
            }
        }

        return null;
    }

    @Override
    public int indexOf(Object o) {
        Node<T> node = root;
        int index = 0;

        while (node != null) {

            if (node.value.equals(o)) {
                return index;
            } else {
                node = node.next;
            }

            index++;
        }

        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        Node<T> node = root;
        int index = 0;
        int ret = -1;

        while (node != null) {

            if (node.value.equals(o)) ret = index;

            index++;
            node = node.next;
        }

        return ret;
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException("Unsupported operation!");
    }

    @Override
    public ListIterator<T> listIterator(int i) {
        throw new UnsupportedOperationException("Unsupported operation!");
    }

    @Override
    public List<T> subList(int start, int stop) {
        List<T> ret = new ArrayList<>(stop - start);

        int index = 0;
        Node<T> node = root;

        while (node != null) {

            if (index >= stop) break;
            if (index >= start) ret.add(node.value);

            index++;
            node = node.next;
        }

        return ret;
    }

    @Override
    public Object[] toArray() {
        Object[] temp = new Object[size];
        Node<T> node = root;
        int index = 0;

        while (node != null) {
            temp[index++] = node.value;
            node = node.next;
        }

        return temp;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> E[] toArray(E[] array) {
        Objects.requireNonNull(array);

        if (array.length < size) {
            array = Arrays.copyOf(array, size);
        }

        int index = 0;
        Node<T> node = root;

        while (node != null) {
            array[index++] = (E) node.value;
            node = node.next;
        }

        return array;
    }
}
