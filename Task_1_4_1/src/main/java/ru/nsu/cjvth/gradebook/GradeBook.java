package ru.nsu.cjvth.gradebook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A record book for students. Supports adding and changing semester grades and calculating some
 * statistics.
 */
public class GradeBook {

    private final int id;
    private final int totalSemesters;
    private final List<Map<String, Grade>> subjects;

    private final Grade diploma = new Grade(GradeType.DIFFERENTIATED_CREDIT);

    /**
     * The constructor.
     *
     * @param id             the grade book's identification number
     * @param totalSemesters positive number of semesters that the student will is supposed to
     *                       attend
     */
    public GradeBook(int id, int totalSemesters) {
        this.id = id;
        this.totalSemesters = totalSemesters;
        subjects = new ArrayList<>(totalSemesters);
        for (int i = 0; i < totalSemesters; i++) {
            subjects.add(new HashMap<>());
        }
    }

    private boolean isSemesterInvalid(int number) {
        return number < 1 || number > totalSemesters;
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
        return totalSemesters;
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
     *                                  or if such semester does not exist
     */
    public void addSubject(int semester, String title, GradeType gradeType) {
        if (isSemesterInvalid(semester)) {
            throw new IllegalArgumentException("Semester number is invalid");
        }
        if (subjects.get(semester - 1).containsKey(title)) {
            throw new IllegalArgumentException(
                "Subject with this title already exists in this semester");
        }
        subjects.get(semester - 1).put(title, new Grade(gradeType));
    }

    /**
     * Remove subject in certain semester. If there is no such subject, nothing happens
     *
     * @param semester number of the semester
     * @param title    name of the subject
     * @throws IllegalArgumentException if semester number is illegal
     */
    public void removeSubject(int semester, String title) {
        if (isSemesterInvalid(semester)) {
            throw new IllegalArgumentException("Semester number is invalid");
        }
        subjects.get(semester - 1).remove(title);
    }

    /**
     * Get list of subjects in certain semester.
     *
     * @param semester number of the semester
     * @return set of subject names
     * @throws IllegalArgumentException if semester number is illegal
     */
    public Set<String> getSubjects(int semester) {
        if (isSemesterInvalid(semester)) {
            throw new IllegalArgumentException("Semester number is invalid");
        }
        return subjects.get(semester - 1).keySet();
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
        if (isSemesterInvalid(semester)) {
            throw new IllegalArgumentException("Semester number is invalid");
        }
        if (!subjects.get(semester - 1).containsKey(title)) {
            throw new IllegalArgumentException(
                "Subject with this name does not exist in this semester");
        }
        Grade g = subjects.get(semester - 1).get(title);
        if (g.hasMark) {
            return g.mark;
        } else {
            return null;
        }
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
     *                                  or if mark is out of bounds for its grade type
     */
    public void setMark(int semester, String title, int mark) {
        if (isSemesterInvalid(semester)) {
            throw new IllegalArgumentException("Semester number is invalid");
        }
        if (!subjects.get(semester - 1).containsKey(title)) {
            throw new IllegalArgumentException(
                "Subject with this name does not exist in this semester");
        }
        Grade g = subjects.get(semester - 1).get(title);
        if (g.isNewMarkIllegal(mark)) {
            throw new IllegalArgumentException("Mark value is out of range for this type of grade");
        }
        g.hasMark = true;
        g.mark = mark;
    }

    /**
     * Remove mark for previously added subject. Does nothing if there is no mark.
     *
     * @param semester number of the semester
     * @param title    name of the subject
     * @throws IllegalArgumentException if subject with this title at this semester does not exist
     */
    public void unsetMark(int semester, String title) {
        if (isSemesterInvalid(semester)) {
            throw new IllegalArgumentException("Semester number is invalid");
        }
        if (!subjects.get(semester - 1).containsKey(title)) {
            throw new IllegalArgumentException(
                "Subject with this name does not exist in this semester");
        }
        Grade g = subjects.get(semester - 1).get(title);
        if (g.hasMark) {
            g.hasMark = false;
        }
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
        if (isSemesterInvalid(semester)) {
            throw new IllegalArgumentException("Semester number is invalid");
        }
        if (!subjects.get(semester - 1).containsKey(title)) {
            throw new IllegalArgumentException(
                "Subject with this name does not exist in this semester");
        }
        return subjects.get(semester - 1).get(title).gradeType;
    }

    /**
     * Get mean mark value for certain semester. Unset marks are ignored.
     *
     * @param semester number of the semester
     * @return the mean mark of subjects with mark in the semester or null if no subjects have mark
     * @throws IllegalArgumentException if semester number is illegal
     */
    public Double meanSemesterMark(int semester) {
        if (isSemesterInvalid(semester)) {
            throw new IllegalArgumentException("Semester number is invalid");
        }
        int total = 0;
        int count = 0;
        for (Grade g : subjects.get(semester - 1).values()) {
            if (g.hasMark) {
                count++;
                if (g.gradeType == GradeType.BOOL_CREDIT) {
                    total += g.mark * 5;
                } else {
                    total += g.mark;
                }
            }
        }
        if (count == 0) {
            return null;
        } else {
            return (double) total / count;
        }
    }

    /**
     * Get mean mark value across all semesters. Unset marks are ignored.
     *
     * @return the mean mark of subjects with mark for all time or null if no subjects have mark
     */
    public Double meanMark() {
        int total = 0;
        int count = 0;
        for (int sem = 0; sem < totalSemesters; sem++) {
            for (Grade g : subjects.get(sem).values()) {
                if (g.hasMark) {
                    count++;
                    if (g.gradeType == GradeType.BOOL_CREDIT) {
                        total += g.mark * 5;
                    } else {
                        total += g.mark;
                    }
                }
            }
        }
        if (count == 0) {
            return null;
        } else {
            return (double) total / count;
        }
    }

    /**
     * Get what government academic stipend the student should be given in the next semester after
     * the given one.
     *
     * @param semester number of the semester, based on which marks the student is going to get
     *                 stipend in the next semester
     * @return 0 of none, 1 if normal, 2 if increased, -1 if not enough information to know
     * @throws IllegalArgumentException if semester number is illegal
     */
    public int semesterStipend(int semester) {
        if (isSemesterInvalid(semester)) {
            throw new IllegalArgumentException("Semester number is invalid");
        }
        boolean hasEmpty = false;
        boolean has4 = false;
        boolean has3 = false;
        for (Grade g : subjects.get(semester - 1).values()) {
            if (g.hasMark) {
                if (g.gradeType == GradeType.BOOL_CREDIT) {
                    if (g.mark == 0) {
                        has3 = true;
                    }
                } else {
                    if (g.mark < 5) {
                        has4 = true;
                        if (g.mark < 4) {
                            has3 = true;
                        }
                    }
                }
            } else {
                hasEmpty = true;
            }
        }
        if (has3) {
            return 0;
        } else if (hasEmpty) {
            return -1;
        } else if (has4) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * Put or change mark for student's diploma work.
     *
     * @param mark     the mark, should be from 0 to 5
     * @throws IllegalArgumentException if mark is out of bounds
     */
    public void setDiplomaMark(int mark) {
        if (diploma.isNewMarkIllegal(mark)) {
            throw new IllegalArgumentException("Mark value is out of range for diploma");
        }
        diploma.hasMark = true;
        diploma.mark = mark;
    }

    /**
     * Remove mark for the diploma work. Does nothing if there is no mark.
     */
    public void unsetDiplomaMark() {
        diploma.hasMark = false;
    }

    /**
     * Check if the student will definitely get a diploma with honors.
     *
     * @return false if student doesn't satisfy criteria or some marks are not set, true otherwise
     */
    public boolean getsHonourDegree() {
        if (!Integer.valueOf(5).equals(getDiplomaMark())) {
            return false;
        }
        Map<String, Integer> finalMarks = new HashMap<>();
        for (var semester : subjects) {
            for (var subject : semester.entrySet()) {
                Grade grade = subject.getValue();
                if (!grade.hasMark) {
                    return false;
                }
                if (grade.gradeType == GradeType.BOOL_CREDIT) {
                    if (grade.mark == 0) {
                        return false;
                    }
                    finalMarks.put(subject.getKey(), 5);
                } else {
                    if (grade.mark < 4) {
                        return false;
                    }
                    finalMarks.put(subject.getKey(), grade.mark);
                }
            }
        }
        int excellent = 0;
        int total = 0;
        for (int subject : finalMarks.values()) {
            total++;
            if (subject == 5) {
                excellent++;
            }
        }
        return excellent * 4 >= total * 3;
    }

    /**
     * Get the mark for subject if it is set.
     *
     * @return mark value if it is set or null if it isn't
     */
    public Integer getDiplomaMark() {
        if (diploma.hasMark) {
            return diploma.mark;
        } else {
            return null;
        }
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

    private static class Grade {

        public final GradeType gradeType;
        public int mark;
        public boolean hasMark;

        public Grade(GradeType gradeType) {
            this.gradeType = gradeType;
            hasMark = false;
        }

        public boolean isNewMarkIllegal(int mark) {
            return mark < 0 || ((gradeType != GradeType.BOOL_CREDIT || mark > 1)
                && (gradeType == GradeType.BOOL_CREDIT || mark > 5));
        }
    }
}
