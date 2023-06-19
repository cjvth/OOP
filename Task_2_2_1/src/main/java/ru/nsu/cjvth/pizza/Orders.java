package ru.nsu.cjvth.pizza;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Orders {
    private final Deque<Integer> availableOrders;
    private final AtomicBoolean allCooked;

    public Orders(int orders) {
        this.availableOrders = new LinkedList<>();
        for (int i = 1; i <= orders; i++) {
            availableOrders.add(i);
        }
        allCooked = new AtomicBoolean(false);
    }

    public synchronized Integer take() {
        return availableOrders.poll();
    }

    public void setAllCooked() {
        synchronized (allCooked) {
            allCooked.set(true);
        }
    }

    public boolean isAllCooked() {
        return allCooked.get();
    }
}
