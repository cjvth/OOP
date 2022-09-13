package ru.nsu.cjvth.heapsort;

import java.util.ArrayList;

public class Heap {
    private ArrayList<Integer> array;

    public Heap() {
        array = new ArrayList<>();
    }

    public Heap(Iterable<Integer> a) {
        array = new ArrayList<>();
        for (Integer i : a)
            add(i);
    }

    public Heap(Integer[] a) {
        array = new ArrayList<>();
        for (Integer i : a)
            add(i);
    }

    private void siftUp(int i) {
        int j;
        while (i > 0 && array.get((j = (i - 1) / 2)) > array.get(i)) {
            var t = array.get(i);
            array.set(i, array.get(j));
            array.set(j, t);
            i = j;
        }
    }

    private void siftDown(int i) {
        int j;
        while ((j = i * 2 + 1) < array.size()) {
            if (j + 1 < array.size() && array.get(j + 1) < array.get(j))
                j++;
            if (array.get(j) < array.get(i)) {
                var t = array.get(i);
                array.set(i, array.get(j));
                array.set(j, t);
                i = j;
            }
            else
                break;
        }
    }

    public void add(Integer x) {
        array.add(x);
        siftUp(array.size() - 1);
    }

    public Integer pop() {
        Integer x = array.get(0);
        if (array.size() > 1) {
            array.set(0, array.remove(array.size() - 1));
            siftDown(0);
        }
        else
            array.clear();
        return x;
    }
}