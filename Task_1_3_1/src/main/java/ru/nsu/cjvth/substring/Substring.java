package ru.nsu.cjvth.substring;


import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
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

        char[] buffer = new char[2 * len];

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
            int iterationLimit = stop ? countRead : len;
            for (int i = 0; i < iterationLimit; i++, pos++) {
                if (pos + prefixes[pos - lastBegin] >= lastEnd) {
                    int pref = Integer.max(0,
                        Integer.min(prefixes[pos - lastBegin], lastEnd - pos));
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
            if (i < lastEnd && i + prefixes[i - lastBegin] < lastEnd) {
                prefixes[i] = prefixes[i - lastBegin];
            } else {
                int pref = Integer.max(lastEnd - i, 0);
                for (int j = i + pref; j < len; j++, pref++) {
                    if (array[j] != array[pref]) {
                        break;
                    }
                }
                prefixes[i] = pref;
                if (pref > 1) {
                    lastBegin = i;
                    lastEnd = i + pref;
                }
            }
        }
        return prefixes;
    }
}
