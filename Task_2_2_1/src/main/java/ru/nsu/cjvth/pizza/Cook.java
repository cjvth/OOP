package ru.nsu.cjvth.pizza;

import java.util.List;

/**
 * A cook that can make pizza in some amount of time.
 */
@SuppressWarnings("BusyWait")
public class Cook implements Runnable {
    private final int id;
    private final int cookingTime;
    private final Orders availableOrders;
    private final Store store;
    private final List<LogEntry> log;

    /**
     * Constructor.
     *
     * @param id              cook's id
     * @param cookingTime     time to make one pizza
     * @param availableOrders a shared container with orders that were not started
     * @param store           place to store cooked pizza before delivery takes it,
     *                        shared among cooks and delivery
     * @param log             log of status messages from cooks and delivery
     */
    public Cook(int id, int cookingTime, Orders availableOrders, Store store, List<LogEntry> log) {
        this.id = id;
        this.cookingTime = cookingTime;
        this.availableOrders = availableOrders;
        this.store = store;
        this.log = log;
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
                synchronized (log) {
                    log.add(new LogEntry(id, LogEntry.ActorType.COOK, order,
                            LogEntry.OrderStatus.COOK_START));
                }
                Thread.sleep(cookingTime);
                System.out.printf("Cook %d finished order %d\n", id, order);
                synchronized (log) {
                    log.add(new LogEntry(id, LogEntry.ActorType.COOK, order,
                            LogEntry.OrderStatus.COOK_FINISH));
                }
                store.put(order);
                System.out.printf("Cook %d put to store order %d\n", id, order);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
