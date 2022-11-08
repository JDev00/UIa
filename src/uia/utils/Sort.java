package uia.utils;

public class Sort {

    /**
     * Swap two elements of the given array
     *
     * @param a a valid array of integers
     * @param i the position of the first element
     * @param j the position of the last element
     */

    private static void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    /**
     * Partition implementation
     */

    private static int partition(int[] array, int p, int q) {
        int i = p - 1;
        int x = array[q];

        for (int j = p; j < q; j++) {

            if (array[j] < x) {
                i++;
                swap(array, i, j);
            }
        }

        swap(array, i + 1, q);

        return i + 1;
    }

    /**
     * Sort in ascending order with the QuickSort algorithm
     *
     * @param array a not null array of integers
     * @param p     the first anchor point
     * @param q     the last anchor point
     */

    public static void quickSort(int[] array, int p, int q) {
        if (p < q) {
            int r = partition(array, p, q);
            quickSort(array, p, r - 1);
            quickSort(array, r + 1, q);
        }
    }
}
