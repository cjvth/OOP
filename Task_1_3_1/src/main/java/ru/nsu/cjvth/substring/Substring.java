package ru.nsu.cjvth.substring;


import java.io.IOException;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

/**
 * Class with a static function.
 */
public class Substring {

    /**
     * Find all positions of reader stream, that a sequence of symbols from that position matches
     * the template.
     *
     * @param template the substring, non-empty string to be found in the stream
     * @param reader   stream of characters, where substring is being searched
     * @return list of positions, at which substring entries are found in stream
     */
    public static List<Integer> getSubstringEntries(String template, Reader reader)
        throws IOException {
        if (template.length() == 0) {
            throw new IllegalArgumentException("Template substring must be non-empty");
        }
        List<Integer> entries = new ArrayList<>();
        int len = template.length();
        char[] array = template.toCharArray();
        int[] prefixes = arrayZfunction(array);

        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < len; i++) {
            queue.add(reader.read());
        }
        int pos = 0;
        int lastBegin = -1;
        int lastEnd = -1;
        while (queue.getFirst() != -1) {
            if (pos >= lastEnd) {
                int pref = 0;
                for (int i : queue) {
                    if (i != array[pref]) {
                        break;
                    }
                    pref++;
                }
                lastBegin = pos;
                lastEnd = pos + pref;
                if (pref == len) {
                    entries.add(pos);
                }
            } else if (pos + prefixes[pos - lastBegin] >= lastEnd) {
                // Unfortunately, I can't get needed element by index, so iterating
                Iterator<Integer> iter = queue.iterator();
                int pref = lastEnd - pos;
                for (int i = 0; i < pref; i++) {
                    iter.next(); // pref is less than queue's size
                }
                while (iter.hasNext()) {
                    if (iter.next() != array[pref]) {
                        break;
                    }
                    pref++;
                }
                lastBegin = pos;
                lastEnd = pos + pref;
                if (pref == len) {
                    entries.add(pos);
                }
            }
            // if pos + prefixes[pos - lastBegin] < lastEnd then the prefix is less than len
            pos++;
            queue.removeFirst();
            queue.addLast(reader.read());
        }
        return entries;
    }

    /**
     * Calculate Z-function for given array of chars.
     *
     * @param array non-empty char array, on which Z-function is calculated
     * @return int array, i-th element of which is the length of prefix between the char array and
     *      its suffix that starts from i-th element
     */
    public static int[] arrayZfunction(char[] array) {
        int len = array.length;
        int[] prefixes = new int[len];
        if (len == 0) {
            return prefixes;
        }
        prefixes[0] = len;
        int lastBegin = -1;
        int lastEnd = -1;
        for (int i = 1; i < len; i++) {
            if (i >= lastEnd) {
                for (int j = i, k = 0; j < len; j++, k++) {
                    if (array[j] != array[k]) {
                        break;
                    }
                    prefixes[i]++;
                }
                if (prefixes[i] > 1) {
                    lastBegin = i;
                    lastEnd = i + prefixes[i];
                }
            } else if (i + prefixes[i - lastBegin] < lastEnd) {
                prefixes[i] = prefixes[i - lastBegin];
            } else {
                int known = lastEnd - i;
                int j = lastEnd;
                for (int k = known; j < len; j++, k++) {
                    if (array[j] != array[k]) {
                        break;
                    }
                    known++;
                }
                prefixes[i] = known;
                lastBegin = i;
                lastEnd = j;
            }
        }
        return prefixes;
    }
}
