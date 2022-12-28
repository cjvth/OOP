package ru.nsu.cjvth.notebook;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class NotebookTest {

    @Test
    void add() {
        Notebook notebook = new Notebook();
        assertTrue(notebook.add("1", "123"));
        assertTrue(notebook.add("2", "234"));
        assertFalse(notebook.add("1", "124"));
        var show = notebook.show(null, null, null).stream()
            .map(e -> Map.entry(e.getKey(), e.getValue().text())).collect(Collectors.toList());
        assertIterableEquals(List.of(Map.entry("1", "123"), Map.entry("2", "234")), show);
    }

    @Test
    void remove() {
        Notebook notebook = new Notebook();
        notebook.add("1", "123");
        notebook.add("2", "234");
        notebook.add("3", "345");
        notebook.add("4", "456");
        assertTrue(notebook.remove("3"));
        assertFalse(notebook.remove("5"));
        assertFalse(notebook.remove("3"));
        assertTrue(notebook.remove("2"));
        var show = notebook.show(null, null, null).stream()
            .map(e -> Map.entry(e.getKey(), e.getValue().text())).collect(Collectors.toList());
        assertIterableEquals(List.of(Map.entry("1", "123"), Map.entry("4", "456")), show);
    }

    @Test
    void keywords() {
        Notebook notebook = new Notebook();
        notebook.add("aaa bbb ccc", "123");
        notebook.add("bbb ccc fff", "345");
        notebook.add("eee ggg mmm", "456");
        notebook.add("aaa ddd eee", "234");
        notebook.add("ggg 123 fds", "456");
        var show = notebook.show(new String[]{"mmm", "aaa"}, null, null).stream()
            .map(e -> Map.entry(e.getKey(), e.getValue().text())).collect(Collectors.toList());
        assertIterableEquals(
            List.of(Map.entry("bbb ccc fff", "345"), Map.entry("ggg 123 fds", "456")), show);
    }

    @Test
    void time() throws InterruptedException {
        Notebook notebook = new Notebook();
        Thread.sleep(1);
        final LocalDateTime t1 = LocalDateTime.now();
        Thread.sleep(3);
        notebook.add("1", "123");
        Thread.sleep(1);
        final LocalDateTime t2 = LocalDateTime.now();
        Thread.sleep(0);
        notebook.add("2", "234");
        Thread.sleep(1);
        final LocalDateTime t3 = LocalDateTime.now();
        Thread.sleep(1);
        notebook.add("3", "345");
        Thread.sleep(3);
        final LocalDateTime t4 = LocalDateTime.now();
        Thread.sleep(1);
        notebook.add("4", "456");
        Thread.sleep(3);
        final LocalDateTime t5 = LocalDateTime.now();

        var show1 = notebook.show(null, t3, null).stream()
            .map(e -> Map.entry(e.getKey(), e.getValue().text())).collect(Collectors.toList());
        assertIterableEquals(List.of(Map.entry("3", "345"), Map.entry("4", "456")), show1);

        var show2 = notebook.show(null, null, t4).stream()
            .map(e -> Map.entry(e.getKey(), e.getValue().text())).collect(Collectors.toList());
        assertIterableEquals(
            List.of(Map.entry("1", "123"), Map.entry("2", "234"), Map.entry("3", "345")), show2);

        var show3 = notebook.show(null, t1, t3).stream()
            .map(e -> Map.entry(e.getKey(), e.getValue().text())).collect(Collectors.toList());
        assertIterableEquals(List.of(Map.entry("1", "123"), Map.entry("2", "234")), show3);

        var show4 = notebook.show(null, t2, t5).stream()
            .map(e -> Map.entry(e.getKey(), e.getValue().text())).collect(Collectors.toList());
        assertIterableEquals(
            List.of(Map.entry("2", "234"), Map.entry("3", "345"), Map.entry("4", "456")), show4);

        var show5 = notebook.show(null, t4, t3).stream()
            .map(e -> Map.entry(e.getKey(), e.getValue().text())).collect(Collectors.toList());
        assertIterableEquals(List.of(), show5);
    }
}