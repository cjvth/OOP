package ru.nsu.cjvth.pizza;

@SuppressWarnings("BusyWait")
public class Cook implements Runnable {
    private final int id;
    private final int cookingTime;
    private final Orders availableOrders;
    private final Store store;

    public Cook(int id, int cookingTime, Orders availableOrders, Store store) {
        this.id = id;
        this.cookingTime = cookingTime;
        this.availableOrders = availableOrders;
        this.store = store;
    }


    @Override
    public void run() {
        try {
            while (true) {
                Integer order;
                System.out.printf("Cook %d is waiting\n", id);
                synchronized (availableOrders) {
                    order = availableOrders.take();
                    if (order == null) {
                        System.out.printf("Cook %d is done\n", id);
                        return;
                    }
                }
                System.out.printf("Cook %d took order %d\n", id, order);
                Thread.sleep(cookingTime);
                System.out.printf("Cook %d finished order %d\n", id, order);
                store.put(order);
                System.out.printf("Cook %d put to store order %d\n", id, order);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
