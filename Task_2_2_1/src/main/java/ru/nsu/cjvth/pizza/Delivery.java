package ru.nsu.cjvth.pizza;

import java.util.List;

@SuppressWarnings("BusyWait")
public class Delivery implements Runnable {
    private final int id;
    private final int deliveryTime;
    private final Store store;
    private final List<LogEntry> log;

    public Delivery(int id, int deliveryTime,
                    Store store, List<LogEntry> log) {
        this.id = id;
        this.deliveryTime = deliveryTime;
        this.store = store;
        this.log = log;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Integer order;
                System.out.printf("Delivery %d is waiting\n", id);
                if (store.isAllCooked()) {
                    order = store.takeNotWait();
                    if (order == null) {
                        System.out.printf("Delivery %d is done\n", id);
                        return;
                    }
                } else {
                    order = store.take();
                    if (order == null) {
                        System.out.printf("Delivery %d is done\n", id);
                        return;
                    }
                }
                System.out.printf("Delivery %d took order %d\n", id, order);
                synchronized (log) {
                    log.add(new LogEntry(id, LogEntry.ActorType.DELIVERY, order,
                            LogEntry.OrderStatus.DELIVERY_START));
                }
                Thread.sleep(deliveryTime);
                System.out.printf("Delivery %d delivered order %d\n", id, order);
                synchronized (log) {
                    log.add(new LogEntry(id, LogEntry.ActorType.DELIVERY, order,
                            LogEntry.OrderStatus.DELIVERY_FINISH));
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
