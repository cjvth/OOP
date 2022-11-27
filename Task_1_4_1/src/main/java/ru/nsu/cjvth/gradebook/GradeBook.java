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

    /**
     * Constructor. Last semester at which student has some final grades is set to 0, meaning that
     * they haven't got any marks
     *
     * @param id             the grade book's identification number
     * @param totalSemesters positive number of semesters that the student will is supposed to
     *                       attend
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

    /**
     * Add a subject to receive a grade in certain semester. You can have multiple subject with
     * certain title only in different semesters, also you can have different grade types for one
     * subject
     *
     * @param semester  number of the semester
     * @param title     name of the subject
     * @param gradeType method of getting the mark from {@link GradeType}
     * @throws IllegalArgumentException if subject with this title already exists in this semester
     *                                  or if the semester does not exist
     */
    public void addSubject(int semester, String title, GradeType gradeType) {
        throw new IllegalArgumentException(
            "Subject with this title already exists in this semester");
    }

    /**
     * Remove subject in certain semester. If there is no such subject, nothing happens
     *
     * @param semester number of the semester
     * @param title    name of the subject
     */
    public void removeSubject(int semester, String title) {

    }

    /**
     * Get list of subjects in certain semester.
     *
     * @param semester number of the semester
     * @return List of subject names
     */
    public List<String> getSubjects(int semester) {
        return null;
    }

    /**
     * Put or change mark for previously added subject.
     *
     * @param semester number of the semester
     * @param title    name of the subject
     * @param mark     the mark, should be either 0 or 1 for {@link GradeType#BOOL_CREDIT} grade
     *                 type or from 0 to 5 for {@link GradeType#DIFFERENTIATED_CREDIT} and
     *                 {@link GradeType#EXAM}
     * @throws IllegalArgumentException if subject with this title at this semester does not exist;
     *                                  or if mark is out of bounds
     */
    public void setMark(int semester, String title, int mark) {

    }

    /**
     * Remove mark for previously added subject. Does nothing if there is no mark.
     *
     * @param semester number of the semester
     * @param title    name of the subject
     * @throws IllegalArgumentException if subject with this title at this semester does not exist
     */
    public void unsetMark(int semester, String title) {

    }

    /**
     * Remove mark for previously added subject. Does nothing if there is no mark.
     *
     * @param semester number of the semester
     * @param title    name of the subject
     * @return type of the grade from {@link GradeType}
     * @throws IllegalArgumentException if subject with this title at this semester does not exist
     */
    public GradeType getGradeType(int semester, String title) {
        return null;
    }

    /**
     * Get the mark for subject if it is set.
     *
     * @param semester number of the semester
     * @param title    name of the subject
     * @return mark value if it is set or null if it isn't
     * @throws IllegalArgumentException if subject with this title at this semester does not exist
     */
    public Integer getMark(int semester, String title) {
        return 5;
    }

    /**
     * Get mean mark value for certain semester.
     *
     * @param semester number of the semester
     * @return the mean mark of semester
     * @throws IllegalArgumentException if no such semester
     */
    public double meanSemesterMark(int semester) {
        return 4;
    }

    /**
     * Get mean mark value across all semesters.
     *
     * @return the mean mark of all semesters
     */
    public double meanMark() {
        return 3;
    }

    /**
     * Get what stipend will the student get in the next semester.
     *
     * @return 0 of none, 1 if normal, 2 if increased, -1 if not enough information to know
     */
    public int nextSemesterStipend() {
        return 0;
    }

    /**
     * Type of evaluating the student's achievements: {@link #BOOL_CREDIT},
     * {@link #DIFFERENTIATED_CREDIT}, {@link #EXAM}.
     */
    public enum GradeType {
        /**
         * Student gets 1 if passes some threshold by working during the semester, 0 otherwise.
         */
        BOOL_CREDIT,
        /**
         * Student's work during semester is evaluated from 0 to 5.
         */
        DIFFERENTIATED_CREDIT,
        /**
         * Student's answer on an exam is evaluated from 0 to 5.
         */
        EXAM
    }

    private class Grade {

        private GradeType gradeType;
        private int mark;
        private boolean hasMark;

        public Grade(GradeType gradeType) {
            this.gradeType = gradeType;
            hasMark = false;
        }
    }
}
