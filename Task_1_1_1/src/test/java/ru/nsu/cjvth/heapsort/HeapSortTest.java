package ru.nsu.cjvth.heapsort;

import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HeapSortTest {

    @Test
    void heapSort() {
        int[] empty = new int[]{};
        HeapSort.heapSort(empty);
        Assertions.assertArrayEquals(empty, new int[]{}, "Test empty array");

        int[] singleton = new int[]{1};
        HeapSort.heapSort(singleton);
        Assertions.assertArrayEquals(singleton, new int[]{1}, "Test singleton array");

        int[][] arrays = new int[][]
            {
                {4, 8},
                {4, 3, 6, 2, 1, 6, 3, 2, 0, 5, 3, 2},
                {1, 9, 2, 8, 3, 7, 4, 6},
                {5, 7, 2, 6, 4, 9, 1, 3, 8, 2},
            };

        for (int[] array : arrays) {
            int[] arrayCopy = Arrays.copyOf(array, array.length);
            HeapSort.heapSort(array);
            Arrays.sort(arrayCopy);
            Assertions.assertArrayEquals(array, arrayCopy, "Test various long arrays");
        }
        int[] negatives = new int[]{-55, 23, 65, 0, -12, 53};
        int[] negativesCopy = Arrays.copyOf(negatives, negatives.length);
        Arrays.sort(negativesCopy);
        HeapSort.heapSort(negatives);
        Assertions.assertArrayEquals(negatives, negativesCopy, "Test array with negative numbers");
    }
}