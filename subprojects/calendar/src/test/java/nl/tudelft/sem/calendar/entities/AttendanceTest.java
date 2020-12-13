package nl.tudelft.sem.calendar.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AttendanceTest {
    private static Attendance attendance;
    private static int lectureId;
    private static String studentId;
    private static boolean physical;

    @BeforeEach
    /**
     * Creates an attendance entry used in all the test cases.
     */
    void setup(){
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

    /**
     * Tests the getter and setter of lecture attribute associated with the attendance.
     * This attribute is only set when an existing attendance entry is retrieved from the database.
     */
    @Test
    void testGetSetLecture() {
        Course course = new Course(Arrays.asList("someNetId", "someNetId2"));
        LocalDate date = LocalDate.of(2020, 2, 1);
        Lecture lecture = Lecture.builder().course(course).date(date).durationInMinutes(90).build();

        assertNull(attendance.getLecture());
        attendance.setLecture(lecture);
        assertEquals(lecture, attendance.getLecture());
    }

    /**
     * Tests the getter and setter of AttendanceId attribute which forms the composite key of the
     * attendance object required for the database.
     * This attribute is only set when saving to the database.
     */
    @Test
    void testGetSetAttendanceId() {
        AttendanceId id = new AttendanceId(lectureId, studentId);
        assertNull(attendance.getAttendanceId());
        attendance.setAttendanceId(id);
        assertEquals(id, attendance.getAttendanceId());
    }
}