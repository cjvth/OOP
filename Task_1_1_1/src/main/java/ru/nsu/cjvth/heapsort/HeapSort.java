package ru.nsu.cjvth.heapsort;

/**
 * Provides static methods to heapsort arrays.
 */
public class HeapSort {

    /**
     * Sorts given array using heapsort algorithm.
     *
     * @param array array of int to be sorted
     */
    public static void heapSort(int[] array) {
        for (int i = array.length / 2; i >= 0; i--) {
            siftDown(array, i, array.length);
        }
        for (int i = array.length - 1; i > 0; i--) {
            int t = array[0];
            array[0] = array[i];
            array[i] = t;
            siftDown(array, 0, i);
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
