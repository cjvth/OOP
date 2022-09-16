package ru.nsu.cjvth.heapsort;

/**
 * Provides static methods to heapsort arrays
 */
public class HeapSort {

    /**
     * Sort given array using heapsort algorithm
     *
     * @param a array of int to be sorted
     */
    public static void heapSort(int[] a) {
        for (int i = a.length / 2; i >= 0; i--) {
            siftDown(a, i, a.length);
        }
        for (int i = a.length - 1; i > 0; i--) {
            int t = a[0];
            a[0] = a[i];
            a[i] = t;
            siftDown(a, 0, i);
        }
    }

    private static void siftDown(int[] heapArray, int element, int length) {
        for (int i = element, j = i * 2 + 1; j < length; i = j, j = i * 2 + 1) {
            if (j + 1 < length && heapArray[j + 1] > heapArray[j]) {
                j++;
            }
            if (heapArray[j] > heapArray[i]) {
                int t = heapArray[i];
                heapArray[i] = heapArray[j];
                heapArray[j] = t;
            } else {
                break;
            }
        }
    }
}
