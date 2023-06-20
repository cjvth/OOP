package ru.nsu.cjvth.pizza;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class CookTest {
    @Test
    void cookTest() throws InterruptedException {
        int orders = 5;
        var store = new Store(3);
        List<LogEntry> log = new ArrayList<>(100);
        Orders availableOrders = new Orders(orders);
        int id = 4;
        var cook = new Thread(new Cook(id, 200, availableOrders, store, log));
        cook.start();
        for (int i = 0; i < orders; i++) {
            store.take();
        }
        cook.join();
        List<LogEntry.OrderStatus> orderStatuses = new ArrayList<>(Collections.nCopies(
                5, LogEntry.OrderStatus.NOT_STARTED));
        for (LogEntry i : log) {
            assertEquals(id, i.getActor());
            assertEquals(LogEntry.ActorType.COOK, i.getActorType());
            int orderIndex = i.getOrder() - 1;
            assertTrue(orderIndex >= 0 && orderIndex < orders);
            switch (i.getOrderStatus()) {
                case COOK_START:
                    assertEquals(LogEntry.OrderStatus.NOT_STARTED, orderStatuses.get(orderIndex));
                    orderStatuses.set(orderIndex, LogEntry.OrderStatus.COOK_START);
                    break;
                case COOK_FINISH:
                    assertEquals(LogEntry.OrderStatus.COOK_START, orderStatuses.get(orderIndex));
                    orderStatuses.set(orderIndex, LogEntry.OrderStatus.COOK_FINISH);
                    break;
                default:
                    fail(String.format("Illegal event %s", i.getOrderStatus().toString()));
            }
        }
        for (LogEntry.OrderStatus j: orderStatuses) {
            assertEquals(LogEntry.OrderStatus.COOK_FINISH, j);
        }
    }
}