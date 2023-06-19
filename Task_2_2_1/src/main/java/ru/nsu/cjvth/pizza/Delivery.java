package ru.nsu.cjvth.pizza;

@SuppressWarnings("BusyWait")
public class Delivery implements Runnable {
    private final int id;
    private final int deliveryTime;
    private final Orders availableOrders;
    private final Store store;

    public Delivery(int id, int deliveryTime, Orders availableOrders, Store store) {
        this.id = id;
        this.deliveryTime = deliveryTime;
        this.availableOrders = availableOrders;
        this.store = store;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Integer order;
                System.out.printf("Delivery %d is waiting\n", id);
                if (availableOrders.isAllCooked()) {
                    order = store.takeNotWait();
                    if (order == null) {
                        System.out.printf("Delivery %d is done\n", id);
                        return;
                    }
                } else {
                    order = store.take();
                }
                System.out.printf("Delivery %d took order %d\n", id, order);
                Thread.sleep(deliveryTime);
                System.out.printf("Delivery %d delivered order %d\n", id, order);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
