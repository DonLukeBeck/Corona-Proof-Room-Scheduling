package nl.tudelft.sem.calendar.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AttendanceTest {
    private static Attendance attendance;
    private static int lectureId;
    private static String studentId;
    private static boolean physical;

    /**
     * Creates an attendance entry used in all the test cases.
     */
    @BeforeEach
    void setup() {
        lectureId = 10;
        studentId = "mbjdegoede";
        physical = false;
        attendance = Attendance.builder()
                .lectureId(lectureId)
                .physical(physical)
                .studentId(studentId).build();
    }

    /**
     * Tests the getter of the student id.
     */
    @Test
    void testGetStudentId() {
        assertEquals(studentId, attendance.getStudentId());
    }

    /**
     * Tests the getter of the lecture id.
     */
    @Test
    void testGetLectureId() {
        assertEquals(lectureId, attendance.getLectureId());
    }

    /**
     * Tests the getter of the boolean indicating physical presence.
     */
    @Test
    void testGetPhysical() {
        assertEquals(physical, attendance.getPhysical());
    }

    /**
     * Tests the setter of the student id.
     */
    @Test
    void testSetStudentId() {
        attendance.setStudentId("abobe");
        assertEquals("abobe", attendance.getStudentId());
    }

    /**
     * Tests the setter of the lecture id.
     */
    @Test
    void testSetLectureId() {
        attendance.setLectureId(120);
        assertEquals(120, attendance.getLectureId());
    }

    /**
     * Tests the setter of boolean indicating physical presence.
     */
    @Test
    void testSetPhysical() {
        assertEquals(attendance.getPhysical(), physical);
        attendance.setPhysical(!physical);
        assertNotEquals(physical, attendance.getPhysical());

    }
}