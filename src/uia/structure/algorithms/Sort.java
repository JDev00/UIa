package uia.structure.algorithms;

import uia.structure.list.LinkedList;

import java.util.Objects;

public final class Sort {

    private Sort() {
    }

    /**
     * Swaps two elements of the given array
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

    /*
     *
     * Selection sort
     *
     */

    /**
     * Sorts in ascending order a given array with the selectionSort algorithm.
     * Complexity: T(n) = Theta(n^2)
     *
     * @param array a non-null array of integers
     */

    public static void selectionSort(int[] array) {
        if (array != null && array.length >= 2) {
            int key;

            for (int i = 0; i < array.length - 1; i++) {
                key = i;

                for (int l = i + 1; l < array.length; l++) {

                    if (array[l] < array[key]) key = l;
                }

                // @Safe even when key == i
                swap(array, i, key);
            }
        }
    }

    /*
     *
     * Insertion sort
     *
     */

    /**
     * InsertionSort implementation
     *
     * @param array a non-null array of integers to order
     */

    /* 1) 2,1,4,8,3
       2) 1,2,4,8,3
       3) 1,2,3,4,8 */
    public static void insertionSort(int[] array) {
        if (array != null && array.length >= 2) {
            int key;
            int j;

            for (int i = 1; i < array.length; i++) {
                key = array[i];
                j = i - 1;

                while (j >= 0 && array[j] > key) {
                    array[j + 1] = array[j];
                    j--;
                }

                array[j + 1] = key;
            }
        }
    }

    /*
     *
     * Merge sort
     *
     */

    /**
     * Merges two sorted parts of array a
     *
     * @param a two parts sorted array
     * @param p init of the first sorted part
     * @param r end of the first sorted part, r + 1 -> init of the second part
     * @param q end of the second sorted part
     * @apiNote if a is null or banal the method return.
     */

    private static void merge(int[] a, int p, int r, int q) {
        if (a == null || a.length <= 1) return;

        int n1 = r - p + 1; // con r incluso!
        int n2 = q - r;
        int i = 0;
        int j = 0;
        int index = 0;

        int[] s1 = new int[n1];
        int[] s2 = new int[n2];

        for (int k = 0; k < s1.length; k++) s1[k] = a[p + k];

        for (int k = 0; k < s2.length; k++) s2[k] = a[r + 1 + k];


        while (i < n1 && j < n2) {

            if (s1[i] <= s2[j]) {
                a[index++] = s1[i++];
            } else {
                a[index++] = s2[j++];
            }
        }


        if (j < n2) {
            for (int k = j; k < n2; k++) a[index++] = s2[k];
        } else {
            for (int k = i; k < n1; k++) a[index++] = s1[k];
        }
    }

    /**
     * MergeSort implementation
     * Time complexity: T(n) = Theta(nlog(n))
     *
     * @param array a non-null array of integers to sort
     */

    @Deprecated
    public static void mergeSort(int[] array, int i, int j) {
        if (i < j) {
            int r = (i + j) / 2;

            mergeSort(array, i, r);
            mergeSort(array, r + 1, j);
            merge(array, i, r, j);
        }
    }

    /*
     *
     * QuickSort
     *
     */

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
     * Sorts in ascending order using the QuickSort algorithm
     *
     * @param array a valid array of integers
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

    /**
     * Returns the maximum number inside the given array
     *
     * @param array a non-null array of integers
     * @return the greatest number
     */

    private static int max(int[] array) {
        int max = array[0];

        for (int i = 1; i < array.length; i++) {
            int temp = array[i];

            if (temp > max) max = temp;
        }

        return max;
    }

    /*
     *
     * Counting sort
     *
     */

    /**
     * Counting sort implementation; it takes Theta(n) to sort in ascending order an array of elements
     *
     * @param array  the array to order
     * @param sorted the array to fill with the sorted numbers
     */

    public static void countingSort(int[] array, int[] sorted) {
        if (array != null && sorted != null) {
            int max = max(array);
            int[] c = new int[max + 1];

            for (int i : array) {
                c[i]++;
            }

            for (int i = 1; i < c.length; i++) {
                c[i] += c[i - 1];
            }

            for (int i = array.length - 1; i >= 0; i--) {
                sorted[c[array[i]] - 1] = array[i];
                c[array[i]]--;
            }
        }
    }

    /*
     *
     * Bucket sort
     *
     */

    /**
     * Bucket sort implementation
     *
     * @param array  the array to order
     * @param sorted the array to fill with the sorted numbers
     */

    @SuppressWarnings("unchecked")
    public static void bucketSort(int[] array, int[] sorted) {
        LinkedList<Integer>[] lists = new LinkedList[5];

        for (int i = 0; i < lists.length; i++) {
            lists[i] = new LinkedList<>();
        }

        int max = max(array) + 1;

        // Fill buckets [0,1)
        for (int i : array) {
            float e = (float) i / max;

            if (e < 0.2f) {
                lists[0].add(i);
            } else if (e < 0.4f) {
                lists[1].add(i);
            } else if (e < 0.6f) {
                lists[2].add(i);
            } else if (e < 0.8f) {
                lists[3].add(i);
            } else if (e < 1) {
                lists[4].add(i);
            }
        }

        //Sort buckets and fill sorted array
        int bSize = 0;
        for (LinkedList<Integer> i : lists) {

            if (!i.isEmpty()) {
                int size = 0;
                int[] temp = new int[i.size()];
                LinkedList.Node<Integer> node = i.getRoot();

                while (node != null) {
                    temp[size++] = node.getValue();
                    node = node.next;
                }

                selectionSort(temp);

                for (int l : temp) {
                    sorted[bSize++] = l;
                }
            }
        }
    }

    /*
     *
     * RadixSort
     *
     */

    /**
     * Test. Could be refined!
     * This method is a modified version of countingSort
     *
     * @param A     a valid array of integers
     * @param index the position of the digit in the given array
     * @return a new valid array with digits sorted according to the given index
     * @throws NullPointerException if {@code A == null}
     */

    /*
     * Esempio:
     *             Start
     * {             |
     * new int[]{1,0,9},
     * new int[]{0,9,5},
     * new int[]{0,8,5},
     * new int[]{0,8,2},
     * new int[]{0,0,3}};
     * */
    private static int[][] countingSortModified(int[][] A, int index) {
        Objects.requireNonNull(A);

        int k = A[0][index];

        for (int[] l : A) {
            int temp = l[index];

            if (temp > k) k = temp;
        }

        int[] c = new int[k + 1];
        int[][] B = new int[A.length][A[0].length];


        for (int[] i : A) c[i[index]]++;

        for (int i = 1; i < c.length; i++) c[i] += c[i - 1];

        for (int i = A.length - 1; i >= 0; i--) {
            B[c[A[i][index]] - 1] = A[i];
            c[A[i][index]]--;
        }

        return B;
    }

    /**
     * Radix sort implementation
     *
     * @param array a valid array of integers
     * @param d     the number of digits
     * @throws NullPointerException if {@code array == null}
     */

    public static int[][] radixSort(int[][] array, int d) {
        Objects.requireNonNull(array);

        int[][] tempA = array;

        for (int i = d - 1; i >= 0; i--) {
            tempA = countingSortModified(tempA, i);
        }

        return tempA;
    }

    /*
     *
     */

    /*public static void main(String[] args) {
        //int[] array = {-100, 2000, 2000, 0, 0, 0, 100, 2000, 30, 4, 10, 0, 20, 100, 2, -10, 2, 3};
        int[] array = {0, 1, 10, 2, 9, 10, 8, 7, 9, 6, 9, 10};
        int[] sortedArray = new int[array.length];

        for (int i = 0; i < sortedArray.length; i++) {
            sortedArray[i] = array[i];
        }

        mergeSort(sortedArray, 0, sortedArray.length - 1);

        int i = 0;
        int before = Integer.MIN_VALUE;

        while (i < sortedArray.length && before <= sortedArray[i]) {
            before = sortedArray[i++];
        }

        System.out.println(i == sortedArray.length);
    }*/
}
