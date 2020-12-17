package nl.tudelft.sem.courses;

import nl.tudelft.sem.shared.entity.BareEnrollment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BareEnrollmentTest {
    private static BareEnrollment enrollment;

    /**
     * The initial setup before each test.
     */
    @BeforeEach
    void createCourse() {
        enrollment = new BareEnrollment("CSE1230", "ST12");
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
