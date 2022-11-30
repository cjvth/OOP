package ru.nsu.cjvth.substring;


import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
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

        char[] buffer = new char[3 * len];
        System.arraycopy(template.toCharArray(), 0, buffer, 0, len);

        int[] zfunction = new int[2 * len];

        zfunction[0] = len;
        utilZfunction(buffer, zfunction, 1, len, len, len, new Last(-1, -1));
        int[] prefixes = Arrays.copyOfRange(zfunction, 0, len);

        int countRead = reader.read(buffer, len, len);
        boolean stop = false;

        int pos = 0;
        int lastBegin = -1;
        int lastEnd = -1;
        int maxIndex;
        while (countRead == len || !stop) {
            System.arraycopy(buffer, len, buffer, 0, len);
            if (countRead == len) {
                countRead = reader.read(buffer, len, len);
                if (countRead == -1) {
                    countRead = 0;
                }
                maxIndex = len + countRead;
            } else {
                stop = true;
                maxIndex = countRead;
            }
//            lastBegin -= len;
//            lastEnd -= len;
            int iterationLimit = stop ? countRead : len;
            for (int i = 0; i < iterationLimit; i++, pos++) {
                int pref;
                if (pos >= lastEnd) {
                    pref = 0;
                } else if (pos + prefixes[pos - lastBegin] >= lastEnd) {
                    pref = Integer.min(prefixes[pos - lastBegin], lastEnd - pos);
                } else {
                    continue;
                }
                for (int j = i + pref; j < i + len && j < maxIndex; j++) {
                    if (buffer[j] != array[pref]) {
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
        }
        return entries;
    }

    private static void utilZfunction(char[] charArray, int[] result, int from, int to,
        int substrLen, int iterationLimit, Last last) {
        for (int i = from; i < to; i++) {
            if (i < last.end && i + result[i - last.begin] < last.end) {
                result[i] = result[i - last.begin];
            } else {
                int pref = Integer.max(last.end - i, 0);
                for (int j = i + pref; j < iterationLimit && pref < substrLen; j++, pref++) {
                    if (charArray[j] != charArray[pref]) {
                        break;
                    }
                }
                result[i] = pref;
                if (pref > 1) {
                    last.begin = i;
                    last.end = i + pref;
                }
            }
        }
    }

    /**
     * Calculate Z-function for given array of chars.
     *
     * @param array non-empty char array, on which Z-function is calculated
     * @return int array, i-th element of which is the length of prefix between the char array and
     * its suffix that starts from i-th element
     */
    public static int[] arrayZfunction(char[] array) {
        int len = array.length;
        int[] prefixes = new int[len];
        if (len == 0) {
            return prefixes;
        }
        prefixes[0] = len;
        Last last = new Last(-1, -1);
        utilZfunction(array, prefixes, 1, len, len, len, last);
        return prefixes;
    }

    private static class Last {

        public int begin;
        public int end;

        public Last(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }
    }
}
