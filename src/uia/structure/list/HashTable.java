package uia.structure.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HashTable<K, E> {
    private final LinkedList<E>[] root;

    private final List<K> keySet;

    @SuppressWarnings("unchecked")
    public HashTable(int m) {
        root = new LinkedList[m];

        keySet = new ArrayList<>();
    }

    /**
     * Clears this hash table
     */

    public void clear() {
        Arrays.fill(root, null);
        keySet.clear();
    }

    /**
     * Adds a new object to this hash table
     *
     * @param k the key of associated to the entry
     * @param e the entry to add
     */

    public void add(K k, E e) {
        int hash = hash(k);

        if (root[hash] == null) {
            root[hash] = new LinkedList<>();
            keySet.add(k);
        }

        root[hash].add(e);
    }

    /**
     * Removes a key from this table.
     * Note: every entry mapped to the given key will be deleted
     *
     * @param k the key to remove
     */

    public void remove(K k) {
        int hash = hash(k);

        if (root[hash] != null) {
            root[hash].clear();
            keySet.remove(k);
        }
    }

    /**
     * Removes an entry from this table
     *
     * @param k the key associated to the entry
     * @param e the entry to remove
     */

    public void remove(K k, E e) {
        LinkedList<E> list = root[hash(k)];

        if (list != null
                && list.remove(e)) {

            if (list.size() == 0) {
                keySet.remove(k);
            }
        }
    }

    /**
     * Calculates the hash function for the given object
     *
     * @param k a key to map into a hash value
     * @return the hash value for the given key
     * @throws NullPointerException if {@code k == null}
     */

    public int hash(K k) {
        Objects.requireNonNull(k);
        return k.hashCode() % root.length;
    }

    /**
     * @return the number of keys
     */

    public final int size() {
        return root.length;
    }

    /**
     * @param k a key to test
     * @return true if the given key is contained inside this hash table
     * @throws NullPointerException if {@code k == null}
     */

    public final boolean containsKey(K k) {
        return root[hash(k)] != null;
    }

    /**
     * @param e the entry to search
     * @return true if the entry exists inside this hash table
     */

    public final boolean containsValue(E e) {
        for (LinkedList<E> i : root) {

            if (i != null && i.contains(e)) return true;
        }

        return false;
    }

    /**
     * @return the linkedList associated to the given key
     */

    public final LinkedList<E> get(K k) {
        return root[hash(k)];
    }

    /**
     * @return the key set
     */

    public final List<K> getKeySet() {
        return keySet;
    }

    /**
     * @return an ArrayList filled with the content of the hash table
     */

    public final List<E> getContent() {
        int size = 0;

        for (LinkedList<E> i : root) {
            size += (i == null ? 0 : i.size());
        }

        List<E> list = new ArrayList<>(size);
        for (LinkedList<E> i : root) {
            if (i != null) list.addAll(i);
        }

        return list;
    }
}
