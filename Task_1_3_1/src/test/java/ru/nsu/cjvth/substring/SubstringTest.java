package ru.nsu.cjvth.substring;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class SubstringTest {

    @Test
    void testSimpleString() {
        String string = "abobababoba";
        List<Integer> result = Substring.getSubstringEntries("abo", new StringReader(string));
        assertIterableEquals(Arrays.asList(0, 6), result);
    }
}