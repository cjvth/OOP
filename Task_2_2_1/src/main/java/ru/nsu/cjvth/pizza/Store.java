package ru.nsu.cjvth.pizza;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Store {
    private final int capacity;
    private final AtomicInteger used;
    private final AtomicBoolean allCooked = new AtomicBoolean(false);

    private final Lock ordersLock = new ReentrantLock();
    private final Condition notEmpty = ordersLock.newCondition();
    private final Condition notFull = ordersLock.newCondition();

    private final Deque<Integer> orders;

    public Store(int capacity) {
        this.capacity = capacity;
        this.orders = new LinkedList<>();
        this.used = new AtomicInteger(0);
    }

    public void put(int order) throws InterruptedException {
        ordersLock.lock();
        try {
            while (used.get() >= capacity) {
                notFull.await();
            }
            if (used.incrementAndGet() == 1) {
                notEmpty.signal();
            }
            orders.add(order);
        } finally {
            ordersLock.unlock();
        }
        System.out.printf("Store used on %d of %d\n", used.get(), capacity);
    }

    public Integer take() throws InterruptedException {
        ordersLock.lock();
        try {
            while (used.get() == 0) {
                notEmpty.await();
                if (used.get() == 0 && allCooked.get()) {
                    return null;
                }
            }
            if (used.decrementAndGet() == capacity - 1) {
                notFull.signal();
            }
            Integer pop = this.orders.pop();
            System.out.printf("Store used on %d of %d\n", used.get(), capacity);
            return pop;
        } finally {
            ordersLock.unlock();
        }
    }

    public Integer takeNotWait() throws InterruptedException {
        ordersLock.lock();
        try {
            if (this.orders.isEmpty()) {
                return null;
            }
            Integer pop = this.orders.pop();
            System.out.printf("Store used on %d of %d\n", used.get(), capacity);
            return pop;
        } finally {
            ordersLock.unlock();
        }
    }

    public void setAllCooked() {
        ordersLock.lock();
        allCooked.set(true);
        notEmpty.signal();
        ordersLock.unlock();
    }

    public boolean isAllCooked() {
        ordersLock.lock();
        var val = allCooked.get();
        ordersLock.unlock();
        return val;
    }
}
