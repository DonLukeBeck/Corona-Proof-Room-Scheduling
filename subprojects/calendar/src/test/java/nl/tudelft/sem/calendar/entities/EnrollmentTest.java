package nl.tudelft.sem.calendar.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnrollmentTest {
    private transient String studentId;
    private transient String courseId;
    private transient Enrollment enrollment;

    /**
     * Creates an enrollment and its attributes used in all test cases.
     */
    @BeforeEach
    void setup() {
        studentId = "abobe";
        courseId = "CSE1200";
        enrollment = new Enrollment(courseId, studentId);
    }

    /**
     * Tests the getter of the student id.
     */
    @Test
    void testGetStudentId() {
        assertEquals(studentId, enrollment.getStudentId());
    }

    /**
     * Tests the getter of the course id.
     */
    @Test
    void testGetCourseId() {
        assertEquals(courseId, enrollment.getCourseId());
    }

    /**
     * Tests the setter of the student id.
     */
    @Test
    void testSetStudentId() {
        enrollment.setStudentId("mbjdegoede");
        assertEquals("mbjdegoede", enrollment.getStudentId());
    }

    /**
     * Tests the setter of the course id.
     */
    @Test
    void testSetCourseId() {
        enrollment.setCourseId("CSE1315");
        assertEquals("CSE1315", enrollment.getCourseId());
    }
}