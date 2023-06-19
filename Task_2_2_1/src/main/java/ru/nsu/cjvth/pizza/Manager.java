package ru.nsu.cjvth.pizza;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that controls the cooking cycle.
 */
public class Manager {
    private final int cooks;
    private final int delivery;
    private final Orders availableOrders;
    private final Store store;

    public Manager(int orders, int cooks, int delivery, int storeCapacity) {
        this.cooks = cooks;
        this.delivery = delivery;
        this.availableOrders = new Orders(orders);
        this.store = new Store(storeCapacity);
    }

    public void run() throws InterruptedException {
        List<Thread> cookList = new ArrayList<>(cooks);
        for (int i = 0; i < this.cooks; i++) {
            cookList.add(new Thread(new Cook(i, 200, availableOrders, store)));
        }
        List<Thread> deliveryList = new ArrayList<>(delivery);
        for (int i = 0; i < this.delivery; i++) {
            deliveryList.add(new Thread(new Delivery(i, 150, availableOrders, store)));
        }
        for (var i : cookList) {
            i.start();
        }
        for (var i : deliveryList) {
            i.start();
        }
        for (var i : cookList) {
            i.join();
        }
        availableOrders.setAllCooked();
        for (var i : deliveryList) {
            i.join();
        }
    }
}
