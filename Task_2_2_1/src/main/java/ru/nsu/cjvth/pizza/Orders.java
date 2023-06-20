package ru.nsu.cjvth.pizza;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Pool of orders that no one has started cooking.
 */
public class Orders {
    private final Deque<Integer> availableOrders;

    /**
     * Constructor.
     *
     * @param numOrders number of orders
     */
    public Orders(int numOrders) {
        this.availableOrders = new LinkedList<>();
        for (int i = 1; i <= numOrders; i++) {
            availableOrders.add(i);
        }
    }

    /**
     * Take one order from the pool.
     *
     * @return number of the order if there is some, null if all is already done
     */
    public synchronized Integer take() {
        return availableOrders.poll();
    }
}
