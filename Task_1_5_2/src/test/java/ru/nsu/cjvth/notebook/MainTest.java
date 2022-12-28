package ru.nsu.cjvth.notebook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class MainTest {

    private static final String dateTimeFormatString = "dd.MM.yyyy HH:mm:ss";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
        dateTimeFormatString);

    @Test
    void readNotebook() throws IOException {
        Reader reader = new StringReader(
            "{\"notes\":{\"1\":{\"lastEdit\":1672235489183,\"text\":\"2\"}, "
                + "\"4\": {\"lastEdit\":1672236489183,\"text\":\"5\"}}}");
        Notebook notebook = Main.readNotebook(reader);
        var show = notebook.show(null, null, null).stream()
            .map(e -> Map.entry(e.getKey(), e.getValue().text())).collect(Collectors.toList());
        assertIterableEquals(List.of(Map.entry("1", "2"), Map.entry("4", "5")), show);
    }

    @Test
    void writeNotebook() throws IOException {
        Notebook notebook = new Notebook();
        notebook.add("0", "2");
        notebook.add("3", "4");
        Writer writer = new StringWriter();
        Main.writeNotebook(writer, notebook);
        String json = writer.toString();
        Reader reader = new StringReader(json);
        Notebook notebook2 = Main.readNotebook(reader);
        var show = notebook2.show(null, null, null).stream()
            .map(e -> Map.entry(e.getKey(), e.getValue().text())).collect(Collectors.toList());
        assertIterableEquals(List.of(Map.entry("0", "2"), Map.entry("3", "4")), show);
    }

    @Test
    void parseAdd() {
        Notebook notebook = new Notebook();
        assertEquals("", Main.parseArgs(new String[]{"add", "1", "123"}, notebook));
        assertEquals("", Main.parseArgs(new String[]{"add", "2", "234"}, notebook));
        assertNotEquals("", Main.parseArgs(new String[]{"add", "1", "124"}, notebook));
        var show = notebook.show(null, null, null).stream()
            .map(e -> Map.entry(e.getKey(), e.getValue().text())).collect(Collectors.toList());
        assertIterableEquals(List.of(Map.entry("1", "123"), Map.entry("2", "234")), show);
    }

    @Test
    void parseRemove() {
        Notebook notebook = new Notebook();
        Main.parseArgs(new String[]{"add", "1", "123"}, notebook);
        Main.parseArgs(new String[]{"add", "2", "234"}, notebook);
        Main.parseArgs(new String[]{"add", "3", "345"}, notebook);
        Main.parseArgs(new String[]{"add", "4", "456"}, notebook);
        assertEquals("", Main.parseArgs(new String[]{"remove", "3"}, notebook));
        assertNotEquals("", Main.parseArgs(new String[]{"remove", "5"}, notebook));
        assertNotEquals("", Main.parseArgs(new String[]{"remove", "3"}, notebook));
        assertEquals("", Main.parseArgs(new String[]{"remove", "2"}, notebook));
        var show = notebook.show(null, null, null).stream()
            .map(e -> Map.entry(e.getKey(), e.getValue().text())).collect(Collectors.toList());
        assertIterableEquals(List.of(Map.entry("1", "123"), Map.entry("4", "456")), show);
    }

    @Test
    void parseShowKeywords() {
        Notebook notebook = new Notebook();
        Main.parseArgs(new String[]{"add", "aaa bbb ccc", "123"}, notebook);
        Main.parseArgs(new String[]{"add", "bbb ccc fff", "34 56"}, notebook);
        Main.parseArgs(new String[]{"add", "eee ggg mmm", "456"}, notebook);
        Main.parseArgs(new String[]{"add", "aaa ddd eee", "234"}, notebook);
        Main.parseArgs(new String[]{"add", "ggg 123 fds", "456"}, notebook);

        String[] showLines = Main.parseArgs(new String[]{"show", "-f", "mmm", "--find", "aaa"},
            notebook).split("\n");
        assertEquals("bbb ccc fff", showLines[0]);
        assertEquals("34 56", showLines[2]);
        assertEquals("ggg 123 fds", showLines[6]);
        assertEquals("456", showLines[8]);
    }

    @Test
    void parseShowTime() {
        Notebook notebook = new Notebook();
        final LocalDateTime t1 = LocalDateTime.now().minusMinutes(2);
        notebook.add("1", "123");
        notebook.add("2", "234");
        final LocalDateTime t2 = LocalDateTime.now().plusMinutes(2);

        String[] showLines1 = Main.parseArgs(new String[]{"show", "--after",
            "\"" + dateTimeFormatter.format(ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(t1.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()),
                ZoneId.systemDefault())) + "\""}, notebook).split("\n");
        assertEquals("1", showLines1[0]);
        assertEquals("123", showLines1[2]);
        assertEquals("2", showLines1[6]);
        assertEquals("234", showLines1[8]);

        String[] showLines2 = Main.parseArgs(new String[]{"show", "-b",
            "\"" + dateTimeFormatter.format(ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(t2.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()),
                ZoneId.systemDefault())) + "\""}, notebook).split("\n");
        assertEquals("1", showLines2[0]);
        assertEquals("123", showLines2[2]);
        assertEquals("2", showLines2[6]);
        assertEquals("234", showLines2[8]);

        String[] showLines3 = Main.parseArgs(new String[]{"show", "-a",
            "\"" + dateTimeFormatter.format(ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(t2.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()),
                ZoneId.systemDefault())) + "\"", "--before", "\"" + dateTimeFormatter.format(
            ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(t2.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()),
                ZoneId.systemDefault())) + "\""}, notebook).split("\n");
        assertEquals(1, showLines3.length);
    }
}