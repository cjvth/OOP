package ru.nsu.cjvth.pizza;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ManagerTest {

    private static class PizzeriaConfig {
        int numOrders;
        int storeCapacity;
        List<CookConfig> cooks;
        List<DeliveryConfig> delivery;

        public PizzeriaConfig(int numOrders, int storeCapacity) {
            this.numOrders = numOrders;
            this.storeCapacity = storeCapacity;
            this.cooks = new ArrayList<>();
            this.delivery = new ArrayList<>();
        }

        public static class CookConfig {
            public CookConfig(int cookTime) {
                this.cookTime = cookTime;
            }

            public int cookTime;
        }

        public static class DeliveryConfig {
            public int deliveryTime;

            public DeliveryConfig(int deliveryTime) {
                this.deliveryTime = deliveryTime;
            }
        }
    }

    private static PizzeriaConfig readConfig(Reader reader) throws IOException {
        Gson gson = new Gson();
        return gson.getAdapter(PizzeriaConfig.class).fromJson(reader);
    }

    private static void writeConfig(Writer writer, PizzeriaConfig notebook) throws IOException {
        Gson gson = new Gson();
        gson.getAdapter(PizzeriaConfig.class).toJson(writer, notebook);
    }

    @Test
    void makeTest() throws IOException {
        var pizzeria = new PizzeriaConfig(10, 4);
        pizzeria.cooks.add(new PizzeriaConfig.CookConfig(1000));
        pizzeria.cooks.add(new PizzeriaConfig.CookConfig(1200));
        pizzeria.cooks.add(new PizzeriaConfig.CookConfig(900));
        pizzeria.cooks.add(new PizzeriaConfig.CookConfig(1000));
        pizzeria.delivery.add(new PizzeriaConfig.DeliveryConfig(800));
        pizzeria.delivery.add(new PizzeriaConfig.DeliveryConfig(700));
        pizzeria.delivery.add(new PizzeriaConfig.DeliveryConfig(600));
        try (var writer = new FileWriter("src/test/resources/sample.json")) {
            writeConfig(writer, pizzeria);
        }
    }

    @ParameterizedTest(name = "Testing with file {0}")
    @ValueSource(strings = {"test1.json", "test2.json"})
    void managerTest(String fileName) throws InterruptedException, IOException {
        try (var reader = new FileReader("src/test/resources/".concat(fileName))) {
            PizzeriaConfig config = readConfig(reader);
            List<LogEntry> log = new ArrayList<>(config.numOrders * 4);
            Orders availableOrders = new Orders(config.numOrders);
            Store store = new Store(config.storeCapacity);
            List<Cook> cooks = new ArrayList<>(config.cooks.size());
            int n = 0;
            for (var i : config.cooks) {
                n++;
                cooks.add(new Cook(n, i.cookTime, availableOrders, store, log));
            }
            List<Delivery> delivery = new ArrayList<>(config.cooks.size());
            n = 0;
            for (var i : config.delivery) {
                n++;
                delivery.add(new Delivery(n, i.deliveryTime, store, log));
            }
            Manager manager = new Manager(store, cooks, delivery);
            manager.start();


            List<LogEntry.OrderStatus> orderStatuses = new ArrayList<>(Collections.nCopies(
                    config.numOrders, LogEntry.OrderStatus.NOT_STARTED));
            List<LogEntry.OrderStatus> cookStatuses = new ArrayList<>(Collections.nCopies(
                    config.cooks.size(), LogEntry.OrderStatus.COOK_FINISH));
            List<LogEntry.OrderStatus> deliveryStatuses = new ArrayList<>(Collections.nCopies(
                    config.delivery.size(), LogEntry.OrderStatus.DELIVERY_FINISH));
            for (LogEntry i : log) {
                int orderIndex = i.getOrder() - 1;
                int actorIndex = i.getActor() - 1;
                assertTrue(orderIndex >= 0 && orderIndex < config.numOrders);
                switch (i.getOrderStatus()) {
                    case COOK_START:
                        assertEquals(LogEntry.OrderStatus.NOT_STARTED, orderStatuses.get(orderIndex));
                        orderStatuses.set(orderIndex, LogEntry.OrderStatus.COOK_START);
                        break;
                    case COOK_FINISH:
                        assertEquals(LogEntry.OrderStatus.COOK_START, orderStatuses.get(orderIndex));
                        orderStatuses.set(orderIndex, LogEntry.OrderStatus.COOK_FINISH);
                        break;
                    case DELIVERY_START:
                        assertEquals(LogEntry.OrderStatus.COOK_FINISH, orderStatuses.get(orderIndex));
                        orderStatuses.set(orderIndex, LogEntry.OrderStatus.DELIVERY_START);
                        break;
                    case DELIVERY_FINISH:
                        assertEquals(LogEntry.OrderStatus.DELIVERY_START, orderStatuses.get(orderIndex));
                        orderStatuses.set(orderIndex, LogEntry.OrderStatus.DELIVERY_FINISH);
                        break;
                    default:
                        fail(String.format("Illegal event for an order%s", i.getOrderStatus().toString()));
                }
                if (i.getActorType() == LogEntry.ActorType.COOK) {
                    assertTrue(actorIndex >= 0 && actorIndex < config.cooks.size());
                    switch (i.getOrderStatus()) {
                        case COOK_START:
                            assertEquals(LogEntry.OrderStatus.COOK_FINISH, cookStatuses.get(actorIndex));
                            cookStatuses.set(actorIndex, LogEntry.OrderStatus.COOK_START);
                            break;
                        case COOK_FINISH:
                            assertEquals(LogEntry.OrderStatus.COOK_START, cookStatuses.get(actorIndex));
                            cookStatuses.set(actorIndex, LogEntry.OrderStatus.COOK_FINISH);
                            break;
                        default:
                            fail(String.format("Illegal event for a cook%s", i.getOrderStatus().toString()));
                    }
                } else {
                    assertTrue(actorIndex >= 0 && actorIndex < config.delivery.size());
                    switch (i.getOrderStatus()) {
                        case DELIVERY_START:
                            assertEquals(LogEntry.OrderStatus.DELIVERY_FINISH, deliveryStatuses.get(actorIndex));
                            deliveryStatuses.set(actorIndex, LogEntry.OrderStatus.DELIVERY_START);
                            break;
                        case DELIVERY_FINISH:
                            assertEquals(LogEntry.OrderStatus.DELIVERY_START, deliveryStatuses.get(actorIndex));
                            deliveryStatuses.set(actorIndex, LogEntry.OrderStatus.DELIVERY_FINISH);
                            break;
                        default:
                            fail(String.format("Illegal event for delivery%s", i.getOrderStatus().toString()));
                    }
                }
            }
            for (LogEntry.OrderStatus j : orderStatuses) {
                assertEquals(LogEntry.OrderStatus.DELIVERY_FINISH, j);
            }
            for (LogEntry.OrderStatus j : cookStatuses) {
                assertEquals(LogEntry.OrderStatus.COOK_FINISH, j);
            }
            for (LogEntry.OrderStatus j : deliveryStatuses) {
                assertEquals(LogEntry.OrderStatus.DELIVERY_FINISH, j);
            }
        }
    }
}