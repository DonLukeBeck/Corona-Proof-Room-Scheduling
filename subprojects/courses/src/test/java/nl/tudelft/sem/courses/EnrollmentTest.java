package nl.tudelft.sem.courses;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import nl.tudelft.sem.courses.entity.Enrollment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EnrollmentTest {
    private static Enrollment enrollment;

    /**
     * The initial setup before each test.
     */
    @BeforeEach
    void createCourse() {
        enrollment = new Enrollment();
        enrollment.setCourseId("CSE1230");
        enrollment.setStudentId("ST12");
    }

    @Test
    public void constructorNotNull() {
        assertNotNull(enrollment);
    }

    @Test
    void getCourseIdTest() {
        assertEquals("CSE1230", enrollment.getCourseId());
    }

    @Test
    void setCourseIdTest() {
        enrollment.setCourseId("CSE3012");
        assertEquals("CSE3012", enrollment.getCourseId());
    }

    @Test
    void getStudentIdTest() {
        assertEquals("ST12", enrollment.getStudentId());
    }

    @Test
    void setStudentIdTest() {
        enrollment.setStudentId("Student");
        assertEquals("Student", enrollment.getStudentId());
    }
}
