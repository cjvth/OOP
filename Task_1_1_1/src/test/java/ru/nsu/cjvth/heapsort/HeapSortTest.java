package ru.nsu.cjvth.heapsort;

import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HeapSortTest {

    @Test
    public void heapSortEmpty() {
        int[] empty = new int[]{};
        HeapSort.heapSort(empty);
        Assertions.assertArrayEquals(empty, new int[]{}, "Test empty array");
    }

    @Test
    public void heapSortSingleTon() {
        int[] singleton = new int[]{1};
        HeapSort.heapSort(singleton);
        Assertions.assertArrayEquals(singleton, new int[]{1}, "Test singleton array");
    }

    @Test
    public void heapSortNegatives() {
        int[] negatives = new int[]{-55, 23, 65, 0, -12, 53};
        String message = "Test array with negative elements: " + Arrays.toString(negatives);
        int[] negativesCopy = Arrays.copyOf(negatives, negatives.length);
        HeapSort.heapSort(negatives);
        Arrays.sort(negativesCopy);
        Assertions.assertArrayEquals(negatives, negativesCopy, message);
    }

    @Test
    public void heapSortEquals() {
        int[] equals = new int[]{5, 4, 5, 5, 2, 7, 5, 5, 3, 6, 5};
        String message = "Test array with many equal elements: " + Arrays.toString(equals);
        int[] negativesCopy = Arrays.copyOf(equals, equals.length);
        HeapSort.heapSort(equals);
        Arrays.sort(negativesCopy);
        Assertions.assertArrayEquals(equals, negativesCopy, message);
    }

    @Test
    public void heapSortVarious() {
        int[][] arrays = new int[][]
            {
                {4, 8},
                {4, 3, 6, 2, 1, 6, 3, 2, 0, 5, 3, 2},
                {1, 9, 2, 8, 3, 7, 4, 6},
                {5, 7, 2, 6, 4, 9, 1, 3, 8, 2},
                {3, 5, 2, 1, 8, 6, 5, 1, 9, 0, 4}
            };
        for (int[] array : arrays) {
            String message = "Test various long arrays: " + Arrays.toString(array);
            int[] arrayCopy = Arrays.copyOf(array, array.length);
            HeapSort.heapSort(array);
            Arrays.sort(arrayCopy);
            Assertions.assertArrayEquals(array, arrayCopy, message);
        }
    }
}