package ru.nsu.cjvth.notebook;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Class that stores the notes.
 */
public class Notebook {

    private final Map<String, Note> notes;

    /**
     * Create empty map.
     */
    public Notebook() {
        notes = new HashMap<>();
    }

    /**
     * Add new note.
     *
     * @param title title of the note
     * @param text  body of the note
     * @return true if the note was added, false if this name is already occupied
     */
    public boolean add(String title, String text) {
        if (notes.containsKey(title)) {
            return false;
        }
        notes.put(title, new Note(text, System.currentTimeMillis()));
        return true;
    }

    /**
     * Delete existing note.
     *
     * @param title title of the note
     * @return true if the note was removed, false if it doesn't exist
     */
    public boolean remove(String title) {
        if (!notes.containsKey(title)) {
            return false;
        }
        notes.remove(title);
        return true;
    }

    /**
     * Show all notes. If `keywords` specified, show only notes with at least one of the words in
     * the title. If `after`/`before` specified, show only notes that were created after/before some
     * datetime
     *
     * @param keywords list of words, only notes that contain one of them in title are returned; or
     *                 null if not specified
     * @param after    datetime, only notes that were created after that time are returned; or null
     *                 if not specified
     * @param before   datetime, only notes that were created before that time are returned; or null
     *                 if not specified
     * @return List of pairs: title, {}
     */
    public List<Map.Entry<String, Note>> show(String[] keywords, LocalDateTime after,
        LocalDateTime before) {
        long a = after != null
            ? after.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() : Long.MIN_VALUE;
        long b = before != null
            ? before.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() : Long.MAX_VALUE;
        Stream<Map.Entry<String, Note>> s = notes.entrySet().stream();
        s = s.filter(entry -> entry.getValue().created <= b);
        s = s.filter(entry -> entry.getValue().created >= a);
        if (keywords != null) {
            s = s.filter(
                entry -> Arrays.stream(keywords).noneMatch(word -> entry.getKey().contains(word)));
        }
        return s.toList();
    }

    /**
     * Class for storing creation date and body of the note together.
     */
    @SuppressWarnings("ClassCanBeRecord")
    public static class Note {

        private final long created;
        private final String text;

        /**
         * Constructor.
         *
         * @param text    body of the note
         * @param created millis datetime of creation of the note
         */
        public Note(String text, long created) {
            this.text = text;
            this.created = created;
        }

        /**
         * Get millis datetime of creation of the note.
         */
        public long created() {
            return created;
        }

        /**
         * Get body of the note.
         */
        public String text() {
            return text;
        }
    }
}
