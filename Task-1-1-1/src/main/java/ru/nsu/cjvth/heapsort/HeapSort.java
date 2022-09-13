package ru.nsu.cjvth.heapsort;

import java.util.ArrayList;

public class HeapSort {
    static void heapSort(ArrayList<Integer> a) {
        Heap h = new Heap(a);
        for (int i = 0; i < a.size(); i++) {
            a.set(i, h.pop());
        }
    }

    static void heapSort(Integer[] a) {
        Heap h = new Heap(a);
        for (int i = 0; i < a.length; i++) {
            a[i] = h.pop();
        }
    }
}
