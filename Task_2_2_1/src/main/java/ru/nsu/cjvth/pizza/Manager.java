package ru.nsu.cjvth.pizza;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that controls the cooking cycle.
 */
public class Manager {
    private final List<Cook> cooks;
    private final List<Delivery> delivery;
    private final Store store;

    public Manager(Store store, List<Cook> cooks, List<Delivery> delivery) {
        this.cooks = cooks;
        this.delivery = delivery;
        this.store = store;
    }

    public void start() throws InterruptedException {
        List<Thread> cookThreads = new ArrayList<>(cooks.size());
        for (var i : cooks) {
            cookThreads.add(new Thread(i));
        }
        List<Thread> deliveryThreads = new ArrayList<>(delivery.size());
        for (var i : delivery) {
            deliveryThreads.add(new Thread(i));
        }
        for (var i : cookThreads) {
            i.start();
        }
        for (var i : deliveryThreads) {
            i.start();
        }
        for (var i : cookThreads) {
            i.join();
        }
        store.setAllCooked();
        for (var i : deliveryThreads) {
            i.join();
        }
    }
}
