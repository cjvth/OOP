package ru.nsu.cjvth.gradebook;

import java.util.List;

/**
 * A record book for students. Supports adding and changing semester grades and calculating some
 * statistics.
 */
public class GradeBook {

    private final int id;
    private final int semesters;
    private int lastSemester;

    public enum GradeTypes {
        BOOL_CREDIT, DIFFERENTIATED_CREDIT, EXAM
    }

    /**
     * Constructor. Last semester at which student has some final grades is set to 0, meaning that
     * they haven't got any marks
     *
     * @param id             the grade book's identification number
     * @param totalSemesters number of semesters that the student will is supposed to attend
     */
    public GradeBook(int id, int totalSemesters) {
        this.id = id;
        this.semesters = totalSemesters;
        lastSemester = 0;
    }

    /**
     * Get the id of the book.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Get the number of semesters that the student is supposed to attend in total.
     *
     * @return the id of the book
     */
    public int getTotalSemestersCount() {
        return semesters;
    }

    /**
     * Get the number of last semester the student got some semester marks.
     *
     * @return the number of that semester. 0 means that the student hasn't finished any semester
     */
    public int lastSemester() {
        return lastSemester;
    }

    public void addSubject(int semester, String title, GradeTypes gradeType) {

    }

    public void removeSubject(int semester, String title) {

    }

    public List<String> getSubjects(int semester) {
        return null;
    }

    public void setMark(int semester, String title, int mark) {

    }

    public void unsetMark(int semester, String title) {

    }

    public int getMark(int semester, String title) {
        return 5;
    }
}