package ru.nsu.cjvth.heapsort;

public class Main {

    public static void main(String[] args) {
        //        Scanner sc = new Scanner(System.in);
        //        int n = sc.nextInt();
        //        int[] a = new int[n];
        //        for (int i = 0; i < n; i++) {
        //            a[i] = sc.nextInt();
        //        }
        //        System.out.println();

        int n = 10;
        int[] a = new int[]{5, 7, 2, 6, 4, 9, 1, 3, 8, 2};

        HeapSort.heapSort(a);
        for (int i = 0; i < n; i++) {
            System.out.println(a[i]);
        }
    }
}