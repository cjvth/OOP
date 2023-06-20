package ru.nsu.cjvth.pizza;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


class ManagerTest {

    private static class PizzeriaConfig {
        int n_orders;
        int store_capacity;
        List<CookConfig> cooks;
        List<DeliveryConfig> delivery;

        public PizzeriaConfig(int n_orders, int store_capacity) {
            this.n_orders = n_orders;
            this.store_capacity = store_capacity;
            this.cooks = new ArrayList<>();
            this.delivery = new ArrayList<>();
        }

        public static class CookConfig {
            public CookConfig(int cook_time) {
                this.cook_time = cook_time;
            }

            public int cook_time;
        }

        public static class DeliveryConfig {
            public int delivery_time;

            public DeliveryConfig(int delivery_time) {
                this.delivery_time = delivery_time;
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
            List<LogEntry> log = new ArrayList<>(config.n_orders * 4);
            Orders availableOrders = new Orders(config.n_orders);
            Store store = new Store(config.store_capacity);
            List<Cook> cooks = new ArrayList<>(config.cooks.size());
            int n = 0;
            for (var i : config.cooks) {
                n++;
                cooks.add(new Cook(n, i.cook_time, availableOrders, store, log));
            }
            List<Delivery> delivery = new ArrayList<>(config.cooks.size());
            n = 0;
            for (var i : config.delivery) {
                n++;
                delivery.add(new Delivery(n, i.delivery_time, store, log));
            }
            Manager manager = new Manager(store, cooks, delivery);
            manager.start();
        }


//        int orders = 5;
//        var store = new Store(3);
//        List<LogEntry> log = new ArrayList<>(100);
//        Orders availableOrders = new Orders(orders);
//        int id = 4;
//        var cook = new Thread(new Cook(id, 200, availableOrders, store, log));
//        cook.start();
//        for (int i = 0; i < orders; i++) {
//            store.take();
//        }
//        cook.join();
//        List<LogEntry.OrderStatus> orderStatuses = new ArrayList<>(Collections.nCopies(
//                5, LogEntry.OrderStatus.NOT_STARTED));
//        for (LogEntry i : log) {
//            assertEquals(id, i.getActor());
//            assertEquals(LogEntry.ActorType.COOK, i.getActorType());
//            int order_index = i.getOrder() - 1;
//            assertTrue(order_index >= 0 && order_index < orders);
//            switch (i.getOrderStatus()) {
//                case COOK_START:
//                    assertEquals(LogEntry.OrderStatus.NOT_STARTED, orderStatuses.get(order_index));
//                    orderStatuses.set(order_index, LogEntry.OrderStatus.COOK_START);
//                    break;
//                case COOK_FINISH:
//                    assertEquals(LogEntry.OrderStatus.COOK_START, orderStatuses.get(order_index));
//                    orderStatuses.set(order_index, LogEntry.OrderStatus.COOK_FINISH);
//                    break;
//                default:
//                    fail(String.format("Illegal event %s", i.getOrderStatus().toString()));
//            }
//        }
//        for (LogEntry.OrderStatus j : orderStatuses) {
//            assertEquals(LogEntry.OrderStatus.COOK_FINISH, j);
//        }
    }
}