package ru.nsu.cjvth.pizza;

import java.util.Deque;
import java.util.LinkedList;

public class Orders {
    private final Deque<Integer> availableOrders;

    public Orders(int orders) {
        this.availableOrders = new LinkedList<>();
        for (int i = 1; i <= orders; i++) {
            availableOrders.add(i);
        }
    }

    public synchronized Integer take() {
        return availableOrders.poll();
    }
}
