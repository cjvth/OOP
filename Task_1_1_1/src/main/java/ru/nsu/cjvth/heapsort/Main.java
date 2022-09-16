package ru.nsu.cjvth.heapsort;

import java.util.Scanner;

/**
 * Contains main executable method.
 */
public class Main {

    /**
     * Reads N - array size and N integers - its contents. Sorts the array and prints every item on
     * a new line.
     *
     * @param args command line arguments, has no effect
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
        }
        System.out.println();

        HeapSort.heapSort(a);
        for (int i = 0; i < n; i++) {
            System.out.println(a[i]);
        }
    }
}