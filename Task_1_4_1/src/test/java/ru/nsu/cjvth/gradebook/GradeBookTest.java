package ru.nsu.cjvth.gradebook;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;
import org.junit.jupiter.api.Test;
import ru.nsu.cjvth.gradebook.GradeBook.GradeType;

class GradeBookTest {

    @Test
    void getId() {
        GradeBook g = new GradeBook(618, 8);
        assertEquals(618, g.getId());
    }

    @Test
    void getTotalSemestersCount() {
        GradeBook g = new GradeBook(1, 8);
        assertEquals(8, g.getTotalSemestersCount());
    }

    @Test
    void addSubjects() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.DIFFERENTIATED_CREDIT);
        g.addSubject(1, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.addSubject(2, "Maths", GradeType.EXAM);
        g.addSubject(1, "C", GradeType.DIFFERENTIATED_CREDIT);
        assertEquals(Set.of("C", "History", "Maths"), g.getSubjects(1));
    }

    @Test
    void addSubjectTwice() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.addSubject(1, "C", GradeType.DIFFERENTIATED_CREDIT);
        g.addSubject(2, "Maths", GradeType.EXAM);
        assertThrows(IllegalArgumentException.class,
            () -> g.addSubject(1, "Maths", GradeType.BOOL_CREDIT));
    }

    @Test
    void addSubjectBadSemester() {
        GradeBook g = new GradeBook(1, 8);
        assertThrows(IllegalArgumentException.class,
            () -> g.addSubject(0, "Maths", GradeType.EXAM));
        assertDoesNotThrow(() -> g.addSubject(1, "Maths", GradeType.EXAM));
        assertDoesNotThrow(() -> g.addSubject(8, "Maths", GradeType.EXAM));
        assertThrows(IllegalArgumentException.class,
            () -> g.addSubject(9, "Maths", GradeType.EXAM));
    }

    @Test
    void removeSubject() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.DIFFERENTIATED_CREDIT);
        g.addSubject(1, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.addSubject(2, "Maths", GradeType.EXAM);
        g.addSubject(1, "C", GradeType.DIFFERENTIATED_CREDIT);
        g.removeSubject(1, "Maths");
        assertEquals(Set.of("C", "History"), g.getSubjects(1));
    }

    @Test
    void removeSubjectTwice() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.removeSubject(1, "Maths");
        assertDoesNotThrow(() -> g.removeSubject(1, "Maths"));
    }

    @Test
    void removeSubjectBadSemester() {
        GradeBook g = new GradeBook(1, 8);
        assertThrows(IllegalArgumentException.class,
            () -> g.removeSubject(0, "Maths"));
        g.addSubject(1, "Maths", GradeType.EXAM);
        assertDoesNotThrow(() -> g.removeSubject(1, "Maths"));
        g.addSubject(8, "Maths", GradeType.EXAM);
        assertDoesNotThrow(() -> g.removeSubject(8, "Maths"));
        assertThrows(IllegalArgumentException.class,
            () -> g.removeSubject(9, "Maths"));
    }

    @Test
    void getSubjectsBadSemester() {
        GradeBook g = new GradeBook(0, 6);
        assertThrows(IllegalArgumentException.class, () -> g.getSubjects(0));
        assertDoesNotThrow(() -> g.getSubjects(1));
        assertDoesNotThrow(() -> g.getSubjects(6));
        assertThrows(IllegalArgumentException.class, () -> g.getSubjects(7));
    }
}