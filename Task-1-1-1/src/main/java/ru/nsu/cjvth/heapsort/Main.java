package ru.nsu.cjvth.heapsort;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Integer[] a = new Integer[n];
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
        }
        System.out.println();

//        int n = 10;
//        Integer[] a = new Integer[]{5, 7, 2, 6, 4, 9, 1, 3, 8, 2};

        HeapSort.heapSort(a);
        for (int i = 0; i < n; i++) {
            System.out.println(a[i]);
        }
    }
}