package uia.structure.tree.heap;

public class HeapTest {

    public static void main(String[] args) {
        Heap<String> heap = new MaxHeap<>(100);
        heap.insert(10, "Gialli");
        heap.insert(0, "Rossi");
        heap.insert(1, "Verdi");
        heap.insert(5, "Boh");
        heap.insert(20, "Rossi");
        heap.insert(100, "Okay!");

        int[] key = {0};

        System.out.println(Heap.show(heap, ", "));
        System.out.println(heap.extract(key) + ", " + key[0]);

        heap.modifyKey(1, 100);
        System.out.println(Heap.show(heap, ", "));

        System.out.println(heap.getValue(20));

        /*System.out.println(heap.extract());
        System.out.println(heap.extract());*/
    }
}
