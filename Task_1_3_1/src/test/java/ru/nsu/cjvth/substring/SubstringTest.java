package ru.nsu.cjvth.substring;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import org.junit.jupiter.api.Test;

class SubstringTest {

    @Test
    void testEmptyArrayZ() {
        String string = "";
        int[] result = Substring.arrayZfunction(string.toCharArray());
        assertArrayEquals(new int[0], result);
    }

    @Test
    void testSingletonArrayZ() {
        String string = "!";
        int[] result = Substring.arrayZfunction(string.toCharArray());
        assertArrayEquals(new int[]{1}, result);
    }

    @Test
    void testUniqueArrayZ() {
        String string = "5621308fasjb";
        int[] result = Substring.arrayZfunction(string.toCharArray());
        assertArrayEquals(new int[]{12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, result);
    }

    @Test
    void testSameArrayZ() {
        String string = "aaaaaaaa";
        int[] result = Substring.arrayZfunction(string.toCharArray());
        assertArrayEquals(new int[]{8, 7, 6, 5, 4, 3, 2, 1}, result);
    }

    @Test
    void testSeparatorArrayZ() {
        String string = "aaaabaaaa";
        int[] result = Substring.arrayZfunction(string.toCharArray());
        assertArrayEquals(new int[]{9, 3, 2, 1, 0, 4, 3, 2, 1}, result);
    }

    @Test
    void testDifferentArrayZ() {
        String string = "ababobaboobabab";
        int[] result = Substring.arrayZfunction(string.toCharArray());
        int[] expected = new int[]{15, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 4, 0, 2, 0};
        assertArrayEquals(expected, result);
    }

    @Test
    void testEmptyTemplateSubstring() {
        String string = "abobabababa";
        assertThrows(IllegalArgumentException.class,
            () -> Substring.getSubstringEntries("", new StringReader(string)));
    }

    @Test
    void testEmptyStringSubstring() throws IOException {
        String string = "";
        List<Integer> result = Substring.getSubstringEntries("aba",
            new StringReader(string));
        assertIterableEquals(List.of(), result);
    }

    @Test
    void testSingleTemplateSubstring() throws IOException {
        String string = "aaaaaaa";
        List<Integer> result = Substring.getSubstringEntries("a",
            new StringReader(string));
        assertIterableEquals(List.of(0, 1, 2, 3, 4, 5, 6), result);
    }

    @Test
    void test3BufferStringSubstring() throws IOException {
        String string = "abadab";
        List<Integer> result = Substring.getSubstringEntries("ab",
            new StringReader(string));
        assertIterableEquals(List.of(0, 4), result);
    }

    @Test
    void test4BufferStringSubstring() throws IOException {
        String string = "dbabadab";
        List<Integer> result = Substring.getSubstringEntries("ab",
            new StringReader(string));
        assertIterableEquals(List.of(2, 6), result);
    }

    @Test
    void testSimpleSubstring() throws IOException {
        String string = "abobabababo";
        List<Integer> result = Substring.getSubstringEntries("aba",
            new StringReader(string));
        assertIterableEquals(List.of(4, 6), result);
    }

    @Test
    void testRepeatedSubstring() throws IOException {
        String string = "abababababobababa";
        List<Integer> result = Substring.getSubstringEntries("ababa",
            new StringReader(string));
        assertIterableEquals(List.of(0, 2, 4, 12), result);
    }
}
