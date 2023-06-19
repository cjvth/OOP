package ru.nsu.cjvth.pizza;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StoreTest {
    @Test
    void storeTest() throws InterruptedException {
        var store = new Store(3);
        store.put(6);
        store.put(1);
        assertEquals(6, store.take());
        store.put(8);
        assertEquals(1, store.take());
        assertEquals(8, store.take());
    }
    // Хорошо бы проверить случаи с полным и пустым, но это надо запариться, а времени мало

    @Test
    void takeNotWaitTest() throws InterruptedException {
        var store = new Store(10);
        assertNull(store.takeNotWait());
    }
}