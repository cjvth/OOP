package ru.nsu.cjvth.gradebook;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void getMarkInitial() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.DIFFERENTIATED_CREDIT);
        g.addSubject(2, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        assertNull(g.getMark(1, "History"));
        assertNull(g.getMark(2, "Maths"));
    }

    @Test
    void getMarkBadSemester() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.DIFFERENTIATED_CREDIT);
        assertThrows(IllegalArgumentException.class, () -> g.getMark(9, "History"));
    }

    @Test
    void getMarkBadSubject() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.DIFFERENTIATED_CREDIT);
        assertThrows(IllegalArgumentException.class, () -> g.getMark(1, "Maths"));
    }

    @Test
    void setMark() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.DIFFERENTIATED_CREDIT);
        g.addSubject(2, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(2, "Maths", 5);
        g.setMark(1, "History", 4);
        assertEquals(4, g.getMark(1, "History"));
        assertEquals(5, g.getMark(2, "Maths"));
        g.setMark(1, "History", 5);
        assertEquals(5, g.getMark(1, "History"));
    }

    @Test
    void setMarkBadSemester() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.DIFFERENTIATED_CREDIT);
        assertThrows(IllegalArgumentException.class, () -> g.setMark(9, "History", 3));
    }

    @Test
    void setMarkBadSubject() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.DIFFERENTIATED_CREDIT);
        assertThrows(IllegalArgumentException.class, () -> g.setMark(1, "Maths", 2));
    }

    @Test
    void setMarkBadMarks() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "Maths", GradeType.BOOL_CREDIT);
        g.addSubject(2, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.addSubject(3, "Maths", GradeType.EXAM);
        assertThrows(IllegalArgumentException.class, () -> g.setMark(1, "Maths", 5));
        assertDoesNotThrow(() -> g.setMark(2, "Maths", 5));
        assertDoesNotThrow(() -> g.setMark(3, "Maths", 4));
        assertThrows(IllegalArgumentException.class, () -> g.setMark(2, "Maths", -1));
        assertThrows(IllegalArgumentException.class, () -> g.setMark(3, "Maths", 100));
        assertDoesNotThrow(() -> g.setMark(1, "Maths", 0));
    }

    @Test
    void unsetMark() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.DIFFERENTIATED_CREDIT);
        g.addSubject(2, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(2, "Maths", 5);
        g.unsetMark(2, "Maths");
        g.unsetMark(1, "History");
        assertNull(g.getMark(1, "History"));
        assertNull(g.getMark(2, "Maths"));
    }

    @Test
    void unsetMarkBadSemester() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.DIFFERENTIATED_CREDIT);
        assertThrows(IllegalArgumentException.class, () -> g.unsetMark(9, "History"));
    }

    @Test
    void unsetMarkBadSubject() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.DIFFERENTIATED_CREDIT);
        assertThrows(IllegalArgumentException.class, () -> g.unsetMark(1, "Maths"));
    }

    @Test
    void getGradeType() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.DIFFERENTIATED_CREDIT);
        g.addSubject(2, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.removeSubject(2, "Maths");
        g.addSubject(2, "Maths", GradeType.EXAM);
        assertEquals(GradeType.DIFFERENTIATED_CREDIT, g.getGradeType(1, "History"));
        assertEquals(GradeType.EXAM, g.getGradeType(2, "Maths"));
    }

    @Test
    void getGradeTypeBadSemester() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.DIFFERENTIATED_CREDIT);
        assertThrows(IllegalArgumentException.class, () -> g.getGradeType(9, "History"));
    }

    @Test
    void getGradeTypeBadSubject() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.DIFFERENTIATED_CREDIT);
        assertThrows(IllegalArgumentException.class, () -> g.getGradeType(1, "Maths"));
    }

    @Test
    void getMeanSemesterMarkNoBool() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "PE", GradeType.DIFFERENTIATED_CREDIT);
        g.addSubject(1, "History", GradeType.EXAM);
        g.setMark(1, "History", 4);
        g.addSubject(1, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(1, "Maths", 3);
        g.addSubject(2, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(2, "Maths", 4);
        g.addSubject(1, "English", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(1, "English", 5);
        assertEquals(4., g.meanSemesterMark(1));
    }

    @Test
    void getMeanSemesterMarkBoolPassed() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.EXAM);
        g.setMark(1, "History", 4);
        g.addSubject(1, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(1, "Maths", 3);
        g.addSubject(1, "English", GradeType.BOOL_CREDIT);
        g.setMark(1, "English", 1);
        g.addSubject(1, "Physics", GradeType.BOOL_CREDIT);
        g.setMark(1, "Physics", 1);
        assertEquals(4.25, g.meanSemesterMark(1));
    }

    @Test
    void getMeanSemesterMarkBoolFailed() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.EXAM);
        g.setMark(1, "History", 4);
        g.addSubject(1, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(1, "Maths", 3);
        g.addSubject(1, "English", GradeType.BOOL_CREDIT);
        g.setMark(1, "English", 0);
        g.addSubject(1, "Physics", GradeType.BOOL_CREDIT);
        g.setMark(1, "Physics", 1);
        g.addSubject(1, "Java", GradeType.BOOL_CREDIT);
        assertEquals(3., g.meanSemesterMark(1));
    }

    @Test
    void getMeanSemesterMarkEmpty() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.EXAM);
        g.addSubject(1, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.addSubject(1, "English", GradeType.BOOL_CREDIT);
        g.addSubject(1, "Physics", GradeType.BOOL_CREDIT);
        assertNull(g.meanSemesterMark(1));
    }

    @Test
    void getMeanSemesterBadSemester() {
        GradeBook g = new GradeBook(1, 8);
        assertThrows(IllegalArgumentException.class, () -> g.meanSemesterMark(-4));
    }

    @Test
    void getMeanMarkNoBool() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "PE", GradeType.DIFFERENTIATED_CREDIT);
        g.addSubject(1, "History", GradeType.EXAM);
        g.setMark(1, "History", 4);
        g.addSubject(1, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(1, "Maths", 3);
        g.addSubject(2, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(2, "Maths", 4);
        g.addSubject(2, "English", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(2, "English", 5);
        assertEquals(4., g.meanMark());
    }

    @Test
    void getMeanMarkBoolPassed() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(3, "History", GradeType.EXAM);
        g.setMark(3, "History", 4);
        g.addSubject(1, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(1, "Maths", 3);
        g.addSubject(1, "English", GradeType.BOOL_CREDIT);
        g.setMark(1, "English", 1);
        g.addSubject(2, "Physics", GradeType.BOOL_CREDIT);
        g.setMark(2, "Physics", 1);
        assertEquals(4.25, g.meanMark());
    }

    @Test
    void getMeanMarkBoolFailed() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.EXAM);
        g.setMark(1, "History", 4);
        g.addSubject(1, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(1, "Maths", 3);
        g.addSubject(1, "English", GradeType.BOOL_CREDIT);
        g.setMark(1, "English", 0);
        g.addSubject(1, "Physics", GradeType.BOOL_CREDIT);
        g.setMark(1, "Physics", 1);
        g.addSubject(1, "Java", GradeType.BOOL_CREDIT);
        assertEquals(3., g.meanMark());
    }

    @Test
    void getMeanMarkEmpty() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.EXAM);
        g.addSubject(2, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.addSubject(2, "English", GradeType.BOOL_CREDIT);
        g.addSubject(3, "Physics", GradeType.BOOL_CREDIT);
        assertNull(g.meanMark());
    }

    @Test
    void semesterStipendGood() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.EXAM);
        g.setMark(1, "History", 5);
        g.addSubject(1, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(1, "Maths", 5);
        g.addSubject(1, "English", GradeType.BOOL_CREDIT);
        g.setMark(1, "English", 1);
        g.addSubject(1, "Physics", GradeType.BOOL_CREDIT);
        g.setMark(1, "Physics", 1);
        assertEquals(2, g.semesterStipend(1));
    }

    @Test
    void semesterStipendGoodUnknown() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.EXAM);
        g.setMark(1, "History", 5);
        g.addSubject(1, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(1, "Maths", 5);
        g.addSubject(1, "English", GradeType.BOOL_CREDIT);
        g.setMark(1, "English", 1);
        g.addSubject(1, "Physics", GradeType.BOOL_CREDIT);
        g.setMark(1, "Physics", 1);
        g.addSubject(1, "Python", GradeType.DIFFERENTIATED_CREDIT);
        assertEquals(-1, g.semesterStipend(1));
    }

    @Test
    void semesterStipendOk() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.EXAM);
        g.setMark(1, "History", 5);
        g.addSubject(1, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(1, "Maths", 4);
        g.addSubject(1, "English", GradeType.BOOL_CREDIT);
        g.setMark(1, "English", 1);
        g.addSubject(1, "Physics", GradeType.BOOL_CREDIT);
        g.setMark(1, "Physics", 1);
        assertEquals(1, g.semesterStipend(1));
    }

    @Test
    void semesterStipendOkUnknown() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.EXAM);
        g.setMark(1, "History", 5);
        g.addSubject(1, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(1, "Maths", 4);
        g.addSubject(1, "English", GradeType.BOOL_CREDIT);
        g.setMark(1, "English", 1);
        g.addSubject(1, "Physics", GradeType.BOOL_CREDIT);
        g.setMark(1, "Physics", 1);
        g.addSubject(1, "Python", GradeType.DIFFERENTIATED_CREDIT);
        assertEquals(-1, g.semesterStipend(1));
    }

    @Test
    void semesterBadGood() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.EXAM);
        g.setMark(1, "History", 5);
        g.addSubject(1, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(1, "Maths", 3);
        g.addSubject(1, "English", GradeType.BOOL_CREDIT);
        g.setMark(1, "English", 1);
        g.addSubject(1, "Physics", GradeType.BOOL_CREDIT);
        g.setMark(1, "Physics", 1);
        assertEquals(0, g.semesterStipend(1));
    }

    @Test
    void semesterStipendBadUnknown() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.EXAM);
        g.setMark(1, "History", 5);
        g.addSubject(1, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(1, "Maths", 5);
        g.addSubject(1, "English", GradeType.BOOL_CREDIT);
        g.setMark(1, "English", 0);
        g.addSubject(1, "Physics", GradeType.BOOL_CREDIT);
        g.setMark(1, "Physics", 1);
        g.addSubject(1, "Python", GradeType.DIFFERENTIATED_CREDIT);
        assertEquals(0, g.semesterStipend(1));
    }

    @Test
    void semesterStipendBadSemester() {
        GradeBook g = new GradeBook(1, 8);
        assertThrows(IllegalArgumentException.class, () -> g.semesterStipend(10));
    }

    @Test
    void gradeMark() {
        GradeBook g = new GradeBook(1, 8);
        g.setDiplomaMark(4);
        assertEquals(4, g.getDiplomaMark());
        g.setDiplomaMark(5);
        assertEquals(5, g.getDiplomaMark());
    }

    @Test
    void getGradeMarkNotSet() {
        GradeBook g = new GradeBook(1, 8);
        assertNull(g.getDiplomaMark());
    }

    @Test
    void setGradeMarkBad() {
        GradeBook g = new GradeBook(1, 8);
        assertThrows(IllegalArgumentException.class, () -> g.setDiplomaMark(-1));
        assertThrows(IllegalArgumentException.class, () -> g.setDiplomaMark(6));
        assertDoesNotThrow(() -> g.setDiplomaMark(3));
    }

    @Test
    void unsetGradeMark() {
        GradeBook g = new GradeBook(1, 8);
        g.setDiplomaMark(4);
        g.unsetDiplomaMark();
        assertNull(g.getDiplomaMark());
    }

    @Test
    void getHonorDegreeGood() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.EXAM);
        g.setMark(1, "History", 5);
        g.addSubject(1, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(1, "Maths", 4);
        g.addSubject(2, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(2, "Maths", 5);
        g.addSubject(1, "English", GradeType.BOOL_CREDIT);
        g.setMark(1, "English", 1);
        g.addSubject(1, "Physics", GradeType.BOOL_CREDIT);
        g.setMark(1, "Physics", 1);
        g.setDiplomaMark(5);
        assertTrue(g.getsHonourDegree());
    }

    @Test
    void getHonorDegreeNoDiploma() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.EXAM);
        g.setMark(1, "History", 5);
        assertFalse(g.getsHonourDegree());
    }

    @Test
    void getHonorDegreeBadDiploma() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.EXAM);
        g.setMark(1, "History", 5);
        g.setDiplomaMark(4);
        assertFalse(g.getsHonourDegree());
    }

    @Test
    void getHonorDegreeGoodDiploma() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.EXAM);
        g.setMark(1, "History", 5);
        g.setDiplomaMark(5);
        assertTrue(g.getsHonourDegree());
    }

    @Test
    void getHonorDegreeMissingMark() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.EXAM);
        g.addSubject(2, "History", GradeType.EXAM);
        g.setMark(2, "History", 5);
        g.setDiplomaMark(5);
        assertFalse(g.getsHonourDegree());
    }

    @Test
    void getHonorDegreeBadMark() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.EXAM);
        g.setMark(1, "History", 3);
        g.addSubject(2, "History", GradeType.EXAM);
        g.setMark(2, "History", 5);
        g.setDiplomaMark(5);
        assertFalse(g.getsHonourDegree());
    }

    @Test
    void getHonorDegreeBadBoolMark() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(2, "History", GradeType.BOOL_CREDIT);
        g.setMark(2, "History", 0);
        g.setDiplomaMark(5);
        assertFalse(g.getsHonourDegree());
    }

    @Test
    void getHonorDegreeExactly75() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.BOOL_CREDIT);
        g.setMark(1, "History", 1);
        g.addSubject(1, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(1, "Maths", 4);
        g.addSubject(1, "Informatics", GradeType.EXAM);
        g.setMark(1, "Informatics", 5);
        g.addSubject(1, "Physics", GradeType.EXAM);
        g.setMark(1, "Physics", 5);
        g.setDiplomaMark(5);
        assertTrue(g.getsHonourDegree());
    }

    @Test
    void getHonorDegreeLessThan75() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.BOOL_CREDIT);
        g.setMark(1, "History", 1);
        g.addSubject(1, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(1, "Maths", 4);
        g.addSubject(1, "Informatics", GradeType.EXAM);
        g.setMark(1, "Informatics", 5);
        g.addSubject(1, "Physics", GradeType.EXAM);
        g.setMark(1, "Physics", 4);
        g.addSubject(1, "English", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(1, "English", 5);
        g.setDiplomaMark(5);
        assertFalse(g.getsHonourDegree());
    }

    @Test
    void getHonorDegreeMoreThan75() {
        GradeBook g = new GradeBook(1, 8);
        g.addSubject(1, "History", GradeType.BOOL_CREDIT);
        g.setMark(1, "History", 1);
        g.addSubject(1, "Maths", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(1, "Maths", 4);
        g.addSubject(1, "Informatics", GradeType.EXAM);
        g.setMark(1, "Informatics", 5);
        g.addSubject(1, "Physics", GradeType.EXAM);
        g.setMark(1, "Physics", 5);
        g.addSubject(1, "English", GradeType.DIFFERENTIATED_CREDIT);
        g.setMark(1, "English", 5);
        g.setDiplomaMark(5);
        assertTrue(g.getsHonourDegree());
    }
}