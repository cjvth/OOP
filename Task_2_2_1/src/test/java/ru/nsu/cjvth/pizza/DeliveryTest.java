package ru.nsu.cjvth.pizza;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class DeliveryTest {
    @Test
    void deliveryTest() throws InterruptedException {
        int n_orders = 5;
        var store = new Store(3);
        List<LogEntry> log = new ArrayList<>(100);
        Orders availableOrders = new Orders(n_orders);
        int id = 6;
        var delivery = new Thread(new Delivery(id, 200, availableOrders, store, log));
        delivery.start();
        for (int i = 1; i <= n_orders; i++) {
            store.put(i);
        }
        availableOrders.setAllCooked();
        delivery.join();
        List<LogEntry.OrderStatus> orderStatuses = new ArrayList<>(Collections.nCopies(
                5, LogEntry.OrderStatus.COOK_FINISH));
        for (LogEntry i : log) {
            assertEquals(id, i.getActor());
            assertEquals(LogEntry.ActorType.DELIVERY, i.getActorType());
            int order_index = i.getOrder() - 1;
            assertTrue(order_index >= 0 && order_index < n_orders);
            switch (i.getOrderStatus()) {
                case DELIVERY_START:
                    assertEquals(LogEntry.OrderStatus.COOK_FINISH, orderStatuses.get(order_index));
                    orderStatuses.set(order_index, LogEntry.OrderStatus.DELIVERY_START);
                    break;
                case DELIVERY_FINISH:
                    assertEquals(LogEntry.OrderStatus.DELIVERY_START, orderStatuses.get(order_index));
                    orderStatuses.set(order_index, LogEntry.OrderStatus.DELIVERY_FINISH);
                    break;
                default:
                    fail(String.format("Illegal event %s", i.getOrderStatus().toString()));
            }
        }
        for (LogEntry.OrderStatus j: orderStatuses) {
            assertEquals(LogEntry.OrderStatus.DELIVERY_FINISH, j);
        }
    }
}